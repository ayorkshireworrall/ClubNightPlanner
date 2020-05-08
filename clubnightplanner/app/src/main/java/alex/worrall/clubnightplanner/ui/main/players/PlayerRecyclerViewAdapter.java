package alex.worrall.clubnightplanner.ui.main.players;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import alex.worrall.clubnightplanner.R;
import alex.worrall.clubnightplanner.persistence.models.player.Player;

public class PlayerRecyclerViewAdapter extends RecyclerView.Adapter<PlayerRecyclerViewAdapter.ViewHolder> {
    private List<Player> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    PlayerRecyclerViewAdapter(Context context, List<Player> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recyclerview_row_player, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Player player = mData.get(position);
        Resources res = holder.levelTextView.getContext().getResources();
        holder.nameTextView.setText(player.getName());
        holder.levelTextView.setText(res.getString(R.string.level, player.getLevel()));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView nameTextView;
        TextView levelTextView;

        ViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.playerName);
            levelTextView = itemView.findViewById(R.id.playerLevel);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) {
                mClickListener.onItemClick(view, getAdapterPosition());
            }
        }
    }

    public interface ItemClickListener {
        void onItemClick (View view, int position);
    }

    public void setClickListener (ItemClickListener listener) {
        this.mClickListener = listener;
    }

    public Player getItem(int position) {
        return mData.get(position);
    }
}
