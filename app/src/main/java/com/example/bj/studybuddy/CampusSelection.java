package com.example.bj.studybuddy;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class CampusSelection extends AppCompatActivity {
    String results = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campus_selection);
        getCourses task = new getCourses();
        try {task.execute().get();}
        catch (Exception e){e.printStackTrace();};

        //CREATE LISTVIEW CONTENTS
        final ListView listview = (ListView) findViewById(R.id.listview);
        String[] values = results.split(",");
        final ArrayList<String> list = new ArrayList<String>();
        for (int i = 0; i < values.length; ++i) {
            list.add(values[i]);
        }
        //APPLY ARRAY ADAPTER
        final StableArrayAdapter adapter = new StableArrayAdapter(this, android.R.layout.simple_list_item_1, list);
        listview.setAdapter(adapter);

        //SET LISTENER
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)

            //DEFINE CLICK LISTENER
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                final String item = (String) parent.getItemAtPosition(position);
                Intent intent = new Intent(view.getContext(), ClassSelection.class);
                intent.putExtra("campus", item);
                startActivity(intent);
            }

        });
    }

    private class getCourses extends AsyncTask{
        @Override
        protected Long doInBackground(Object... params) {
            String link = "http://designsbybenji.com/studybuddy/getCampusList.php";
            //Log.d("results","test");
            try{
                URL url = new URL(link);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                InputStream stream = new BufferedInputStream(connection.getInputStream());
                results = readStream(stream);
                Log.d("results",results);
                return null;
            } catch(IOException e) {
                Log.e("MYAPP", "exception", e);
                return null;
            }
        }

        private String readStream(InputStream is) {
            try {
                ByteArrayOutputStream bo = new ByteArrayOutputStream();
                int i = is.read();
                while(i != -1) {
                    bo.write(i);
                    i = is.read();
                }
                return bo.toString();
            } catch (IOException e) {
                return "";
            }
        }
    }
}


