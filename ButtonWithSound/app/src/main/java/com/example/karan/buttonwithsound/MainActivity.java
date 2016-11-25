package com.example.karan.buttonwithsound;

import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final MediaPlayer clickSoundMP = MediaPlayer.create(this, R.raw.mouse_click);

        Button playClickAdd = (Button) findViewById(R.id.btnAdd);
        Button playClickDelete = (Button) findViewById(R.id.btnDelete);

        playClickAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickSoundMP.start();
            }
        });

        playClickDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickSoundMP.start();
            }
        });
    }
}
