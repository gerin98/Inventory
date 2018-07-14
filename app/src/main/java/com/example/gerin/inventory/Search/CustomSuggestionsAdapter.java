package com.example.gerin.inventory.Search;


import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.gerin.inventory.R;
import com.mancj.materialsearchbar.adapter.SuggestionsAdapter;

class SuggestionHolder extends RecyclerView.ViewHolder{

    public TextView name;

    public SuggestionHolder(View itemView) {
        super(itemView);

        name = (TextView) itemView.findViewById(R.id.search_text);
    }
}

public class CustomSuggestionsAdapter extends SuggestionsAdapter<SearchResult, SuggestionHolder>{

    public CustomSuggestionsAdapter(LayoutInflater inflater) {
        super(inflater);
    }

    @Override
    public void onBindSuggestionHolder(SearchResult suggestion, SuggestionHolder holder, int position) {
        holder.name.setText(suggestion.getName());
    }

    @Override
    public int getSingleViewHeight() {
        return 60;
    }

    @NonNull
    @Override
    public SuggestionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = getLayoutInflater().inflate(R.layout.search_item, parent, false);
        return new SuggestionHolder(view);
    }
}
