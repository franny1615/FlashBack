package com.example.flashback;

import androidx.appcompat.app.AppCompatActivity;
import android.widget.SearchView;
import androidx.recyclerview.widget.RecyclerView;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;

import com.example.flashback.DataSources.FlashcardsDataSource;
import com.example.flashback.RecyclerViewAdapters.RecyclerViewAdapter;

public class SearchAllCardsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_all_cards);
        // Associate searchable configuration with the SearchView
        SearchView searchView = findViewById(R.id.search_all_cards_searchview);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);
        //
        RecyclerView searchRecyclerView = findViewById(R.id.search_all_cards_recyclerview);
        FlashcardsDataSource flashDS = new FlashcardsDataSource(this);
        RecyclerViewAdapter myCardsAdapter = new RecyclerViewAdapter(flashDS.loadAllFlashcardsFromDB(),this);
        searchRecyclerView.setAdapter(myCardsAdapter);
        //
        // TODO submit empty string
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                myCardsAdapter.getFilter().filter(query);
                return false;
            }
            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                myCardsAdapter.getFilter().filter(query);
                return false;
            }
        });
    }
}