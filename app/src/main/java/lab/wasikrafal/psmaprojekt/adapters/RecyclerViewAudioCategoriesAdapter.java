package lab.wasikrafal.psmaprojekt.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import lab.wasikrafal.psmaprojekt.R;
import lab.wasikrafal.psmaprojekt.models.AudioCategory;

public class RecyclerViewAudioCategoriesAdapter extends  RecyclerView.Adapter<RecyclerViewAudioCategoriesAdapter.AudioCategoryDataHolder>
{
    private List<AudioCategory> categories;

    public RecyclerViewAudioCategoriesAdapter(List<AudioCategory> cat)
    {
        categories = cat;
    }

    @NonNull
    @Override
    public RecyclerViewAudioCategoriesAdapter.AudioCategoryDataHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_audio_category, parent, false);
        return new RecyclerViewAudioCategoriesAdapter.AudioCategoryDataHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAudioCategoriesAdapter.AudioCategoryDataHolder holder, int position)
    {
        AudioCategory category = categories.get(position);

        holder.title.setText(category.name);
        holder.category = category;
    }

    @Override
    public int getItemCount()
    {
        return categories.size();
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView)
    {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public void setData(List<AudioCategory> cat)
    {
        categories = cat;
    }

    public static class AudioCategoryDataHolder extends RecyclerView.ViewHolder
    {
        public AudioCategory category;
        CardView cardView;
        TextView title;

        public AudioCategoryDataHolder(final View itemView)
        {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.cv_audio_cat_list);
            title = (TextView) itemView.findViewById(R.id.tv_audio_cat_list_title);
        }

    }
}
