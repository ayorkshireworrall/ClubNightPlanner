package alex.worrall.clubnightplanner.ui.main.fixtures;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import alex.worrall.clubnightplanner.MainActivity;
import alex.worrall.clubnightplanner.R;
import alex.worrall.clubnightplanner.model.fixture.Fixture;
import alex.worrall.clubnightplanner.model.fixture.FixtureRepository;
import alex.worrall.clubnightplanner.ui.main.TabPositions;
import alex.worrall.clubnightplanner.utils.Status;
import alex.worrall.clubnightplanner.utils.TimeUtil;

public class ViewMatchesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_matches);
        final Bundle extras = getIntent().getExtras();
        //Would have used parcelable, but weird error with second player when writing courts from
        //parcel
        int fixtureId = extras.getInt(MainActivity.EXTRA_FIXTURE_ID);
        FixtureRepository fixtureRepository = new FixtureRepository(this.getApplication());
        final Fixture fixture = fixtureRepository.getFixtureById(fixtureId);
        TextView fixtureTime = findViewById(R.id.view_fixture_time);
        fixtureTime.setText(TimeUtil.timeConverter(fixture.getTimeslot()));
        RecyclerView recyclerView = findViewById(R.id.view_fixture_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        MatchListAdapter adapter = new MatchListAdapter(this, fixture.getCourts());
        recyclerView.setAdapter(adapter);
        if (fixture != null && fixture.getPlayStatus().equals(Status.COMPLETED)) {
            fixtureTime.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        }
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
