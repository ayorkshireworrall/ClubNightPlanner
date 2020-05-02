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
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import alex.worrall.clubnightplanner.R;
import alex.worrall.clubnightplanner.service.ServiceApi;
import alex.worrall.clubnightplanner.service.Status;
import alex.worrall.clubnightplanner.ui.main.players.Player;

import static alex.worrall.clubnightplanner.service.Status.COMPLETED;
import static alex.worrall.clubnightplanner.service.Status.IN_PROGRESS;
import static alex.worrall.clubnightplanner.service.Status.LATER;
import static alex.worrall.clubnightplanner.service.Status.NEXT;

public class FixturesFragment extends Fragment implements FixtureRecyclerViewAdapter.ItemClickListener {

    private FixturesViewModel mViewModel;
    private RecyclerView recyclerView;
    private TextView emptyListMsg;
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
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleAddFixtureClick();
            }
        });
        recyclerView = rootView.findViewById(R.id.fixtures_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        List<Fixture> data = service.getOrderedFixtures();
        adapter = new FixtureRecyclerViewAdapter(getContext(), data);
        adapter.setItemClickListener(this);
        recyclerView.setAdapter(adapter);
        new ItemTouchHelper(simpleCallback).attachToRecyclerView(recyclerView);
        emptyListMsg = rootView.findViewById(R.id.empty_view_fixtures);
        displayEmptyMsgCheck(data);
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
        startViewFixtureActivity(clickedFixture);
        //Might implement different functionality later
//        switch (clickedFixture.getPlayStatus()) {
//            case NEXT:
//                startViewFixtureActivity(clickedFixture);
//                break;
//            case LATER:
//                startViewFixtureActivity(clickedFixture);
//                break;
//            case COMPLETED:
//                break;
//            case IN_PROGRESS:
//                startViewFixtureActivity(clickedFixture);
//                break;
//            default:
//                break;
//        }
    }

    private void startViewFixtureActivity(Fixture fixture) {
        Intent intent = new Intent(getActivity(), ViewFixtureActivity.class);
        intent.putExtra(getString(R.string.fixture_timeslot_key), fixture.getTimeSlot());
        startActivity(intent);
    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0,
            ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            final List<Fixture> fixtures = service.getOrderedFixtures();
            final Fixture swipedFixture = fixtures.get(viewHolder.getAdapterPosition());
            switch (swipedFixture.getPlayStatus()) {
                case NEXT:
                    deleteFixture(swipedFixture);
                    break;
                case LATER:
                    swipeLater(swipedFixture, fixtures);
                    break;
                case COMPLETED:
                    Toast.makeText(getContext(), "Fixture completed, no actions available",
                            Toast.LENGTH_SHORT).show();
                    adapter.notifyDataSetChanged();
                    break;
                case IN_PROGRESS:
                    swipeInProgress(swipedFixture);
                    break;
                default:
                    break;
            }
        }

        private void swipeInProgress(final Fixture swipedFixture) {
            new AlertDialog.Builder(getContext())
                    .setTitle("Mark As Completed")
                    .setMessage("Are you sure you wish to mark the " + swipedFixture.toString() +
                            " fixture as completed?")
                    .setCancelable(false)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            service.markFixtureComplete(swipedFixture);
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

        private void swipeLater(final Fixture swipedFixture, List<Fixture> fixtures) {
            if (fixtureIsStartable(swipedFixture, fixtures)) {
                startFixture(swipedFixture);
            } else {
                deleteFixture(swipedFixture);
            }
        }

        private boolean fixtureIsStartable(Fixture swipedFixture, List<Fixture> fixtures) {
            Fixture inProgress = null;
            int completedCount = 0;
            for (Fixture fixture : fixtures) {
                if (fixture.getPlayStatus().equals(IN_PROGRESS)) {
                    inProgress = fixture;
                    break;
                }
                if (fixture.getPlayStatus().equals(COMPLETED)) {
                    completedCount++;
                }
            }
            if (inProgress != null || fixtures.get(completedCount) != swipedFixture) {
                return false;
            }
            return true;
        }

        private void startFixture(final Fixture swipedFixture) {
            new AlertDialog.Builder(getContext())
                    .setTitle("Start Fixture")
                    .setMessage("Are you sure you wish to start this fixture?")
                    .setCancelable(false)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            service.startFixture(swipedFixture);
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

        private void deleteFixture(final Fixture swipedFixture) {
            new AlertDialog.Builder(getContext())
                    .setTitle("Delete Fixture")
                    .setMessage("Are you sure you wish to delete the " + swipedFixture.toString() +
                            " fixture?")
                    .setCancelable(false)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            service.removeFixture(swipedFixture);
                            adapter.setmData(service.getOrderedFixtures());
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

    private void displayEmptyMsgCheck(List<?> viewData) {
        if (viewData.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            emptyListMsg.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyListMsg.setVisibility(View.GONE);
        }
    }

    private void handleAddFixtureClick() {
        List<Player> players = service.getPlayers();
        List<String> availableCourts = service.getAvailableCourts();

        if (availableCourts.size() > 0 && players.size() > 1) {
            Intent intent = new Intent(getActivity(), AddFixtureActivity.class);
            startActivity(intent);
        } else{
            String message = "To add a fixture ";
            if (availableCourts.size() > 0 && players.size() == 1){
                message += "add at least 1 more player";
            } else if (availableCourts.size() > 0) {
                message += "add at least 2 more players";
            } else if (players.size() > 1) {
                message += "add at least 1 more court";
            } else if (players.size() == 1) {
                message += "add at least 1 more court and add at least 1 more player";
            } else {
                message += "add at least 1 more court and add at least 2 more players";
            }
            Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
        }
    }
}
