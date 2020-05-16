package alex.worrall.clubnightplanner.ui.main.players;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import alex.worrall.clubnightplanner.R;
import alex.worrall.clubnightplanner.model.PlannerDatabase;

public class AddPlayerActivity extends AppCompatActivity {

    public static final String EXTRA_NAME = AddPlayerActivity.class.getName() + ".NAME";
    public static final String EXTRA_LEVEL = AddPlayerActivity.class.getName() + ".LEVEL";

    private EditText nameText;
    private EditText levelText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_player);
        nameText = findViewById(R.id.add_player_name);
        levelText = findViewById(R.id.add_player_level);
        Button submitButton = findViewById(R.id.submit_new_player);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameText.getText().toString();
                if (name.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please add a non empty player name",
                            Toast.LENGTH_SHORT).show();
                } else if (isNameUsed(name)) {
                    Toast.makeText(getApplicationContext(), "The name " + name +
                                    " is already in use, please choose another name",
                            Toast.LENGTH_SHORT).show();
                } else if (levelText.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please add a player level",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Intent replyIntent = new Intent();
                    replyIntent.putExtra(EXTRA_NAME, name);
                    replyIntent.putExtra(EXTRA_LEVEL, levelText.getText().toString());
                    setResult(RESULT_OK, replyIntent);
                    finish();
                }
            }
        });
    }

    private boolean isNameUsed(String name) {
        //TODO implement this by checking current data for name
        return false;
    }
}
