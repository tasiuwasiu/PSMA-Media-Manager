package lab.wasikrafal.psmaprojekt.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import lab.wasikrafal.psmaprojekt.models.AudioCategory;

@Dao
public interface AudioCategoryDAO
{
    @Insert
    void insertCategory(AudioCategory category);

    @Insert
    void insertAll(AudioCategory... categories);

    @Query("SELECT * FROM AudioCategory")
    AudioCategory[] loadAllCategories();

    @Query("SELECT * FROM AudioCategory WHERE categoryId = :id")
    AudioCategory getCategoryById(int id);

    @Delete
    void deleteCategory(AudioCategory category);
}
