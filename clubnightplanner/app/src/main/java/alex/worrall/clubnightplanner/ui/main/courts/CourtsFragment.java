package alex.worrall.clubnightplanner.ui.main.courts;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import alex.worrall.clubnightplanner.R;
import alex.worrall.clubnightplanner.service.ServiceApi;

public class CourtsFragment extends Fragment implements CourtRecyclerViewAdapter.ItemClickListener {

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
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_courts, container, false);
        FloatingActionButton fab = rootView.findViewById(R.id.fab_courts);
        recyclerView = rootView.findViewById(R.id.courts_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        List<String> viewData = service.getAvailableCourts();
        adapter = new CourtRecyclerViewAdapter(getContext(), viewData);
        adapter.setmClickListener(this);
        recyclerView.setAdapter(adapter);
        new ItemTouchHelper(itemTouchHelper).attachToRecyclerView(recyclerView);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> courts = service.getAvailableCourts();
                String newCourtName = null;
                if (courts.size() > 0) {
                    String lastCourtName = courts.get(courts.size() - 1);
                    newCourtName = "";
                    if (lastCourtName.matches("^Court \\d*$")) {
                        int count = Integer.parseInt(lastCourtName.replace("Court " , ""));
                        newCourtName = "Court " + (count + 1);
                    } else {
                        newCourtName = "Court " + (courts.size() + 1);
                    }
                } else {
                    newCourtName = "Court 1";
                }
                service.addCourt(newCourtName);
                adapter.notifyDataSetChanged();
            }
        });
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(CourtsViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public void onItemClick() {
        //currently do nothing but may change in future
        System.out.println("Fragment Says Hello");
    }

    ItemTouchHelper.SimpleCallback itemTouchHelper = new ItemTouchHelper.SimpleCallback(0,
            ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            List<String> data = service.getAvailableCourts();
            final String court = data.get(viewHolder.getAdapterPosition());
            new AlertDialog.Builder(getContext())
                    .setTitle("Remove Court")
                    .setMessage("Are you sure you wish to remove " + court)
                    .setCancelable(false)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            service.removeCourt(court);
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
}
