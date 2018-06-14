package lab.wasikrafal.psmaprojekt.fragments;

import android.Manifest;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import lab.wasikrafal.psmaprojekt.R;
import lab.wasikrafal.psmaprojekt.database.MediaDatabase;
import lab.wasikrafal.psmaprojekt.models.Movie;

public class VideoRecorderFragment extends Fragment
{
    VideoRecorderServiceFragment videoRecorderServiceFragment;
    MediaDatabase database;
    private String[] permissions = {Manifest.permission.RECORD_AUDIO, Manifest.permission.CAPTURE_VIDEO_OUTPUT, Manifest.permission.CAMERA};
    boolean isRecording = false;
    String path;

    @Override
    public void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        int resCode = 0;
        requestPermissions(permissions, resCode);

        database = MediaDatabase.getInstance(getActivity());

        if (getFragmentManager().findFragmentByTag("video_service") == null) {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            videoRecorderServiceFragment = new VideoRecorderServiceFragment();
            ft.add(videoRecorderServiceFragment, "video_service").commit();
        } else {
            videoRecorderServiceFragment = (VideoRecorderServiceFragment) getFragmentManager().findFragmentByTag("video_service");
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_video_recorder, container, false);
        final Button record = (Button) view.findViewById(R.id.but_record);
        if (videoRecorderServiceFragment.isRecording())
        {
            isRecording = true;
            record.setText("stop");
        }
        record.setOnClickListener(new View.OnClickListener()
        {
            boolean isRecording = false;

            @Override
            public void onClick(View view)
            {
                if (!isRecording) {
                    startRecording();
                    isRecording = true;
                    record.setText("stop");
                } else {
                    stopRecording();
                    isRecording = false;
                    record.setText("start");
                }
            }
        });

        return view;

    }

    private void startRecording()
    {
        Calendar currentTime = Calendar.getInstance();
        SimpleDateFormat time = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.ENGLISH);
        String filePath = "PSMA/video/" + time.format(currentTime.getTime()) + ".mp4";

        File videofile = new File(Environment.getExternalStorageDirectory(),filePath);
        try {
            videofile.createNewFile();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        path = videofile.getAbsolutePath();
        videoRecorderServiceFragment.startRecording(path);

    }

    private void stopRecording()
    {
        videoRecorderServiceFragment.stopRecording();
        final Movie movie= new Movie();
        movie.fileName = videoRecorderServiceFragment.getPath();
        movie.date = new Date();
        movie.length = videoRecorderServiceFragment.getLength();

        //dialog
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.dialog_video_recorded);

        final EditText title = (EditText) dialog.findViewById(R.id.et_video_title);
        final EditText description = (EditText) dialog.findViewById(R.id.et_video_desc);
        Button okButton = (Button) dialog.findViewById(R.id.but_vid_rec_ok);
        okButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                //movie.thumbnail = saveThumbnail(path);

                movie.title = title.getText().toString();
                movie.description = description.getText().toString();
                database.movieDAO().insertMovie(movie);
                dialog.dismiss();
            }
        });
        dialog.show();
        Window window = dialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    private String saveThumbnail(String path)
    {
        String thumbnailPath = path + ".png";
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();

        mediaMetadataRetriever.setDataSource(path);
        Bitmap bmFrame = mediaMetadataRetriever.getFrameAtTime(1);

        FileOutputStream out = null;
        try {
            out = new FileOutputStream(thumbnailPath);
            bmFrame.compress(Bitmap.CompressFormat.PNG, 100, out);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return thumbnailPath;
    }

    @Override
    public void onStop()
    {
        super.onStop();
    }
}
