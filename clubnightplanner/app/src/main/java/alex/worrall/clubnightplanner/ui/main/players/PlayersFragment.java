package alex.worrall.clubnightplanner.ui.main.players;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import alex.worrall.clubnightplanner.Observer;
import alex.worrall.clubnightplanner.R;
import alex.worrall.clubnightplanner.service.ServiceApi;

import static alex.worrall.clubnightplanner.service.RequestCodes.EDIT_PLAYER_REQUEST;

public class PlayersFragment extends Fragment
        implements PlayerRecyclerViewAdapter.ItemClickListener, Observer {

    private PlayersViewModel viewModel;
    private RecyclerView recyclerView;
    private TextView emptyListMsg;
    private ServiceApi service = ServiceApi.getInstance();
    private PlayerRecyclerViewAdapter adapter;
    private static PlayersFragment instance;

    public static PlayersFragment getInstance() {
        if (instance == null) {
            instance = new PlayersFragment();
        }
        return instance;
    }

    private PlayersFragment() {
        subscribe();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_players, container, false);
        FloatingActionButton fab = root.findViewById(R.id.fab_players);
        recyclerView = root.findViewById(R.id.players_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        emptyListMsg = root.findViewById(R.id.empty_view_players);
        List<Player> viewData = service.getPlayers();
        displayEmptyMsgCheck(viewData);
        adapter = new PlayerRecyclerViewAdapter(getContext(), viewData);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddPlayerActivity.class);
                startActivity(intent);
            }
        });
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(PlayersViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public void onItemClick(View view, int position) {
        List<Player> players = service.getPlayers();
        Player player = players.get(position);
        Intent intent = new Intent(getActivity(), EditPlayerActivity.class);
        intent.putExtra(getString(R.string.player_uuid_key), player.getUuid());
        startActivityForResult(intent, EDIT_PLAYER_REQUEST);
    }

    @Override
    public void update() {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void subscribe() {
        EditPlayerFragment.getInstance().register(this);
    }

    ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0
            , ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView,
                              @NonNull RecyclerView.ViewHolder viewHolder,
                              @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            List<Player> data = service.getPlayers();
            final Player player = data.get(viewHolder.getAdapterPosition());
            new AlertDialog.Builder(getContext())
                    .setTitle("Remove Player")
                    .setMessage("Are you sure you wish to remove " + player.getName())
                    .setCancelable(false)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            service.removePlayer(player.getUuid());
                            displayEmptyMsgCheck(service.getPlayers());
                            adapter.notifyDataSetChanged();
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            adapter.notifyDataSetChanged();
                        }
                    }).show();
        }
    };

    private void displayEmptyMsgCheck(List<?> viewData) {
        if (viewData.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            emptyListMsg.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyListMsg.setVisibility(View.GONE);
        }
    }
}
