package alex.worrall.clubnightplanner.ui.main.players;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import alex.worrall.clubnightplanner.R;
import alex.worrall.clubnightplanner.model.player.Player;

public class PlayerListAdapter extends RecyclerView.Adapter<PlayerListAdapter.ViewHolder>
        implements Filterable {
    private List<Player> playerList;
    private List<Player> playerListFiltered;
    private LayoutInflater inflater;
    private ItemClickListener itemClickListener;

    public PlayerListAdapter(Context context) {
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewItem = inflater.inflate(R.layout.recyclerview_row_players, parent, false);
        return new ViewHolder(viewItem);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (playerListFiltered != null) {
            Player player = playerListFiltered.get(position);
            holder.name.setText(player.getName());
            Resources res = holder.level.getContext().getResources();
            holder.level.setText(res.getString(R.string.level, player.getLevel()));
            holder.current.setText("Now:\t" + player.getCurrentCourt());
            holder.next.setText("Next:\t" + player.getNextCourt());
        }
    }

    @Override
    public int getItemCount() {
        if (playerListFiltered != null) {
            return playerListFiltered.size();
        }
        return 0;
    }

    public List<Player> getPlayerList() {
        return playerListFiltered;
    }

    public void setPlayerList(List<Player> playerList) {
        this.playerList = playerList;
        this.playerListFiltered = playerList;
        notifyDataSetChanged();
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    playerListFiltered = playerList;
                } else {
                    List<Player> filterList = new ArrayList<>();
                    for (Player player : playerList) {
                        if (player.getName().toLowerCase().contains(charString.toLowerCase())) {
                            filterList.add(player);
                        }
                    }
                    playerListFiltered = filterList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = playerListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                playerListFiltered = (ArrayList<Player>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    interface ItemClickListener {
        void onItemClick (View view, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView name;
        private TextView level;
        private TextView next;
        private TextView current;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.player_name);
            level = itemView.findViewById(R.id.player_level);
            next = itemView.findViewById(R.id.player_next_court);
            current = itemView.findViewById(R.id.player_current_court);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (itemClickListener != null) {
                itemClickListener.onItemClick(v, getAdapterPosition());
            }
        }
    }
}
