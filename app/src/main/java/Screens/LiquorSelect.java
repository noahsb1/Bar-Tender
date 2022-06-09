package Screens;

import Utilities.InternalMemory;
import Utilities.RecycleViewAdapter;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.bartender.R;

import java.io.IOException;
import java.util.*;

public class LiquorSelect extends AppCompatActivity {
    private HashMap<String, List<String>> liquorsOnline;
    private RecycleViewAdapter adapter;
    private ArrayList<String> liquorsInInventory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liquor_select);

        // Initialize variables and copy inventory
        Bundle extras = getIntent().getExtras();
        liquorsOnline = (HashMap<String, List<String>>) extras.get("liquors");
        liquorsInInventory = (ArrayList<String>) extras.get("inventory");
        Button backButton = findViewById(R.id.backButton2);
        Button updateButton = findViewById(R.id.executeAddToInventory);

        // Liquor Hashmap translated into string Array to be displayed
        ArrayList<String> liquorsOnlineAsList = new ArrayList<>();
        ArrayList<Integer> viewTypes = new ArrayList<>();
        Set<String> keys = liquorsOnline.keySet();
        Object[] temp = keys.toArray();
        for (Object key : temp) {
            liquorsOnlineAsList.add(key + ":");
            viewTypes.add(0);
            for (String liquorBrand : liquorsOnline.get(key)) {
                liquorsOnlineAsList.add(liquorBrand);
                viewTypes.add(1);
            }
        }

        // Sort liquor list alphabetically and initialize recycler view
        RecyclerView recyclerView = findViewById(R.id.liquorRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new RecycleViewAdapter(liquorsOnlineAsList, liquorsInInventory, viewTypes);
        recyclerView.setAdapter(adapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

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
                if (liquorsOnlineAsList.contains(str)) {
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