package org.mistu.android.exam.db;

import android.provider.BaseColumns;

/**
 * Created by kedee on 24/3/17.
 */

public final class ExamDbContract {

    /*
     * To prevent some accidentally create instance of
     * this class.
     */
    private ExamDbContract() {
    }

    /*
     * Inner class for Exam table
     */
    public static class ExamsTaken implements BaseColumns {
        public static final String TABLE_NAME = "ExamsTaken";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_TIMESTAMP = "timeStamp";
        public static final String COLUMN_NAME_TOTAL_QUESTION_COUNT = "questionCount";
        public static final String COLUMN_NAME_ATTEMPED_QUESTION_COUNT = "attemptedCount";
        public static final String COLUMN_NAME_CORRECT_QUESTION_COUNT = "correctCount";
        public static final String COLUMN_NAME_TIME_TAKEN = "timeTaken";
        public static final String COLUMN_NAME_ANSWER_RESPONSE_MAP = "answerResponseMap";
    }
}
