package com.example.mackapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class CRUD {
    SQLiteOpenHelper dbHandeler;
    SQLiteDatabase db;

    private static final String[] columns = {
        NoteDatabase.ID,
        NoteDatabase.CONTENT,
        NoteDatabase.TIME,
        NoteDatabase.MODE
    };

    public CRUD(Context context){
        dbHandeler = new NoteDatabase(context);
    }

    public void open(){
        db = dbHandeler.getWritableDatabase();
    }

    public void close(){
        dbHandeler.close();
    }

    public Note addNote(Note note){
        ContentValues contentValues = new ContentValues();
        contentValues.put(NoteDatabase.CONTENT,note.getContent());
        contentValues.put(NoteDatabase.TIME,note.getTime());
        contentValues.put(NoteDatabase.MODE,note.getTag());
        long insertId = db.insert(NoteDatabase.TABLE_NAME,null,contentValues);
        note.setId(insertId);
        return note;
    }

    public Note getNote(long id){
        Cursor cursor = db.query(NoteDatabase.TABLE_NAME,columns,NoteDatabase.ID + "=?",
                new String[]{String.valueOf(id)},null,null,null,null);
        if (cursor != null){
            cursor.moveToFirst();
        }
        Note n = new Note(cursor.getString(1),cursor.getString(2),cursor.getInt(3));
        return n;
    }

    public List<Note> getAllNote(){
        Cursor cursor = db.query(NoteDatabase.TABLE_NAME,columns,null,null,null,null,null);
        List<Note> notes = new ArrayList<>();
        if (cursor.getCount()>0){
            while (cursor.moveToNext()){
                Note note = new Note();
                note.setId(cursor.getLong(cursor.getColumnIndex(NoteDatabase.ID)));
                note.setContent(cursor.getString(cursor.getColumnIndex(NoteDatabase.CONTENT)));
                note.setTime(cursor.getString(cursor.getColumnIndex(NoteDatabase.TIME)));
                note.setTag(cursor.getInt(cursor.getColumnIndex(NoteDatabase.MODE)));
                notes.add(note);
            }
        }
        return notes;
    }

    public int updataNote(Note note){
        ContentValues contentValues = new ContentValues();
        contentValues.put(NoteDatabase.CONTENT,note.getContent());
        contentValues.put(NoteDatabase.TIME,note.getTime());
        contentValues.put(NoteDatabase.MODE,note.getTag());
        return db.update(NoteDatabase.TABLE_NAME,contentValues,NoteDatabase.ID + "=?",new String[]{String.valueOf(note.getId())});
    }

    public void removeNote(Note note){
        db.delete(NoteDatabase.TABLE_NAME,NoteDatabase.ID + "=" + note.getId(),null);
    }
}
