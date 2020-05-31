package com.antonageev.popularlibs;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.antonageev.popularlibs.models.GitHubUsers;

import java.util.List;

public class UsersListAdapter extends RecyclerView.Adapter<UsersListAdapter.ViewHolder> {

    private List<GitHubUsers> source;

    private OnItemClickListener onItemClickListener;

    public UsersListAdapter(List<GitHubUsers> source) {
        this.source = source;
    }

    public void setSource (List<GitHubUsers> source) {
        this.source = source;
    }

    public List<GitHubUsers> getSource() {
        return source;
    }

    @NonNull
    @Override
    public UsersListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_layout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull UsersListAdapter.ViewHolder holder, int position) {
        GitHubUsers user = source.get(position);
        holder.getLogin().setText(user.getLogin());
        holder.getUrl().setText(user.getUrl());
        //clickListeners?
    }

    @Override
    public int getItemCount() {
        return source == null ? 0 : source.size();
    }

    public interface OnItemClickListener{
        void onItemClick(View v, int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        View cardView;
        TextView login;
        TextView url;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView;
            login = cardView.findViewById(R.id.login);
            url = cardView.findViewById(R.id.url);
            cardView.setOnClickListener(v -> {
                if (onItemClickListener != null) onItemClickListener.onItemClick(v, getAdapterPosition());
            });
        }

        public TextView getLogin() {
            return login;
        }

        public TextView getUrl() {
            return url;
        }
    }

}
