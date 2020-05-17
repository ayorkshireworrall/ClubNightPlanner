package alex.worrall.clubnightplanner;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Parcelable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import alex.worrall.clubnightplanner.model.PlannerViewModel;
import alex.worrall.clubnightplanner.model.player.Player;
import alex.worrall.clubnightplanner.ui.main.SectionsPagerAdapter;
import alex.worrall.clubnightplanner.ui.main.TabPositions;
import alex.worrall.clubnightplanner.ui.main.players.AddPlayerActivity;
import alex.worrall.clubnightplanner.ui.main.players.EditPlayerActivity;
import alex.worrall.clubnightplanner.utils.ClearDataActions;
import alex.worrall.clubnightplanner.utils.CourtnameUtils;

public class MainActivity extends AppCompatActivity {

    public static final int ADD_PLAYER_ACTIVITY_REQUEST_CODE = 1;
    public static final int EDIT_PLAYER_ACTIVITY_REQUEST_CODE = 2;
    public static final String EXTRA_PLAYER = MainActivity.class.getName() + "PLAYER";

    private PlannerViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        final TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        final FloatingActionButton fab = findViewById(R.id.fab);
        mViewModel = new ViewModelProvider(this).get(PlannerViewModel.class);
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
                    case TabPositions.PLAYERS:setMenuItemsVisible(true, false, false);
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
                                Snackbar.make(v, "Fixtures Clicked",
                                        Snackbar.LENGTH_SHORT)
                                        .setAction("Action", null).show();
                            }
                        });
                        break;
                }
            }

            private void setMenuItemsVisible(boolean players, boolean courts, boolean fixtures) {
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

        if (requestCode == EDIT_PLAYER_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Player player = extras.getParcelable(EXTRA_PLAYER);
            mViewModel.updatePlayer(player);
        }
    }
}