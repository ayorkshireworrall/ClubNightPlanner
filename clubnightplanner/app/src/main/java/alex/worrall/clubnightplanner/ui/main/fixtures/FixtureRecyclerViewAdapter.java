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
import alex.worrall.clubnightplanner.persistence.models.fixture.Fixture;
import alex.worrall.clubnightplanner.service.Status;

public class FixtureRecyclerViewAdapter extends RecyclerView.Adapter<FixtureRecyclerViewAdapter.ViewHolder> {
    private List<Fixture> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mItemClickListener;

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
        if (fixture.getPlayStatus().equals(Status.COMPLETED)) {
            holder.timeSlot.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        }
        holder.status.setText(fixture.getPlayStatus().getMessage());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView timeSlot;
        private TextView status;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            timeSlot = itemView.findViewById(R.id.fixtureTime);
            status = itemView.findViewById(R.id.fixtureStatus);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(view, getAdapterPosition());
            }
        }
    }

    public interface ItemClickListener {
        public void onItemClick(View view, int pos);
    }

    public void setItemClickListener(ItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public void setmData(List<Fixture> data) {
        this.mData = data;
    }
}
