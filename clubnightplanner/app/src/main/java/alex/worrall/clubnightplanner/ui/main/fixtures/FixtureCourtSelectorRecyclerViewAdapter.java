package alex.worrall.clubnightplanner.ui.main.fixtures;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import alex.worrall.clubnightplanner.R;

public class FixtureCourtSelectorRecyclerViewAdapter extends
        RecyclerView.Adapter<FixtureCourtSelectorRecyclerViewAdapter.ViewHolder>{
    private List<String> mData;
    private LayoutInflater mInflater;
    private boolean isCheckedAll;

    public FixtureCourtSelectorRecyclerViewAdapter(Context context, List<String> mData) {
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
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.courtName.setText(mData.get(position));
        holder.isSelected.setChecked(this.isCheckedAll);
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
        System.out.println("Select All Clicked");
        this.isCheckedAll = checkedAll;
        notifyDataSetChanged();
    }
}
