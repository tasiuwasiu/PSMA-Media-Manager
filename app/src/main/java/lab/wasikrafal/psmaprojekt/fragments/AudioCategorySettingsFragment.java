package lab.wasikrafal.psmaprojekt.fragments;

import android.app.Dialog;
import android.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;

import lab.wasikrafal.psmaprojekt.R;
import lab.wasikrafal.psmaprojekt.adapters.RecyclerViewAudioCategoriesAdapter;
import lab.wasikrafal.psmaprojekt.database.MediaDatabase;
import lab.wasikrafal.psmaprojekt.libraries.SwipeableRecyclerViewTouchListener;
import lab.wasikrafal.psmaprojekt.models.AudioCategory;

public class AudioCategorySettingsFragment extends Fragment
{
    MediaDatabase database;
    List<AudioCategory> categories;
    RecyclerViewAudioCategoriesAdapter recyclerViewAudioCategoriesAdapter;

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
        if (savedInstanceState != null) {

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
        categories = Arrays.asList(database.audioCategoryDAO().loadAllCategories());


        View view = inflater.inflate(R.layout.fragment_audio_category_settings, container, false);
        FloatingActionButton floatingActionButton = (FloatingActionButton) view.findViewById(R.id.fab_aud_cat_set);
        floatingActionButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                addCategory();
            }
        });

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.rv_audio_categories);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(llm);
        recyclerViewAudioCategoriesAdapter = new RecyclerViewAudioCategoriesAdapter(categories);
        recyclerView.setAdapter(recyclerViewAudioCategoriesAdapter);

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
                    RecyclerViewAudioCategoriesAdapter.AudioCategoryDataHolder holder = (RecyclerViewAudioCategoriesAdapter.AudioCategoryDataHolder) recyclerView.findViewHolderForAdapterPosition(position);
                    removeItem(holder.category);
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

    private void removeItem(AudioCategory ac)
    {
        database.audioCategoryDAO().deleteCategory(ac);
        categories = Arrays.asList(database.audioCategoryDAO().loadAllCategories());
        recyclerViewAudioCategoriesAdapter.setData(categories);
        recyclerViewAudioCategoriesAdapter.notifyDataSetChanged();
    }

    private void addCategory()
    {
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.dialog_add_audio_category);

        final EditText name = (EditText) dialog.findViewById(R.id.et_add_aud_cat_name);
        Button okButton = (Button) dialog.findViewById(R.id.but_add_aud_cat_ok);
        okButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                AudioCategory ac = new AudioCategory(name.getText().toString());
                database.audioCategoryDAO().insertCategory(ac);
                refresh();
                dialog.dismiss();
            }
        });

        dialog.show();
        Window window = dialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    private void refresh()
    {
        categories = Arrays.asList(database.audioCategoryDAO().loadAllCategories());
        recyclerViewAudioCategoriesAdapter.setData(categories);
        recyclerViewAudioCategoriesAdapter.notifyDataSetChanged();
    }

}
