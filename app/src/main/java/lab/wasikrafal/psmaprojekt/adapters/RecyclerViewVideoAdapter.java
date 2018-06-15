package lab.wasikrafal.psmaprojekt.adapters;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import lab.wasikrafal.psmaprojekt.R;
import lab.wasikrafal.psmaprojekt.activities.AudioPlayerActivity;
import lab.wasikrafal.psmaprojekt.activities.VideoPlayerActivity;
import lab.wasikrafal.psmaprojekt.models.Movie;

public class RecyclerViewVideoAdapter extends RecyclerView.Adapter<RecyclerViewVideoAdapter.VideoDataHolder>
{
    private List<Movie> movieList;

    public RecyclerViewVideoAdapter(List<Movie> mov)
    {
        movieList = mov;
    }

    @NonNull
    @Override
    public VideoDataHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_video, parent, false);
        return new VideoDataHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoDataHolder holder, int position)
    {
        Movie movie = movieList.get(position);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.ENGLISH);
        long second = (movie.length / 1000) % 60;
        long minute = (movie.length / (1000 * 60)) % 60;
        long hour = (movie.length / (1000 * 60 * 60)) % 24;

        File imgFile = new File(movie.thumbnail);

        if(imgFile.exists()){

            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            holder.thumbnail.setImageBitmap(myBitmap);
        }

        holder.title.setText(movie.title);
        holder.description.setText(movie.description);
        holder.date.setText(dateFormat.format(movie.date));
        holder.length.setText(String.format("%02d:%02d:%02d", hour, minute, second));

        holder.movie = movie;
    }

    @Override
    public int getItemCount()
    {
        return movieList.size();
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView)
    {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public void setData(List<Movie> mov)
    {
        movieList = mov;
    }

    public static class VideoDataHolder extends RecyclerView.ViewHolder
    {
        public Movie movie;
        CardView cardView;
        TextView title;
        TextView description;
        TextView date;
        TextView length;
        ImageView thumbnail;

        public VideoDataHolder(final View itemView)
        {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.cv_video_list);
            title = (TextView) itemView.findViewById(R.id.tv_video_list_title);
            description = (TextView) itemView.findViewById(R.id.tv_video_list_desc);
            date = (TextView) itemView.findViewById(R.id.tv_video_list_date);
            length = (TextView) itemView.findViewById(R.id.tv_video_list_length);
            thumbnail = (ImageView) itemView.findViewById(R.id.iv_video_list_thumbnail);
            cardView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    Intent intent = new Intent(itemView.getContext(), VideoPlayerActivity.class);
                    intent.putExtra("filepath", movie.fileName);
                    itemView.getContext().startActivity(intent);
                }
            });
        }

    }


}