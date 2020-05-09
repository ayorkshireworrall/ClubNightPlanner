package alex.worrall.clubnightplanner.ui.main.fixtures;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import alex.worrall.clubnightplanner.R;
import alex.worrall.clubnightplanner.persistence.models.courtname.CourtName;

public class FixtureCourtSelectorRecyclerViewAdapter extends
        RecyclerView.Adapter<FixtureCourtSelectorRecyclerViewAdapter.ViewHolder>{
    private List<CourtName> mData;
    private LayoutInflater mInflater;
    private boolean isCheckedAll;
    private Set<CourtName> selectedCourts = new HashSet<>();

    public FixtureCourtSelectorRecyclerViewAdapter(Context context, List<CourtName> mData) {
        this.mData = mData;
        this.mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recyclerview_row_select_courts, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final CourtName courtName = mData.get(position);
        holder.courtName.setText(courtName.getName());
        holder.isSelected.setChecked(this.isCheckedAll);
        if (holder.isSelected.isChecked()) {
            selectedCourts.add(courtName);
        } else {
            selectedCourts.remove(courtName);
        }
        holder.isSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.isSelected.isChecked()) {
                    selectedCourts.add(courtName);
                } else {
                    selectedCourts.remove(courtName);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView courtName;
        private CheckBox isSelected;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            courtName = itemView.findViewById(R.id.select_court_name);
            isSelected = itemView.findViewById(R.id.select_court_checkbox);
        }
    }

    public void setCheckedAll(boolean checkedAll) {
        this.isCheckedAll = checkedAll;
        if (checkedAll) {
            this.selectedCourts.addAll(mData);
        } else {
            this.selectedCourts = new HashSet<>();
        }
        notifyDataSetChanged();
    }

    public Set<CourtName> getSelectedCourts() {
        return this.selectedCourts;
    }
}
