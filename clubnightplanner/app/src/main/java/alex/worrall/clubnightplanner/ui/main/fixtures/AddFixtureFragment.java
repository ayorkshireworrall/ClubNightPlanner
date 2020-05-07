package alex.worrall.clubnightplanner.ui.main.fixtures;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import alex.worrall.clubnightplanner.R;
import alex.worrall.clubnightplanner.persistence.models.CourtName;
import alex.worrall.clubnightplanner.service.ServiceApi;
import alex.worrall.clubnightplanner.service.Status;
import alex.worrall.clubnightplanner.service.TimeUtil;
import alex.worrall.clubnightplanner.ui.main.TabPositions;

public class AddFixtureFragment extends Fragment {
    private ServiceApi service = ServiceApi.getInstance();
    private boolean initialiseAllCourts = true;
    private int hr;
    private int min;
    private TextView timeOutput;
    private int sessionLength = 20;
    private FixtureCourtSelectorRecyclerViewAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_add_fixture, container, false);
        RecyclerView recyclerView = rootView.findViewById(R.id.fixture_court_selector);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        List<CourtName> data = service.getAvailableCourts();
        adapter = new FixtureCourtSelectorRecyclerViewAdapter(getContext(), data);
        recyclerView.setAdapter(adapter);
        final CheckBox selectAll = rootView.findViewById(R.id.select_all_courts);
        selectAll.setChecked(initialiseAllCourts);
        adapter.setCheckedAll(initialiseAllCourts);
        selectAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.setCheckedAll(selectAll.isChecked());
            }
        });
        setInitialTime();
        timeOutput = rootView.findViewById(R.id.fixture_time_output);
        timeOutput.setText(TimeUtil.timeConverter(hr, min));
        timeOutput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(getContext(), timePickerListener, hr, min, false).show();
            }
        });
        Button submit = rootView.findViewById(R.id.add_new_fixture_submit);
        submit.setOnClickListener(submitListener);
        return rootView;
    }

    private TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            hr = hourOfDay;
            min = minute;
            timeOutput.setText(TimeUtil.timeConverter(hr, min));
        }
    };

    private void setInitialTime() {
        Set<Integer> times = service.getFixtures().keySet();
        int latest = times.isEmpty() ? 1150 : Collections.max(times);
        int initialTime = latest + sessionLength;
        min = initialTime % 60;
        hr = (initialTime - min) / 60;
    }

    View.OnClickListener submitListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Set<CourtName> selectedCourts = adapter.getSelectedCourts();
            int timeslot = hr * 60 + min;
            Fixture earliestFixture = getEarliestFixture();
            if (earliestFixture != null && earliestFixture.getTimeSlot() > timeslot) {
                String recentlyCompletedTime = TimeUtil.timeConverter(earliestFixture.getTimeSlot());
                String message = "Please ensure that the time for this fixture is later than "
                        + recentlyCompletedTime;
                Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
                return;
            }
            List<CourtName> courts = new ArrayList<>(selectedCourts);
            Collections.sort(courts);
            service.addFixture(timeslot, courts);
//              TODO below is work around, should really use custom observable pattern and finish
//               activity.....
            Intent parentActivityIntent = getActivity().getParentActivityIntent();
            parentActivityIntent.putExtra("TAB_POSITION", TabPositions.FIXTURES);
            getActivity().navigateUpTo(parentActivityIntent);
        }

        //Get the most recently completed or in progress fixture to determine earliest
        // available timeslot
        private Fixture getEarliestFixture() {
            Fixture inProgress = null;
            List<Fixture> orderedFixtures = service.getOrderedFixtures();
            for (Fixture fixture : orderedFixtures) {
                if (fixture.getPlayStatus().equals(Status.COMPLETED)) {
                    inProgress = fixture;
                }
                if (fixture.getPlayStatus().equals(Status.IN_PROGRESS)) {
                    inProgress = fixture;
                    break;
                }
                if (fixture.getPlayStatus().equals(Status.LATER)) {
                    break;
                }
            }
            return inProgress;
        }
    };

}
