package lab.wasikrafal.psmaprojekt;

import android.Manifest;
import android.app.Fragment;
import android.content.pm.PackageManager;
import android.graphics.Camera;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class VideoRecorderFragment extends Fragment
{
    MediaRecorder mediaRecorder;
    Camera camera;
    Preview preview;

    private String [] permissions = {Manifest.permission.RECORD_AUDIO, Manifest.permission.CAPTURE_VIDEO_OUTPUT, Manifest.permission.CAMERA};



    @Override
    public void onCreate(Bundle bundle)
    {
        int resCode=0;
        requestPermissions( permissions, resCode);

        super.onCreate(bundle);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_video_recorder, container, false);
        Button record = (Button) view.findViewById(R.id.but_record);
        record.setOnClickListener(new View.OnClickListener()
        {
            boolean isRecording = false;
            @Override
            public void onClick(View view)
            {
                  if (!isRecording)
                  {
                      startRecording();
                      isRecording = true;
                  }
                  else
                  {
                      stopRecording();
                      isRecording = false;
                  }
            }
        });

        preview = view.findViewById(R.id.preview);
        return view;

    }

    private void startRecording()
    {
        Calendar currentTime = Calendar.getInstance();
        SimpleDateFormat time = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.ENGLISH);
        String filePath = MainActivity.PATH + time.format(currentTime.getTime());

        mediaRecorder = new MediaRecorder();
        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.MPEG_4_SP);
        mediaRecorder.setOutputFile(filePath);
        mediaRecorder.setPreviewDisplay(preview.holder.getSurface());

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
        mediaRecorder.release();
        mediaRecorder = null;
    }

    @Override
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
