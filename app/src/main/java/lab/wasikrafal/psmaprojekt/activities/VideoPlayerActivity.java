package lab.wasikrafal.psmaprojekt.activities;

import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.MediaController;
import android.widget.VideoView;

import lab.wasikrafal.psmaprojekt.R;
import lab.wasikrafal.psmaprojekt.libraries.PlayerController;

public class VideoPlayerActivity extends AppCompatActivity implements PlayerController.MediaPlayerControl, MediaPlayer.OnCompletionListener
{
    VideoView videoView;
    String path;
    //MediaController controller;
    PlayerController controller;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_video_player);

        videoView = (VideoView) findViewById(R.id.vv_video_player);

        handler = new Handler();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            path = extras.getString("filepath");
            initPlayer();
        } else
            finish();
        if (savedInstanceState != null) {
            videoView.seekTo(savedInstanceState.getInt("time"));
            videoView.pause();
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outBundle)
    {
        super.onSaveInstanceState(outBundle);
        outBundle.putInt("time", videoView.getCurrentPosition());
    }

    private void initPlayer()
    {
        videoView.setVideoPath(path);
        videoView.setOnCompletionListener(this);
        controller = new PlayerController(this);
        controller.setMediaPlayer(this);
        controller.setAnchorView((FrameLayout)findViewById(R.id.layout_video_player));
        //videoView.setMediaController(controller);
        controller.setEnabled(true);
        controller.show();
        videoView.start();
    }

    @Override
    public void onStop()
    {
        super.onStop();
        controller.hide();
        videoView.pause();
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
        videoView.start();
    }

    @Override
    public void pause()
    {
        videoView.pause();
    }

    @Override
    public int getDuration()
    {
        return videoView.getDuration();
    }

    @Override
    public int getCurrentPosition()
    {
        return videoView.getCurrentPosition();
    }

    @Override
    public void seekTo(int i)
    {
        videoView.seekTo(i);
    }

    @Override
    public boolean isPlaying()
    {
        return videoView.isPlaying();
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
    public void onCompletion(MediaPlayer mediaPlayer)
    {
        controller.updatePausePlay();
    }
}
