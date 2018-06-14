package lab.wasikrafal.psmaprojekt.models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.Date;

@Entity
public class Movie
{
    @NonNull
    @PrimaryKey
    public String fileName;

    public String title;

    public String description;

    public long length;

    public String thumbnail;

    public Date date;
}
