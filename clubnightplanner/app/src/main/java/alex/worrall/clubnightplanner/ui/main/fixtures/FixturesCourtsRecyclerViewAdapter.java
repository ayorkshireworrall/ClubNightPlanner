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
import alex.worrall.clubnightplanner.ui.main.players.Player;

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
        holder.courtName.setText(court.getCourtName());
        if (court.getPlayerA() == null) {
            holder.v.setText("Empty Court");
        } else {
            holder.player1.setText(getPlayerString(court.getPlayerA()));
            holder.player2.setText(getPlayerString(court.getPlayerB()));
        }
    }

    private String getPlayerString(Player player) {
        return player.getName() + "(" + player.getLevel() + ")";
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView courtName;
        private TextView player1;
        private TextView player2;
        private TextView v;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            courtName = itemView.findViewById(R.id.fixture_view_court);
            player1 = itemView.findViewById(R.id.fixture_view_player1);
            player2 = itemView.findViewById(R.id.fixture_view_player2);
            v = itemView.findViewById(R.id.fixture_view_vs);
        }
    }
}
