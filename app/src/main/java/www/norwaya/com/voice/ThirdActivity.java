package www.norwaya.com.voice;

import android.Manifest;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresPermission;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import www.norwaya.com.voice.custom_view.CircleDrawable;

import static android.support.v4.content.PermissionChecker.PERMISSION_GRANTED;

public class ThirdActivity extends AppCompatActivity {
    String TAG = "third";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);
        requestPermission();
    }

    private static final int READ_CONTACTS_CODE = 949;

    private void requestPermission() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, READ_CONTACTS_CODE);
            } else {
                initialView();
            }
        } else {
            initialView();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == READ_CONTACTS_CODE && grantResults[0] == PERMISSION_GRANTED) {
            initialView();
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    ContentResolver cr;

    private void initialView() {
        cr = getContentResolver();
        Cursor query = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, "_id asc");
        int index = 0;
        if (query != null) {
            while (query.moveToNext()) {
                Log.i(TAG, "initialView: index-> " + index++);
                int colIndex = query.getColumnIndex("_id");
                int contact_id = query.getInt(colIndex);
                Log.i(TAG, "initialView: " + query.getInt(colIndex));
                getContactInfoByRawContactId(contact_id);
            }
            query.close();
        } else {
            Log.i(TAG, "initialView: query is null");
        }
    }
    private void getContactInfoByRawContactId(int raw_contact_id){
        Cursor query1 = cr.query(Uri.parse("content://com.android.contacts/data"),
                new String[]{ContactsContract.Data.RAW_CONTACT_ID, ContactsContract.Data.MIMETYPE, ContactsContract.Data.DATA1},
                "raw_contact_id = ?", new String[]{String.valueOf(raw_contact_id)}, null);
        if (query1 != null) {
            while (query1.moveToNext()) {
                Log.i(TAG, "initialView: " + query1.getInt(0) + "\t" +
                        query1.getString(1) + "\t" +
                        query1.getString(2));
            }
            query1.close();
        }
    }

}