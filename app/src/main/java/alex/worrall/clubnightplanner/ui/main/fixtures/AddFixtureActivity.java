package alex.worrall.clubnightplanner.ui.main.fixtures;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import alex.worrall.clubnightplanner.R;
import alex.worrall.clubnightplanner.model.PlannerViewModel;
import alex.worrall.clubnightplanner.model.court.CourtName;
import alex.worrall.clubnightplanner.model.fixture.Fixture;
import alex.worrall.clubnightplanner.model.settings.Preferences;
import alex.worrall.clubnightplanner.utils.TimeUtil;
import alex.worrall.clubnightplanner.utils.schedulers.SchedulerV3;

public class AddFixtureActivity extends AppCompatActivity {
    PlannerViewModel viewModel;
    RecyclerView recyclerView;
    CourtPickerListAdapter adapter;
    private int min;
    private int hr;
    private Preferences preferences;
    private TextView timeOutput;
    private boolean allCheckedDefault = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_fixture);
        recyclerView = findViewById(R.id.fixture_court_picker);
        adapter = new CourtPickerListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        viewModel = new ViewModelProvider(this).get(PlannerViewModel.class);
        viewModel.getAllCourtsLive().observe(this, new Observer<List<CourtName>>() {
            @Override
            public void onChanged(List<CourtName> courtNames) {
                List<String> courts = new ArrayList<>();
                for (CourtName courtName : courtNames) {
                    courts.add(courtName.getName());
                }
                adapter.setCourts(courts);
                adapter.setCheckedAll(allCheckedDefault);
            }
        });
        preferences = viewModel.getActivePreferences();
        final CheckBox allSelected = findViewById(R.id.select_all_courts);
        allSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.setCheckedAll(allSelected.isChecked());
            }
        });
        allSelected.setChecked(allCheckedDefault);

        setInitialTime();
        timeOutput = findViewById(R.id.fixture_time_output);
        timeOutput.setText(TimeUtil.convertTimeToString(hr, min));
        timeOutput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(AddFixtureActivity.this, timePickerListener, hr, min, false)
                        .show();
            }
        });

        Button submit = findViewById(R.id.add_new_fixture_submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });
    }

    private void setInitialTime() {
        List<Integer> times = getFixtureTimes(viewModel.getAllFixtures());
        int latest;
        int initialTime;
        if (times.isEmpty()) {
            latest = preferences.getStartTime();
            initialTime = latest;
        } else {
            latest = Collections.max(times);
            initialTime = latest + preferences.getSessionLength();
        }
        min = initialTime % 60;
        hr = (initialTime - min) / 60;
    }

    private List<Integer> getFixtureTimes(@Nullable List<Fixture> fixtures) {
        List<Integer> times = new ArrayList<>();
        if (fixtures != null) {
            for (Fixture fixture : fixtures) {
                times.add(fixture.getTimeslot());
            }
        }
        return times;
    }

    private TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            hr = hourOfDay;
            min = minute;
            timeOutput.setText(TimeUtil.convertTimeToString(hr, min));
        }
    };

    private void submit() {
        Intent replyIntent = new Intent();
        Set<String> checkedCourts = adapter.getCheckedCourts();
        int timeSlot = hr * 60 + min;
        Fixture earliestFixture = viewModel.getMostRecentFixture();
        if (earliestFixture != null && timeSlot < earliestFixture.getTimeslot()) {
            String earliest = TimeUtil.convertTimeToString(earliestFixture.getTimeslot());
            Toast.makeText(this, "Please ensure that the fixture time is later than " + earliest,
                    Toast.LENGTH_SHORT).show();
        }
        //TODO schedule fixture with timeslot and available courts
        new SchedulerV3(this).generateSchedule(timeSlot, new ArrayList<>(checkedCourts));
        setResult(RESULT_OK,replyIntent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent replyIntent = new Intent();
                setResult(RESULT_OK, replyIntent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
