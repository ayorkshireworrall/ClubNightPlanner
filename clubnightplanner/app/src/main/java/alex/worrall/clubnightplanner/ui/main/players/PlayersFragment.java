package alex.worrall.clubnightplanner.ui.main.players;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import alex.worrall.clubnightplanner.R;
import alex.worrall.clubnightplanner.model.PlannerViewModel;
import alex.worrall.clubnightplanner.model.player.Player;

public class PlayersFragment extends Fragment {
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
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        new ItemTouchHelper(simpleCallback).attachToRecyclerView(recyclerView);
        emptyListMessage = rootView.findViewById(R.id.empty_view_players);
        mViewModel = new ViewModelProvider(getActivity()).get(PlannerViewModel.class);
        mViewModel.getActivePlayers().observe(getActivity(), new Observer<List<Player>>() {
            @Override
            public void onChanged(List<Player> players) {
                adapter.setPlayerList(players);
                displayEmptyMessage(players);
            }
        });
        return rootView;
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
}
