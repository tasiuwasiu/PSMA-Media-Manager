package lab.wasikrafal.psmaprojekt.activities;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.FragmentTransaction;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import lab.wasikrafal.psmaprojekt.R;
import lab.wasikrafal.psmaprojekt.database.MediaDatabase;
import lab.wasikrafal.psmaprojekt.fragments.AudioCategorySettingsFragment;
import lab.wasikrafal.psmaprojekt.fragments.AudioListFragment;
import lab.wasikrafal.psmaprojekt.fragments.AudioRecorderFragment;
import lab.wasikrafal.psmaprojekt.fragments.SettingsFragment;
import lab.wasikrafal.psmaprojekt.fragments.VideoListFragment;
import lab.wasikrafal.psmaprojekt.fragments.VideoRecorderFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener
{
    public static final int SORT_NAME = 0;
    public static final int SORT_DATE = 1;
    public static final int SORT_LENGTH = 2;

    int moviesSort = SORT_NAME;
    int audioSort = SORT_LENGTH;

    Fragment currentFragment;
    MediaDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setDatabase();
        setContentView(R.layout.activity_main);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        loadData();

        if (savedInstanceState != null) {
            currentFragment = getFragmentManager().getFragment(savedInstanceState, "currFrag");
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, currentFragment);
            transaction.commit();
        } else {
            startVideoListFragment();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        getFragmentManager().putFragment(outState, "currFrag", currentFragment);
    }

    private void loadData()
    {
        SharedPreferences sharedPreferences = getSharedPreferences("preferences", Context.MODE_PRIVATE);
        moviesSort = sharedPreferences.getInt("videoSort", MainActivity.SORT_NAME);
        audioSort = sharedPreferences.getInt("audioSort", MainActivity.SORT_NAME);
    }

    public void setVideoSort(int code)
    {
        moviesSort = code;
    }

    public void setAudioSort(int code)
    {
        audioSort = code;
    }

    private void setDatabase()
    {
        database = MediaDatabase.getInstance(this);
    }

    @Override
    public void onBackPressed()
    {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item)
    {
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            startVideoRecorderFragment();
        } else if (id == R.id.nav_mic) {
            startAudioRecorderFragment();
        } else if (id == R.id.nav_aud_list) {
            startAudioListFragment();
        } else if (id == R.id.nav_vid_list) {
            startVideoListFragment();
        } else if (id == R.id.nav_cat_settings) {
            startCategorySettingsFragment();
        } else if (id == R.id.nav_settings) {
            startSettingsFragment();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //Fragment starters

    private void startVideoRecorderFragment()
    {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        currentFragment = new VideoRecorderFragment();
        transaction.replace(R.id.fragment_container, currentFragment);
        transaction.commit();
    }

    private void startAudioRecorderFragment()
    {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        currentFragment = new AudioRecorderFragment();
        transaction.replace(R.id.fragment_container, currentFragment);
        transaction.commit();
    }

    private void startAudioListFragment()
    {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putInt("sort", audioSort);
        currentFragment = new AudioListFragment();
        currentFragment.setArguments(bundle);
        transaction.replace(R.id.fragment_container, currentFragment);
        transaction.commit();
    }

    private void startVideoListFragment()
    {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putInt("sort", moviesSort);
        currentFragment = new VideoListFragment();
        currentFragment.setArguments(bundle);
        transaction.replace(R.id.fragment_container, currentFragment);
        transaction.commit();
    }

    private void startCategorySettingsFragment()
    {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        currentFragment = new AudioCategorySettingsFragment();
        transaction.replace(R.id.fragment_container, currentFragment);
        transaction.commit();
    }

    private void startSettingsFragment()
    {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        currentFragment = new SettingsFragment();
        transaction.replace(R.id.fragment_container, currentFragment);
        transaction.commit();
    }
}
