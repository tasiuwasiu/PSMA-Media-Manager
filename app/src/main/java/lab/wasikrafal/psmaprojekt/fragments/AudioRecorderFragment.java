package lab.wasikrafal.psmaprojekt.fragments;

import android.Manifest;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.arch.persistence.room.Room;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import lab.wasikrafal.psmaprojekt.R;
import lab.wasikrafal.psmaprojekt.database.MediaDatabase;
import lab.wasikrafal.psmaprojekt.models.AudioCategory;
import lab.wasikrafal.psmaprojekt.models.Recording;

public class AudioRecorderFragment extends Fragment
{

    private String[] permissions = {Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    MediaDatabase database;
    String path;
    boolean isRecording = false;
    AudioRecorderServiceFragment audioRecorderServiceFragment;
    private static final int RESPONSE = 2;
    Button record;

    @Override
    public void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        requestPermissions(permissions, RESPONSE);
        database = MediaDatabase.getInstance(getActivity());

        if (getFragmentManager().findFragmentByTag("service_fragment") == null) {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            audioRecorderServiceFragment = new AudioRecorderServiceFragment();
            ft.add(audioRecorderServiceFragment, "service_fragment").commit();
        } else {
            audioRecorderServiceFragment = (AudioRecorderServiceFragment) getFragmentManager().findFragmentByTag("service_fragment");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {

        View view = inflater.inflate(R.layout.fragment_audio_recorder, container, false);
        record = (Button) view.findViewById(R.id.but_aud_rec);
        final TextView status = (TextView) view.findViewById(R.id.tv_audio_recording_status);
        if (audioRecorderServiceFragment.isRecording()) {
            isRecording = true;
            record.setText(R.string.aud_rec_stop_recording);
            status.setText(R.string.aud_rec_recording);
        }
        record.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (isRecording) {
                    stopRecording();
                    isRecording = false;
                    status.setText("");
                    record.setText(R.string.aud_rec_start_recording);
                } else {
                    startRecording();
                    isRecording = true;
                    status.setText(R.string.aud_rec_recording);
                    record.setText(R.string.aud_rec_stop_recording);
                }
            }
        });

        return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults)
    {
        switch (requestCode) {
            case RESPONSE: {
                if (grantResults.length > 1
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {

                    record.setEnabled(true);

                } else {
                    record.setEnabled(false);
                }
            }
        }
    }

    private void startRecording()
    {
        Calendar currentTime = Calendar.getInstance();
        SimpleDateFormat time = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.ENGLISH);
        String filePath = "PSMA/audio/" + time.format(currentTime.getTime()) + ".3gp";

        File audiofolder = new File(Environment.getExternalStorageDirectory(), "PSMA/audio/");
        if (!audiofolder.exists())
            audiofolder.mkdirs();


        File audiofile = new File(Environment.getExternalStorageDirectory(), filePath);
        try {
            audiofile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        path = audiofile.getAbsolutePath();
        audioRecorderServiceFragment.startRecording(path);
    }

    private void stopRecording()
    {
        audioRecorderServiceFragment.stopRecording();
        final Recording recording = new Recording();
        recording.filename = audioRecorderServiceFragment.getPath();
        recording.date = new Date();
        recording.length = audioRecorderServiceFragment.getLength();

        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.dialog_audio_recorded);

        final Spinner spinner = (Spinner) dialog.findViewById(R.id.sp_aud_categories);
        ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_dropdown_item, database.audioCategoryDAO().loadAllCategories());
        spinner.setAdapter(spinnerArrayAdapter);

        final EditText title = (EditText) dialog.findViewById(R.id.et_audio_title);
        final EditText description = (EditText) dialog.findViewById(R.id.et_audio_desc);
        Button okButton = (Button) dialog.findViewById(R.id.but_aud_rec_ok);
        okButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                recording.description = description.getText().toString();
                recording.title = title.getText().toString();
                AudioCategory curr = (AudioCategory) spinner.getSelectedItem();
                recording.catID = curr.categoryId;
                database.recordingDAO().insertRecording(recording);
                dialog.dismiss();
            }
        });

        dialog.show();
        Window window = dialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    public void onStop()
    {
        super.onStop();

    }

}
