package lab.wasikrafal.psmaprojekt.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import lab.wasikrafal.psmaprojekt.R;

public class IntroActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        Button b = findViewById(R.id.button);
        b.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startApp();
            }
        });
    }

    private void startApp()
    {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }
}
