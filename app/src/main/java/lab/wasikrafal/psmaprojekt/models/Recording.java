package lab.wasikrafal.psmaprojekt.models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.Date;

import lab.wasikrafal.psmaprojekt.models.AudioCategories;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(foreignKeys = @ForeignKey(entity = AudioCategories.class,
        parentColumns = "categoryId",
        childColumns = "catID",
        onDelete = CASCADE))
public class Recording
{
    @NonNull
    @PrimaryKey
    public String filename;

    public String title;

    public String description;

    public int catID;

    public Date date;

    public long length;
}
