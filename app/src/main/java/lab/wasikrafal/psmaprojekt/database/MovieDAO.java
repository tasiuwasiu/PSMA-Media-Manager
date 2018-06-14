package lab.wasikrafal.psmaprojekt.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import lab.wasikrafal.psmaprojekt.models.Movie;

@Dao
public interface MovieDAO
{
    @Insert
    void insertMovie(Movie movie);

    @Delete
    void deleteMovie(Movie movie);

    @Query("SELECT * FROM Movie")
    List<Movie> getAllMovies();
}
