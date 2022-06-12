package Screens;

import Utilities.InternalMemory;
import Utilities.RecycleViewAdapters.ParentRecycleViewAdapter;
import Utilities.RecycleViewAdapters.RecycleViewAdapter;
import Utilities.SetToArrayList;
import android.content.Intent;
import android.view.MotionEvent;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.bartender.R;

import java.io.IOException;
import java.util.*;

public class LiquorSelect extends AppCompatActivity {
    private HashMap<String, HashMap<String, ArrayList<String>>> liquorsOnline;
    private ParentRecycleViewAdapter   adapter;
    private ArrayList<String> liquorsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liquor_select);

        // Initialize variables and copy inventory
        Bundle extras = getIntent().getExtras();
        liquorsOnline = (HashMap<String, HashMap<String, ArrayList<String>>>) extras.get("liquors");
        liquorsList = (ArrayList<String>) extras.get("list");
        Button backButton = findViewById(R.id.backButton2);
        Button updateButton = findViewById(R.id.executeAddToInventory);

        ArrayList<String> hashMapKey = SetToArrayList.setToArrayList(liquorsOnline.keySet());

        ArrayList<Integer> rowTypes = new ArrayList<>();
        for (int i = 0; i < liquorsOnline.size(); i++) {
            rowTypes.add(0);
        }

        // Sort liquor list alphabetically and initialize recycler view
        RecyclerView recyclerView = findViewById(R.id.liquorRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ParentRecycleViewAdapter(liquorsOnline, rowTypes, hashMapKey, this);
        recyclerView.setAdapter(adapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);


        RecyclerView.OnItemTouchListener mScrollTouchListener = new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                int action = e.getAction();
                switch (action) {
                case MotionEvent.ACTION_MOVE:
                    rv.getParent().requestDisallowInterceptTouchEvent(true);
                    break;
                }
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        };

        recyclerView.addOnItemTouchListener(mScrollTouchListener);

        // Define action on back button press
        backButton.setOnClickListener(view -> {
            Intent intent = new Intent(LiquorSelect.this, Inventory.class);
            startActivity(intent);
            this.finish();
        });

        // Define action on update button press
        updateButton.setOnClickListener(view -> {
            try {
                ArrayList<String> temp2 = Inventory.getSelectedLiquors();
                for (String str : temp2) {
                    if (!liquorsList.contains(str)) {
                        Inventory.removeSelectedLiquors(str);
                    }
                }
                InternalMemory.addToInventory(this, Inventory.getSelectedLiquors());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}