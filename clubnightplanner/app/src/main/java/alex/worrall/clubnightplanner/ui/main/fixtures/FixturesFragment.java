package alex.worrall.clubnightplanner.ui.main.fixtures;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import alex.worrall.clubnightplanner.R;

public class FixturesFragment extends Fragment {

    private FixturesViewModel mViewModel;

    public static FixturesFragment newInstance() {
        return new FixturesFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_fixtures, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(FixturesViewModel.class);
        // TODO: Use the ViewModel
    }

}
