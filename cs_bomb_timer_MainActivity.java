package com.example.bomb_timer_csgo_3;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private CountDownTimer timer;
    private Button plantButton;
    private Button defButton;
    private TextView licznik;
    private MediaPlayer mediaPlayerBeeps;


    private boolean czyTimerStoi = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        plantButton = findViewById(R.id.plant);
        defButton = findViewById(R.id.def);
        licznik = findViewById(R.id.licznik);

        plantButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTimer(44000);
                MediaPlayer mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.plants);
                mediaPlayer.start();
                czyTimerStoi = false;

            }
        });

        defButton.setOnTouchListener(new View.OnTouchListener() {
            private Handler handler;
            private boolean longClickPerformed;
            private MediaPlayer mediaPlayer;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                longClickPerformed = true;
                                stopTimer();
                                if (mediaPlayer != null) {
                                    mediaPlayer.stop();
                                    mediaPlayer.release();
                                    mediaPlayer = null;
                                }
                            }
                        }, 5000);
                        if (mediaPlayer == null) {
                            mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.defs);
                        }
                        if(czyTimerStoi==false){
                            mediaPlayer.start();
                        }
                        return true;
                    case MotionEvent.ACTION_UP:
                        if (!longClickPerformed) {
                            handler.removeCallbacksAndMessages(null);
                            mediaPlayer.stop();
                            mediaPlayer.release();
                            mediaPlayer = null;
                        }
                        longClickPerformed = false;
                        return true;
                }
                return false;
            }
        });
    }



    private void startTimer(long milliseconds) {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }

        if (mediaPlayerBeeps != null) {
            mediaPlayerBeeps.stop();
            mediaPlayerBeeps.release();
            mediaPlayerBeeps = null;
        }

        mediaPlayerBeeps = MediaPlayer.create(MainActivity.this, R.raw.beeps);
        mediaPlayerBeeps.setLooping(true);
        mediaPlayerBeeps.start();



        timer = new CountDownTimer(milliseconds, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                licznik.setText("" + millisUntilFinished / 1000);


            }

            @Override
            public void onFinish() {

                licznik.setText("wybuch");
                czyTimerStoi = true;

                MediaPlayer mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.exps);
                mediaPlayer.start();

                if (mediaPlayerBeeps != null) {
                    mediaPlayerBeeps.stop();
                    mediaPlayerBeeps.release();
                    mediaPlayerBeeps = null;
                }

                
            }
        }.start();
    }

    private void stopTimer() {
        if (timer != null) {
            timer.cancel();
            timer = null;

            licznik.setText("bomba rozbrojona");
            czyTimerStoi = true;
            MediaPlayer mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.hasdefs);
            mediaPlayer.start();

            if (mediaPlayerBeeps != null) {
                mediaPlayerBeeps.stop();
                mediaPlayerBeeps.release();
                mediaPlayerBeeps = null;
            }
           
        }
    }
}
