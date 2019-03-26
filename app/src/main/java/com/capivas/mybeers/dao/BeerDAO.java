package com.capivas.mybeers.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.capivas.mybeers.model.Beer;

import java.util.ArrayList;
import java.util.List;

public class BeerDAO extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "MyBeers";
    private static final String TABLE_NAME = "Beers";
    private static final int DATABASE_VERSION = 1;

    public BeerDAO(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + TABLE_NAME +" (" +
                "id INTEGER PRIMARY KEY," +
                "name TEXT NOT NULL," +
                "tagline TEXT," +
                "description TEXT," +
                "imageLocation TEXT);";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void save(Beer aluno) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues dados = getBeerValues(aluno);
        db.insert(TABLE_NAME, null, dados);
    }

    public void delete(Beer beer) {
        SQLiteDatabase db = getWritableDatabase();
        String[] params = {String.valueOf(beer.getId())};
        db.delete(TABLE_NAME, "id=?", params);
    }

    public boolean exists(Beer beer) {
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE id=? LIMIT 1";
        Cursor cursor = db.rawQuery(sql, new String[]{beer.getId().toString()});
        int quantidade = cursor.getCount();
        return quantidade > 0;
    }

    public List<Beer> getPage(int page, int max_per_page) {
        Cursor c = getAll();
        int startPosition = 1 + ((page - 1) * max_per_page);
        int endPosition = startPosition + (max_per_page - 1);
        int currentPosition = 1;
        List<Beer> beers = new ArrayList<>();
        while (c.moveToNext()) {
            if(currentPosition >= startPosition && currentPosition <= endPosition) {
                Beer beer = new Beer();
                beer.setId(c.getLong(c.getColumnIndex("id")));
                beer.setName(c.getString(c.getColumnIndex("name")));
                beer.setTagline(c.getString(c.getColumnIndex("tagline")));
                beer.setDescription(c.getString(c.getColumnIndex("description")));
                beer.setImageLocation(c.getString(c.getColumnIndex("imageLocation")));
                beer.setIsFavorite(true);
                beers.add(beer);
            }
            currentPosition++;
        }
        return beers;
    }

    private Cursor getAll() {
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_NAME;
        return db.rawQuery(sql, null);
    }

    private ContentValues getBeerValues(Beer beer) {
        ContentValues dados = new ContentValues();
        dados.put("id", beer.getId());
        dados.put("name", beer.getName());
        dados.put("tagline", beer.getTagline());
        dados.put("description", beer.getDescription());
        dados.put("imagelocation", beer.getImageLocation());
        return dados;
    }
}
