package com.example.sam01.black_jack_app;

import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import static com.example.sam01.black_jack_app.MainActivity.ctRestart;

//Description: This second activity shows the end screen when a user

public class Main2Activity extends AppCompatActivity {
    ListView listView;
    ArrayList handList = new ArrayList<String>();
    TextView totalAmount;
    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        totalAmount = (TextView) findViewById(R.id.totalAmount);
        listView = findViewById(R.id.listView);
        spinner = (Spinner) findViewById(R.id.spinner);
        Intent intentMessage = getIntent();
        String strHandList = intentMessage.getStringExtra("HandList");
        String result = intentMessage.getExtras().getString("Result");
        strHandList = strHandList.replace("[", "");
        strHandList = strHandList.replaceAll("]", "");
        strHandList = strHandList.replaceAll(",", "\n");
        handList.add(strHandList);
        totalAmount.setText(result);

        ArrayAdapter adapterHand =
                new ArrayAdapter(this,R.layout.list_label, handList);
        listView.setAdapter(adapterHand);

        ArrayList<String> buyIn = new ArrayList<String>();
        buyIn.add("100");
        buyIn.add("500");
        buyIn.add("1000");;

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, buyIn);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);

        ctRestart++;
    }

    //Allows the user to quit the app. Creates a fragment AND an alert dialog for confirmation.
    public void Quit(View view) {
        QuitFragment fragment = new QuitFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentDialog, fragment);
        transaction.commit();
    }

    //Let's the user restart with the amount of money of their choice.
    public void Restart(View view) {
        Intent intentBuyIn = new Intent(this, MainActivity.class);
        int BuyIn = Integer.parseInt(spinner.getSelectedItem().toString());
        MainActivity.totalAmount += BuyIn;
        startActivity(intentBuyIn);
    }
}
