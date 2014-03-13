package com.github.daclouds.diary.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

import com.github.daclouds.diary.R;

public class DiaryDbHelper extends SQLiteOpenHelper {

	Context context;
	
	public DiaryDbHelper(Context context) {
		this(context, "MyDiary.db", null, 1);
	}
	
	public DiaryDbHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		this.context = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(context.getString(R.string.create_entry_schema));
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(context.getString(R.string.drop_entry_schema));
		onCreate(db);
	}

}
