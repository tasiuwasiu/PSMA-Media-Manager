package lab.wasikrafal.psmaprojekt.activities;

import android.os.Bundle;
import android.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import lab.wasikrafal.psmaprojekt.R;
import lab.wasikrafal.psmaprojekt.fragments.AudioRecorderFragment;
import lab.wasikrafal.psmaprojekt.fragments.VideoRecorderFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener
{
    VideoRecorderFragment videoRecorderFragment;
    AudioRecorderFragment audioRecorderFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        videoRecorderFragment = new VideoRecorderFragment();
        audioRecorderFragment = new AudioRecorderFragment();
        startVideoListFragment();
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
    public boolean onNavigationItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            startVideoRecorderFragment();
        } else if (id == R.id.nav_mic) {
            startAudioRecorderFragment();
        }  else if (id == R.id.nav_aud_list) {
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

        transaction.replace(R.id.fragment_container, videoRecorderFragment);
        transaction.addToBackStack(null);

        transaction.commit();
    }

    private void startAudioRecorderFragment()
    {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        transaction.replace(R.id.fragment_container, audioRecorderFragment);
        transaction.addToBackStack(null);

        transaction.commit();
    }

    private void startAudioListFragment()
    {

    }

    private void startVideoListFragment()
    {

    }

    private void startCategorySettingsFragment()
    {

    }

    private void startSettingsFragment()
    {

    }
}
