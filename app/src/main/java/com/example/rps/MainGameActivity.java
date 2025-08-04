package com.example.rps;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class MainGameActivity extends AppCompatActivity {
    //-1 = Player lost, 0 = Draw, 1 = Player won
    int gameOutcomes[][] = {
                        //Player, CPU
        {
                0,      //Rock, Rock
                -1,     //Rock, Paper
                1       //Rock, Scissors
        },
        {
                1,      //Paper, Rock
                0,      //Paper, Paper
                -1      //Paper, Scissors
        },
        {
                -1,     //Scissors, Rock
                1,      //Scissors, Paper
                0       //Scissors, Scissors
        }
    };


    int totalWinCount = 3;

    int playerGameWins = 0;
    int cpuGameWins = 0;

    boolean canPlay = false;

    int playerRoundResources[] = {R.id.img_player_win1, R.id.img_player_win2, R.id.img_player_win3, R.id.img_player_win4, R.id.img_player_win5};
    int cpuRoundResources[] = {R.id.img_cpu_win1, R.id.img_cpu_win2, R.id.img_cpu_win3, R.id.img_cpu_win4, R.id.img_cpu_win5};

    void setPlayersTotalRoundViews() {
        for (int i=0;i<playerRoundResources.length;i++)
            findViewById(playerRoundResources[i]).setVisibility(i < totalWinCount ? View.VISIBLE : View.INVISIBLE);
        for (int i=0;i<cpuRoundResources.length;i++)
            findViewById(cpuRoundResources[i]).setVisibility(i < totalWinCount ? View.VISIBLE : View.INVISIBLE);
    }

    void setPlayersViewState() {
        for (int i=0;i<playerRoundResources.length;i++)
            ((ImageView)findViewById(playerRoundResources[i])).setImageDrawable(getDrawable(i < playerGameWins ? R.drawable.wins_haswon : R.drawable.wins_outer));
        for (int i=0;i<cpuRoundResources.length;i++)
            ((ImageView)findViewById(cpuRoundResources[i])).setImageDrawable(getDrawable(i < cpuGameWins ? R.drawable.wins_haswon : R.drawable.wins_outer));
    }

    public int playerHasWon(int playerMove, int cpuMove) {
        return gameOutcomes[playerMove][cpuMove];
    }

    private Animation roundTextAnimation1() {
        Animation inFromTop = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, -0.7f,
                Animation.RELATIVE_TO_PARENT, 0.0f);
        inFromTop.setDuration(500);
        inFromTop.setStartOffset(400);
        inFromTop.setInterpolator(new DecelerateInterpolator());
        return inFromTop;
    }
    private Animation roundTextAnimation2() {
        Animation inFromTop = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 1.0f);
        inFromTop.setDuration(500);
        inFromTop.setStartOffset(1300);
        inFromTop.setInterpolator(new AccelerateInterpolator());
        return inFromTop;
    }

    private void animateReadyText() {
        Animation rndStart = roundTextAnimation1();

        rndStart.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                findViewById(R.id.tv_rndCount).setVisibility(View.VISIBLE);
                canPlay = false;
                Intent sfx_rndReadyIntent = new Intent(MainGameActivity.this, SoundManager.class);
                sfx_rndReadyIntent.putExtra("cmd", "sfx");
                sfx_rndReadyIntent.putExtra("id", 3);
                startService(sfx_rndReadyIntent);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Animation rndAnimationOut = roundTextAnimation2();
                rndAnimationOut.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) { }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        findViewById(R.id.tv_rndCount).setVisibility(View.INVISIBLE);
                        canPlay = true;
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) { }
                });

                findViewById(R.id.tv_rndCount).startAnimation(rndAnimationOut);
            }

            @Override
            public void onAnimationRepeat(Animation animation) { }
        });

        findViewById(R.id.tv_rndCount).bringToFront();
        findViewById(R.id.tv_rndCount).startAnimation(rndStart);
    }

    private void animateReadyWinLoseText() {
        Animation rndStart = roundTextAnimation1();

        rndStart.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                findViewById(R.id.tv_rndCount).setVisibility(View.VISIBLE);
                canPlay = false;
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Animation rndAnimationOut = roundTextAnimation2();
                rndAnimationOut.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) { }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        findViewById(R.id.tv_rndCount).setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) { }
                });

                findViewById(R.id.tv_rndCount).startAnimation(rndAnimationOut);
            }

            @Override
            public void onAnimationRepeat(Animation animation) { }
        });

        findViewById(R.id.tv_rndCount).bringToFront();
        findViewById(R.id.tv_rndCount).startAnimation(rndStart);
    }

    private class RPSClickListener implements View.OnClickListener, View.OnLongClickListener {
        Random rng = new Random();

        @Override
        public void onClick(View v) {
            if (!canPlay) return;

            int btnID = v.getId();

            //Rock = 0, paper = 1, scissors = 2
            int playerMove = btnID == R.id.btn_player_rock ? 0 : (btnID == R.id.btn_player_paper ? 1 : 2);
            int cpuMove = rng.nextInt(3);

            int playerMoveImg = playerMove == 0 ? R.drawable.hand_rock : (playerMove == 1 ? R.drawable.hand_paper : R.drawable.hand_scissors);
            int cpuMoveImg = cpuMove == 0 ? R.drawable.hand_rock : (cpuMove == 1 ? R.drawable.hand_paper : R.drawable.hand_scissors);

            ((ImageView)findViewById(R.id.img_player_play)).setImageDrawable(getDrawable(playerMoveImg));
            ((ImageView)findViewById(R.id.img_cpu_play)).setImageDrawable(getDrawable(cpuMoveImg));

            int outcome = playerHasWon(playerMove, cpuMove);
            int sfx_id = -1;

            if (outcome == -1) {
                cpuGameWins++;
                sfx_id = 0;
            } else if (outcome == 1) {
                playerGameWins++;
                sfx_id = 2;
            } else if (outcome == 0) {
                sfx_id = 5;
            }

            if (playerGameWins == totalWinCount) {
                sfx_id = 1;
                ((TextView)findViewById(R.id.tv_rndCount)).setText("You Won!");
                animateReadyWinLoseText();
            } else if (cpuGameWins == totalWinCount) {
                sfx_id = 4;
                ((TextView)findViewById(R.id.tv_rndCount)).setText("You Lost");
                animateReadyWinLoseText();
            }

            if (sfx_id != -1) {
                Intent musicPlayer = new Intent(MainGameActivity.this, SoundManager.class);
                musicPlayer.putExtra("cmd", "sfx");
                musicPlayer.putExtra("id", sfx_id);
                startService(musicPlayer);
            }

            setPlayersViewState();

            //Toast.makeText(MainGameActivity.this, (outcome == -1 ? "You have lost :(" : (outcome == 0 ? "Draw game" : "You won!")), Toast.LENGTH_LONG).show();
        }

        @Override
        public boolean onLongClick(View v) {
            int btnID = v.getId();

            switch (btnID) {
                case R.id.btn_player_rock:
                    Toast.makeText(MainGameActivity.this, "Rock beats scissors, paper beats rock", Toast.LENGTH_LONG).show();
                    break;
                case R.id.btn_player_paper:
                    Toast.makeText(MainGameActivity.this, "Paper beats rock, scissors beats paper", Toast.LENGTH_LONG).show();
                    break;
                case R.id.btn_player_scis:
                    Toast.makeText(MainGameActivity.this, "Scissors beats paper, rock beats scissors", Toast.LENGTH_LONG).show();
                    break;
            }

            return true;
        }
    }

    void resetGameState() {
        playerGameWins = 0;
        cpuGameWins = 0;
        canPlay = false;
        ((ImageView)findViewById(R.id.img_player_play)).setImageDrawable(null);
        ((ImageView)findViewById(R.id.img_cpu_play)).setImageDrawable(null);
        setPlayersViewState();
    }

    void gameStart() {
        resetGameState();

        Intent musicPlay = new Intent(this, SoundManager.class);
        musicPlay.putExtra("cmd", "playtrack");
        musicPlay.putExtra("track", 1);
        startService(musicPlay);

        ((TextView)findViewById(R.id.tv_rndCount)).setText("Ready?");
        animateReadyText();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maingame);


        Intent musicPlayer = new Intent(this, SoundManager.class);
        musicPlayer.putExtra("cmd", "create");
        startService(musicPlayer);

        Intent incoming = getIntent();

        totalWinCount = incoming.getIntExtra("numNeededWins", 3);

        resetGameState();

        setPlayersTotalRoundViews();

        findViewById(R.id.btn_ingame_restart).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetGameState();
                gameStart();
            }
        });

        findViewById(R.id.btn_ingame_exit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        findViewById(R.id.btn_player_rock).setOnClickListener(new RPSClickListener());
        findViewById(R.id.btn_player_paper).setOnClickListener(new RPSClickListener());
        findViewById(R.id.btn_player_scis).setOnClickListener(new RPSClickListener());
        findViewById(R.id.btn_player_rock).setOnLongClickListener(new RPSClickListener());
        findViewById(R.id.btn_player_paper).setOnLongClickListener(new RPSClickListener());
        findViewById(R.id.btn_player_scis).setOnLongClickListener(new RPSClickListener());
    }

    @Override
    protected void onResume() {
        super.onResume();

        gameStart();
        Intent musicPlay = new Intent(this, SoundManager.class);
        musicPlay.putExtra("cmd", "play");
        startService(musicPlay);
    }

    @Override
    protected void onPause() {
        super.onPause();

        Intent musicPause = new Intent(this, SoundManager.class);
        musicPause.putExtra("cmd", "pause");
        startService(musicPause);
    }
}
