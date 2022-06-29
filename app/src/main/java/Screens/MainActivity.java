package Screens;

import Fragments.Inventory;
import Utilities.ErrorDisplay;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.bartender.R;

public class MainActivity extends AppCompatActivity {
    boolean connectedGlobal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize variables
        Button startButton = findViewById(R.id.startButton);

        // Define action on start button press
        startButton.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, Inventory.class);
            startActivity(intent);
            this.finish();
        });
    }
}