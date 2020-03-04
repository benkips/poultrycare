package com.mabnets.www.poultry;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import maes.tech.intentanim.CustomIntent;

public class moreinfo extends AppCompatActivity {
    private String  diseasename;
    private String  symptoms;
    private String  cures;
    private TextView tvdis;
    private TextView tvsys;
    private TextView tvcurss;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moreinfo);
        Bundle bundle=getIntent().getExtras();
        if(bundle!=null){
            diseasename=bundle.getString("diseas");
            symptoms=bundle.getString("sysmptom");
            cures=bundle.getString("cure");
            getSupportActionBar().setTitle("more information");
        }
        tvdis=(TextView)findViewById(R.id.disz);
        tvsys=(TextView)findViewById(R.id.sys);
        tvcurss=(TextView)findViewById(R.id.curez);

        tvdis.setText(diseasename);
        tvsys.setText(symptoms);
        tvcurss.setText(cures);

    }
    public void finish() {
        super.finish();
        CustomIntent.customType(moreinfo.this,"right-to-left");
    }
}
