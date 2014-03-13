package com.github.daclouds.diary;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.github.daclouds.diary.contentprovider.EntryContentProvider;

public class ItemActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_item);

		ActionBar actionBar = getSupportActionBar();
	    actionBar.setDisplayHomeAsUpEnabled(true);
	    
		Bundle extras = getIntent().getExtras();
		// Uri entryUri = (savedInstanceState == null) ? null
		// : (Uri) savedInstanceState
		// .getParcelable(EntryContentProvider.CONTENT_ITEM_TYPE);

		if (extras != null) {
			Uri entryUri = extras
					.getParcelable(EntryContentProvider.CONTENT_ITEM_TYPE);
			fillData(entryUri);
		} else {
			TextView lastUpdated = (TextView) findViewById(R.id.lastUpdated);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm",
					Locale.KOREAN);
			lastUpdated.setText(sdf.format(new Date()));
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.item, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int itemId = item.getItemId();
		Bundle extras = getIntent().getExtras();
		switch (itemId) {
		case R.id.action_settings:
			break;
		case R.id.action_save:
			ContentValues values = new ContentValues();
			String title = ((EditText) findViewById(R.id.title)).getText()
					.toString();
			String content = ((EditText) findViewById(R.id.content)).getText()
					.toString();
			values.put("title", title);
			values.put("content", content);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm",
					Locale.KOREAN);
			values.put("lastUpdated", sdf.format(new Date()));
			if (extras == null) {
				getContentResolver().insert(EntryContentProvider.CONTENT_URI,
						values);
			} else {
				Uri entryUri = extras
						.getParcelable(EntryContentProvider.CONTENT_ITEM_TYPE);
				getContentResolver().update(entryUri, values, null, null);
			}
			setResult(RESULT_OK);
			finish();
			break;
		case R.id.action_remove:
			if (extras != null) {
				Uri entryUri = extras
						.getParcelable(EntryContentProvider.CONTENT_ITEM_TYPE);
				getContentResolver().delete(entryUri, null, null);
				setResult(RESULT_CANCELED);
			} else {
				setResult(RESULT_OK);
			}
			finish();
			break;
		}

		return super.onOptionsItemSelected(item);
	}

	private void fillData(Uri uri) {
		String[] projection = { "_id", "title", "content", "lastUpdated" };
		Cursor cursor = getContentResolver().query(uri, projection, null, null,
				null);
		if (cursor != null) {
			cursor.moveToFirst();
			EditText title = (EditText) findViewById(R.id.title);
			EditText content = (EditText) findViewById(R.id.content);
			TextView lastUpdated = (TextView) findViewById(R.id.lastUpdated);
			title.setText(cursor.getString(cursor
					.getColumnIndexOrThrow("title")));
			content.setText(cursor.getString(cursor
					.getColumnIndexOrThrow("content")));
			lastUpdated.setText(cursor.getString(cursor
					.getColumnIndexOrThrow("lastUpdated")));
			cursor.close();
		}
	}

}
