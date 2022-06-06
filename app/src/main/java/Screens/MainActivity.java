package Screens;

import Objects.MixedDrink;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.bartender.R;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ArrayList<MixedDrink> drinkObjects;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drinkObjects = new ArrayList<>();

    }
}