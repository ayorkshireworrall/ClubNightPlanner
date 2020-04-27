package alex.worrall.clubnightplanner.ui.main.fixtures;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView ;

import java.util.List;

import alex.worrall.clubnightplanner.R;
import alex.worrall.clubnightplanner.ui.main.courts.Court;

public class FixturesCourtsRecyclerViewAdapter extends
        RecyclerView.Adapter<FixturesCourtsRecyclerViewAdapter.ViewHolder>{

    List<Court> mData;
    LayoutInflater mLayoutInflater;

    public FixturesCourtsRecyclerViewAdapter(Context context, List<Court> mData) {
        this.mData = mData;
        this.mLayoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =
                mLayoutInflater.inflate(R.layout.recyclerview_row_view_fixtures, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Court court = mData.get(position);
        String matchText = getMatchText(court);
        holder.match.setText(matchText);
    }

    private String getMatchText(Court court) {
        String matchText;
        if (court.getPlayerA() == null) {
            matchText = court.getCourtName() + ": No match in play";
        } else {
            matchText = court.getCourtName() + ": " + court.getPlayerA().getName() + " V " +
                                court.getPlayerB().getName();
        }
        return matchText;
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView match;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            match = itemView.findViewById(R.id.fixture_view_match);
        }
    }
}
