package org.mistu.android.exam.db;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

/**
 * Created by kedee on 25/3/17.
 */

public final class DbUtils {

    public static void insertExamRow(SQLiteDatabase db, ContentValues examTakenContent) {
        if (db == null) {
            return;
        }

        try {
            db.beginTransaction();
            db.insert(ExamDbContract.ExamsTaken.TABLE_NAME, null, examTakenContent);
            db.setTransactionSuccessful();
            Log.i("INSERT SUCCESSFUL ", examTakenContent.toString() );
        } catch (SQLiteException ex) {
            // do stuffs
        } finally {
            db.endTransaction();
        }
    }
    
}
