package lab.wasikrafal.psmaprojekt.fragments;

import android.Manifest;
import android.app.Fragment;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import lab.wasikrafal.psmaprojekt.R;

public class AudioRecorderFragment extends Fragment
{
    MediaRecorder mediaRecorder;
    private String [] permissions = {Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE};

    @Override
    public void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        int resCode=0;
        requestPermissions( permissions, resCode);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {

        View view = inflater.inflate(R.layout.fragment_audio_recorder, container, false);
        final Button record = (Button) view.findViewById(R.id.but_aud_rec);
        final TextView status = (TextView) view.findViewById(R.id.tv_audio_recording_status);
        record.setOnClickListener(new View.OnClickListener()
        {
            boolean isRecording = false;

            @Override
            public void onClick(View view)
            {
                if(isRecording)
                {
                    stopRecording();
                    isRecording = false;
                    status.setText(R.string.aud_rec_not_recording);
                    record.setText(R.string.aud_rec_start_recording);
                }
                else
                {
                    startRecording();
                    isRecording = true;
                    status.setText(R.string.aud_rec_recording);
                    record.setText(R.string.aud_rec_stop_recording);
                }
            }
        });

        return view;
    }

    private void startRecording()
    {
        Calendar currentTime = Calendar.getInstance();
        SimpleDateFormat time = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.ENGLISH);
        String filePath = "PSMA/audio/" + time.format(currentTime.getTime()) + ".3gp";

        File audiofile = new File(Environment.getExternalStorageDirectory(),filePath);
        try {
            audiofile.createNewFile();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        mediaRecorder.setOutputFile(audiofile.getAbsolutePath());
        Log.d("path", audiofile.getAbsolutePath());
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

    private void stopRecording()
    {
        mediaRecorder.stop();
        mediaRecorder.reset();
        mediaRecorder.release();
        mediaRecorder = null;
    }

    public void onStop()
    {
        super.onStop();
        if (mediaRecorder!= null)
        {
            mediaRecorder.release();
            mediaRecorder = null;
        }
    }

}
