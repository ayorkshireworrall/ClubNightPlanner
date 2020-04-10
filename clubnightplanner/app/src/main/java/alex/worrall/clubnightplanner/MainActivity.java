package alex.worrall.clubnightplanner;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import alex.worrall.clubnightplanner.ui.main.SectionsPagerAdapter;

import static alex.worrall.clubnightplanner.service.RequestCodes.EDIT_PLAYER_REQUEST;
import static alex.worrall.clubnightplanner.service.RequestCodes.EDIT_PLAYER_RESULT;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EDIT_PLAYER_REQUEST && resultCode == EDIT_PLAYER_RESULT) {
            System.out.println("Recorded finish");
        }
    }
}