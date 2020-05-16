package alex.worrall.clubnightplanner.ui.main.courts;

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
import alex.worrall.clubnightplanner.model.court.CourtName;

public class CourtsFragment extends Fragment {
    PlannerViewModel mViewModel;
    private CourtListAdapter adapter;
    RecyclerView courtRecyclerView;
    TextView emptyListMessage;
    public CourtsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_courts, container, false);
        mViewModel = new ViewModelProvider(getActivity()).get(PlannerViewModel.class);
        courtRecyclerView = rootView.findViewById(R.id.courts_recyclerview);
        adapter = new CourtListAdapter(getActivity());
        courtRecyclerView.setAdapter(adapter);
        courtRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        new ItemTouchHelper(simpleCallback).attachToRecyclerView(courtRecyclerView);
        emptyListMessage = rootView.findViewById(R.id.empty_view_courts);
        mViewModel.getAllCourts().observe(getActivity(), new Observer<List<CourtName>>() {
            @Override
            public void onChanged(List<CourtName> courtNames) {
                adapter.setmCourtNames(courtNames);
                displayEmptyMessage(courtNames);
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
            List<CourtName> courtNames = adapter.getmCourtNames();
            final CourtName courtName = courtNames.get(viewHolder.getAdapterPosition());
            new AlertDialog.Builder(getContext())
                    .setTitle("Remove Court")
                    .setMessage("Are you sure you wish to remove " + courtName.getName())
                    .setCancelable(false)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mViewModel.deleteCourt(courtName);
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
            courtRecyclerView.setVisibility(View.GONE);
            emptyListMessage.setVisibility(View.VISIBLE);
        } else {
            courtRecyclerView.setVisibility(View.VISIBLE);
            emptyListMessage.setVisibility(View.GONE);
        }
    }
}
