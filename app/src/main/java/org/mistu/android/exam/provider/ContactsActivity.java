package org.mistu.android.exam.provider;

import android.content.ContentResolver;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.mistu.android.exam.R;

import java.net.URL;

public class ContactsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
    }

    /*private class GetContactTask extends AsyncTask<Void, Void, Cursor> {

        protected Cursor doInBackground(Void... params) {
            // Get the content resolver
            ContentResolver resolver = getContentResolver();

            // Call the query method on the resolver with the correct Uri from the contract class
            Cursor cursor = resolver.query(DroidTermsExampleContract.CONTENT_URI,
                    null, null, null, null);
            return cursor;
        }

        protected void onProgressUpdate() {
        }

        protected void onPostExecute(Cursor cursor) {

        }
    }
*/


}
