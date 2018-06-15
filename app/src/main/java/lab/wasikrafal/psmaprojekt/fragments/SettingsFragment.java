package lab.wasikrafal.psmaprojekt.fragments;

import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.Arrays;
import java.util.List;

import lab.wasikrafal.psmaprojekt.R;
import lab.wasikrafal.psmaprojekt.activities.MainActivity;
import lab.wasikrafal.psmaprojekt.database.MediaDatabase;
import lab.wasikrafal.psmaprojekt.models.AudioCategory;

public class SettingsFragment extends Fragment
{
    MainActivity main;

    int videoSort;
    int audioSort;

    String[] sortTypes = {"Tytuł", "Data wykonania", "Długość"};
    int[] sortCodes = {MainActivity.SORT_NAME, MainActivity.SORT_DATE, MainActivity.SORT_LENGTH};

    @Override
    public void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        main = (MainActivity) getActivity();
        loadData();
    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        Button movie = (Button)view.findViewById(R.id.but_set_movie_sort);
        movie.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                setVideoSorting();
            }
        });

        Button record = (Button)view.findViewById(R.id.but_set_audio_sort);
        record.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                setAudioSorting();
            }
        });

        Button categories = (Button)view.findViewById(R.id.but_set_reset_cat);
        categories.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                resetCategories();
            }
        });

        return view;
    }

    private void loadData()
    {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("preferences", Context.MODE_PRIVATE);
        videoSort = sharedPreferences.getInt("videoSort", MainActivity.SORT_NAME);
        audioSort = sharedPreferences.getInt("audioSort", MainActivity.SORT_NAME);
    }

    private void saveData()
    {

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("videoSort", videoSort);
        editor.putInt("audioSort", audioSort);
        editor.apply();
    }

    private void setVideoSorting()
    {
        //dialog
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.dialog_change_sort);

        final Spinner spinner = (Spinner) dialog.findViewById(R.id.sp_sort_types);
        ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_dropdown_item, sortTypes);
        spinner.setAdapter(spinnerArrayAdapter);

        Button okButton = (Button) dialog.findViewById(R.id.but_set_sort_ok);
        okButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                videoSort = sortCodes[spinner.getSelectedItemPosition()];
                main.setVideoSort(videoSort);
                saveData();
                dialog.dismiss();
            }
        });

        dialog.show();
        Window window = dialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

    }

    private void setAudioSorting()
    {
        //dialog
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.dialog_change_sort);

        final Spinner spinner = (Spinner) dialog.findViewById(R.id.sp_sort_types);
        ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_dropdown_item, sortTypes);
        spinner.setAdapter(spinnerArrayAdapter);

        Button okButton = (Button) dialog.findViewById(R.id.but_set_sort_ok);
        okButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                audioSort = sortCodes[spinner.getSelectedItemPosition()];
                main.setAudioSort(audioSort);
                saveData();
                dialog.dismiss();
            }
        });

        dialog.show();
        Window window = dialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    private void resetCategories()
    {
        ///dialog
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.dialog_set_reset_confirm);

        Button okButton = (Button) dialog.findViewById(R.id.but_set_conf_ok);
        okButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                MediaDatabase database = MediaDatabase.getInstance(getActivity());
                List<AudioCategory> list = Arrays.asList(database.audioCategoryDAO().loadAllCategories());
                for (AudioCategory ac: list)
                {
                    database.audioCategoryDAO().deleteCategory(ac);
                }
                database.audioCategoryDAO().insertAll(AudioCategory.populateData());
                dialog.dismiss();
            }
        });

        Button noButton = (Button) dialog.findViewById(R.id.but_set_conf_no);
        noButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                dialog.dismiss();
            }
        });

        dialog.show();
        Window window = dialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);




    }

}
