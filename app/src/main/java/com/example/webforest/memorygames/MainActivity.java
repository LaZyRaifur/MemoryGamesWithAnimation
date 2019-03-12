package com.example.webforest.memorygames;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //for our hiscore (phase 4)
    SharedPreferences prefs;
    String dataName = "MyData";
    String intName = "MyInt";
    int defaultInt = 0;
    int hiScore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //for our high score(phase 4)
        //initialize our two shared preferences objects
        prefs = getSharedPreferences(dataName,MODE_PRIVATE);

        //Either load our high score or if not available of 0
        hiScore = prefs.getInt(intName,defaultInt);

        //make a reference to the high score textView in our layout
        TextView textHiScore = (TextView)findViewById(R.id.textHiScore);

        //Display the hiscore
        textHiScore.setText("Hi : "+hiScore);

        //Make a button from the button in the layout
        Button button = (Button)findViewById(R.id.button);

        //Make each it listen for clicks
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        Intent i;
        i = new Intent(this,GameActivity.class);
        startActivity(i);
    }
}
