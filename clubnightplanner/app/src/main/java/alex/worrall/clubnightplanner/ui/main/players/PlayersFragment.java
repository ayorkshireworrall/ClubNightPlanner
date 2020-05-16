package alex.worrall.clubnightplanner.ui.main.players;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import alex.worrall.clubnightplanner.R;

public class PlayersFragment extends Fragment {

    public PlayersFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_players, container, false);
    }
}
