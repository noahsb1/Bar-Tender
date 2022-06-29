package Screens;

import Fragments.Inventory;
import Utilities.Adapters.ThreeLevelListAdapter;
import Utilities.HashMapToString;
import Utilities.InternalMemory;
import Utilities.SetToArrayList;
import android.content.Intent;
import android.widget.Button;
import android.widget.ExpandableListView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.bartender.R;

import java.io.IOException;
import java.util.*;

public class LiquorSelect extends AppCompatActivity {
    private HashMap<String, HashMap<String, ArrayList<String>>> liquorsOnline;
    private ArrayList<HashMap<String, ArrayList<String>>> data = new ArrayList<>();
    private ArrayList<String> categories = new ArrayList<>();
    private ArrayList<ArrayList<String>> secondLevel = new ArrayList<>();
    private ExpandableListView expandableListView;
    private ArrayList<String> liquorsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liquor_select);

        // Initialize variables and copy inventory
        Bundle extras = getIntent().getExtras();
        liquorsOnline = (HashMap<String, HashMap<String, ArrayList<String>>>) extras.get("liquors");
        liquorsList = (ArrayList<String>) extras.get("list");
        categories = SetToArrayList.setToArrayList(liquorsOnline.keySet());
        Button backButton = findViewById(R.id.backButton3);
        Button updateButton = findViewById(R.id.executeAddToInventory);


        setUpAdapter();

        // Define action on back button press
        backButton.setOnClickListener(view -> {
            Intent intent = new Intent(LiquorSelect.this, Combined.class);
            startActivity(intent);
            this.finish();
        });

        // Define action on update button press
        updateButton.setOnClickListener(view -> {
            try {
                ArrayList<String> temp2 = new ArrayList<>();
                temp2.addAll(Inventory.getSelectedLiquors());
                for (String str : temp2) {
                    if (!liquorsList.contains(str)) {
                        Inventory.removeSelectedLiquors(str);
                    }
                }

                String strFromHashMap = HashMapToString.hashMapToString(Inventory.getLiquorsInInventory());

                InternalMemory.addToInventory(this, strFromHashMap, "LiquorsInInventory.txt");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void setUpAdapter() {
        for (String str : categories) {
            secondLevel.add(SetToArrayList.setToArrayList(liquorsOnline.get(str).keySet()));
            data.add(liquorsOnline.get(str));
        }

        expandableListView = findViewById(R.id.expandable_listview2);
        ThreeLevelListAdapter threeLevelListAdapter = new ThreeLevelListAdapter(this, categories, secondLevel, data, 1);
        expandableListView.setAdapter(threeLevelListAdapter);
        expandableListView.setOnGroupExpandListener(
            new ExpandableListView.OnGroupExpandListener() {
                int previousGroup = -1;
                @Override
                public void onGroupExpand(int i) {
                    if (i != previousGroup) {
                        expandableListView.collapseGroup(previousGroup);
                    }
                    previousGroup = i;
                }
            });
    }
}