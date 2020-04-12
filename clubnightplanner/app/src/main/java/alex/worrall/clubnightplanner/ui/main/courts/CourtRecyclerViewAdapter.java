package alex.worrall.clubnightplanner.ui.main.courts;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import alex.worrall.clubnightplanner.R;

public class CourtRecyclerViewAdapter extends RecyclerView.Adapter<CourtRecyclerViewAdapter.ViewHolder> {
    private List<String> mData;
    private LayoutInflater mInflater;

    public CourtRecyclerViewAdapter(Context context, List<String> viewData) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = viewData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recyclerview_row_court, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String name = mData.get(position);
        if (name != null) {
            holder.courtName.setText(mData.get(position));
        } else {
            holder.courtName.setText("Court " + position);
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        EditText courtName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            courtName = itemView.findViewById(R.id.courtName);
        }
    }
}
