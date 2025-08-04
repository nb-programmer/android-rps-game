package com.example.rps;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class AppLandingActivity extends AppCompatActivity {
    void playPingSound() {
        Intent musicPlayer = new Intent(AppLandingActivity.this, SoundManager.class);
        musicPlayer.putExtra("cmd", "sfx");
        musicPlayer.putExtra("id", 2);
        startService(musicPlayer);
    }
    void playChangeSound() {
        Intent musicPlayer = new Intent(AppLandingActivity.this, SoundManager.class);
        musicPlayer.putExtra("cmd", "sfx");
        musicPlayer.putExtra("id", 6);
        startService(musicPlayer);
    }

    void playDenySound() {
        Intent musicPlayer = new Intent(AppLandingActivity.this, SoundManager.class);
        musicPlayer.putExtra("cmd", "sfx");
        musicPlayer.putExtra("id", 7);
        startService(musicPlayer);
    }

    enum TitleMenuView {
        MAINMENU,
        SETTINGSMENU,
        QUITMENU
    }

    TitleMenuView state = TitleMenuView.MAINMENU;
    int maxRounds = 3;

    void backNavigate() {
        if (state == TitleMenuView.MAINMENU) {
            LinearLayout main_layout = findViewById(R.id.layout_title_main);
            LinearLayout quit_layout = findViewById(R.id.layout_title_quit);
            main_layout.setVisibility(View.INVISIBLE);
            quit_layout.setVisibility(View.VISIBLE);
            state = TitleMenuView.QUITMENU;
        } else if (state == TitleMenuView.QUITMENU) {
            LinearLayout main_layout = findViewById(R.id.layout_title_main);
            LinearLayout quit_layout = findViewById(R.id.layout_title_quit);
            main_layout.setVisibility(View.VISIBLE);
            quit_layout.setVisibility(View.INVISIBLE);
            state = TitleMenuView.MAINMENU;
        } else if (state == TitleMenuView.SETTINGSMENU) {
            LinearLayout main_layout = findViewById(R.id.layout_title_main);
            LinearLayout settings_layout = findViewById(R.id.layout_title_settings);
            main_layout.setVisibility(View.VISIBLE);
            settings_layout.setVisibility(View.INVISIBLE);
            state = TitleMenuView.MAINMENU;
        }

        playPingSound();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_landing);

        Intent musicPlayer = new Intent(this, SoundManager.class);
        musicPlayer.putExtra("cmd", "create");
        startService(musicPlayer);

        ((TextView)findViewById(R.id.tv_title_numrnds)).setText(((Integer)maxRounds).toString());

        findViewById(R.id.btn_title_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playPingSound();
                Intent startGameIntent = new Intent(AppLandingActivity.this, MainGameActivity.class);
                startGameIntent.putExtra("numNeededWins", maxRounds);
                startActivity(startGameIntent);
            }
        });

        findViewById(R.id.btn_title_settings).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout main_layout = findViewById(R.id.layout_title_main);
                LinearLayout settings_layout = findViewById(R.id.layout_title_settings);
                main_layout.setVisibility(View.INVISIBLE);
                settings_layout.setVisibility(View.VISIBLE);

                state = TitleMenuView.SETTINGSMENU;
                playPingSound();
            }
        });

        findViewById(R.id.btn_title_quit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backNavigate();
            }
        });

        findViewById(R.id.btn_title_quit_no).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backNavigate();
            }
        });

        findViewById(R.id.btn_title_quit_yes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        findViewById(R.id.btn_title_incrnds).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (maxRounds < 5) {
                    maxRounds++;
                    playChangeSound();
                } else
                    playDenySound();

                ((TextView)findViewById(R.id.tv_title_numrnds)).setText(((Integer)maxRounds).toString());
            }
        });

        findViewById(R.id.btn_title_decrnds).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (maxRounds > 2) {
                    maxRounds--;
                    playChangeSound();
                } else
                    playDenySound();

                ((TextView)findViewById(R.id.tv_title_numrnds)).setText(((Integer)maxRounds).toString());
            }
        });

        findViewById(R.id.btn_title_settings_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backNavigate();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        Intent musicPlay = new Intent(this, SoundManager.class);
        musicPlay.putExtra("cmd", "playtrack");
        musicPlay.putExtra("track", 0);
        startService(musicPlay);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Intent musicPause = new Intent(this, SoundManager.class);
        musicPause.putExtra("cmd", "pause");
        startService(musicPause);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        backNavigate();
    }
}
