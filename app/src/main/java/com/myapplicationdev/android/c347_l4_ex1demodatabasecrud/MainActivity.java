package com.myapplicationdev.android.c347_l4_ex1demodatabasecrud;


import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    Button btnAdd, btnEdit, btnRetrieve;
    TextView tvDBContent;
    EditText etContent;
    ArrayList<Note> al;
    ArrayAdapter<Note> aa;
    ListView lv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initialize the variables with UI here
        btnAdd = findViewById(R.id.buttonAdd);
        btnEdit = findViewById(R.id.buttonEdit);
        btnRetrieve = findViewById(R.id.buttonRetrieve);
        etContent = findViewById(R.id.editText);
        tvDBContent = findViewById(R.id.tvDBContent);
        lv = findViewById(R.id.lv);

        al = new ArrayList<>();
        aa = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, al);
        lv.setAdapter(aa);

        lv.setOnItemClickListener((parent, view, position, identity) -> {
            Note data = al.get(position);
            Intent i = new Intent(MainActivity.this,
                    EditActivity.class);
            i.putExtra("data", data);
            startActivityForResult(i, 9);
        });

        btnAdd.setOnClickListener(v -> {
            String data = etContent.getText().toString();
            DBHelper dbh = new DBHelper(MainActivity.this);
            long inserted_id = dbh.insertNote(data);
            dbh.close();

            if (inserted_id != -1) {
                Toast.makeText(MainActivity.this, "Insert successful", Toast.LENGTH_SHORT).show();

            }
        });


        btnRetrieve.setOnClickListener(v -> {
            DBHelper dbh = new DBHelper(MainActivity.this);
            al.clear();

            String currentContent = etContent.getText().toString();

            // Todo: The edit text will be used to filter keywords.
            al.addAll(dbh.getAllNotes(currentContent));
            dbh.close();

            String txt = "";

            for (Note note : al) {
                txt += "ID:" + note.getId() + ", " +
                        note.getNoteContent() + "\n";
            }

            tvDBContent.setText(txt);
        });


        btnEdit.setOnClickListener(v -> {


            // Todo: Keep the application from crashing if the user
            //  clicks edit before clicking retrieve.
            if (al.isEmpty()) {
                DBHelper dbHelper = new DBHelper(MainActivity.this);
                al.clear();
                al.addAll(dbHelper.getAllNotes(""));
                aa.notifyDataSetChanged();
            }


            Note target = al.get(0);
            Intent i = new Intent(MainActivity.this,
                    EditActivity.class);
            i.putExtra("data", target);
            startActivityForResult(i, 9);
        });
    }

    @Override
    /*TODO:Change the way you launch the EditActivity
       in the MainActivity to startActivityForResults ().
       This ensures that onActivityResult() is called,
       and you can add code to refresh it there. */
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == 9) {
            btnRetrieve.performClick();
        }
    }
}


