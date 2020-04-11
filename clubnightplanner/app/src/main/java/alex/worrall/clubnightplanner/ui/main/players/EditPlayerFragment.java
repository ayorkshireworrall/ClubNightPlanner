package alex.worrall.clubnightplanner.ui.main.players;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import alex.worrall.clubnightplanner.ObservableFragment;
import alex.worrall.clubnightplanner.R;
import alex.worrall.clubnightplanner.service.ServiceApi;

public class EditPlayerFragment extends ObservableFragment {

    ServiceApi service = new ServiceApi();

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        registerObservers();
        super.onViewCreated(view, savedInstanceState);
        final Bundle extras = getActivity().getIntent().getExtras();
        final Context context = getContext();
        final Player player = service.getPlayerByUuid(extras.getString(getString(R.string.player_uuid_key)));
        final EditText nameField = view.findViewById(R.id.editTextEditPlayerName);
        nameField.setText(player.getName());
        final EditText levelField = view.findViewById(R.id.editTextEditPlayerLevel);
        levelField.setText(String.valueOf(player.getLevel()));
        final Button submitButton = view.findViewById(R.id.buttonSubmitEditPlayer);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameField.getText().toString();
                String level = levelField.getText().toString();
                if (name.isEmpty()) {
                    String message = "Please fill in the player name field";
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                } else if (level.isEmpty()) {
                    String message = "Please fill in the player level field";
                    Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                } else if (service.isPlayerNameUsed(name, player.getUuid())) {
                    String message = "Player name " + name +
                            " already in use, please use another name";
                    Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                } else {
                    player.setName(name);
                    player.setLevel(Integer.parseInt(level));
                    //TODO improve the way that the player is updated (currently this does nothing
                    // and relies on the fact we are mutating the actual player object!)
                    service.updatePlayer(player);
                    notifyObservers();
                    getActivity().finish();
                }
            }
        });
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_player, container, false);
    }

    private void registerObservers() {
        register(PlayersFragment.getInstance());
    }
}
