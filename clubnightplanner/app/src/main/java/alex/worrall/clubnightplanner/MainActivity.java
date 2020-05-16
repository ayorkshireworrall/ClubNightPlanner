package alex.worrall.clubnightplanner;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

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
import alex.worrall.clubnightplanner.ui.main.SectionsPagerAdapter;
import alex.worrall.clubnightplanner.ui.main.TabPositions;
import alex.worrall.clubnightplanner.ui.main.courts.CourtListAdapter;
import alex.worrall.clubnightplanner.utils.CourtnameUtils;

public class MainActivity extends AppCompatActivity {

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
                                Snackbar.make(v, "Players Clicked",
                                        Snackbar.LENGTH_SHORT)
                                        .setAction("Action", null).show();
                            }
                        });
                        break;
                    case TabPositions.COURTS:
                        fab.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Snackbar.make(v, "Courts Clicked",
                                        Snackbar.LENGTH_SHORT)
                                        .setAction("Action", null).show();
                                String newCourtName = null;
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
}