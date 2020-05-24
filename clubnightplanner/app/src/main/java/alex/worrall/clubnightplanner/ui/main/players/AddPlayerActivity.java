package alex.worrall.clubnightplanner.ui.main.players;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import alex.worrall.clubnightplanner.R;
import alex.worrall.clubnightplanner.model.player.Player;
import alex.worrall.clubnightplanner.model.player.PlayerRepository;

public class AddPlayerActivity extends AppCompatActivity {

    public static final String EXTRA_NAME = AddPlayerActivity.class.getName() + ".NAME";
    public static final String EXTRA_LEVEL = AddPlayerActivity.class.getName() + ".LEVEL";

    private EditText nameText;
    private EditText levelText;

    private PlayerRepository playerRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        playerRepository = new PlayerRepository(getApplication());
        setContentView(R.layout.activity_add_player);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        nameText = findViewById(R.id.player_form_name);
        levelText = findViewById(R.id.player_form_level);
        levelText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) ||
                        (actionId == EditorInfo.IME_ACTION_DONE)) {
                    submit();
                }
                return false;
            }
        });
        Button submitButton = findViewById(R.id.submit_player_form);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });
    }

    private void submit() {
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

    private boolean isNameUsed(String name) {
        return playerRepository.getPlayerByName(name) != null;
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
