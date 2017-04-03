package org.mistu.android.exam.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static org.mistu.android.exam.db.ExamDbContract.*;

/**
 * Created by kedee on 24/3/17.
 */

public class ExamsDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Exams.db";
    private static final int DATABASE_VERSION = 2;

    public ExamsDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // SQLite query to create table
        final String SQL_CREATE_EXAMSTAKEN_TABLE = "CREATE TABLE " + ExamsTaken.TABLE_NAME + " (" +
                ExamsTaken._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                ExamsTaken.COLUMN_NAME_TITLE + " TEXT NOT NULL, " +
                ExamsTaken.COLUMN_NAME_ANSWER_RESPONSE_MAP + " TEXT NOT NULL, " +
                ExamsTaken.COLUMN_NAME_TOTAL_QUESTION_COUNT + " INTEGER NOT NULL, " +
                ExamsTaken.COLUMN_NAME_ATTEMPED_QUESTION_COUNT + " INTEGER NOT NULL, " +
                ExamsTaken.COLUMN_NAME_CORRECT_QUESTION_COUNT + " INTEGER NOT NULL, " +
                ExamsTaken.COLUMN_NAME_TIME_TAKEN + " INTEGER NOT NULL, " +
                ExamsTaken.COLUMN_NAME_TIMESTAMP + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                "); ";
        db.execSQL(SQL_CREATE_EXAMSTAKEN_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + ExamsTaken.TABLE_NAME);
        this.onCreate(db);
    }
}
