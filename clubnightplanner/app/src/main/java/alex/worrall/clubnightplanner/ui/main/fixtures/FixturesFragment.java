package alex.worrall.clubnightplanner.ui.main.fixtures;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import alex.worrall.clubnightplanner.MainActivity;
import alex.worrall.clubnightplanner.R;
import alex.worrall.clubnightplanner.model.PlannerViewModel;
import alex.worrall.clubnightplanner.model.fixture.Court;
import alex.worrall.clubnightplanner.model.fixture.Fixture;
import alex.worrall.clubnightplanner.utils.SchedulerV2;
import alex.worrall.clubnightplanner.utils.Status;

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
            List<Fixture> fixtures = adapter.getFixtures();
            Fixture fixture = fixtures.get(viewHolder.getAdapterPosition());
            if (fixture.getId() == mViewModel.getChangeableFixture().getId()) {
                changeFixture(fixture);
            } else if (fixture.getPlayStatus() != Status.COMPLETED){
                deleteFixtureDialog(fixture);
            } else {
                Toast.makeText(getContext(), "No actions available for completed fixtures",
                        Toast.LENGTH_SHORT).show();
                adapter.notifyDataSetChanged();
            }
        }
    };

    @Override
    public void onItemClicked(View view, int position) {
        Fixture fixture = adapter.getFixtures().get(position);
        Intent intent = new Intent(getActivity(), ViewMatchesActivity.class);
        intent.putExtra(MainActivity.EXTRA_FIXTURE_ID, fixture.getId());
        getActivity().startActivityForResult(intent,
                MainActivity.VIEW_MATCHES_ACTIVITY_REQUEST_CODE);
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

    private void changeFixture(Fixture fixture) {
        if (fixture.getPlayStatus().equals(Status.LATER)) {
            startFixtureDialog(fixture);
        } else if (fixture.getPlayStatus().equals(Status.IN_PROGRESS)){
            completeFixtureDialog(fixture);
        }
    }

    private void startFixtureDialog(final Fixture fixture) {
        new AlertDialog.Builder(this.getActivity())
                .setTitle("Start Fixture")
                .setMessage("Are you sure you wish to start this fixture?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        fixture.setPlayStatus(Status.IN_PROGRESS);
                        Fixture nextFixture = getNextFixture(fixture);
                        nextFixture.setPlayStatus(Status.NEXT);
                        mViewModel.updateFixtures(fixture, nextFixture);
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Do nothing
                        adapter.notifyDataSetChanged();
                    }
                }).show();
    }

    private void completeFixtureDialog(final Fixture fixture) {
        new AlertDialog.Builder(this.getActivity())
                .setTitle("Complete Fixture")
                .setMessage("Are you sure you wish to mark this fixture as complete?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        fixture.setPlayStatus(Status.COMPLETED);
                        Fixture inProgressFixture = getNextFixture(fixture);
                        if (inProgressFixture != null) {
                            inProgressFixture.setPlayStatus(Status.IN_PROGRESS);
                        }
                        Fixture nextFixture = getNextFixture(inProgressFixture);
                        if (nextFixture != null) {
                            nextFixture.setPlayStatus(Status.NEXT);
                        }
                        mViewModel.updateFixtures(fixture, inProgressFixture, nextFixture);
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Do nothing
                        adapter.notifyDataSetChanged();
                    }
                }).show();
    }

    private void deleteFixtureDialog(final Fixture fixture) {
        new AlertDialog.Builder(this.getActivity())
                .setTitle("Delete Fixture")
                .setMessage("Are you sure you wish to permanently delete this fixture?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SchedulerV2 schedulerV2 =
                                new SchedulerV2((AppCompatActivity) FixturesFragment.this.getActivity());
                        schedulerV2.unschedule(fixture);
                        List<Fixture> laterFixtures = getFollowingFixtures(fixture.getTimeslot());
                        schedulerV2.reschedule(laterFixtures);
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        adapter.notifyDataSetChanged();
                    }
                }).show();
    }

    private List<Fixture> getFollowingFixtures(int timeslot) {
        List<Fixture> fixtures = new ArrayList<>();
        for (Fixture fixture : adapter.getFixtures()) {
            if (fixture.getTimeslot() > timeslot) {
                fixtures.add(fixture);
            }
        }
        return fixtures;
    }

    private Fixture getNextFixture(@Nullable Fixture current) {
        if (current == null) {
            return null;
        }
        for (Fixture fixture : adapter.getFixtures()) {
            if (fixture.getTimeslot() > current.getTimeslot()) {
                return fixture;
            }
        }
        return null;
    }
}
