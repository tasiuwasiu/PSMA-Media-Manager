package lab.wasikrafal.psmaprojekt.models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity
public class AudioCategory
{

    @NonNull
    @PrimaryKey(autoGenerate = true)
    public int categoryId;

    public String name;

    public String imagePath;

    public AudioCategory(String name, String imagePath)
    {
        this.name = name;
        this.imagePath = imagePath;
    }

    public String toString()
    {
        return name;
    }

    public static AudioCategory[] populateData()
    {
        return new AudioCategory[]
                {
                        new AudioCategory("Notatki", "path2"),
                        new AudioCategory("Nagrania", "path1"),
                        new AudioCategory("Przypomnienia", "path1")
                };
    }
}
