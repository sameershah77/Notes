package com.example.notes.SQLite

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.notes.model.NotesData

class mySqLite(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME,null, DATABASE_VERSION){

    companion object{
        private const val DATABASE_NAME = "myDataBase"
        private const val DATABASE_VERSION = 1
        private const val NOTES_TABLE = "notesTable"
        private const val KEY_ID = "id"
        private const val KEY_TITLE = "title"
        private const val KEY_CONTENT = "content"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("create table $NOTES_TABLE($KEY_ID INTEGER PRIMARY KEY, $KEY_TITLE TEXT, $KEY_CONTENT TEXT)")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $NOTES_TABLE")
        onCreate(db)
    }

    fun insertNote(note: NotesData) {
        val db = this.writableDatabase

        val contentValue = ContentValues().apply {
            put("$KEY_TITLE",note.title)
            put("$KEY_CONTENT",note.content)
        }
        db.insert(NOTES_TABLE,null,contentValue)
    }

    fun getAllNotes(): List<NotesData> {
        val noteList = mutableListOf<NotesData>()
        val db = this.readableDatabase

        val cursor = db.rawQuery("Select * from $NOTES_TABLE",null)

        while(cursor.moveToNext()) {
            val id = cursor.getInt(0)
            val title = cursor.getString(1)
            val content = cursor.getString(2)
            noteList.add(NotesData(title,content,id))

        }
        cursor.close()
        db.close()
        return noteList
    }

    fun noteById(id: Int): NotesData {
        val noteList = mutableListOf<NotesData>()
        val db = this.readableDatabase

        val cursor = db.rawQuery("Select * from $NOTES_TABLE",null)

        while(cursor.moveToNext()) {
            val currId = cursor.getInt(0)
            val title = cursor.getString(1)
            val content = cursor.getString(2)
            if(currId == id) {
                return NotesData(title,content,id)
            }
        }
        cursor.close()
        db.close()
        
        return NotesData("0","0",-1)
    }

    fun deleteNote(id: Int) {
        val db = this.writableDatabase
        db.delete(NOTES_TABLE,"$KEY_ID = ?", arrayOf(id.toString()))
        db.close()
    }
}