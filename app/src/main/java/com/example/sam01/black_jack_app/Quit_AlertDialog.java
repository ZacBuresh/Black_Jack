package com.example.sam01.black_jack_app;

import android.os.Bundle;
import android.os.Process;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


//Description: This class shows an alert dialog to quit.
public class Quit_AlertDialog extends DialogFragment {
    private Button btnYes, btnNo;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.alert_dialog, container, false);
        btnYes = (Button) view.findViewById(R.id.yesBtn);
        btnNo = (Button) view.findViewById(R.id.noBtn);
        btnNo.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                getDialog().dismiss();
            }
        });

        btnYes.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Process.killProcess(Process.myPid());
            }
        });
        return view;
    }
}
