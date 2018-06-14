package lab.wasikrafal.psmaprojekt.activities;

import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import lab.wasikrafal.psmaprojekt.R;

public class AudioPlayerActivity extends AppCompatActivity
{
    MediaPlayer player;
    String path;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_player);
        Bundle extras = getIntent().getExtras();
        if (extras != null)
        {
            path = extras.getString("filepath");
            initPlayer();
        }
        else
            finish();

    }

    private void initPlayer()
    {

    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        try
        {
            player.stop();
            player.release();
            player = null;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
