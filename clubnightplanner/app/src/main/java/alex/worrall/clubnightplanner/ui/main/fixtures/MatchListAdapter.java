package alex.worrall.clubnightplanner.ui.main.fixtures;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import alex.worrall.clubnightplanner.R;
import alex.worrall.clubnightplanner.model.fixture.Court;

public class MatchListAdapter extends RecyclerView.Adapter<MatchListAdapter.ViewHolder> {
    LayoutInflater inflater;
    List<Court> courts;

    public MatchListAdapter(Context context, List<Court> courts) {
        inflater = LayoutInflater.from(context);
        this.courts = courts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.recyclerview_row_match, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Court court = courts.get(position);
        if (court.getPlayerA() == null) {
            holder.v.setText("Empty Court");
        } else {
            holder.player1.setText(court.getPlayerA().getName());
            holder.player2.setText(court.getPlayerB().getName());
            holder.court.setText(court.getCourtName());
        }
    }

    @Override
    public int getItemCount() {
        if (courts != null) {
            return courts.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView court;
        TextView player1;
        TextView player2;
        TextView v;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            v = itemView.findViewById(R.id.fixture_view_vs);
            player1 = itemView.findViewById(R.id.fixture_view_player1);
            player2 = itemView.findViewById(R.id.fixture_view_player2);
            court = itemView.findViewById(R.id.fixture_view_court);
        }
    }
}
