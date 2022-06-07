package Screens;

import Objects.MixedDrink;
import Utilities.GitAccess;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.bartender.R;

import java.io.IOException;
import java.util.ArrayList;

public class LiquorSelect extends AppCompatActivity {
    private ArrayList<String> liquors;
    private GridLayout liquorListLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liquor_select);

        Bundle extras = getIntent().getExtras();
        liquorListLayout = findViewById(R.id.liquorListLayout);
        liquors = (ArrayList<String>) extras.get("liquor");
        Button backButton = findViewById(R.id.backButton2);

        setupList(this);

        backButton.setOnClickListener(view -> {
            Intent intent = new Intent(LiquorSelect.this, Inventory.class);
            startActivity(intent);
            this.finish();
        });
    }

    private void setupList (Context context) {
        liquorListLayout.setRowCount(liquors.size());
        for(int i = 0; i < liquors.size(); i++) {
            GridLayout.LayoutParams buttonParams = new GridLayout.LayoutParams();
            buttonParams.height = 0;
            buttonParams.width = 0;
            buttonParams.rowSpec = GridLayout.spec(i, (float) 1);
            buttonParams.columnSpec = GridLayout.spec(1, (float) 1);

            CheckBox checkBox = new CheckBox(context);
            checkBox.setId(i);
            checkBox.setLayoutParams(buttonParams);
        }
    }
}