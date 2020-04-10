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
import androidx.fragment.app.Fragment;

import alex.worrall.clubnightplanner.R;
import alex.worrall.clubnightplanner.service.ServiceApi;

public class AddPlayerFragment extends Fragment {

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
        super.onViewCreated(view, savedInstanceState);
        final Context context = getContext();
        final Button submitButton = view.findViewById(R.id.buttonSubmitNewPlayer);
        final EditText nameField = view.findViewById(R.id.editTextNewPlayerName);
        final EditText levelField = view.findViewById(R.id.editTextNewPlayerLevel);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameField.getText().toString();
                String level = levelField.getText().toString();
                if (name.isEmpty()) {
                    String message = "Please fill in the player name field";
                    Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
                    toast.show();
                } else if (level.isEmpty()) {
                    String message = "Please fill in the player level field";
                    Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
                    toast.show();
                } else if (service.isPlayerNameUsed(name)) {
                    String message = "Player name " + name +
                            " already in use, please use another name";
                    Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    service.addPlayer(name, Integer.parseInt(level));
                    String message = "Successfully added " + nameField.getText().toString();
                    Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
                    toast.show();
                    getActivity().finish();
                }
            }
        });
    }


}
