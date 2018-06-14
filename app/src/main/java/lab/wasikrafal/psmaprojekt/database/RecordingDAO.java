package lab.wasikrafal.psmaprojekt.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import lab.wasikrafal.psmaprojekt.models.Recording;

@Dao
public interface RecordingDAO
{
    @Insert
    void insertRecording(Recording recording);

    @Delete
    void deleteRecording(Recording recording);

    @Query("SELECT * FROM Recording")
    List<Recording> getAllRecordings();
}
