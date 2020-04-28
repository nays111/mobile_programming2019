package com.example.todolist;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    private final String dbName = "todolist";
    private final String tableName = "todo";

    ArrayList<HashMap<String, String>> toDoList;
    ListView listView;
    TextView addName, addPriority;
    Button btDelete;

    private static final String TAG_TITLE = "title";
    private static final String TAG_PRIORITY = "priority";

    SQLiteDatabase DB = null;
    ListAdapter adapter;
    HashMap<String, String> toDo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadActivity();
    }

    private void loadActivity() {
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        listView = findViewById(R.id.lv);
        addName = findViewById(R.id.addName);
        addPriority = findViewById(R.id.addPriority);
        toDoList = new ArrayList<>();

        try {
            DB = this.openOrCreateDatabase(dbName, MODE_PRIVATE, null);
s
            DB.execSQL("CREATE TABLE IF NOT EXISTS " + tableName + " (title VARCHAR(50), priority VARCHAR(40) , '_id' VARCHAR(50));");
        } catch (SQLiteException se) {
            Toast.makeText(getApplicationContext(), se.getMessage(), Toast.LENGTH_LONG).show();
        }

        showList();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showInsertAlertDialog();
            }
        });
    }

    protected void showList() {
        try {
            SQLiteDatabase ReadDB = this.openOrCreateDatabase(dbName, MODE_PRIVATE, null);
            @SuppressLint("Recycle") Cursor c = ReadDB.rawQuery("SELECT * FROM " + tableName, null);

            if (c != null) {
                if (c.moveToFirst()) {
                    do {
                        String title = c.getString(c.getColumnIndex("title"));
                        String priority = c.getString(c.getColumnIndex("priority"));

                        toDo = new HashMap<>();

                        toDo.put(TAG_TITLE, title);
                        toDo.put(TAG_PRIORITY, priority);

                        toDoList.add(toDo);
                    } while (c.moveToNext());
                }
            }
            ReadDB.close();

            adapter = new SimpleCursorAdapter(
                    this, R.layout.list_item, c,
                    new String[]{TAG_TITLE, TAG_PRIORITY},
                    new int[]{R.id.title, R.id.priority}
            ) {
                @Override
                public View getView(final int position, View convertView, ViewGroup parent) {
                    if (convertView == null) {
                        LayoutInflater inflater = getLayoutInflater();
                        convertView = inflater.inflate(R.layout.list_item, parent, false);
                    }

                    final TextView tvTitle = convertView.findViewById(R.id.title);
                    tvTitle.setText(toDoList.get(position).get(TAG_TITLE));

                    final TextView tvPriority = convertView.findViewById(R.id.priority);
                    tvPriority.setText(toDoList.get(position).get(TAG_PRIORITY));

                    btDelete = convertView.findViewById(R.id.button);
                    btDelete.setOnClickListener(new Button.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showDeleteAlertDialog(position);
                        }
                    });
                    return convertView;
                }
            };

            listView.setAdapter(adapter);
        } catch (SQLiteException se) {
            Toast.makeText(getApplicationContext(), se.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    protected void showInsertAlertDialog() {
        AlertDialog.Builder ad = new AlertDialog.Builder(MainActivity.this);
        DB = this.openOrCreateDatabase(dbName, MODE_PRIVATE, null);

        LayoutInflater inflater = getLayoutInflater();
        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.test, null);
        ad.setView(view);
        ad.setTitle("Add new ToDo");

        final Button btInsert = view.findViewById(R.id.button4);
        final Button btCancel = view.findViewById(R.id.button5);
        final EditText etName = view.findViewById(R.id.addName);
        final EditText etPriority = view.findViewById(R.id.addPriority);
        final AlertDialog dialog = ad.create();

        btInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DB.execSQL("INSERT INTO " + tableName + "(title, priority) Values ('"
                        + etName.getText() + "', '" + etPriority.getText() + "');");
                loadActivity();
                dialog.dismiss();
            }
        });
        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    protected void showDeleteAlertDialog(final int delete_pos) {
        AlertDialog.Builder ad = new AlertDialog.Builder(MainActivity.this);
        DB = this.openOrCreateDatabase(dbName, MODE_PRIVATE, null);

        ad.setTitle("Delete To Do");
        ad.setMessage("Do you want to DELETE this To Do List?");
        ad.setCancelable(false);
        ad.setPositiveButton("Delete",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        DB.execSQL("DELETE FROM " + tableName + " WHERE title = '"
                                + toDoList.get(delete_pos).get(TAG_TITLE) + "' AND priority = '"
                                + toDoList.get(delete_pos).get(TAG_PRIORITY) + "';");
                        loadActivity();
                    }
                })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        AlertDialog alertDialog = ad.create();
        alertDialog.show();
    }

    @Override
    public void onDestroy() {
        DB.close();
        super.onDestroy();
    }
}
