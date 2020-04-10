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
import androidx.fragment.app.Fragment;

import java.util.List;

import alex.worrall.clubnightplanner.R;
import alex.worrall.clubnightplanner.service.ServiceApi;

public class EditPlayerFragment extends Fragment {

    ServiceApi service = new ServiceApi();

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
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
                } else if (service.isPlayerNameUsed(name)) {
                    //TODO possible to change the player level but not the name...
                    String message = "Player name " + name +
                            " already in use, please use another name";
                    Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                } else {
                    //TODO improve the way that the player is updated
                    player.setName(name);
                    player.setLevel(Integer.parseInt(level));
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
}
