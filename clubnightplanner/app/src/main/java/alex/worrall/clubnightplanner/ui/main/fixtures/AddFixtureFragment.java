package alex.worrall.clubnightplanner.ui.main.fixtures;

import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import alex.worrall.clubnightplanner.R;
import alex.worrall.clubnightplanner.service.ServiceApi;
import alex.worrall.clubnightplanner.service.TimeUtil;

public class AddFixtureFragment extends Fragment {
    private ServiceApi service = ServiceApi.getInstance();
    private boolean initialiseAllCourts = true;
    private int hr;
    private int min;
    private TextView timeOutput;
    private int sessionLength = 20;

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
        final FixtureCourtSelectorRecyclerViewAdapter adapter =
                new FixtureCourtSelectorRecyclerViewAdapter(getContext(), data);
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
                new TimePickerDialog(getContext(), timePickerListener, 0, 0, false).show();
            }
        });
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
        int latest = Collections.max(times);
        int initialTime = latest + sessionLength;
        min = initialTime % 60;
        hr = (initialTime - min) / 60;
    }

}
