package com.example.humantestdetection;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity2 extends AppCompatActivity {

    File[] files;
    boolean[] selection;
    List<String> filesList;
    int filesFoundCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        final Button delete_button = findViewById(R.id.b1);
        final Button back_button = findViewById(R.id.b2);

        final ListView listView = findViewById(R.id.listView);
        final TextAdapter textAdapter1 = new TextAdapter();
        listView.setAdapter(textAdapter1);

        final String rootPath = Environment.
                getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/PicturesTest";
        final File dir = new File(rootPath);

        files = dir.listFiles();
        final TextView pathOutput = findViewById(R.id.pathOutput);
        pathOutput.setText(rootPath.substring(rootPath.lastIndexOf('/')+1));
        filesFoundCount = files.length;

        filesList = new ArrayList<>();

        for (int i = 0; i < filesFoundCount; i++){
            filesList.add(files[i].getAbsolutePath());
        }

        textAdapter1.setData(filesList);

        selection = new boolean[files.length];

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                selection[position] = !selection[position];
                textAdapter1.setSelection(selection);
                return false;
            }
        });

        delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(int i = 0; i<files.length; i++){
                    if (selection[i]){
                        files[i].delete();
                        selection[i]=false;
                    }
                }
                files = dir.listFiles();
                filesFoundCount = files.length;
                filesList.clear();
                for (int i = 0; i < filesFoundCount; i++){
                    filesList.add(files[i].getAbsolutePath());
                }

                textAdapter1.setData(filesList);
            }
        });

        back_button.setOnClickListener((view)->{
            startActivity(new Intent(MainActivity2.this, MainActivity.class));
        });
    }


    class TextAdapter extends BaseAdapter {

        private final List<String> data = new ArrayList<>();

        private boolean[] selection;

        public void setData(List<String> data) {
            if (data != null) {
                this.data.clear();
                if (data.size() > 0) {
                    this.data.addAll(data);
                }
                notifyDataSetChanged();
            }
        }

        void setSelection(boolean [] selection){
            if(selection != null){
                this.selection = new boolean[selection.length];
                for(int i = 0; i<selection.length; i++){
                    this.selection[i] = selection[i];
                }
                notifyDataSetChanged();
            }
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public String getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null){
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
                convertView.setTag(new ViewHolder(convertView.findViewById(R.id.textItem)));
            }
            ViewHolder holder = (ViewHolder) convertView.getTag();
            final String item = getItem(position);
            holder.info.setText(item.substring(item.lastIndexOf('/')+1));
            if (selection != null){
                if(selection[position]){
                    holder.info.setBackgroundColor(Color.GRAY);
                }else{
                    holder.info.setBackgroundColor(Color.WHITE);
                }
            }
            return convertView;
        }

        class ViewHolder{
            TextView info;

            ViewHolder(TextView info){
                this.info = info;
            }
        }
    }
}