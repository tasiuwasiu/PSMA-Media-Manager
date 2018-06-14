package lab.wasikrafal.psmaprojekt.models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity
public class AudioCategories
{

    @NonNull
    @PrimaryKey(autoGenerate = true)
    public int categoryId;

    public String name;

    public String imagePath;
}
