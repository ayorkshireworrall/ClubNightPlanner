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
import alex.worrall.clubnightplanner.model.court.CourtName;

public class CourtPickerListAdapter extends RecyclerView.Adapter<CourtPickerListAdapter.ViewHolder> {
    private List<String> courts;
    private LayoutInflater inflater;
    private boolean isCheckedAll;
    private Set<String> checkedCourts = new HashSet<>();

    public CourtPickerListAdapter(Context context) {
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.recyclerview_row_fixture_courtpicker, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final String courtName = courts.get(position);
        holder.courtName.setText(courtName);
        holder.isSelected.setChecked(isCheckedAll);
        if (holder.isSelected.isChecked()) {
            checkedCourts.add(courtName);
        } else {
            checkedCourts.remove(courtName);
        }
        holder.isSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.isSelected.isChecked()) {
                    checkedCourts.add(courtName);
                } else {
                    checkedCourts.remove(courtName);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (courts != null) {
            return courts.size();
        }
        return 0;
    }

    public List<String> getCourts() {
        return courts;
    }

    public void setCourts(List<String> courts) {
        this.courts = courts;
        notifyDataSetChanged();
    }

    public void setCheckedAll(boolean checkedAll) {
        isCheckedAll = checkedAll;
        if (checkedAll) {
            this.checkedCourts.addAll(courts);
        } else {
            this.checkedCourts = new HashSet<>();
        }
        notifyDataSetChanged();
    }

    public Set<String> getCheckedCourts() {
        return checkedCourts;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView courtName;
        CheckBox isSelected;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            courtName = itemView.findViewById(R.id.select_court_name);
            isSelected = itemView.findViewById(R.id.select_court_checkbox);
        }
    }
}
