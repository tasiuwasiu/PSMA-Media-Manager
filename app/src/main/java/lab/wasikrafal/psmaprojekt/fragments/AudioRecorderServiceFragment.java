package lab.wasikrafal.psmaprojekt.fragments;

import android.app.Fragment;
import android.media.MediaRecorder;
import android.os.Bundle;

import java.io.IOException;

public class AudioRecorderServiceFragment extends Fragment
{
    boolean isRecording = false;
    String path;
    long length;

    MediaRecorder mediaRecorder;

    @Override
    public void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        setRetainInstance(true);
    }

    public void startRecording(String path)
    {
        this.path = path;
        isRecording = true;
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        mediaRecorder.setOutputFile(path);
        length = System.currentTimeMillis();
        try
        {
            mediaRecorder.prepare();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        mediaRecorder.start();
    }

    public void stopRecording()
    {
        isRecording = false;
        mediaRecorder.stop();
        mediaRecorder.reset();
        mediaRecorder.release();
        mediaRecorder = null;
    }

    @Override
    public void onStop()
    {
        super.onStop();
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        if (mediaRecorder!= null)
        {
            mediaRecorder.release();
            mediaRecorder = null;
        }
    }

    public String getPath()
    {
        return path;
    }

    public boolean isRecording()
    {
        return isRecording;
    }

    public long getLength()
    {
        return System.currentTimeMillis() - length;
    }
}
