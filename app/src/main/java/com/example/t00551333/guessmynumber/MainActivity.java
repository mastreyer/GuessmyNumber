package com.example.t00551333.guessmynumber;

import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private TextView descriptionText;
    private EditText secretNumber;
    private RatingBar timeBar;
    private Button startGuessing;
    private SeekBar seekBar01;
    int counter = 0;
    int secret = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        timeBar = (RatingBar) findViewById(R.id.ratingBar01);
        descriptionText = (TextView) findViewById(R.id.textView01);
        secretNumber = (EditText) findViewById(R.id.editText01);
        startGuessing = (Button) findViewById(R.id.button01);
        seekBar01 = (SeekBar) findViewById(R.id.seekBar01);
    }


    public void startGuessing(View v) {
        Toast.makeText(getBaseContext(), "Start Guessing!", Toast.LENGTH_SHORT).show();
        if (secretNumber.getText().toString().isEmpty()){
            Toast.makeText(getBaseContext(), "Please Enter a Number!", Toast.LENGTH_SHORT);
        } else {
            counter = 0;
            timeBar.setRating(counter);
            int complexity = seekBar01.getProgress();
            secret = Integer.valueOf(secretNumber.getText().toString());
            Log.d("Guess", "Secret Number is = " + secret);
            int range = (complexity + 1) * 10000000;

            GuessinBG background = new GuessinBG();
            background.execute(secret, range);


        }
    }

    public class GuessinBG extends AsyncTask<Integer, String, Integer>{

        Handler timingHandler = new Handler();
        Boolean flag = true;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            descriptionText.setText("");
            descriptionText.setTextSize(25);
            descriptionText.setTextColor(Color.RED);
            timingHandler.postDelayed(updateBar, 1000);
        }

        private Runnable updateBar = new Runnable(){

            @Override
            public void run() {
                if (counter < 5 && flag){
                    counter++;
                    timeBar.setRating(counter);
                    timingHandler.postDelayed(this, 1000);
                }
            }
        };

        @Override
        protected Integer doInBackground(Integer... integers) {
            Random rand = new Random();
            int rand_num = 0;
            int rate = 0;
            int secret = integers[0];
            int range = integers[1];
            while (rand_num != secret && counter < 5){
                rand_num = Math.abs(rand.nextInt()% range);
                rate++;
                if (rate % 1000 == 0){
                    publishProgress(String.valueOf(rand_num));
                }
            }
            flag = Boolean.FALSE;
            return rand_num;
        }

        @Override
        protected void onProgressUpdate(String... values){
            super.onProgressUpdate(values);
            descriptionText.setText(values[0]);
        }

        @Override
        protected void onPostExecute(Integer answer){
            super.onPostExecute(answer);
            if (counter < 5){
                descriptionText.setText("NUMBER FOUND = " + String.valueOf(answer));
            } else {
                descriptionText.setText("NUMBER NOT FOUND");
            }
        }
    }
}