package alex.worrall.clubnightplanner.ui.main.courts;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import alex.worrall.clubnightplanner.MainActivity;
import alex.worrall.clubnightplanner.R;
import alex.worrall.clubnightplanner.model.PlannerViewModel;
import alex.worrall.clubnightplanner.model.court.CourtName;

public class CourtsFragment extends Fragment {
    PlannerViewModel mViewModel;
    private CourtListAdapter adapter;
    public CourtsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_courts, container, false);
        mViewModel = new ViewModelProvider(getActivity()).get(PlannerViewModel.class);
        RecyclerView courtRecyclerView = rootView.findViewById(R.id.courts_recyclerview);
        adapter = new CourtListAdapter(getActivity());
        courtRecyclerView.setAdapter(adapter);
        courtRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mViewModel.getAllCourts().observe(getActivity(), new Observer<List<CourtName>>() {
            @Override
            public void onChanged(List<CourtName> courtNames) {
                adapter.setmCourtNames(courtNames);
            }
        });

        return rootView;
    }
}
