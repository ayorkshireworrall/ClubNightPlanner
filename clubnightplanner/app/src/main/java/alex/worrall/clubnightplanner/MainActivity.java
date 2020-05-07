package alex.worrall.clubnightplanner;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
    ServiceApi service;
    PlayersFragment playersFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        service = ServiceApi.getInstance(this);
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
                                    clearPlayersDialog();
                                    return true;
                                }
                                if (item.getItemId() == R.id.clear_courts) {
                                    clearCourtsDialog();
                                    return true;
                                }
                                if (item.getItemId() == R.id.clear_fixtures) {
                                    clearFixturesDialog();
                                    return true;
                                }
                                if (item.getItemId() == R.id.clear_all_data1 ||
                                        item.getItemId() == R.id.clear_all_data2 ||
                                        item.getItemId() == R.id.clear_all_data3
                                ) {
                                    clearAllDataDialog();
                                    return true;
                                }
                                return false;
                            }
                        });
                }
            }

            private void clearPlayersDialog() {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Clear All Players")
                        .setMessage("Are you sure you wish to clear all players in the current " +
                                "list? This action is irreversible")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                service.clearPlayers();
                                PlayersFragment.getInstance().updateData();
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        }).show();
            }

            private void clearCourtsDialog() {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Clear All Courts")
                        .setMessage("Are you sure you wish to clear all courts in the current " +
                                "list? This action is irreversible")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                service.clearCourts();
                                CourtsFragment.getInstance().updateData();
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        }).show();
            }

            private void clearFixturesDialog() {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Clear All Fixtures")
                        .setMessage("Are you sure you wish to clear all fixtures in the current " +
                                "list? This action is irreversible")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                service.clearFixtures();
                                FixturesFragment.getInstance().updateData();
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        }).show();
            }

            private void clearAllDataDialog() {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Reset All Data")
                        .setMessage("Are you sure you wish to reset all data for this session. " +
                                "All players, courts and fixtures will be removed. This action is" +
                                " irreversible")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                service.clearData();
                                PlayersFragment.getInstance().updateData();
                                CourtsFragment.getInstance().updateData();
                                FixturesFragment.getInstance().updateData();
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        }).show();
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