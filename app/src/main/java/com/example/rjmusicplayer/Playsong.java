package com.example.rjmusicplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class Playsong extends AppCompatActivity {

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
        mediaPlayer.release();
        updateseek.interrupt();
    }

    TextView textview;
    TextView currenttime;
    TextView totaltime;
    ImageView play, previous, next,imageView;
    ArrayList<File> songs;
    MediaPlayer mediaPlayer;
    String textcontent;
    int position;
    SeekBar seekbar;
    Thread updateseek;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playsong);
        textview = findViewById(R.id.textView);
        play = findViewById(R.id.play);
        previous = findViewById(R.id.previous);
        next = findViewById(R.id.next);
        seekbar = findViewById(R.id.seekbar);
        imageView = findViewById(R.id.imageView);

        Animation rotateAnimation = AnimationUtils.loadAnimation(this, R.anim.roatate);
        imageView.startAnimation(rotateAnimation);
        imageView.setColorFilter(Color.rgb(205,245,246), PorterDuff.Mode.MULTIPLY);
        imageView.setAlpha(0.7F);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        songs = (ArrayList) bundle.getParcelableArrayList("songlist");
        textcontent = intent.getStringExtra("currentsong");
        textview.setText(textcontent);
        textview.setSelected(true);
        position = intent.getIntExtra("position", 0);
        Uri uri = Uri.parse(songs.get(position).toString());
        mediaPlayer = MediaPlayer.create(this, uri);
        mediaPlayer.start();
        // int cutime = mediaPlayer.getDuration();
        //  currenttime.setText(cutime);

        rotate(imageView, 360f);
        seekbar.setMax(mediaPlayer.getDuration());
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());
            }
        });

        updateseek = new Thread() {
            @Override
            public void run() {
                int currentposition = 0;
                try {
                    while (currentposition < mediaPlayer.getDuration()) {
                        currentposition = mediaPlayer.getCurrentPosition();
                        seekbar.setProgress(currentposition);
                        sleep(800);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        updateseek.start();

        seekbar.getProgressDrawable().setColorFilter(getResources().getColor(R.color.purple_200), PorterDuff.Mode.MULTIPLY);
        seekbar.getThumb().setColorFilter(getResources().getColor(R.color.purple_200), PorterDuff.Mode.SRC_IN);

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer.isPlaying()) {
                    play.setImageResource(R.drawable.play);
                    rotateAnimation.cancel();
                    mediaPlayer.pause();
                } else {
                    play.setImageResource(R.drawable.pause);
                    rotateAnimation.cancel();
                    imageView.startAnimation(rotateAnimation);
                    mediaPlayer.start();
                }
            }
        });

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                mediaPlayer.release();
                if (position != 0) {
                    position = position - 1;
                } else {
                    position = songs.size() - 1;
                }

                Uri uri = Uri.parse(songs.get(position).toString());
                mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
                mediaPlayer.start();
                play.setImageResource(R.drawable.pause);
                seekbar.setMax(mediaPlayer.getDuration());
                textcontent = songs.get(position).getName().toString();
                textview.setText(textcontent);
                startanimation(imageView, -360f);
            }
        });


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                mediaPlayer.release();
                if (position != songs.size() - 1) {
                    position = position + 1;
                } else {
                    position = 0;
                }

                Uri uri = Uri.parse(songs.get(position).toString());
                mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
                mediaPlayer.start();
                play.setImageResource(R.drawable.pause);
                seekbar.setMax(mediaPlayer.getDuration());
                textcontent = songs.get(position).getName().toString();
                textview.setText(textcontent);
                startanimation(imageView, 360f);
            }
        });
    }

    public void startanimation (View view, Float degree){
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(imageView, "rotation", 0f, degree);
        objectAnimator.setDuration(1000);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(objectAnimator);
        animatorSet.start();
    }

    public void rotate (View view, Float degree){
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(imageView, "rotation", 0f, degree);
        objectAnimator.setDuration(mediaPlayer.getDuration());
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(objectAnimator);
        animatorSet.start();

    }

}