package lab.wasikrafal.psmaprojekt.adapters;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import lab.wasikrafal.psmaprojekt.R;
import lab.wasikrafal.psmaprojekt.activities.AudioPlayerActivity;
import lab.wasikrafal.psmaprojekt.models.AudioCategory;
import lab.wasikrafal.psmaprojekt.models.Recording;

public class RecyclerViewAudioAdapter extends RecyclerView.Adapter<RecyclerViewAudioAdapter.AudioDataHolder>
{
    private List<Recording> recordingList;
    private List<AudioCategory> audioCategoryList;

    public RecyclerViewAudioAdapter(List<Recording> rec, List<AudioCategory> cat)
    {
        recordingList = rec;
        audioCategoryList = cat;
    }

    @NonNull
    @Override
    public AudioDataHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_audio, parent, false);
        return new AudioDataHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AudioDataHolder holder, int position)
    {
        Recording recording = recordingList.get(position);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.ENGLISH);
        long second = (recording.length/1000) %60;
        long minute = (recording.length/(1000*60)) %60;
        long hour = (recording.length/(1000*60*60)) % 24;

        holder.title.setText(recording.title);
        holder.description.setText(recording.description);
        holder.date.setText(dateFormat.format(recording.date));
        holder.length.setText(String.format("%02d:%02d:%02d", hour, minute, second));
        holder.recording = recording;
    }

    @Override
    public int getItemCount()
    {
        return recordingList.size();
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView)
    {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public void setData(List<Recording> rec)
    {
        recordingList = rec;
    }

    public static class AudioDataHolder extends RecyclerView.ViewHolder
    {
        public Recording recording;
        CardView cardView;
        TextView title;
        TextView description;
        TextView date;
        TextView length;

        public AudioDataHolder(final View itemView)
        {
            super(itemView);
            cardView = (CardView)itemView.findViewById(R.id.cv_audio_list);
            title = (TextView)itemView.findViewById(R.id.tv_audio_list_title);
            description = (TextView)itemView.findViewById(R.id.tv_audio_list_desc);
            date = (TextView)itemView.findViewById(R.id.tv_audio_list_date);
            length = (TextView)itemView.findViewById(R.id.tv_audio_list_length);
            cardView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    Intent intent = new Intent(itemView.getContext(), AudioPlayerActivity.class);
                    intent.putExtra("filepath", recording.filename);
                    itemView.getContext().startActivity(intent);
                }
            });
        }

    }




}
