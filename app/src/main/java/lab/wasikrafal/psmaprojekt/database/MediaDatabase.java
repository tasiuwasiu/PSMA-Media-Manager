package lab.wasikrafal.psmaprojekt.database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.concurrent.Executors;

import lab.wasikrafal.psmaprojekt.models.AudioCategory;
import lab.wasikrafal.psmaprojekt.models.Movie;
import lab.wasikrafal.psmaprojekt.models.Recording;

@Database(entities = {Movie.class, Recording.class, AudioCategory.class}, version = 1, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class MediaDatabase extends RoomDatabase
{
    private static MediaDatabase INSTANCE = null;

    public abstract MovieDAO movieDAO();
    public abstract RecordingDAO recordingDAO();
    public abstract AudioCategoryDAO audioCategoryDAO();

    public synchronized static MediaDatabase getInstance(Context context)
    {
        if (INSTANCE == null)
        {
            INSTANCE = buildDatabase(context);
        }
        return INSTANCE;
    }

    private static MediaDatabase buildDatabase(final Context context)
    {
        return Room.databaseBuilder(context, MediaDatabase.class,
                "mediaDatabase").allowMainThreadQueries().addCallback(new Callback()
        {
            @Override
            public void onCreate(@NonNull SupportSQLiteDatabase db)
            {
                super.onCreate(db);
                Executors.newSingleThreadScheduledExecutor().execute(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        getInstance(context).audioCategoryDAO().insertAll(AudioCategory.populateData());
                    }
                });
            }
        }).build();
    }
}
