package com.pankaj.nptest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements CustomAsyncTask.CustomAsyncListener {

    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.text);

        CustomAsyncTask customAsyncTask = new CustomAsyncTask<>("Hello Pankaj", this);
        customAsyncTask.execute();
    }

    @Override
    public void onPreExecute() {
        Toast.makeText(MainActivity.this, "Starting background thread", Toast.LENGTH_SHORT).show();
    }

    @Override
    public Object doInBackground(Object obj) {
        return obj;
    }

    @Override
    public void onPostExecute(Object obj) {
        textView.setText(obj +"");
        Toast.makeText(MainActivity.this, obj +"", Toast.LENGTH_SHORT).show();
    }
}
