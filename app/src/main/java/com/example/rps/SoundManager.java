package com.example.rps;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class SoundManager extends Service {
    class Song {
        int resource;
        int loopPoint = 0;
        int volume = 100;
        boolean loop = false;

        Song(int resID) {
            this.resource = resID;
        }

        Song(int resID, int volume) {
            this.resource = resID;
            this.volume = volume;
        }

        Song(int resID, boolean loop) {
            this.resource = resID;
            this.loop = loop;
        }

        Song(int resID, int volume, boolean loop) {
            this.resource = resID;
            this.volume = volume;
            this.loop = loop;
        }

        Song(int resID, boolean loop, int loopPoint) {
            this.resource = resID;
            this.loop = loop;
            this.loopPoint = loopPoint;
        }

        Song(int resID, int volume, boolean loop, int loopPoint) {
            this.resource = resID;
            this.volume = volume;
            this.loop = loop;
            this.loopPoint = loopPoint;
        }
    }

    class SongPlayer {
        private MediaPlayer player;
        Song play_song;

        SongPlayer(Context ctx, Song song) {
            player = MediaPlayer.create(ctx, song.resource);
            play_song = song;
            player.setVolume(play_song.volume, play_song.volume);

            player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    if (play_song.loop) {
                        player.seekTo(play_song.loopPoint);
                        player.start();
                    }
                }
            });
        }

        void play() {
            player.start();
        }

        void seek(int msec) {
            player.seekTo(msec);
        }

        void pause() {
            player.pause();
        }

        void stop() {
            player.stop();
        }

        void release() {
            stop();
            player.release();
        }
    }

    SongPlayer[] player;
    SongPlayer[] sfx_player;
    int track = 0;

    Song[] songs = {
        new Song(R.raw.bgm_title, 30,true),
        new Song(R.raw.bgm_ingame, 30, true, 902)
    };
    Song[] sfx_sounds = {
        new Song(R.raw.sfx_shoot),
        new Song(R.raw.sfx_fanfare),
        new Song(R.raw.sfx_click),
        new Song(R.raw.sfx_ready),
        new Song(R.raw.sfx_fail),
        new Song(R.raw.sfx_draw),
        new Song(R.raw.sfx_change),
        new Song(R.raw.sfx_denied)
    };

    @Override
    public void onCreate() {
        super.onCreate();

        player = new SongPlayer[songs.length];
        for (int i=0;i<songs.length;i++)
            player[i] = new SongPlayer(this, songs[i]);

        sfx_player = new SongPlayer[sfx_sounds.length];
        for (int i=0;i<sfx_sounds.length;i++)
            sfx_player[i] = new SongPlayer(this, sfx_sounds[i]);
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        String cmd = intent.getStringExtra("cmd");

        if (cmd.equalsIgnoreCase("play"))
            player[track].play();
        else if (cmd.equalsIgnoreCase("pause"))
            player[track].pause();
        else if (cmd.equalsIgnoreCase("playtrack")) {
            track = intent.getIntExtra("track", 0);
            player[track].seek(0);
            player[track].play();
        } else if (cmd.equalsIgnoreCase("seek")) {
            int pos = intent.getIntExtra("msec", 0);
            player[track].seek(pos);
        } else if (cmd.equalsIgnoreCase("sfx")) {
            int sfx_id = intent.getIntExtra("id", 0);
            sfx_player[sfx_id].seek(0);
            sfx_player[sfx_id].play();
        }

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        for (int i=0;i<songs.length;i++)
            player[i].release();
        for (int i=0;i<sfx_sounds.length;i++)
            sfx_player[i].release();
    }

    @Override
    public void onLowMemory() {

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
