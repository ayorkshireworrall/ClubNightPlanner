package alex.worrall.clubnightplanner.ui.main.players;

import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import alex.worrall.clubnightplanner.R;
import alex.worrall.clubnightplanner.service.ServiceApi;

import static alex.worrall.clubnightplanner.service.RequestCodes.EDIT_PLAYER_REQUEST;

public class PlayersFragment extends Fragment implements PlayerRecyclerViewAdapter.ItemClickListener {

    private PlayersViewModel viewModel;
    private ServiceApi service;

    public static PlayersFragment newInstance() {
        return new PlayersFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        service = new ServiceApi();
        View root = inflater.inflate(R.layout.fragment_players, container, false);
        FloatingActionButton fab = root.findViewById(R.id.fab_players);
        RecyclerView recyclerView = root.findViewById(R.id.players_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        List<Player> viewData = service.getPlayers();
        PlayerRecyclerViewAdapter adapter = new PlayerRecyclerViewAdapter(getContext(), viewData);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
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
}
