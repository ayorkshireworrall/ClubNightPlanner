package alex.worrall.clubnightplanner;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.List;

import alex.worrall.clubnightplanner.model.PlannerViewModel;
import alex.worrall.clubnightplanner.model.court.CourtName;
import alex.worrall.clubnightplanner.model.player.Player;
import alex.worrall.clubnightplanner.ui.main.SectionsPagerAdapter;
import alex.worrall.clubnightplanner.ui.main.TabPositions;
import alex.worrall.clubnightplanner.ui.main.courts.CourtListAdapter;
import alex.worrall.clubnightplanner.ui.main.players.AddPlayerActivity;
import alex.worrall.clubnightplanner.utils.CourtnameUtils;

public class MainActivity extends AppCompatActivity {

    public static final int ADD_PLAYER_ACTIVITY_REQUEST_CODE = 1;

    private PlannerViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        final FloatingActionButton fab = findViewById(R.id.fab);
        mViewModel = new ViewModelProvider(this).get(PlannerViewModel.class);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                switch (position) {
                    case TabPositions.PLAYERS:
                        fab.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(MainActivity.this,
                                        AddPlayerActivity.class);
                                startActivityForResult(intent, ADD_PLAYER_ACTIVITY_REQUEST_CODE);
                            }
                        });
                        break;
                    case TabPositions.COURTS:
                        fab.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                addCourt();
                            }
                        });
                        break;
                    case TabPositions.FIXTURES:
                        fab.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Snackbar.make(v, "Fixtures Clicked",
                                        Snackbar.LENGTH_SHORT)
                                        .setAction("Action", null).show();
                            }
                        });
                        break;
                }
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private void addCourt() {
        String courtNameText = CourtnameUtils.getCourtNameText(this,
                mViewModel.getAllCourts());
        mViewModel.addCourt(courtNameText);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_PLAYER_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            String playerName = data.getStringExtra(AddPlayerActivity.EXTRA_NAME);
            int playerLevel = Integer.parseInt(data.getStringExtra(AddPlayerActivity.EXTRA_LEVEL));
            mViewModel.addPlayer(new Player(playerLevel, playerName));
        }
    }
}