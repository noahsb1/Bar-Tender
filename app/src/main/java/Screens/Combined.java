package Screens;

import Fragments.DrinkMenu;
import Fragments.Inventory;
import Utilities.Adapters.ViewPagerAdapter;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;
import com.example.bartender.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;

public class Combined extends AppCompatActivity implements TabLayoutMediator.TabConfigurationStrategy {
    TabLayout tabLayout;
    ViewPager2 viewPager2;
    ArrayList<String> tabs = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_combined);

        tabLayout = findViewById(R.id.tabLayout);
        viewPager2 = findViewById(R.id.viewPager);
        tabs.add("Inventory");
        tabs.add("Drink Menu");
        setViewPagerAdapter();
        new TabLayoutMediator(tabLayout, viewPager2, this).attach();
    }

    public void setViewPagerAdapter() {
        ViewPagerAdapter viewPagerAdapter = new
            ViewPagerAdapter(this);
        ArrayList<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new Inventory());
        fragmentList.add(new DrinkMenu());

        viewPagerAdapter.setData(fragmentList);
        viewPager2.setAdapter(viewPagerAdapter);
    }

    @Override
    public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
        tab.setText(tabs.get(position));
    }
}