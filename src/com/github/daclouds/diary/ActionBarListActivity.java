package com.github.daclouds.diary;

import com.github.daclouds.diary.contentprovider.EntryContentProvider;

import android.app.LoaderManager;
import android.app.ActionBar.LayoutParams;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.HeaderViewListAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleCursorAdapter;

public abstract class ActionBarListActivity extends ActionBarActivity implements
		LoaderManager.LoaderCallbacks<Cursor> {

	private ListView mListView;

	// This is the Adapter being used to display the list's data
	SimpleCursorAdapter mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// Create a progress bar to display while the list loads
		ProgressBar progressBar = new ProgressBar(this);
		progressBar.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT, Gravity.CENTER));
		progressBar.setIndeterminate(true);

		getListView().setEmptyView(progressBar);
		mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> l, View v, int position,
					long id) {
				Intent intent = new Intent(getBaseContext(), ItemActivity.class);
			    Uri entryUri = Uri.parse(EntryContentProvider.CONTENT_URI + "/" + id);
			    intent.putExtra(EntryContentProvider.CONTENT_ITEM_TYPE, entryUri);
				startActivity(intent);
			}
			
		});
		
		// Must add the progress bar to the root of the layout
		ViewGroup root = (ViewGroup) findViewById(android.R.id.content);
		root.addView(progressBar);

		// For the cursor adapter, specify which columns go into which views
		String[] fromColumns = { "title", "lastUpdated" };
		int[] toViews = { android.R.id.text1, android.R.id.text2 };

		// Create an empty adapter we will use to display the loaded data.
		// We pass null for the cursor, then update it in onLoadFinished()
		mAdapter = new SimpleCursorAdapter(this,
				android.R.layout.simple_list_item_2, null, fromColumns,
				toViews, 0);

		setListAdapter(mAdapter);

		// Prepare the loader. Either re-connect with an existing one,
		// or start a new one.
		getLoaderManager().initLoader(0, null, this);
	}

	protected ListView getListView() {
		if (mListView == null) {
			mListView = (ListView) findViewById(R.id.list);
		}
		return mListView;
	}

	protected void setListAdapter(ListAdapter adapter) {
		getListView().setAdapter(adapter);
	}

	protected ListAdapter getListAdapter() {
		ListAdapter adapter = getListView().getAdapter();
		if (adapter instanceof HeaderViewListAdapter) {
			return ((HeaderViewListAdapter) adapter).getWrappedAdapter();
		} else {
			return adapter;
		}
	}

	// Called when a new Loader needs to be created
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		// Now create and return a CursorLoader that will take care of
		// creating a Cursor for the data being displayed.
		return new CursorLoader(this, EntryContentProvider.CONTENT_URI,
				new String[] { "_id", "title", "content", "lastUpdated" },
				null, null, null);
	}

	// Called when a previously created loader has finished loading
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		// Swap the new cursor in. (The framework will take care of closing the
		// old cursor once we return.)
		mAdapter.swapCursor(data);
		if (data.getCount() == 0) {
			Intent intent = new Intent(this, ItemActivity.class);
			startActivity(intent);
		}
	}

	// Called when a previously created loader is reset, making the data
	// unavailable
	public void onLoaderReset(Loader<Cursor> loader) {
		// This is called when the last Cursor provided to onLoadFinished()
		// above is about to be closed. We need to make sure we are no
		// longer using it.
		mAdapter.swapCursor(null);
	}
	
	public void onListItemClick(ListView l, View v, int position, long id) {
	    Intent intent = new Intent(this, ItemActivity.class);
	    Uri entryUri = Uri.parse(EntryContentProvider.CONTENT_URI + "/" + id);
	    intent.putExtra(EntryContentProvider.CONTENT_ITEM_TYPE, entryUri);
		startActivity(intent);
	}

}
