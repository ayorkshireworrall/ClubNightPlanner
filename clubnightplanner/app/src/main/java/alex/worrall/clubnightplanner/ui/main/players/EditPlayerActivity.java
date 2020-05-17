package alex.worrall.clubnightplanner.ui.main.players;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import alex.worrall.clubnightplanner.MainActivity;
import alex.worrall.clubnightplanner.R;
import alex.worrall.clubnightplanner.model.player.Player;
import alex.worrall.clubnightplanner.model.player.PlayerRepository;

public class EditPlayerActivity extends AppCompatActivity {

    public static final String EXTRA_ID = AddPlayerActivity.class.getName() + ".ID";
    public static final String EXTRA_NAME = AddPlayerActivity.class.getName() + ".NAME";
    public static final String EXTRA_LEVEL = AddPlayerActivity.class.getName() + ".LEVEL";

    private EditText nameText;
    private EditText levelText;

    private PlayerRepository playerRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_player);
        playerRepository = new PlayerRepository(getApplication());
        final Bundle extras = getIntent().getExtras();
        final Player player = extras.getParcelable(MainActivity.EXTRA_PLAYER);
        setContentView(R.layout.activity_add_player);
        nameText = findViewById(R.id.player_form_name);
        nameText.setText(player.getName());
        levelText = findViewById(R.id.player_form_level);
        levelText.setText(String.valueOf(player.getLevel()));
        Button submitButton = findViewById(R.id.submit_player_form);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameText.getText().toString();
                if (name.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please add a non empty player name",
                            Toast.LENGTH_SHORT).show();
                } else if (!name.equals(player.getName()) && isNameUsed(name)) {
                    Toast.makeText(getApplicationContext(), "The name " + name +
                                    " is already in use, please choose another name",
                            Toast.LENGTH_SHORT).show();
                } else if (levelText.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please add a player level",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Intent replyIntent = new Intent();
                    player.setLevel(Integer.parseInt(levelText.getText().toString()));
                    player.setName(name);
                    replyIntent.putExtra(MainActivity.EXTRA_PLAYER, player);
                    setResult(RESULT_OK, replyIntent);
                    finish();
                }
            }
        });
    }

    private boolean isNameUsed(String name) {
        return playerRepository.getPlayerByName(name) != null;
    }
}
