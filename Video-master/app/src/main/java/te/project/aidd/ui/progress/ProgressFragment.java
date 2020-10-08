package te.project.aidd.ui.progress;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;

import te.project.aidd.Colormatchp;
import te.project.aidd.Flipcardsp;
import te.project.aidd.HomeActivity;
import te.project.aidd.R;
import te.project.aidd.ui.exercises.ExercisesFragment;
import te.project.aidd.ui.logout.LogoutViewModel;

public class ProgressFragment extends Fragment {
    private ProgressViewModel progressViewModel;
    Button cmp,fcp;
    CardView colormatch;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        progressViewModel =
                ViewModelProviders.of(this).get(ProgressViewModel.class);
        View root = inflater.inflate(R.layout.fragment_progress, container, false);
        colormatch=root.findViewById(R.id.colormatch);
        fcp=(Button)root.findViewById(R.id.fcp);
        colormatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cmpin=new Intent(ProgressFragment.this.getActivity(), Colormatchp.class);
                startActivity(cmpin);
            }
        });
        fcp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent fcpin=new Intent(ProgressFragment.this.getActivity(), Flipcardsp.class);
                startActivity(fcpin);
            }
        });

        return root;
    }

}

