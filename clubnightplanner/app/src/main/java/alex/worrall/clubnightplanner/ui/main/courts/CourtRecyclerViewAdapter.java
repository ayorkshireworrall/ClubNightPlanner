package alex.worrall.clubnightplanner.ui.main.courts;

import android.content.Context;
import android.util.TypedValue;
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
    private ItemClickListener mClickListener;
    private Context mContext;

    public CourtRecyclerViewAdapter(Context context, List<String> viewData) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = viewData;
        this.mContext = context;
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
            holder.displayCourtName.setText(name);
            holder.editCourtName.setText(name);
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView displayCourtName;
        EditText editCourtName;
        final float scale = mContext.getResources().getDisplayMetrics().density;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            displayCourtName = itemView.findViewById(R.id.courtName);
            editCourtName = itemView.findViewById(R.id.courtNameEditable);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mClickListener != null) {
                mClickListener.onItemClick();
            }
            //TODO Make the text editable
//            toggleDisplay(displayCourtName, false);
//            toggleDisplay(editCourtName, true);
        }

        private void toggleDisplay(TextView view, boolean visible) {
            if (visible) {
                makeDisplayVisible(view);
            } else {
                makeDisplayInvisible(view);
            }
        }

        private void makeDisplayInvisible(TextView view) {
            view.setVisibility(View.INVISIBLE);
            view.setPadding(0, 0, 0, 0);
            view.setTextSize(0);
        }

        private void makeDisplayVisible(TextView view) {
            int dp8 = (int) (8 * scale + 0.5f);
            int dp16 = (int) (16 * scale + 0.5f);
            view.setVisibility(View.VISIBLE);
            view.setPadding(dp8, dp16, dp8, dp16);
            view.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
        }
    }

    public interface ItemClickListener {
        public void onItemClick();
    }

    public void setmClickListener(ItemClickListener listener) {
        this.mClickListener = listener;
    }

    public void setmData(List<String> mData) {
        this.mData = mData;
    }
}
