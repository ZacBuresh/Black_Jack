package com.example.sam01.black_jack_app;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * A simple {@link Fragment} subclass.
 */

//Description: This class populates the frame view with the quit fragment.
public class QuitFragment extends Fragment {
    Button btnQuit, btnNo;

    public QuitFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_quit, container, false);
        //View view = inflater.inflate(R.layout.alert_dialog, container, false);
        btnQuit = view.findViewById(R.id.yesBtn);
        btnQuit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Log.d("QuitFragment","onClick: opening dialog");
                Quit_AlertDialog dialog = new Quit_AlertDialog();
                dialog.setTargetFragment(QuitFragment.this,1);
                dialog.show(getFragmentManager(), "Quit_AlertDialog");
            }
        });
        btnNo = (Button) view.findViewById(R.id.noBtn);
        btnNo.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                view.setVisibility(View.INVISIBLE);
            }
        });
        return view;
    }

}
