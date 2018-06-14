package lab.wasikrafal.psmaprojekt.activities;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.MediaController;

import lab.wasikrafal.psmaprojekt.R;

public class AudioPlayerActivity extends AppCompatActivity implements MediaPlayer.OnPreparedListener, MediaController.MediaPlayerControl
{
    MediaPlayer player;
    String path;
    MediaController controller;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_player);
        handler = new Handler();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            path = extras.getString("filepath");
            initPlayer();
        } else
            finish();
        if (savedInstanceState!=null)
        {
            player.seekTo(savedInstanceState.getInt("time"));
            player.pause();
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outBundle)
    {
        super.onSaveInstanceState(outBundle);
        outBundle.putInt("time", player.getCurrentPosition());
    }

    private void initPlayer()
    {
        player = new MediaPlayer();
        player.setOnPreparedListener(this);
        controller = new MediaController(this){
            @Override
            public void show(int timeout)
            {
                super.show(0);
            }
            @Override
            public boolean dispatchKeyEvent(KeyEvent event)
            {
                if (event.getKeyCode() == KeyEvent.KEYCODE_BACK)
                    ((Activity) getContext()).finish();
                return super.dispatchKeyEvent(event);
            }
        };
        try {
            player.setDataSource(path);
            player.prepare();
            player.start();
        } catch (Exception e) {
            finish();
        }
    }

    @Override
    public void onStop()
    {
        super.onStop();
        controller.hide();
        player.stop();
        player.release();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        controller.show();
        return false;
    }

    @Override
    public void start()
    {
        player.start();
    }

    @Override
    public void pause()
    {
        player.pause();
    }

    @Override
    public int getDuration()
    {
        return player.getDuration();
    }

    @Override
    public int getCurrentPosition()
    {
        return player.getCurrentPosition();
    }

    @Override
    public void seekTo(int i)
    {
        player.seekTo(i);
    }

    @Override
    public boolean isPlaying()
    {
        return player.isPlaying();
    }

    @Override
    public int getBufferPercentage()
    {
        return 0;
    }

    @Override
    public boolean canPause()
    {
        return true;
    }

    @Override
    public boolean canSeekBackward()
    {
        return true;
    }

    @Override
    public boolean canSeekForward()
    {
        return true;
    }

    @Override
    public int getAudioSessionId()
    {
        return 0;
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        controller.setMediaPlayer(this);
        controller.setAnchorView(findViewById(R.id.layout_audio_player));

        handler.post(new Runnable() {
            public void run() {
                controller.setEnabled(true);
                controller.show();
            }
        });
    }
}
