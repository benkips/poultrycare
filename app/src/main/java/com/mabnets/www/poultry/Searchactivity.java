package com.mabnets.www.poultry;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.ClientError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.kosalgeek.android.json.JsonConverter;

import java.util.ArrayList;

import maes.tech.intentanim.CustomIntent;

import static android.content.ContentValues.TAG;

public class Searchactivity extends AppCompatActivity {
    private RecyclerView rvsrch;
    private searchAdapter searchAdapter;
    private Mycommand mycommand;
    final String Tag=this.getClass().getName();
    private ProgressDialog pd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchactivity);
        rvsrch=(RecyclerView)findViewById(R.id.rvsrchid);
        getSupportActionBar().setTitle("Diseases");
        mycommand=new Mycommand(Searchactivity.this);
        pd=new ProgressDialog(Searchactivity.this);
        pd.setMessage("Loading");
        LinearLayoutManager manager=new LinearLayoutManager(Searchactivity.this);
        rvsrch.setLayoutManager(manager);
        rvsrch.setHasFixedSize(true);
        loadsrchdata();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_file,menu);
        MenuItem searchItem=menu.findItem(R.id.android_search);
        SearchView searchView=(SearchView)searchItem.getActionView();
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchAdapter.getFilter().filter(newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
    private void loadsrchdata(){
        String url="http://lewis.mabnets.com/search.php";
        StringRequest strrq=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pd.dismiss();
                Log.d(Tag,response);
                if(!response.isEmpty()){
                    if(response.contains("No entry")){
                        Toast.makeText(Searchactivity.this, response, Toast.LENGTH_SHORT).show();
                    } else {
                        Log.d(Tag, response);
                        final ArrayList<srchdata> mgmtlist = new JsonConverter<srchdata>().toArrayList(response,srchdata.class);
                        searchAdapter = new searchAdapter(Searchactivity.this,mgmtlist);
                        rvsrch.setAdapter(searchAdapter);
                    }

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(error!=null) {
                    Log.d(TAG, error.toString());
                    if (error instanceof TimeoutError) {
                        pd.dismiss();
                        AlertDialog.Builder alert=new AlertDialog.Builder(Searchactivity.this);
                        alert.setMessage("please check your internet connectivity");
                        alert.setNeutralButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                startActivity(new Intent(Searchactivity.this,home.class));
                            }
                        });
                        alert.show();
                    } else if (error instanceof NoConnectionError) {
                        pd.dismiss();
                        AlertDialog.Builder alert=new AlertDialog.Builder(Searchactivity.this);
                        alert.setMessage("please check your internet connectivity");
                        alert.setNeutralButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                startActivity(new Intent(Searchactivity.this,home.class));
                            }
                        });
                        alert.show();
                    } else if (error instanceof NetworkError) {
                        pd.dismiss();
                        AlertDialog.Builder alert=new AlertDialog.Builder(Searchactivity.this);
                        alert.setMessage("please check your internet connectivity");
                        alert.setNeutralButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                startActivity(new Intent(Searchactivity.this,home.class));
                            }
                        });
                        alert.show();
                    } else if (error instanceof AuthFailureError) {
                        pd.dismiss();
                        Toast.makeText(Searchactivity.this, "error time out ", Toast.LENGTH_SHORT).show();
                    } else if (error instanceof ParseError) {
                        pd.dismiss();
                        Toast.makeText(Searchactivity.this, "error time out ", Toast.LENGTH_SHORT).show();
                    } else if (error instanceof ServerError) {
                        pd.dismiss();
                        Toast.makeText(Searchactivity.this, "error time out ", Toast.LENGTH_SHORT).show();
                    } else if (error instanceof ClientError) {
                        pd.dismiss();
                        Toast.makeText(Searchactivity.this, "error time out ", Toast.LENGTH_SHORT).show();
                    } else {
                        pd.dismiss();
                        Toast.makeText(Searchactivity.this, "error time out ", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        mycommand.add(strrq);
        pd.show();
        mycommand.execute();
        mycommand.remove(strrq);
    }
    @Override
    public void finish() {
        super.finish();
        CustomIntent.customType(Searchactivity.this,"right-to-left");
    }
}
