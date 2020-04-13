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

public class FixtureRecyclerViewAdapter extends RecyclerView.Adapter<FixtureRecyclerViewAdapter.ViewHolder> {
    private List<Fixture> mData;
    private LayoutInflater mInflater;

    public FixtureRecyclerViewAdapter(Context context, List<Fixture> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recyclerview_row_fixture, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Fixture fixture = mData.get(position);
        holder.timeSlot.setText(String.valueOf(fixture.toString()));
        holder.status.setText(fixture.getPlayStatus().getMessage());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView timeSlot;
        private TextView status;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            timeSlot = itemView.findViewById(R.id.fixtureTime);
            status = itemView.findViewById(R.id.fixtureStatus);
        }
    }
}
