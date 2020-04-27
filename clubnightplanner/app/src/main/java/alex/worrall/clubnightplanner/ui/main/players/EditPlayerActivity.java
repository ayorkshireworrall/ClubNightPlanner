package alex.worrall.clubnightplanner.ui.main.players;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import alex.worrall.clubnightplanner.R;
import alex.worrall.clubnightplanner.ui.main.GeneralFragment;

public class EditPlayerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_player);
        Toolbar toolbar = findViewById(R.id.toolbarEditPlayer);
        GeneralFragment generalFragment = new GeneralFragment(getSupportFragmentManager(),
                EditPlayerFragment.getInstance());
        ViewPager viewPager = findViewById(R.id.view_pager_edit_player);
        viewPager.setAdapter(generalFragment);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
