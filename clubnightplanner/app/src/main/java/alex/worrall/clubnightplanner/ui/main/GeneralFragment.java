package alex.worrall.clubnightplanner.ui.main;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainer;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentFactory;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager2.adapter.FragmentViewHolder;

import alex.worrall.clubnightplanner.ObservableFragment;
import alex.worrall.clubnightplanner.ui.main.players.EditPlayerFragment;

/**
 * A massive hack! When using the fragment directly in an activity xml Java calls instanciate()
 * via reflection. Constructors for fragments have been set to private and instead use a
 * getInstance() method. Private constructors result in an error when instanciate() is called.
 * This class acts as a container so that the fragment can be set using getInstance
 * TODO find a better container!!!!
 */
public class GeneralFragment extends FragmentPagerAdapter {
    Fragment fragment;

    public GeneralFragment(@NonNull FragmentManager fm) {
        super(fm);
    }

    public GeneralFragment(@NonNull FragmentManager fm, ObservableFragment fragment) {
        super(fm);
        this.fragment = fragment;
    }



    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragment;
    }

    @Override
    public int getCount() {
        return 1;
    }
}
