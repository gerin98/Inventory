package com.example.gerin.inventory.Search;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.gerin.inventory.R;

import java.util.List;


class SearchViewHolder extends RecyclerView.ViewHolder{

    public TextView searchName;

    public SearchViewHolder(View itemView) {
        super(itemView);

        searchName = (TextView) itemView.findViewById(R.id.search_text);
    }
}

public class SearchAdapter extends RecyclerView.Adapter<SearchViewHolder>{

    private Context context;
    private List<SearchResult> searchResults;

    public SearchAdapter (Context context, List<SearchResult> searchResults){
        this.context = context;
        this.searchResults = searchResults;
    }

    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.search_item, parent, false);

        return new SearchViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {

        holder.searchName.setText(searchResults.get(position).getName());

    }

    @Override
    public int getItemCount() {
        return searchResults.size();
    }
}
