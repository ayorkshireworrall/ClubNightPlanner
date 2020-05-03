package alex.worrall.clubnightplanner.ui.main;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import alex.worrall.clubnightplanner.R;
import alex.worrall.clubnightplanner.ui.main.courts.CourtsFragment;
import alex.worrall.clubnightplanner.ui.main.fixtures.FixturesFragment;
import alex.worrall.clubnightplanner.ui.main.players.PlayersFragment;

import static alex.worrall.clubnightplanner.ui.main.TabPositions.COURTS;
import static alex.worrall.clubnightplanner.ui.main.TabPositions.FIXTURES;
import static alex.worrall.clubnightplanner.ui.main.TabPositions.PLAYERS;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab_text_1, R.string.tab_text_2, R.string.tab_text_3};
    private final Context mContext;

    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        return getFragment(position);

    }

    private Fragment getFragment(int position) {
        switch (position) {
            case PLAYERS:
                return PlayersFragment.getInstance();
            case COURTS:
                return CourtsFragment.getInstance();
            case FIXTURES:
                return FixturesFragment.getInstance();
            default:
                return null;
        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        // Show 2 total pages.
        return 3;
    }
}