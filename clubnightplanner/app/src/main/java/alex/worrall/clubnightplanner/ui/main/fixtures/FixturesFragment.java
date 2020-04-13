package alex.worrall.clubnightplanner.ui.main.fixtures;

import androidx.lifecycle.ViewModelProviders;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

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
        new ItemTouchHelper(simpleCallback).attachToRecyclerView(recyclerView);
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

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0,
            ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            List<Fixture> fixtures = service.getOrderedFixtures();
            final Fixture swipedFixture = fixtures.get(viewHolder.getAdapterPosition());
            switch (swipedFixture.getPlayStatus()) {
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
    };
}
