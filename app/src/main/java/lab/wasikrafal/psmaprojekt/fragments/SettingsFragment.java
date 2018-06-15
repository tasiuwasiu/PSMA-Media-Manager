package lab.wasikrafal.psmaprojekt.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import lab.wasikrafal.psmaprojekt.R;
import lab.wasikrafal.psmaprojekt.activities.MainActivity;

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



        return view;
    }




    private void loadData()
    {

    }

    private void saveData()
    {

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

}
