package Screens;

import Utilities.InternalMemory;
import Utilities.RecycleViewAdapter;
import android.content.Intent;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.bartender.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class LiquorSelect extends AppCompatActivity {
    private ArrayList<String> liquorsOnline;
    private RecycleViewAdapter adapter;
    private ArrayList<String> liquorsInInventory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liquor_select);

        // Initialize variables and copy inventory
        Bundle extras = getIntent().getExtras();
        liquorsOnline = (ArrayList<String>) extras.get("liquors");
        liquorsInInventory = (ArrayList<String>) extras.get("inventory");
        Button backButton = findViewById(R.id.backButton2);
        Button updateButton = findViewById(R.id.executeAddToInventory);

        // Sort liquor list alphabetically and initialize recycler view
        Collections.sort(this.liquorsOnline);
        RecyclerView recyclerView = findViewById(R.id.liquorRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecycleViewAdapter(liquorsOnline, liquorsInInventory, 0);
        recyclerView.setAdapter(adapter);

        // Define action on back button press
        backButton.setOnClickListener(view -> {
            Intent intent = new Intent(LiquorSelect.this, Inventory.class);
            startActivity(intent);
            this.finish();
        });

        // Define action on update button press
        updateButton.setOnClickListener(view -> {
            ArrayList<String> liquorsInInventoryUpdated = new ArrayList<>();
            for (String str : adapter.getSelectedLiquors()) {
                if (liquorsOnline.contains(str)) {
                    liquorsInInventoryUpdated.add(str);
                }
            }
            
            try {
                InternalMemory.addToInventory(this, liquorsInInventoryUpdated);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}