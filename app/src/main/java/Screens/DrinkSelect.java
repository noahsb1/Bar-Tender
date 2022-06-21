package Screens;

import Objects.MixedDrink;
import Utilities.Adapters.BaseRecycleViewAdapter;
import Utilities.ErrorDisplay;
import android.content.Intent;
import android.widget.Button;
import android.widget.SearchView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.bartender.R;

import java.util.ArrayList;

public class DrinkSelect extends AppCompatActivity {
    public BaseRecycleViewAdapter adapter;
    public ArrayList<MixedDrink> mixedDrinks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drink_select);
        Bundle extras = getIntent().getExtras();

        Button backButton = findViewById(R.id.backButton3);
        SearchView searchView = findViewById(R.id.searchBar);

        mixedDrinks = (ArrayList<MixedDrink>) extras.get("mixedDrinks");
        ArrayList<Integer> rowTypes = new ArrayList<>();
        for (int i = 0; i < mixedDrinks.size(); i++) {
            rowTypes.add(i, 2);
        }

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return false;
            }
        });

        RecyclerView recyclerView = findViewById(R.id.drinkSelectRecyclerView);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        adapter = new BaseRecycleViewAdapter(mixedDrinks, rowTypes, this);
        recyclerView.setAdapter(adapter);

        // Define action on back button press
        backButton.setOnClickListener(view -> {
            Intent intent = new Intent(DrinkSelect.this, Inventory.class);
            startActivity(intent);
            this.finish();
        });
    }

    private void filter(String text) {
        ArrayList<MixedDrink> filteredlist = new ArrayList<>();
        ArrayList<Integer> rowTypes = new ArrayList<>();

        for (MixedDrink item : mixedDrinks) {
            if (item.getName().toLowerCase().contains(text.toLowerCase())) {
                filteredlist.add(item);
                rowTypes.add(2);
            }
        }
        if (filteredlist.isEmpty()) {
            ErrorDisplay.displayError(this, "No mixed drinks found");
        } else {
            adapter.filterList(filteredlist, rowTypes);
        }
    }
}