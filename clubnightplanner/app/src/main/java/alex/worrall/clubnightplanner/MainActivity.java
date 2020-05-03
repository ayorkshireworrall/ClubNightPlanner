package alex.worrall.clubnightplanner;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import alex.worrall.clubnightplanner.service.ServiceApi;
import alex.worrall.clubnightplanner.ui.main.SectionsPagerAdapter;
import alex.worrall.clubnightplanner.ui.main.TabPositions;
import alex.worrall.clubnightplanner.ui.main.courts.CourtsFragment;
import alex.worrall.clubnightplanner.ui.main.fixtures.Fixture;
import alex.worrall.clubnightplanner.ui.main.fixtures.FixturesFragment;
import alex.worrall.clubnightplanner.ui.main.players.PlayersFragment;

public class MainActivity extends AppCompatActivity {
    Toolbar toolbar;
    ServiceApi service = ServiceApi.getInstance();
    PlayersFragment playersFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.topAppBar);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager_main);
        viewPager.setAdapter(sectionsPagerAdapter);
        Bundle extras = getIntent().getExtras();
        int tabPosition = 0;
        if (extras != null) {
            tabPosition = extras.getInt("TAB_POSITION");
        }
        viewPager.setCurrentItem(tabPosition);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {


            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                switch (position) {
                    case TabPositions.COURTS:
                        toolbar.getMenu().clear();
                        toolbar.inflateMenu(R.menu.courts_bar_menu);
                        break;
                    case TabPositions.FIXTURES:
                        toolbar.getMenu().clear();
                        toolbar.inflateMenu(R.menu.fixtures_bar_menu);
                        break;
                    default:
                        toolbar.getMenu().clear();
                        toolbar.inflateMenu(R.menu.players_bar_menu);
                        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                if (item.getItemId() == R.id.clear_players) {
                                    service.clearPlayers();
                                    PlayersFragment.getInstance().updateData();
                                    return true;
                                }
                                if (item.getItemId() == R.id.clear_courts) {
                                    service.clearCourts();
                                    CourtsFragment.getInstance().updateData();
                                    return true;
                                }
                                if (item.getItemId() == R.id.clear_fixtures) {
                                    service.clearFixtures();
                                    FixturesFragment.getInstance().updateData();
                                    return true;
                                }
                                return false;
                            }
                        });
                }
            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case TabPositions.COURTS:
                        toolbar.getMenu().clear();
                        toolbar.inflateMenu(R.menu.courts_bar_menu);
                        break;
                    case TabPositions.FIXTURES:
                        toolbar.getMenu().clear();
                        toolbar.inflateMenu(R.menu.fixtures_bar_menu);
                        break;
                    default:
                        toolbar.getMenu().clear();
                        toolbar.inflateMenu(R.menu.players_bar_menu);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }

            private void showItems(int position) {
                View playerSearch = findViewById(R.id.player_search);
                View clearPlayers = findViewById(R.id.clear_players);
                View clearCourts = findViewById(R.id.clear_courts);
                View clearFixtures = findViewById(R.id.clear_fixtures);
                switch (position) {
                    case TabPositions.COURTS:
                        setVisibility(playerSearch, false);
                        setVisibility(clearPlayers, false);
                        setVisibility(clearCourts, true);
                        setVisibility(clearFixtures, false);
                        break;
                    case TabPositions.FIXTURES:
                        setVisibility(playerSearch, false);
                        setVisibility(clearPlayers, false);
                        setVisibility(clearCourts, false);
                        setVisibility(clearFixtures, true);
                        break;
                    default:
                        setVisibility(playerSearch, true);
                        setVisibility(clearPlayers, true);
                        setVisibility(clearCourts, false);
                        setVisibility(clearFixtures, false);
                }
            }

            private void setVisibility(View view, boolean isVisible) {
                if (view == null) {
                    return;
                }
                view.setVisibility(isVisible ? View.VISIBLE : View.INVISIBLE);
            }
        });
    }
}