package com.bmc.mgit.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bmc.mgit.R;
import com.bmc.mgit.model.RepositoryListItem;

import java.util.List;

public class RepositoryListItemAdapter extends RecyclerView.Adapter<RepositoryListItemAdapter.ViewHolder> {

    private static final String TAG = RepositoryListItemAdapter.class.getSimpleName();

    private List<RepositoryListItem> repos;
    private int rowLayout;
    private Context mContext;

    public RepositoryListItemAdapter(List<RepositoryListItem> repositories, int rowLayout, Context context) {
        this.repos = repositories;
        this.rowLayout = rowLayout;
        this.mContext = context;
    }

    public RepositoryListItemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RepositoryListItemAdapter.ViewHolder viewHolder, int position) {
        RepositoryListItem repo = repos.get(position);
        viewHolder.name.setText(repo.mName);
        viewHolder.description.setText(repo.mDescription);
        viewHolder.watchers.setText(String.valueOf(repo.mWatchers));
        if (repo.mLanguage != null && !repo.mLanguage.isEmpty()) {
            viewHolder.language.setText(repo.mLanguage);
        } else {
            viewHolder.language.setText(mContext.getString(R.string.unknown));
        }
        viewHolder.forks.setText(String.valueOf(repo.mForks));
    }

    @Override
    public int getItemCount() {
        return repos == null ? 0 : repos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView name;
        public TextView description;
        public TextView language;
        public TextView watchers;
        public TextView forks;

        public ViewHolder(View itemView) {
            super(itemView);
            // Set all decor of card as clickable
            name = (TextView) itemView.findViewById(R.id.name);
            name.setOnClickListener(this);
            description = (TextView) itemView.findViewById(R.id.description);
            description.setOnClickListener(this);
            language = (TextView) itemView.findViewById(R.id.language);
            language.setOnClickListener(this);
            watchers = (TextView) itemView.findViewById(R.id.watchers);
            watchers.setOnClickListener(this);
            forks = (TextView) itemView.findViewById(R.id.forks);
            forks.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            Toast.makeText(mContext, name.getText().toString(), Toast.LENGTH_LONG).show();
        }
    }
}
