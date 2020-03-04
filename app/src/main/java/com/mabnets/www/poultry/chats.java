package com.mabnets.www.poultry;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.kosalgeek.android.json.JsonConverter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import maes.tech.intentanim.CustomIntent;

import static android.content.ContentValues.TAG;

public class chats extends AppCompatActivity {
    private RecyclerView recyclerV;
    private EditText msgg;
    private FloatingActionButton flsend;
    List<chatmodel> list_modell=new ArrayList<>();
    private SharedPreferences preff;
    private Mycommand mycommand;
    private String phone;
    private String vid;
    private ProgressDialog pd;
    final String Tag=this.getClass().getName();
    private Thread upp;
    private Handler handler;
    msgadapter msgadapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chats);
        recyclerV=(RecyclerView)findViewById(R.id.rvchats);
        handler=new Handler();
        msgg=(EditText)findViewById(R.id.etchat);
        flsend=(FloatingActionButton)findViewById(R.id.flchats);
        mycommand=new Mycommand(chats.this);
        preff=getSharedPreferences("logininfo.conf",MODE_PRIVATE);
        phone=preff.getString("phone","");
        Log.d(Tag,phone);
        pd=new ProgressDialog(chats.this);
        pd.setMessage("Loading");

        Bundle bundle=getIntent().getExtras();
        if(bundle!=null){
            vid=bundle.getString("vid");
            Log.d(Tag, vid);
        }
        LinearLayoutManager manager = new LinearLayoutManager(chats.this);
        manager.setReverseLayout(true);
        recyclerV.setLayoutManager(manager);
        refresh();
        loadchats();

        flsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String text=msgg.getText().toString();
                if(text.equals("")) {
                    msgg.requestFocus();
                    return;

                }else{
                    sendmessag(text);
                    msgg.setText("");

                }

            }
        });


    }
    private  void refresh() {

        upp = new Thread() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                       rf();
                        handler.postDelayed(this, 2000);
                    }
                });
            }

        };
        upp.setDaemon(true);
        upp.start();
    }
    private  void sendmessag(final String message) {
        String url = "http://lewis.mabnets.com/chatsaver.php";
        StringRequest stringRequest=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
               /* loadchats();*/
                /* Toast.makeText(chats.this, response, Toast.LENGTH_SHORT).show();*/

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (error instanceof TimeoutError) {
                    
                    Toast.makeText(chats.this, "error time out ", Toast.LENGTH_SHORT).show();
                } else if (error instanceof NoConnectionError) {
                    
                    Toast.makeText(chats.this, "error no connection", Toast.LENGTH_SHORT).show();
                } else if (error instanceof NetworkError) {
                    
                    Toast.makeText(chats.this, "error network error", Toast.LENGTH_SHORT).show();
                } else if (error instanceof AuthFailureError) {
                    
                    Toast.makeText(chats.this, "errorin Authentication", Toast.LENGTH_SHORT).show();
                } else if (error instanceof ParseError) {
                    
                    Toast.makeText(chats.this, "error while parsing", Toast.LENGTH_SHORT).show();
                } else if (error instanceof ServerError) {
                    
                    Toast.makeText(chats.this, "error  in server", Toast.LENGTH_SHORT).show();
                } else if (error instanceof ClientError) {
                    
                    Toast.makeText(chats.this, "error with Client", Toast.LENGTH_SHORT).show();
                } else {
                    
                    Toast.makeText(chats.this, "error while loading", Toast.LENGTH_SHORT).show();
                }
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> params=new HashMap<>();
                params.put("sender",phone);
                params.put("to",vid);
                params.put("mssg",message);
                return params;
            }
        };
        mycommand.add(stringRequest);
        mycommand.execute();
        mycommand.remove(stringRequest);
    }
    private void loadchats(){
        String url="http://lewis.mabnets.com/getchats.php";
        StringRequest strrq=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pd.dismiss();
                Log.d(Tag,response);

                if(!response.isEmpty()) {
                    if (response.equals("no entry")) {
                        Toast.makeText(chats.this, "No chats  currently", Toast.LENGTH_SHORT).show();
                    } else {
                    ArrayList<chatitems> navdetails = new JsonConverter<chatitems>().toArrayList(response, chatitems.class);
                    msgadapter=new msgadapter(navdetails,chats.this,vid);
                    recyclerV.setAdapter(msgadapter);
                   /* ArrayList<String> title = new ArrayList<String>();
                    list_modell.clear();
                    for (chatitems value : navdetails) {
                        if (value.sender.equals(vid + "f")) {
                            title.add(value.message);
                            chatmodel chatmodel = new chatmodel(value.message, false);
                            list_modell.add(chatmodel);
                            recyclerV.setHasFixedSize(true);
                            chatAdapter chatAdapter = new chatAdapter(chats.this, list_modell);
                            recyclerV.setAdapter(chatAdapter);
                        } else {
                            chatmodel chatmodell = new chatmodel(value.message, true);

                            list_modell.add(chatmodell);
                            recyclerV.setHasFixedSize(true);
                            chatAdapter chatAdapter = new chatAdapter(chats.this, list_modell);
                            recyclerV.setAdapter(chatAdapter);
                        }
                        LinearLayoutManager manager = new LinearLayoutManager(chats.this);
                        manager.setReverseLayout(true);
                        recyclerV.setLayoutManager(manager);
                        recyclerV.scrollToPosition(0);
                    }*/
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
                        AlertDialog.Builder alert=new AlertDialog.Builder(chats.this);
                        alert.setMessage("please check your internet connectivity");
                        alert.setNeutralButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                startActivity(new Intent(chats.this,home.class));
                            }
                        });
                        alert.show();
                    } else if (error instanceof NoConnectionError) {
                        pd.dismiss();
                        AlertDialog.Builder alert=new AlertDialog.Builder(chats.this);
                        alert.setMessage("please check your internet connectivity");
                        alert.setNeutralButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                startActivity(new Intent(chats.this,home.class));
                            }
                        });
                        alert.show();
                    } else if (error instanceof NetworkError) {
                        pd.dismiss();
                        AlertDialog.Builder alert=new AlertDialog.Builder(chats.this);
                        alert.setMessage("please check your internet connectivity");
                        alert.setNeutralButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                startActivity(new Intent(chats.this,home.class));
                            }
                        });
                        alert.show();
                    } else if (error instanceof AuthFailureError) {
                        pd.dismiss();
                        Toast.makeText(chats.this, "error time out ", Toast.LENGTH_SHORT).show();
                    } else if (error instanceof ParseError) {
                        pd.dismiss();
                        Toast.makeText(chats.this, "error time out ", Toast.LENGTH_SHORT).show();
                    } else if (error instanceof ServerError) {
                        pd.dismiss();
                        Toast.makeText(chats.this, "error time out ", Toast.LENGTH_SHORT).show();
                    } else if (error instanceof ClientError) {
                        pd.dismiss();
                        Toast.makeText(chats.this, "error time out ", Toast.LENGTH_SHORT).show();
                    } else {
                        pd.dismiss();
                        Toast.makeText(chats.this, "error time out ", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("id",vid);
                params.put("fon", phone);
                return params;
            }

            ;
        };
        mycommand.add(strrq);
        pd.show();
        mycommand.execute();
        mycommand.remove(strrq);
    }
    private void rf(){
        String url="http://lewis.mabnets.com/getchats.php";
        StringRequest strrq=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pd.dismiss();
                Log.d(Tag,response);

                if(!response.isEmpty()) {
                    if (response.equals("no entry")) {
                        Toast.makeText(chats.this, "No chats  currently", Toast.LENGTH_SHORT).show();
                    } else {
                        ArrayList<chatitems> navdetails = new JsonConverter<chatitems>().toArrayList(response, chatitems.class);
                        msgadapter=new msgadapter(navdetails,chats.this,vid);
                        msgadapter.updateData(navdetails);

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
                        AlertDialog.Builder alert=new AlertDialog.Builder(chats.this);
                        alert.setMessage("please check your internet connectivity");
                        alert.setNeutralButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                startActivity(new Intent(chats.this,home.class));
                            }
                        });
                        alert.show();
                    } else if (error instanceof NoConnectionError) {
                        pd.dismiss();
                        AlertDialog.Builder alert=new AlertDialog.Builder(chats.this);
                        alert.setMessage("please check your internet connectivity");
                        alert.setNeutralButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                startActivity(new Intent(chats.this,home.class));
                            }
                        });
                        alert.show();
                    } else if (error instanceof NetworkError) {
                        pd.dismiss();
                        AlertDialog.Builder alert=new AlertDialog.Builder(chats.this);
                        alert.setMessage("please check your internet connectivity");
                        alert.setNeutralButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                startActivity(new Intent(chats.this,home.class));
                            }
                        });
                        alert.show();
                    } else if (error instanceof AuthFailureError) {
                        pd.dismiss();
                        Toast.makeText(chats.this, "error time out ", Toast.LENGTH_SHORT).show();
                    } else if (error instanceof ParseError) {
                        pd.dismiss();
                        Toast.makeText(chats.this, "error time out ", Toast.LENGTH_SHORT).show();
                    } else if (error instanceof ServerError) {
                        pd.dismiss();
                        Toast.makeText(chats.this, "error time out ", Toast.LENGTH_SHORT).show();
                    } else if (error instanceof ClientError) {
                        pd.dismiss();
                        Toast.makeText(chats.this, "error time out ", Toast.LENGTH_SHORT).show();
                    } else {
                        pd.dismiss();
                        Toast.makeText(chats.this, "error time out ", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("id",vid);
                params.put("fon", phone);
                return params;
            }

            ;
        };
        mycommand.add(strrq);
        mycommand.execute();
        mycommand.remove(strrq);
    }
    @Override
    public void finish() {
        super.finish();
        CustomIntent.customType(chats.this,"right-to-left");
    }
}
