package alex.worrall.clubnightplanner.ui.main.settings;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import java.util.List;

import alex.worrall.clubnightplanner.R;
import alex.worrall.clubnightplanner.model.PlannerViewModel;
import alex.worrall.clubnightplanner.model.settings.Preferences;
import alex.worrall.clubnightplanner.utils.TimeUtil;

public class ViewSettingsActivity extends AppCompatActivity {
    private PlannerViewModel viewModel;
    private Spinner spinner;
    int hr;
    int min;
    private TextView startTime;
    private TextView sessionLength;
    private Preferences activePreferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        viewModel = new ViewModelProvider(this).get(PlannerViewModel.class);

//        spinner = (Spinner) findViewById(R.id.settings_spinner);
//        List<Preferences> allPreferences = viewModel.getAllPreferences();
//        Preferences[] spinnerItems = allPreferences.toArray(new Preferences[allPreferences.size()]);
//        ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(this,
//                R.layout.simple_spinner_dropdown_item, spinnerItems);
//        spinnerArrayAdapter.setDropDownViewResource(R.layout.simple_spinner_item);
//        spinner.setAdapter(spinnerArrayAdapter);

        activePreferences = viewModel.getActivePreferences();
        setInitialTime();
        startTime = findViewById(R.id.settings_start_time_value);
        startTime.setText(TimeUtil.convertTimeToString(hr, min));
        startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(ViewSettingsActivity.this, timePickerListener, hr, min, false)
                        .show();

            }
        });

        sessionLength = findViewById(R.id.settings_session_length_value);
        sessionLength.setSelectAllOnFocus(true);
        sessionLength.setText(Integer.toString(activePreferences.getSessionLength()));
        sessionLength.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) ||
                        (actionId == EditorInfo.IME_ACTION_DONE)) {
                    CharSequence text = v.getText();
                    if (text.length() > 1) {
                        activePreferences.setSessionLength(Integer.parseInt(text.toString()));
                        viewModel.updatePreferences(activePreferences);
                    } else {
                        sessionLength.setText(Integer.toString(activePreferences.getSessionLength()));
                    }
                }
                return false;
            }
        });
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

    private TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            hr = hourOfDay;
            min = minute;
            startTime.setText(TimeUtil.convertTimeToString(hr, min));
            int startTime = TimeUtil.convertTimeToInt(hr, min);
            activePreferences.setStartTime(startTime);
            viewModel.updatePreferences(activePreferences);
        }
    };

    private void setInitialTime() {
        int latest = activePreferences.getStartTime();
        min = latest % 60;
        hr = (latest - min) / 60;
    }


}
