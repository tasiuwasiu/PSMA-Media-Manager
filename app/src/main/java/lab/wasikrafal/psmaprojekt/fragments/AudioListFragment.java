package lab.wasikrafal.psmaprojekt.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import lab.wasikrafal.psmaprojekt.R;
import lab.wasikrafal.psmaprojekt.libraries.SwipeableRecyclerViewTouchListener;
import lab.wasikrafal.psmaprojekt.adapters.RecyclerViewAudioAdapter;
import lab.wasikrafal.psmaprojekt.database.MediaDatabase;
import lab.wasikrafal.psmaprojekt.models.AudioCategory;
import lab.wasikrafal.psmaprojekt.models.Recording;

public class AudioListFragment extends Fragment
{
    MediaDatabase database;
    List<Recording> recordings;
    RecyclerViewAudioAdapter recyclerViewAudioAdapter;

    @Override
    public void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        database = MediaDatabase.getInstance(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        recordings = database.recordingDAO().getAllRecordings();
        List<AudioCategory> categories = Arrays.asList(database.audioCategoryDAO().loadAllCategories());


        View view = inflater.inflate(R.layout.fragment_audio_list, container, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.rv_audio);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(llm);
        recyclerViewAudioAdapter = new RecyclerViewAudioAdapter(recordings, categories);
        recyclerView.setAdapter(recyclerViewAudioAdapter);

        SwipeableRecyclerViewTouchListener swipeTouchListener = new SwipeableRecyclerViewTouchListener(recyclerView, new SwipeableRecyclerViewTouchListener.SwipeListener()
        {
            @Override
            public boolean canSwipeLeft(int position)
            {
                return true;
            }

            @Override
            public boolean canSwipeRight(int position)
            {
                return false;
            }

            @Override
            public void onDismissedBySwipeLeft(RecyclerView recyclerView, int[] reverseSortedPositions)
            {
                for (int position : reverseSortedPositions) {
                    RecyclerViewAudioAdapter.AudioDataHolder holder = (RecyclerViewAudioAdapter.AudioDataHolder) recyclerView.findViewHolderForAdapterPosition(position);
                    removeItem(holder.recording);
                }
            }

            @Override
            public void onDismissedBySwipeRight(RecyclerView recyclerView, int[] reverseSortedPositions)
            {

            }
        });

        recyclerView.addOnItemTouchListener(swipeTouchListener);
        return view;
    }

    private void removeItem(Recording r)
    {
        database.recordingDAO().deleteRecording(r);
        recordings = database.recordingDAO().getAllRecordings();
        File file = new File(r.filename);
        file.delete();
        recyclerViewAudioAdapter.setData(recordings);
        recyclerViewAudioAdapter.notifyDataSetChanged();
    }


}
