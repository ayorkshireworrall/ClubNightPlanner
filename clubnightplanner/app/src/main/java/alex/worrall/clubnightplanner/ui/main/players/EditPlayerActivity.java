package alex.worrall.clubnightplanner.ui.main.players;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import alex.worrall.clubnightplanner.R;

import static alex.worrall.clubnightplanner.service.RequestCodes.EDIT_PLAYER_RESULT;

public class EditPlayerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_player);
        Toolbar toolbar = findViewById(R.id.toolbarEditPlayer);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void finish() {
//        Intent returnIntent = new Intent();
//        returnIntent.putExtra("Dummy", "Data");
        setResult(EDIT_PLAYER_RESULT);
        super.finish();
    }
}
