package Screens;

import Objects.MixedDrink;
import Utilities.FileLoader;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.bartender.R;

import java.io.FileNotFoundException;
import java.util.ArrayList;

public class Inventory extends AppCompatActivity {
    private ArrayList<MixedDrink> drinkObjects;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);

        addDrinks();

    }

    /**
     * Converts drink text files into drink objects
     */
    private void addDrinks() {
        try {
            drinkObjects = FileLoader.fileLoaderInitializer();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}