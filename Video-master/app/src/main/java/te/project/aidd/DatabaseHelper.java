package te.project.aidd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME="register.db";
    public static final String TABLE_NAME="registeruser";
    public static final String COL_1="ID";
    public static final String COL_2="name";
    public static final String COL_3="email";
//    email here is doctors email and username is parents email
    public static final String COL_4="username";
    public static final String COL_5="password";
    public static final String COL_6="childPassword";
    int countID=1;

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE registeruser (ID INTEGER PRIMARY KEY AUTOINCREMENT,name TEXT, email TEXT,username TEXT UNIQUE , password TEXT,childPassword TEXT)");


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);

    }
    public long addUser(String name, String email,String user, String password){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cV=new ContentValues();
        cV.put("name",name);
        cV.put("email",email);
        cV.put("username",user);
        cV.put("password",password);
        cV.put("childPassword",name+String.valueOf(countID));
        long res=db.insert("registeruser",null,cV);
        countID++;
        db.close();
        return res;
    }

    public Boolean checkUser(String username,String password){
//        String[] columns={COL_1};
        SQLiteDatabase db=this.getReadableDatabase();
//        String selection=COL_4 + "=?" + "and" + COL_5 + "=?";
//        String[] selectionArgs={username,password};
//        Cursor cursor=db.query(TABLE_NAME,columns,selection,selectionArgs,null,null,null);
        Cursor cursor=db.rawQuery("select * from registeruser where username=? and password=?", new String[]{username,password});
//        int count=cursor.getCount();
//        cursor.close();
//        db.close();
//        if(count>0)
//            return true;
//        else
//            return false;
        if (cursor.getCount()>0)return true;
        else return false;
    }
    public Boolean childCheckUser(String name,String password ){
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.rawQuery("select * from registeruser where name=? and childPassword=?", new String[]{name,password});
        if (cursor.getCount()>0)return true;
        else return false;
    }

}
