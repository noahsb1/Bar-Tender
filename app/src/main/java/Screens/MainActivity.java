package Screens;

import Objects.MixedDrink;
import Utilities.FileLoader;
import android.content.Intent;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.bartender.R;

import java.io.FileNotFoundException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button startButton = findViewById(R.id.startButton);

        startButton.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, Inventory.class);
            startActivity(intent);
            this.finish();
        });
    }
}