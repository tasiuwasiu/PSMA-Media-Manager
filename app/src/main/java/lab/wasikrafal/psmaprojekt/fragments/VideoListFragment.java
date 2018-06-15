package lab.wasikrafal.psmaprojekt.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;
import java.util.List;

import lab.wasikrafal.psmaprojekt.R;
import lab.wasikrafal.psmaprojekt.adapters.RecyclerViewVideoAdapter;
import lab.wasikrafal.psmaprojekt.database.MediaDatabase;
import lab.wasikrafal.psmaprojekt.libraries.SwipeableRecyclerViewTouchListener;
import lab.wasikrafal.psmaprojekt.models.Movie;

public class VideoListFragment extends Fragment
{
    MediaDatabase database;
    List<Movie> movies;
    RecyclerViewVideoAdapter recyclerViewVideoAdapter;

    @Override
    public void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        database = MediaDatabase.getInstance(getActivity());
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState!=null)
        {

        }
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
        movies = database.movieDAO().getAllMovies();

        View view = inflater.inflate(R.layout.fragment_video_list, container, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.rv_video);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(llm);
        recyclerViewVideoAdapter = new RecyclerViewVideoAdapter(movies);
        recyclerView.setAdapter(recyclerViewVideoAdapter);

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
                    RecyclerViewVideoAdapter.VideoDataHolder holder = (RecyclerViewVideoAdapter.VideoDataHolder) recyclerView.findViewHolderForAdapterPosition(position);
                    removeItem(holder.movie);
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

    private void removeItem(Movie m)
    {
        database.movieDAO().deleteMovie(m);
        movies = database.movieDAO().getAllMovies();
        File file = new File(m.fileName);
        file.delete();
        file = new File (m.thumbnail);
        file.delete();
        recyclerViewVideoAdapter.setData(movies);
        recyclerViewVideoAdapter.notifyDataSetChanged();
    }

}
