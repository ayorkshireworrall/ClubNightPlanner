package alex.worrall.clubnightplanner.ui.main.fixtures;

import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Map;

import alex.worrall.clubnightplanner.R;
import alex.worrall.clubnightplanner.persistence.models.fixture.Fixture;
import alex.worrall.clubnightplanner.service.ServiceApi;
import alex.worrall.clubnightplanner.service.Status;
import alex.worrall.clubnightplanner.service.TimeUtil;
import alex.worrall.clubnightplanner.persistence.models.fixture.court.Court;

public class ViewFixtureFragment extends Fragment {
    ServiceApi service = ServiceApi.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_view_fixture, container, false);
        Map<Integer, Fixture> fixtures = service.getFixtures();
        final Bundle extras = getActivity().getIntent().getExtras();
        int timeSlot = extras.getInt(getString(R.string.fixture_timeslot_key));
        String timeString = TimeUtil.timeConverter(timeSlot);
        Fixture fixture = fixtures.get(timeSlot);
        List<Court> recyclerViewData = fixture.getCourts();
        RecyclerView recyclerView = rootView.findViewById(R.id.view_fixture_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        FixturesCourtsRecyclerViewAdapter adapter =
                new FixturesCourtsRecyclerViewAdapter(getContext(), recyclerViewData);
        recyclerView.setAdapter(adapter);
        TextView timeTextView = rootView.findViewById(R.id.view_fixture_time);
        timeTextView.setText(timeString);
        if (fixture.getPlayStatus().equals(Status.COMPLETED)) {
            timeTextView.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        }
        return rootView;
    }
}
