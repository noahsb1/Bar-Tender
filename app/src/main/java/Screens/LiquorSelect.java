package Screens;

import android.content.Intent;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.bartender.R;

public class LiquorSelect extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liquor_select);

        Button backButton = findViewById(R.id.backButton2);

        backButton.setOnClickListener(view -> {
            Intent intent = new Intent(LiquorSelect.this, Inventory.class);
            startActivity(intent);
            this.finish();
        });
    }
}