package com.example.webforest.memorygames;

import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.Random;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {

    //phase 5 - our animation object
    Animation wobble;

    //for our hiscore (phase 4 )
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    String dataName = "MyData";
    String intName = "MyInt";
    int defaultInt = 0;
    int hiScore;

    //prepare objects and sound references
    //initialize  sound variable
    private SoundPool soundPool;
    int sample1 = -1;
    int sample2 = -1;
    int sample3 = -1;
    int sample4 = -1;

    //for ui
    TextView textScore;
    TextView textDifficulty;
    TextView textWatchGo;

    Button button1;
    Button button2;
    Button button3;
    Button button4;
    Button buttonReplay;

    //some variable for our thread
    int difficultLevel = 3;

    //An array to hold the randomly generated sequence
    int[] sequenceToCopy = new int[100];

    private Handler myHandler;

    //Are we playing a sequence at the moment?
    boolean playSequence = false;

    //And which element of the sequence are we on
    int elementToPlay = 0;

    //For checking the players answer
    int playerResponse;
    int playScore;
    boolean isResponding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        //phase 5 - animation
        wobble = AnimationUtils.loadAnimation(this,R.anim.wobble);

        //phase 4
        //initialize our two sharedprefrence object
        prefs = getSharedPreferences(dataName,MODE_PRIVATE);
        editor = prefs.edit();
        hiScore = prefs.getInt(intName,defaultInt);

        soundPool = new SoundPool(10,AudioManager.STREAM_MUSIC,0);
        try{
            //create objects of the 2 required classes
            AssetManager assetManager = getAssets();
            AssetFileDescriptor descriptor;

            //Create our three fx in memory ready for use
            descriptor = assetManager.openFd("sample1.ogg");
            sample1 = soundPool.load(descriptor,0);

            descriptor = assetManager.openFd("sample2.ogg");
            sample2 = soundPool.load(descriptor,0);

            descriptor = assetManager.openFd("sample3.ogg");
            sample3 = soundPool.load(descriptor,0);

            descriptor = assetManager.openFd("sample3.ogg");
            sample4 = soundPool.load(descriptor,0);

        }catch (IOException e){
            //catch exception here
        }

        //Reference all the elements of our UI with an appropriate object
        //First the text views
        textScore = (TextView)findViewById(R.id.textScore);
        textScore.setText("Score: "+ playScore);

        textDifficulty = (TextView)findViewById(R.id.textDifficulty);
        textDifficulty.setText("Level: "+difficultLevel);

        textWatchGo = (TextView)findViewById(R.id.textWatchGo);

        //Now the button
        button1 = (Button)findViewById(R.id.button);
        button2 = (Button)findViewById(R.id.button2);
        button3 = (Button)findViewById(R.id.button3);
        button4 = (Button)findViewById(R.id.button4);
        buttonReplay = (Button)findViewById(R.id.buttonReplay);

        //Now set all the buttons to listen for clicks
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);
        buttonReplay.setOnClickListener(this);

        //This is our code which will define our thread
        myHandler = new Handler(){
            public void handleMessage(Message msg){
                super.handleMessage(msg);

                if (playSequence){
                    //all the thread action will go here
                    //code not needed as using animation

                    switch (sequenceToCopy[elementToPlay]){
                        case 1:
                            button1.startAnimation(wobble);
                            //play a sound
                            soundPool.play(sample1,1,1,0,0,1);
                            break;
                        case 2:
                            button2.startAnimation(wobble);
                            soundPool.play(sample2,1,1,0,0,1);
                            break;

                        case 3:
                            button3.startAnimation(wobble);
                            soundPool.play(sample3,1,1,0,0,1);
                            break;
                        case 4:
                            button4.startAnimation(wobble);
                            soundPool.play(sample4,1,1,0,0,1);
                            break;
                    }
                    elementToPlay++;
                    if (elementToPlay == difficultLevel){
                        sequenceFinished();
                    }
                }
                myHandler.sendEmptyMessageDelayed(0,900);
            }
        };
        myHandler.sendEmptyMessage(0);


    }


    @Override
    public void onClick(View v) {

        if (!playSequence){
            //only accept input if sequence not playing
            switch (v.getId()){
                //case statement here......
                case R.id.button:
                    soundPool.play(sample1,1,1,0,0,1);
                    checkElement(1);
                    break;
                case R.id.button2:
                    soundPool.play(sample2,1,1,0,0,1);
                    checkElement(2);
                    break;

                case R.id.button3:
                    soundPool.play(sample3,1,1,0,0,1);
                    checkElement(3);
                    break;

                case R.id.button4:
                    soundPool.play(sample4,1,1,0,0,1);
                    checkElement(4);
                    break;

                case R.id.buttonReplay:
                    difficultLevel = 3;
                    playScore = 0;
                    textScore.setText("Score: "+playScore);
                    playASequence();
                    break;
            }
        }
    }


    private void playASequence() {
        createSequence();
        isResponding = false;
        elementToPlay = 0;
        playerResponse = 0;
        textWatchGo.setText("WATCH!!");

        playSequence = true;
    }

    private void createSequence() {


        //For choosing a random button
        Random randInt = new Random();
        int ourRandom;
        for(int i =0;i<difficultLevel;i++){
            //get a random number between 1 to 4
            ourRandom = randInt.nextInt(4);
            ourRandom++;//make sure it is not zero
            //save that number to our arry
            sequenceToCopy[i] = ourRandom;
        }
    }


    private void sequenceFinished() {

        playSequence = false;
        textWatchGo.setText("Go!");
        isResponding = true;
    }

    private void checkElement(int thisElement) {
    if (isResponding){
        playerResponse++;
        if (sequenceToCopy[playerResponse-1] == thisElement)//correct
             {
                 playScore = playScore + ((thisElement + 1)*2);
                 textScore.setText("Score: "+playScore);
                 if (playerResponse == difficultLevel){ //go to the hole sequence
                     //don't checkElement anymore
                     isResponding = false;
                     //now raise the difficulty
                     difficultLevel++;
                     //and play another sequence 
                     playASequence();

                 }


        }else {
            //wrong answer
            textWatchGo.setText("FAILED!");
            //don't checkElement anymore
            isResponding = false;
            
            //for our high score (phase-4)
            
            if (playScore > hiScore){
                hiScore = playScore;
                editor.putInt(intName,hiScore);
                editor.commit();
                Toast.makeText(getApplicationContext(), "New Hi score", Toast.LENGTH_SHORT).show();
            }
        }
    }

    }
}
