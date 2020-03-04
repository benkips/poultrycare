package com.mabnets.www.poultry;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import maes.tech.intentanim.CustomIntent;

public class home extends AppCompatActivity {
    private ListView index_menulv;
    private SharedPreferences.Editor editor;
    private SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        preferences=getSharedPreferences("logininfo.conf",MODE_PRIVATE);
        index_menulv=(ListView)findViewById(R.id.lvindex);
        String [] titlee=getResources().getStringArray(R.array.indexmenuone);
        String [] Description=getResources().getStringArray(R.array.indexmenutwo);
        SimpleAdapter adapter=new SimpleAdapter(home.this,titlee,Description);
        index_menulv.setAdapter(adapter);
        index_menulv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                switch(position){
                    case 0:{
                        startActivity(new Intent(home.this,tipsactivity.class));
                        break;
                    }
                    case  1:{
                        startActivity(new Intent(home.this,Searchactivity.class));
                        break;
                    }
                    case 2:{
                        startActivity(new Intent(home.this,consoltants.class));
                        break;
                    }

                }
            }
        });

    }

    public class SimpleAdapter extends BaseAdapter {
        private Context mcontext;
        private LayoutInflater layoutInflater;
        private TextView title,description;
        private String [] titleArray;
        private String [] Description;
        private ImageView imv;

        public SimpleAdapter(Context context,String [] title ,String [] description){
            mcontext=context;
            titleArray=title;
            Description=description;
            layoutInflater=LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return titleArray.length;

        }

        @Override
        public Object getItem(int i) {
            return titleArray[i];
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if(view==null){
                view=layoutInflater.inflate(R.layout.index_menu,null);

            }
            title=(TextView)view.findViewById(R.id.tvtitleindex_menu);
            description=(TextView)view.findViewById(R.id.tvdescindex_menu);
            imv=(ImageView)view.findViewById(R.id.ivindex_menu);
            title.setText(titleArray[i]);
            description.setText(Description[i]);
            if(titleArray[i].equalsIgnoreCase("tips")){
                imv.setImageResource(R.drawable.tipsz);
            }else if(titleArray[i].equalsIgnoreCase("Search condition")){
                imv.setImageResource(R.drawable.search);
            }else {
            imv.setImageResource(R.drawable.vetinary);
            }
            return view;

        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.logout) {
            editor = preferences.edit();
            editor.clear();
            editor.commit();
            startActivity(new Intent(home.this, Login.class));
            home.this.finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public void finish() {
        super.finish();
        CustomIntent.customType(home.this,"left-to-right");
    }
}
