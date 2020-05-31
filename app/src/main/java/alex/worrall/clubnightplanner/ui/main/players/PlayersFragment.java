package alex.worrall.clubnightplanner.ui.main.players;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import alex.worrall.clubnightplanner.MainActivity;
import alex.worrall.clubnightplanner.R;
import alex.worrall.clubnightplanner.model.PlannerViewModel;
import alex.worrall.clubnightplanner.model.fixture.Court;
import alex.worrall.clubnightplanner.model.fixture.Fixture;
import alex.worrall.clubnightplanner.model.player.Player;
import alex.worrall.clubnightplanner.utils.TimeUtil;

public class PlayersFragment extends Fragment implements PlayerListAdapter.ItemClickListener {
    PlannerViewModel mViewModel;
    RecyclerView recyclerView;
    PlayerListAdapter adapter;
    TextView emptyListMessage;

    public PlayersFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_players, container, false);
        recyclerView = rootView.findViewById(R.id.players_list);
        adapter = new PlayerListAdapter(getActivity());
        adapter.setItemClickListener(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        new ItemTouchHelper(simpleCallback).attachToRecyclerView(recyclerView);
        emptyListMessage = rootView.findViewById(R.id.empty_view_players);
        mViewModel = new ViewModelProvider(getActivity()).get(PlannerViewModel.class);
        mViewModel.getActivePlayers().observe(getActivity(), new Observer<List<Player>>() {
            @Override
            public void onChanged(List<Player> players) {
                setPlayerCurrentCourts(players);
                setPlayerNextCourts(players);
                adapter.setPlayerList(players);
                displayEmptyMessage(players);
            }
        });
        applySearchFilter();
        return rootView;
    }

    private void setPlayerNextCourts(List<Player> players) {
        Map<String, List<Court>> playerNextCourtsMap = getPlayerNextCourtsMap();
        for (Player player : players) {
            List<Court> courts = playerNextCourtsMap.get(player.getId());
            if (courts == null) {
                player.setNextCourt("Finished");
                continue;
            }
            Court nextCourtAppearance = null;
            for (Court court : courts) {
                if (nextCourtAppearance == null ||
                        court.getTimeslot() < nextCourtAppearance.getTimeslot()) {
                    nextCourtAppearance = court;
                }
            }
            if (nextCourtAppearance != null) {
                String court = nextCourtAppearance.getCourtName();
                String time = TimeUtil.timeConverter(nextCourtAppearance.getTimeslot());
                player.setNextCourt(time + " (" + court + ")");
            } else {
                player.setNextCourt("Finished");
            }
        }
    }

    private Map<String, List<Court>> getPlayerNextCourtsMap() {
        Map<String, List<Court>> playerNextCourtsMap = new HashMap<>();
        List<Fixture> allFixtures = mViewModel.getReschedulableFixtures();
        for (Fixture fixture : allFixtures) {
            List<Court> courts = fixture.getCourts();
            for (Court court : courts) {
                Player playerA = court.getPlayerA();
                if (playerA == null) {
                    continue;
                }
                List<Court> playerACourtList = playerNextCourtsMap.get(playerA.getId());
                if (playerACourtList == null) {
                    playerACourtList = new ArrayList<>();
                }
                playerACourtList.add(court);
                Player playerB = court.getPlayerB();
                List<Court> playerBCourtList = playerNextCourtsMap.get(playerB.getId());
                if (playerBCourtList == null) {
                    playerBCourtList = new ArrayList<>();
                }
                playerBCourtList.add(court);
                playerNextCourtsMap.put(playerA.getId(), playerACourtList);
                playerNextCourtsMap.put(playerB.getId(), playerBCourtList);
            }
        }
        return playerNextCourtsMap;
    }

    private Map<String, Court> getPlayerCurrentCourtMap() {
        Fixture currentFixture = mViewModel.getCurrentFixture();
        Map<String, Court> playerCourtMap = new HashMap<>();
        if (currentFixture != null) {
            List<Court> courts = currentFixture.getCourts();
            for (Court court : courts) {
                if (court.getPlayerA() == null) {
                    continue;
                }
                playerCourtMap.put(court.getPlayerA().getId(), court);
                playerCourtMap.put(court.getPlayerB().getId(), court);
            }
        }
        return playerCourtMap;
    }

    private void setPlayerCurrentCourts(List<Player> players) {
        Map<String, Court> playerCourtMap = getPlayerCurrentCourtMap();
        for (Player player : players) {
            Court court = playerCourtMap.get(player.getId());
            if (court == null) {
                player.setCurrentCourt("Resting");
            } else {
                String courtName = court.getCourtName();
                String time = TimeUtil.timeConverter(court.getTimeslot());
                player.setCurrentCourt(time + "(" + courtName + ")");
            }
        }
    }

    private void applySearchFilter() {
        MaterialToolbar toolbar = (MaterialToolbar) getActivity().findViewById(R.id.topAppBar);
        Menu menu = toolbar.getMenu();
        SearchView searchView = (SearchView) menu.findItem(R.id.search_players)
                .getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                adapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                adapter.getFilter().filter(query);
                return false;
            }
        });
    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0,
            ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            List<Player> players = adapter.getPlayerList();
            final Player player = players.get(viewHolder.getAdapterPosition());
            new AlertDialog.Builder(getContext())
                    .setTitle("Remove Player")
                    .setMessage("Are you sure you wish to remove " + player.getName() + "?")
                    .setCancelable(false)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mViewModel.deletePlayer(player);
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            adapter.notifyDataSetChanged();
                        }
                    })
                    .show();
        }
    };

    private void displayEmptyMessage(List<?> data) {
        if (data == null || data.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            emptyListMessage.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyListMessage.setVisibility(View.GONE);
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        Player player = adapter.getPlayerList().get(position);
        Intent intent = new Intent(getActivity(), EditPlayerActivity.class);
        intent.putExtra(MainActivity.EXTRA_PLAYER, player);
        getActivity().startActivityForResult(intent,
                MainActivity.EDIT_PLAYER_ACTIVITY_REQUEST_CODE);
    }
}
