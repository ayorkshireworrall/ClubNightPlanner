package alex.worrall.clubnightplanner.ui.main.courts;

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
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import alex.worrall.clubnightplanner.R;
import alex.worrall.clubnightplanner.service.ServiceApi;

public class CourtsFragment extends Fragment {

    private CourtsViewModel mViewModel;
    private RecyclerView recyclerView;
    private CourtRecyclerViewAdapter adapter;
    private ServiceApi service = ServiceApi.getInstance();
    private static CourtsFragment courtsFragment;

    private CourtsFragment() {

    }

    public static CourtsFragment getInstance() {
        if (courtsFragment == null) {
            courtsFragment = new CourtsFragment();
        }
        return courtsFragment;
    }

    public static CourtsFragment newInstance() {
        return new CourtsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_courts, container, false);
        FloatingActionButton fab = rootView.findViewById(R.id.fab_courts);
        recyclerView = rootView.findViewById(R.id.courts_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        List<String> viewData = service.getAvailableCourts();
        adapter = new CourtRecyclerViewAdapter(getContext(), viewData);
        recyclerView.setAdapter(adapter);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(CourtsViewModel.class);
        // TODO: Use the ViewModel
    }
}
