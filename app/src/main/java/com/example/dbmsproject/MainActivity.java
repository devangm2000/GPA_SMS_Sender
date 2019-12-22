package com.example.dbmsproject;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.dbmsproject.Adapters.MyAdapter;
import com.example.dbmsproject.Model.Student;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    EditText ed1,ed2;
    Button btnAct2;
    Button btnAct3;
    ListView l1;
    DatabaseHelper databaseHelper;
    ArrayList<Student> arrayList;
    MyAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ed1=(EditText)findViewById(R.id.editText1);
        ed2=(EditText)findViewById(R.id.editText2);
        btnAct2=(findViewById(R.id.btnAct2));

        l1=(ListView)findViewById(R.id.listView);
        databaseHelper= new DatabaseHelper(this);
        loadDataInListView();
        arrayList= new ArrayList<>();
        btnAct2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ed2.getText().toString().isEmpty()&&ed1.getText().toString().isEmpty())
                {
                    Toast.makeText(MainActivity.this,"Please enter all fields",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    String name=ed1.getText().toString().trim();
                    String cgpa=ed2.getText().toString().trim();
                    Intent intent= new Intent(MainActivity.this,com.example.dbmsproject.MainActivity2.class);
                    intent.putExtra("name",name);
                    intent.putExtra("cgpa",cgpa);
                    startActivity(intent);
                }
            }
        });

    }
    private void loadDataInListView() {
        arrayList=databaseHelper.getAllData();
        myAdapter= new MyAdapter(this,arrayList);
        l1.setAdapter(myAdapter);
        myAdapter.notifyDataSetChanged();

    }

    public void insert(View v)
    {
        boolean result=databaseHelper.insertData(ed1.getText().toString(),ed2.getText().toString());

        if(result) {
            Toast.makeText(getApplicationContext(), "Data inserted successfully", Toast.LENGTH_LONG).show();
        }
        else
        {
            Toast.makeText(getApplicationContext(), "Data could not be added", Toast.LENGTH_LONG).show();
        }
    }
    public void display(View v)
    {
        Cursor cursor= (Cursor) databaseHelper.getAllData();
        if(cursor.getCount()==0)
        {
            Toast.makeText(getApplicationContext(),"No data available",Toast.LENGTH_SHORT).show();
            return;
        }
        StringBuffer stringbuffer= new StringBuffer();
        while(cursor.moveToNext())
        {
            stringbuffer.append("NAME:"+cursor.getString(1)+"\n");
            stringbuffer.append("CGPA:"+cursor.getString(2)+"\n");
        }
        Toast.makeText(getApplicationContext(),stringbuffer.toString(),Toast.LENGTH_SHORT).show();

    }




}

