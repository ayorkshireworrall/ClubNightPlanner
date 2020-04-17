package alex.worrall.clubnightplanner.ui.main.fixtures;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import alex.worrall.clubnightplanner.R;
import alex.worrall.clubnightplanner.ui.main.TabPositions;


public class AddFixtureActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_fixture);
        Toolbar toolbar = findViewById(R.id.toolbarAddFixture);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent parentIntent = getParentActivityIntent();
                parentIntent.putExtra("TAB_POSITION", TabPositions.FIXTURES);
                navigateUpTo(parentIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
