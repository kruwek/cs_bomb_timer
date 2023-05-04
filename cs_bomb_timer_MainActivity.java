//its garbage, I know

package com.example.bomb_timer_csgo_3;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private CountDownTimer timer;
    private Button plantButton;
    private Button defButton;
    private TextView licznik;
    private MediaPlayer mediaPlayerBeeps;
    private TextView countText;
    private CountDownTimer deftimer;

    private boolean czyTimerStoi = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        plantButton = findViewById(R.id.plant);
        defButton = findViewById(R.id.def);
        licznik = findViewById(R.id.licznik);
        EditText czasBomby = findViewById(R.id.czas_bomby);
        countText = findViewById(R.id.deflicznik);

        plantButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String czasString = czasBomby.getText().toString();

                if (czyTimerStoi == true) {
                    mediaPlayerBeeps = MediaPlayer.create(MainActivity.this, R.raw.beeps4);
                    try {
                        int czasMilisekundy = Integer.parseInt(czasString) * 1000;
                        if (czasMilisekundy == 2137000){
                            MediaPlayer mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.rakietas);
                            mediaPlayer.start();
                            Toast.makeText(MainActivity.this, "O_O", Toast.LENGTH_SHORT).show();
                        } else if (czasMilisekundy > mediaPlayerBeeps.getDuration() || czasMilisekundy==0) {
                            
                            MediaPlayer mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.nos);
                            mediaPlayer.start();
                            Toast.makeText(MainActivity.this, "Wybrany czas jest nieprawidłowy, maxymalny czas to " + mediaPlayerBeeps.getDuration() / 1000 + "sekund", Toast.LENGTH_SHORT).show();
                        } else {
                        
                            MediaPlayer mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.plants);
                            mediaPlayer.start();
                            mediaPlayerBeeps.seekTo(mediaPlayerBeeps.getDuration() - czasMilisekundy);
                            mediaPlayerBeeps.start();
                            startTimer(czasMilisekundy);
                            czyTimerStoi = false;
                        }
                    } catch (NumberFormatException e) {
                       
                        MediaPlayer mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.nos);
                        mediaPlayer.start();
                        Toast.makeText(MainActivity.this, "Podaj prawidłową wartość czasu", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(MainActivity.this, "bomba jest już podłożona", Toast.LENGTH_SHORT).show();
                    MediaPlayer mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.nos);
                    mediaPlayer.start();
                }
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
                            countText.setVisibility(View.VISIBLE);
                            startTimer();
                            mediaPlayer.start();
                        }
                        return true;
                    case MotionEvent.ACTION_UP:
                        resetTimer();
                        countText.setVisibility(View.INVISIBLE);
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

//kolejny licznik

    private void startTimer() {
        if (deftimer != null) {
            deftimer.cancel();
        }

        deftimer = new CountDownTimer(5000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int count = (int) millisUntilFinished / 1000;
                countText.setText(String.valueOf(count+1));
            }

            @Override
            public void onFinish() {
                
                resetTimer();
                countText.setVisibility(View.INVISIBLE);
            }
        }.start();
    }

    private void resetTimer() {
        if (deftimer != null) {
            deftimer.cancel();
            deftimer = null;
        }
    }

}
