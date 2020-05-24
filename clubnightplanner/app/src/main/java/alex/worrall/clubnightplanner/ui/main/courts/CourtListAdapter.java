package alex.worrall.clubnightplanner.ui.main.courts;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import alex.worrall.clubnightplanner.R;
import alex.worrall.clubnightplanner.model.court.CourtName;

public class CourtListAdapter extends RecyclerView.Adapter<CourtListAdapter.ViewHolder> {
    private final LayoutInflater mInflater;
    private List<CourtName> mCourtNames;

    public CourtListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.recyclerview_row_courts, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (mCourtNames != null) {
            CourtName courtName = mCourtNames.get(position);
            holder.courtName.setText(courtName.getName());
        }
    }

    @Override
    public int getItemCount() {
        if (mCourtNames != null) {
            return mCourtNames.size();
        }
        return 0;
    }

    public List<CourtName> getmCourtNames() {
        return mCourtNames;
    }

    public void setmCourtNames(List<CourtName> mCourtNames) {
        this.mCourtNames = mCourtNames;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView courtName;

        private ViewHolder(@NonNull View itemView) {
            super(itemView);
            courtName = itemView.findViewById(R.id.courtName);
        }
    }
}
