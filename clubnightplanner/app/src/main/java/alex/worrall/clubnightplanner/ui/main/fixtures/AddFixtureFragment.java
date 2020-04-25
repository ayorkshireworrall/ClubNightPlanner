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

import java.util.List;

import alex.worrall.clubnightplanner.R;
import alex.worrall.clubnightplanner.service.ServiceApi;

public class AddFixtureFragment extends Fragment {
    private ServiceApi service = ServiceApi.getInstance();
    private boolean initialiseAllCourts = true;
    private int hr;
    private int min;
    private TextView timeOutput;

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
        timeOutput = rootView.findViewById(R.id.fixture_time_output);
        timeOutput.setText(timeConverter(hr, min));
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
            timeOutput.setText(timeConverter(hr, min));
        }
    };

    private String timeConverter(int hr, int min) {
        String am_pm, minutes;
        int hours;
        if (hr > 12) {
            am_pm = " PM";
            hours = hr - 12;
        } else if (hr == 0) {
            am_pm = " AM";
            hours = 12;
        } else {
            am_pm = " AM";
            hours = hr;
        }
        if (min < 10) {
            minutes = "0" + min;
        } else {
            minutes = "" + min;
        }
        return hours + ":" + minutes + am_pm;
    }

}
