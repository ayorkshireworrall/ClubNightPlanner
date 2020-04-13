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

import static alex.worrall.clubnightplanner.service.Status.COMPLETED;
import static alex.worrall.clubnightplanner.service.Status.IN_PROGRESS;
import static alex.worrall.clubnightplanner.service.Status.LATER;
import static alex.worrall.clubnightplanner.service.Status.NEXT;

public class FixturesFragment extends Fragment implements FixtureRecyclerViewAdapter.ItemClickListener {

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
        List<Fixture> data = service.getOrderedFixtures();
        adapter = new FixtureRecyclerViewAdapter(getContext(), data);
        adapter.setItemClickListener(this);
        recyclerView.setAdapter(adapter);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(FixturesViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public void onItemClick(View view, int pos) {
        List<Fixture> orderedFixtures = service.getOrderedFixtures();
        Fixture clickedFixture = orderedFixtures.get(pos);
        switch (clickedFixture.getPlayStatus()) {
            case NEXT:
                System.out.println(NEXT.getMessage());
                break;
            case LATER:
                System.out.println(LATER.getMessage());
                break;
            case COMPLETED:
                System.out.println(COMPLETED.getMessage());
                break;
            case IN_PROGRESS:
                System.out.println(IN_PROGRESS.getMessage());
                break;
            default:
                break;
        }
    }
}
