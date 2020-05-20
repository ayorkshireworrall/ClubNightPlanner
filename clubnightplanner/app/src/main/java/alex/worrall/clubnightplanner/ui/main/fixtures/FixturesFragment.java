package alex.worrall.clubnightplanner.ui.main.fixtures;

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
import alex.worrall.clubnightplanner.model.fixture.Fixture;

public class FixturesFragment extends Fragment implements FixturesListAdapter.ItemClickListener {
    PlannerViewModel mViewModel;
    RecyclerView recyclerView;
    FixturesListAdapter adapter;
    TextView emptyListMessage;

    public FixturesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_fixtures, container, false);
        recyclerView = rootView.findViewById(R.id.fixtures_list);
        adapter = new FixturesListAdapter(getActivity());
        adapter.setItemClickListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        new ItemTouchHelper(simpleCallback).attachToRecyclerView(recyclerView);
        emptyListMessage = rootView.findViewById(R.id.empty_view_fixtures);
        mViewModel = new ViewModelProvider(getActivity()).get(PlannerViewModel.class);
        mViewModel.getAllFixturesLive().observe(getActivity(), new Observer<List<Fixture>>() {
            @Override
            public void onChanged(List<Fixture> fixtures) {
                adapter.setFixtures(fixtures);
                displayEmptyMessage(fixtures);
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
            //TODO start / delete fixture and move notify into cancel action for alert dialog
            adapter.notifyDataSetChanged();
        }
    };

    @Override
    public void onItemClicked(View view, int position) {
        Fixture fixture = adapter.getFixtures().get(position);
    }

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
