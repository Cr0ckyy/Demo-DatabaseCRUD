package com.myapplicationdev.android.c347_l4_ex1demodatabasecrud;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

// TODO: To use the SQLite database in the app,
//  you must first prepare the plumbing.
//  To do so, create a Java class called DBHeper.java that extends SQLiteOpenHelper.
public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "simplenotes.db";
    // To make the app calls onUpgrade(), increment the variable DATABASE_VERSION from 1 to 2.
    private static final int DATABASE_VERSION = 2;
    private static final String TABLE_NOTE = "note";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_NOTE_CONTENT = "note_content";


    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String createNoteTableSql = "CREATE TABLE " + TABLE_NOTE + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_NOTE_CONTENT + " TEXT ) ";
        db.execSQL(createNoteTableSql);
        Log.i("info", "created tables");

        // TODO: to prepare the database for testing,
        //  we could create some dummy data during the table creation process.
        //  Dummy records, to be inserted when the database is created
        for (int i = 0; i < 4; i++) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_NOTE_CONTENT, "Data number " + i);
            db.insert(TABLE_NOTE, null, values);
        }
        Log.i("info", "dummy records inserted");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTE);

        //To add a column called module_name to an existing table, modify onUpgrade() to execute an SQL statement as below.
        db.execSQL("ALTER TABLE " + TABLE_NOTE + " ADD COLUMN module_name TEXT ");

    }

    // TODO:Insert a new record.
    public long insertNote(String noteContent) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NOTE_CONTENT, noteContent);

        // TODO: This line of code will return a number
        //  that represents the record id (the primary key, _id)
        //  of the table for the record that was inserted.
        //  If the insert fails, the id will be -1.
        //  As a result, we can use it to determine whether or not a record was successfully inserted.
        long result = db.insert(TABLE_NOTE, null, values);
        db.close();
        Log.d("SQL Insert", "ID:" + result); //id returned, shouldn’t be -1
        return result;
    }

    // TODO: Record retrieval from database table
    //  This method will retrieve the records and convert each one into a String.
    //  Following that, the Strings are placed in an ArrayList to be returned.
    public ArrayList<Note> getAllNotes() {
        ArrayList<Note> notes = new ArrayList<>();

        String selectQuery = "SELECT " + COLUMN_ID + ","
                + COLUMN_NOTE_CONTENT + " FROM " + TABLE_NOTE;

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String noteContent = cursor.getString(1);
                Note note = new Note(id, noteContent);
                notes.add(note);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return notes;
    }


    /*TODO: In order to perform an update,
       a method called updateNote() must be implemented in DBHelper.java.
        The method will accept a Note object and perform a database update. */
    public int updateNote(Note data) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NOTE_CONTENT, data.getNoteContent());
        String condition = COLUMN_ID + "= ?";
        String[] args = {String.valueOf(data.getId())};


/*TODO: This line of code will return a number representing the number
   of rows in the table that have been affected. We usually expect one
   or more records to be updated when we perform a record update.
   However, in this case, we anticipate only one record.
   As a result, if the affected record is 1,
   we can use it to determine whether or not a record was successfully updated.*/
        int result = db.update(TABLE_NOTE, values, condition, args);

        db.close();
        return result;
    }

    /*TODO:To delete a note, a deleteNote() method in DBHelper.java must be implemented. To delete a record from the database, the method will accept an ID as the primary reference. */
    public int deleteNote(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String condition = COLUMN_ID + "= ?";
        String[] args = {String.valueOf(id)};
        /**/
        int result = db.delete(TABLE_NOTE, condition, args);
        db.close();
        return result;
    }

}

