package lab.wasikrafal.psmaprojekt.fragments;

import android.app.Fragment;
import android.media.MediaRecorder;

import java.io.IOException;

public class VideoRecorderServiceFragment extends Fragment
{
    MediaRecorder mediaRecorder;
    String path;
    long length;
    boolean isRecording = false;


    public void startRecording(String path)
    {
        this.path = path;
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.MPEG_4_SP);
        mediaRecorder.setOutputFile(path);

        try
        {
            mediaRecorder.prepare();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        mediaRecorder.start();
        length = System.currentTimeMillis();
    }

    public void stopRecording()
    {
        mediaRecorder.stop();
        mediaRecorder.release();
        mediaRecorder = null;
    }

    public boolean isRecording()
    {
        return isRecording;
    }

    public long getLength()
    {
        return System.currentTimeMillis() - length;
    }

    public String getPath()
    {
        return path;
    }
}
