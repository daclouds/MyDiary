package com.github.daclouds.diary;

import android.app.SearchManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SimpleCursorAdapter;

import com.github.daclouds.diary.contentprovider.EntryContentProvider;

public class MainActivity extends ActionBarListActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Get the intent, verify the action and get the query
	    Intent intent = getIntent();
		if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
			// handles a search query
			String query = intent.getStringExtra(SearchManager.QUERY);
			showResults(query);
		}
	}

	private void showResults(String query) {
		String[] selectionArgs = new String[] { query, query };
		CursorLoader loader = new CursorLoader(this, EntryContentProvider.CONTENT_URI,
				new String[] { "_id", "title", "content", "lastUpdated" },
				"title=? OR content=?", selectionArgs, null);
		Cursor cursor = loader.loadInBackground();
		Log.d("11", "count: " + cursor.getCount());
		
		String[] fromColumns = { "title", "lastUpdated" };
		int[] toViews = { android.R.id.text1, android.R.id.text2 };

		// Create an empty adapter we will use to display the loaded data.
		// We pass null for the cursor, then update it in onLoadFinished()
		onLoadFinished(loader, cursor);
		mAdapter = new SimpleCursorAdapter(this,
				android.R.layout.simple_list_item_2, cursor, fromColumns,
				toViews, 0);

		setListAdapter(mAdapter);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		
		// Get the SearchView and set the searchable configuration
	    SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
	    SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
	    // Assumes current activity is the searchable activity
	    searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
	    searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int itemId = item.getItemId();
		switch (itemId) {
		case R.id.action_settings:
			return true;
		case R.id.action_new:
			Intent intent = new Intent(this, ItemActivity.class);
			startActivity(intent);
			return true;
		case R.id.action_search:
			onSearchRequested();
		default:
			return false;
		}
	}

}