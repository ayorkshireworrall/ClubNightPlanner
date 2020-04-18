package alex.worrall.clubnightplanner.ui.main.fixtures;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import alex.worrall.clubnightplanner.R;
import alex.worrall.clubnightplanner.service.ServiceApi;

public class AddFixtureFragment extends Fragment {
    private ServiceApi service = ServiceApi.getInstance();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_add_fixture, container, false);
        RecyclerView recyclerView = rootView.findViewById(R.id.fixture_court_selector);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        List<String> data = service.getAvailableCourts();
        FixtureCourtSelectorRecyclerViewAdapter adapter =
                new FixtureCourtSelectorRecyclerViewAdapter(getContext(), data);
        recyclerView.setAdapter(adapter);
        return rootView;
    }
}
