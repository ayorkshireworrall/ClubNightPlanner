package alex.worrall.clubnightplanner.ui.main.players;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.List;

import alex.worrall.clubnightplanner.R;
import alex.worrall.clubnightplanner.service.Scheduler;
import alex.worrall.clubnightplanner.service.ServiceApi;

public class AddPlayerFragment extends Fragment {

    private boolean hasName = false;
    private boolean hasLevel = false;
    private ServiceApi service = new ServiceApi();

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_player, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        final Context context = getContext();
        super.onViewCreated(view, savedInstanceState);
        final Button submitButton = view.findViewById(R.id.buttonSubmitNewPlayer);
        final EditText nameField = view.findViewById(R.id.editTextPlayerName);
        final EditText levelField = view.findViewById(R.id.editTextPlayerLevel);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nameField.getText().toString().isEmpty()) {
                    String message = "Please fill in the player name field";
                    Toast toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
                    toast.show();
                } else if (levelField.getText().toString().isEmpty()) {
                    String message = "Please fill in the player level field";
                    Toast toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
                    toast.show();
                } else if (checkForExistingName(nameField.getText().toString())) {
                    String message = "Player name " + nameField.getText().toString() +
                            " already in use, please use another name";
                    Toast toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
                    toast.show();
                } else {
                    service.addPlayer(nameField.getText().toString(), Integer.parseInt(levelField.getText().toString()));
                    String message = "Successfully added " + nameField.getText().toString();
                    Toast toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
                    toast.show();
                    getActivity().finish();
                }
            }

            private boolean checkForExistingName(String name) {
                boolean exists = false;
                List<Player> players = service.getPlayers();
                for (Player player : players) {
                    if (player.getName().equalsIgnoreCase(name)) {
                        exists = true;
                    }
                }
                return exists;
            }
        });
    }


}
