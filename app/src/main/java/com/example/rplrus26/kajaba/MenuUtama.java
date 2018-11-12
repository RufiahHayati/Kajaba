package com.example.rplrus26.kajaba;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MenuUtama extends AppCompatActivity {
    TextView menu;
    Button btnlihat;
    ProgressBar putar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_utama);

        menu = (TextView)findViewById(R.id.menuutama);
        btnlihat = (Button)findViewById(R.id.Lihatdata);
        putar = (ProgressBar)findViewById(R.id.progressBarmutar);

        btnlihat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuUtama.this,ListViewku.class);
                startActivity(intent);
            }
        });
    }
}
