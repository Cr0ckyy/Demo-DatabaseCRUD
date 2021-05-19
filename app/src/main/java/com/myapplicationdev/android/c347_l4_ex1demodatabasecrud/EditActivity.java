package com.myapplicationdev.android.c347_l4_ex1demodatabasecrud;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class EditActivity extends AppCompatActivity {

    TextView tvID;
    EditText etContent;
    Button btnUpdate, btnDelete;
    Note data;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        //initialize the variables with UI here
        tvID = findViewById(R.id.tvID);
        etContent = findViewById(R.id.etContent);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnDelete = findViewById(R.id.btnDelete);

        Intent i = getIntent();
        data = (Note) i.getSerializableExtra("data");

        tvID.setText("ID: " + data.getId());
        etContent.setText(data.getNoteContent());

        btnUpdate.setOnClickListener(v -> {
            DBHelper dbh = new DBHelper(EditActivity.this);
            data.setNoteContent(etContent.getText().toString());
            dbh.updateNote(data);
            dbh.close();
        });


        btnDelete.setOnClickListener(v -> {
            DBHelper dbh = new DBHelper(EditActivity.this);
            dbh.deleteNote(data.getId());
            dbh.close();
        });

    }
}