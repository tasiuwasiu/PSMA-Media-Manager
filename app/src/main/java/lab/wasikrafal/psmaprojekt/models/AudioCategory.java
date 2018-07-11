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


    public AudioCategory(String name)
    {
        this.name = name;
    }

    public String toString()
    {
        return name;
    }

    public static AudioCategory[] populateData()
    {
        return new AudioCategory[]
                {
                        new AudioCategory("Notatki"),
                        new AudioCategory("Nagrania"),
                        new AudioCategory("Przypomnienia")
                };
    }
}
