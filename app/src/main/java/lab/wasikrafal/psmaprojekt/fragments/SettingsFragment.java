package lab.wasikrafal.psmaprojekt.fragments;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

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



        main.setVideoSort(videoSort);
        saveData();
    }

    private void setAudioSorting()
    {
        //dialog


        main.setAudioSort(audioSort);
        saveData();
    }

    private void resetCategories()
    {
        ///dialog

        MediaDatabase database = MediaDatabase.getInstance(getActivity());
        List<AudioCategory> list = Arrays.asList(database.audioCategoryDAO().loadAllCategories());
        for (AudioCategory ac: list)
        {
            database.audioCategoryDAO().deleteCategory(ac);
        }
        database.audioCategoryDAO().insertAll(AudioCategory.populateData());

    }

}
