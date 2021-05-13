package alex.worrall.clubnightplanner;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import alex.worrall.clubnightplanner.model.PlannerViewModel;
import alex.worrall.clubnightplanner.model.court.CourtName;
import alex.worrall.clubnightplanner.model.fixture.Court;
import alex.worrall.clubnightplanner.model.fixture.Fixture;
import alex.worrall.clubnightplanner.model.player.Player;
import alex.worrall.clubnightplanner.ui.main.SectionsPagerAdapter;
import alex.worrall.clubnightplanner.ui.main.TabPositions;
import alex.worrall.clubnightplanner.ui.main.fixtures.AddFixtureActivity;
import alex.worrall.clubnightplanner.ui.main.players.AddPlayerActivity;
import alex.worrall.clubnightplanner.ui.main.settings.ViewSettingsActivity;
import alex.worrall.clubnightplanner.utils.ClearDataActions;
import alex.worrall.clubnightplanner.utils.CourtnameUtils;
import alex.worrall.clubnightplanner.utils.SchedulerV2;

public class MainActivity extends AppCompatActivity {

    public static final int ADD_PLAYER_ACTIVITY_REQUEST_CODE = 1;
    public static final int EDIT_PLAYER_ACTIVITY_REQUEST_CODE = 2;
    public static final int ADD_FIXTURE_ACTIVITY_REQUEST_CODE = 3;
    public static final int VIEW_MATCHES_ACTIVITY_REQUEST_CODE = 4;
    public static final int VIEW_SETTINGS_ACTIVITY_REQUEST_CODE = 5;
    public static final String EXTRA_PLAYER = MainActivity.class.getName() + "PLAYER";
    public static final String EXTRA_TAB_POSITION = MainActivity.class.getName() + "TAB_POSITION";
    public static final String EXTRA_FIXTURE_ID = MainActivity.class.getName() + "FIXTURE_ID";

    private PlannerViewModel mViewModel;
    private boolean hasCourts;
    private ViewPager viewPager;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        Bundle extras = getIntent().getExtras();
        int tabPosition = 0;
        if (extras != null) {
            tabPosition = extras.getInt(EXTRA_TAB_POSITION);
        }
        viewPager.setCurrentItem(tabPosition);
        final TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        final FloatingActionButton fab = findViewById(R.id.fab);
        mViewModel = new ViewModelProvider(this).get(PlannerViewModel.class);
        mViewModel.getActivePlayers().observe(this, new Observer<List<Player>>() {
            @Override
            public void onChanged(List<Player> players) {
                reschedule(null);
            }
        });
        mViewModel.getAllCourtsLive().observe(this, new Observer<List<CourtName>>() {
            @Override
            public void onChanged(List<CourtName> courtNames) {
                hasCourts = !courtNames.isEmpty();
                List<String> names = new ArrayList<>();
                for (CourtName courtName : courtNames) {
                    names.add(courtName.getName());
                }
                reschedule(names);
            }
        });
        MaterialToolbar toolbar = (MaterialToolbar) findViewById(R.id.topAppBar);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.clear_players:
                        clearDialog(ClearDataActions.PLAYERS);
                        return true;
                    case R.id.clear_courts:
                        clearDialog(ClearDataActions.COURTS);
                        return true;
                    case R.id.clear_fixtures:
                        clearDialog(ClearDataActions.FIXTURES);
                        return true;
                    case R.id.clear_all:
                        clearDialog(ClearDataActions.ALL);
                        return true;
                    case R.id.settings:
                        accessSettings();
                        return true;
                }
                return false;
            }

            private void clearDialog(final ClearDataActions action) {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle(action.getTitle())
                        .setMessage(action.getMessage())
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                actionPicker(action);
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        }).show();
            }

            private void actionPicker(ClearDataActions action) {
                switch (action) {
                    case PLAYERS:
                        mViewModel.deleteAllPlayers();
                        break;
                    case COURTS:
                        mViewModel.deleteAllSessionCourts(0);
                        break;
                    case FIXTURES:
                        mViewModel.deleteAllSessionFixtures(0);
                        break;
                    case ALL:
                        mViewModel.deleteAllPlayers();
                        mViewModel.deleteAllSessionCourts(0);
                        mViewModel.deleteAllSessionFixtures(0);
                        break;
                }
            }
        });
        final Menu menu = toolbar.getMenu();

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                switch (position) {
                    case TabPositions.PLAYERS:
                        setMenuItemsVisible(true, false, false);
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
                        setMenuItemsVisible(false, true, false);
                        fab.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                addCourt();
                            }
                        });
                        break;
                    case TabPositions.FIXTURES:
                        setMenuItemsVisible(false, false, true);
                        fab.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                addFixture();
                            }
                        });
                        break;
                }
            }

            private void setMenuItemsVisible(boolean players, boolean courts, boolean fixtures) {
                menu.findItem(R.id.search_players).setVisible(players);
                menu.findItem(R.id.clear_players).setVisible(players);
                menu.findItem(R.id.clear_courts).setVisible(courts);
                menu.findItem(R.id.clear_fixtures).setVisible(fixtures);
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        setSearchView(menu);
    }

    private void setSearchView(Menu menu) {
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.search_players)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);
    }

    @Override
    public void onBackPressed() {
        // close search view on back button pressed
        if (!searchView.isIconified()) {
            searchView.setIconified(true);
            return;
        }
        super.onBackPressed();
    }

    private void addFixture() {
        if (hasCourts) {
            Intent intent = new Intent(MainActivity.this,
                    AddFixtureActivity.class);
            startActivityForResult(intent, ADD_FIXTURE_ACTIVITY_REQUEST_CODE);
        } else {
            Toast.makeText(this, "To generate fixtures add at least one court",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void accessSettings() {
        Intent intent = new Intent(MainActivity.this, ViewSettingsActivity.class);
        startActivityForResult(intent, VIEW_SETTINGS_ACTIVITY_REQUEST_CODE);
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
            if (playerName != null) {
                int playerLevel = Integer.parseInt(data.getStringExtra(AddPlayerActivity.EXTRA_LEVEL));
                mViewModel.addPlayer(new Player(playerLevel, playerName));
            }
        }

        if (requestCode == EDIT_PLAYER_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            if (extras != null) {
                Player player = extras.getParcelable(EXTRA_PLAYER);
                mViewModel.updatePlayer(player);
            }
        }

        if (requestCode == ADD_FIXTURE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            //fixture scheduling work will be done in the add fixture activity
            viewPager.setCurrentItem(TabPositions.FIXTURES);
        }
    }

    private void reschedule(@Nullable List<String> courtNames) {
        List<Fixture> reschedulableFixtures =
                mViewModel.getReschedulableFixtures();
        if (courtNames != null && !courtNames.isEmpty()) {
            Iterator<Fixture> fixtureIterator = reschedulableFixtures.iterator();
            fixtures: while (fixtureIterator.hasNext()) {
                Fixture fixture = fixtureIterator.next();
                List<Court> courts = fixture.getCourts();
                Iterator<Court> courtIterator = courts.iterator();
                while(courtIterator.hasNext()) {
                    Court court = courtIterator.next();
                    if (!courtNames.contains(court.getCourtName())) {
                        courtIterator.remove();
                        continue fixtures;
                    }
                }
                fixtureIterator.remove();
            }
        }
        SchedulerV2 schedulerV2 = new SchedulerV2(this);
        schedulerV2.reschedule(reschedulableFixtures);
    }
}