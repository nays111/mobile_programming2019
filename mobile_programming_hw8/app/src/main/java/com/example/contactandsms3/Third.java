package com.example.contactandsms3;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class Third extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);
        int REQUEST_PHONE_CALL = 1;

        if (ContextCompat.checkSelfPermission(Third.this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Third.this, new String[]{Manifest.permission.READ_SMS}, REQUEST_PHONE_CALL);
        }


        // Create Inbox box URI
        Uri inboxURI = Uri.parse("content://sms/inbox");

// List required columns
        String[] reqCols = new String[]{"_id", "address", "body"};

// Get Content Resolver object, which will deal with Content Provider
        ContentResolver cr = getContentResolver();

// Fetch Inbox SMS Message from Built-in Content Provider
        Cursor c = cr.query(inboxURI, reqCols, null, null, null);

        int[] contactsListItems = {
                R.id.phone_name,
                R.id.phone_number
        };

// Attached Cursor with adapter and display in listview
       SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
               R.layout.list,
               c,
               new String[]{"address", "body"},
               contactsListItems,
               0);

       ListView lv = (ListView)findViewById(R.id.list2);



        adapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
                if(columnIndex == 1){
                    String number = cursor.getString(1);
                    String name = name(number);
                    TextView text = (TextView) view;
                    text.setText(name);
                    return true;
                }
                return false;
            }
        });
        lv.setAdapter(adapter);
    }
    private String name(String number){
        Uri uri = Uri.withAppendedPath(
                ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
                Uri.encode(number));
        String name = " ";

        ContentResolver contentResolver = getContentResolver();
        Cursor contactLookup = contentResolver.query(uri, new String[] {
                        BaseColumns._ID, ContactsContract.PhoneLookup.DISPLAY_NAME },
                null, null, null);

        try {
            if (contactLookup != null && contactLookup.getCount() > 0) {
                contactLookup.moveToNext();
                name = contactLookup.getString(contactLookup
                        .getColumnIndex(ContactsContract.Data.DISPLAY_NAME));
            }
        } finally {
            if (contactLookup != null) {
                contactLookup.close();
            }
        }
        if(name == " ")
            return number;
        return name;
    }

}
