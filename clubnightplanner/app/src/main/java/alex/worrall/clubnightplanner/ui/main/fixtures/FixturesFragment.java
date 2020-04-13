package alex.worrall.clubnightplanner.ui.main.fixtures;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import alex.worrall.clubnightplanner.R;
import alex.worrall.clubnightplanner.service.ServiceApi;

public class FixturesFragment extends Fragment {

    private FixturesViewModel mViewModel;
    private RecyclerView recyclerView;
    private FixtureRecyclerViewAdapter adapter;
    private ServiceApi service = ServiceApi.getInstance();

    public static FixturesFragment newInstance() {
        return new FixturesFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_fixtures, container, false);
        FloatingActionButton fab = rootView.findViewById(R.id.fab_fixtures);
        recyclerView = rootView.findViewById(R.id.fixtures_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        List<Fixture> data = getFixtures(service.getFixtures());
        adapter = new FixtureRecyclerViewAdapter(getContext(), data);
        recyclerView.setAdapter(adapter);
        return rootView;
    }

    private List<Fixture> getFixtures(Map<Integer, Fixture> fixtures) {
        List<Fixture> orderedFixtures = new ArrayList<>();
        int currentLargestInList = 0;
        for (int i = 0; i < fixtures.size(); i++) {
            int workingSmallest = 1439; //11:59
            for (int time : fixtures.keySet()) {
                if (time <= currentLargestInList) {
                    //Fixture already added to ordered list
                    continue;
                }
                if (time < workingSmallest) {
                    workingSmallest = time;
                }
            }
            currentLargestInList = workingSmallest;
            orderedFixtures.add(fixtures.get(workingSmallest));
        }
        return orderedFixtures;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(FixturesViewModel.class);
        // TODO: Use the ViewModel
    }

}
