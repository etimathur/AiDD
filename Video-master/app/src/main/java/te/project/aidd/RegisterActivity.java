package te.project.aidd;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {
    DatabaseHelper db;
    EditText UserName;
    EditText Password;
    Button register;
    EditText ChildName;
    EditText doctorMail;
    TextView login;

    private static final Pattern PASSWORD_PATTERN=
            Pattern.compile("^" +
            "(?=.*[0-9])" +         //at least 1 digit
            "(?=.*[a-z])" +         //at least 1 lower case letter
            "(?=.*[A-Z])" +         //at least 1 upper case letter
            "(?=.*[a-zA-Z])" +      //any letter
            "(?=.*[@#$%^&+=])" +    //at least 1 special character
            "(?=\\S+$)" +           //no white spaces
            ".{8,}" +
            "$");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        db = new DatabaseHelper(this);
        ChildName = (EditText) findViewById(R.id.edittext_childname);
        doctorMail = (EditText) findViewById(R.id.edittext_mail);
        UserName = (EditText) findViewById(R.id.edittext_username);
        Password = (EditText) findViewById(R.id.edittext_password);
        register = (Button) findViewById(R.id.button_register);
        register.setEnabled(false);
        login = (TextView) findViewById(R.id.textview_login);
        login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent loginIntent = new Intent(RegisterActivity.this, ParentLoginActivity.class);
                startActivity(loginIntent);
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = ChildName.getText().toString().trim();
                String mail = doctorMail.getText().toString().trim();
                String user = UserName.getText().toString().trim();
                String password = Password.getText().toString().trim();
                if (mail.isEmpty()) {
                    if (!Patterns.EMAIL_ADDRESS.matcher(user).matches()) {
                        Toast.makeText(RegisterActivity.this, "Please enter a valid Email address", Toast.LENGTH_SHORT).show();
                    } else if(!PASSWORD_PATTERN.matcher(password).matches()) {
                        Toast.makeText(RegisterActivity.this, "Please enter a strong Password(atleast 8 characters long)", Toast.LENGTH_SHORT).show();
                    } else {
                        long val = db.addUser(name, mail, user, password);
                        if (val > 0) {
                            sendMail();
                            Toast.makeText(RegisterActivity.this, "You have registered", Toast.LENGTH_SHORT).show();
                            Intent moveToLogin = new Intent(RegisterActivity.this, HomeActivity.class);
                            startActivity(moveToLogin);
                        } else {
                            Toast.makeText(RegisterActivity.this, "Registration unsuccessful", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    if (!Patterns.EMAIL_ADDRESS.matcher(user).matches() && !Patterns.EMAIL_ADDRESS.matcher(mail).matches()) {
                        Toast.makeText(RegisterActivity.this, "Please enter a valid Email address", Toast.LENGTH_SHORT).show();
                    } else if(!PASSWORD_PATTERN.matcher(password).matches()) {
                        Toast.makeText(RegisterActivity.this, "Please enter a strong Password(atleast 8 characters long)", Toast.LENGTH_SHORT).show();
                    }else {
                        long val = db.addUser(name, mail, user, password);
                        if (val > 0) {
                            sendMail();
                            Toast.makeText(RegisterActivity.this, "You have registered", Toast.LENGTH_SHORT).show();
                            Intent moveToLogin = new Intent(RegisterActivity.this, HomeActivity.class);
                            startActivity(moveToLogin);
                        } else {
                            Toast.makeText(RegisterActivity.this, "Registration unsuccessful", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

            }
        });
        ChildName.addTextChangedListener(registerTextWatcher);
        UserName.addTextChangedListener(registerTextWatcher);
        Password.addTextChangedListener(registerTextWatcher);

    }

    private void sendMail(){

        String mail="2018.jatin.chhabria@ves.ac.in";
        String message="Hello";
        String subject="Testing mail";
        JavaMailAPI javaMailAPI=new JavaMailAPI(this,mail,subject,message);
        javaMailAPI.execute();
    }

    private TextWatcher registerTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String usernameInput = UserName.getText().toString().trim();
            String passwordInput = Password.getText().toString().trim();
            String nameInput = ChildName.getText().toString().trim();
            register.setEnabled(!usernameInput.isEmpty() && !passwordInput.isEmpty() && !nameInput.isEmpty());
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
}