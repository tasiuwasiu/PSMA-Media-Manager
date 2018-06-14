package lab.wasikrafal.psmaprojekt.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

import lab.wasikrafal.psmaprojekt.models.AudioCategories;
import lab.wasikrafal.psmaprojekt.models.Movie;
import lab.wasikrafal.psmaprojekt.models.Recording;

@Database(entities = {Movie.class, Recording.class, AudioCategories.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class MediaDatabase extends RoomDatabase
{
    public abstract MovieDAO movieDAO();
    public abstract RecordingDAO recordingDAO();
    public abstract AudioCategories audioCategories();
}
