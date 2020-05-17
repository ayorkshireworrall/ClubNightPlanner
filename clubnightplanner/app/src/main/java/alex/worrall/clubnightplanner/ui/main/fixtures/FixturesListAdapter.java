package alex.worrall.clubnightplanner.ui.main.fixtures;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import alex.worrall.clubnightplanner.R;
import alex.worrall.clubnightplanner.model.fixture.Fixture;
import alex.worrall.clubnightplanner.utils.Status;

public class FixturesListAdapter extends RecyclerView.Adapter<FixturesListAdapter.ViewHolder> {
    private List<Fixture> fixtures;
    private LayoutInflater inflater;
    private ItemClickListener itemClickListener;

    public FixturesListAdapter(Context context) {
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewItem = inflater.inflate(R.layout.recyclerview_row_fixtures, parent, false);
        return new ViewHolder(viewItem);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (fixtures != null) {
            Fixture fixture = fixtures.get(position);
            holder.time.setText(fixture.toString());
            if (fixture.getPlayStatus().equals(Status.COMPLETED)) {
                holder.time.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            }
            holder.status.setText(fixture.getPlayStatus().getMessage());
        }
    }

    @Override
    public int getItemCount() {
        if (fixtures != null) {
            return fixtures.size();
        }
        return 0;
    }

    public List<Fixture> getFixtures() {
        return fixtures;
    }

    public void setFixtures(List<Fixture> fixtures) {
        this.fixtures = fixtures;
        notifyDataSetChanged();
    }

    public ItemClickListener getItemClickListener() {
        return itemClickListener;
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    interface ItemClickListener {
        void onItemClicked(View view, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView time;
        TextView status;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            time = itemView.findViewById(R.id.fixtureTime);
            status = itemView.findViewById(R.id.fixtureStatus);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (itemClickListener != null) {
                itemClickListener.onItemClicked(v, getAdapterPosition());
            }
        }
    }
}
