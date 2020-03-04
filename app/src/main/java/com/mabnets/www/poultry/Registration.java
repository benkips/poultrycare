package com.mabnets.www.poultry;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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

import java.util.HashMap;
import java.util.Map;

import maes.tech.intentanim.CustomIntent;

public class Registration extends AppCompatActivity {
    private TextView loginref;
    private EditText unames;
    private EditText idno;
    private EditText phone;
    private Button signbtn;
    private ProgressDialog progressDialog;
    private Mycommand mycommand;
    private View sn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        loginref=(TextView)findViewById(R.id.uloginref);
        signbtn=(Button) findViewById(R.id.usignup);
        unames=(EditText)findViewById(R.id.uname);
        idno=(EditText)findViewById(R.id.uidno);
        phone=(EditText)findViewById(R.id.uphone);

        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("loading..");
        mycommand=new Mycommand(this);

        signbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String un= unames.getText().toString().trim();
                String idnumber= idno.getText().toString().trim();
                String phonee=phone.getText().toString().trim();

                usereg(un,idnumber,phonee);
            }
        });

        loginref.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    startActivity(new Intent(Registration.this,Login.class));
                            CustomIntent.customType(Registration.this,"left-to-right");
            }
        });
    }
    private void usereg(final String f, final String i,final String ph){
        if(f.isEmpty()){
            unames.setError("name is invalid");
            unames.requestFocus();
            return;
        }else if(i.isEmpty()){
            idno.setError("idno is invalid");
            idno.requestFocus();
            return;
        }else if(ph.isEmpty()){
            phone.setError("phone is invalid");
            phone.requestFocus();
            return;
        }else {
       if (!isphone(ph) || (ph.length() != 10 || !ph.startsWith("07"))) {
                phone.setError("phone is invalid");
                phone.requestFocus();
                return;
            } else {
                String url = "http://lewis.mabnets.com/registration.php";
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        if(response.contains("success")){
                            Toast.makeText(Registration.this, response, Toast.LENGTH_SHORT).show();
                            /*startActivity(new Intent(Registration.this,main.class));
                            CustomIntent.customType(leclogin.this,"left-to-right");*/
                        }else{
                            Toast.makeText(Registration.this, response, Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {


                        if (error instanceof TimeoutError) {
                            progressDialog.dismiss();
                            Toast.makeText(Registration.this, "error time out ", Toast.LENGTH_SHORT).show();
                        } else if (error instanceof NoConnectionError) {
                            progressDialog.dismiss();
                            Toast.makeText(Registration.this, "error no connection", Toast.LENGTH_SHORT).show();
                        } else if (error instanceof NetworkError) {
                            progressDialog.dismiss();
                            Toast.makeText(Registration.this, "error network error", Toast.LENGTH_SHORT).show();
                        } else if (error instanceof AuthFailureError) {
                            progressDialog.dismiss();
                            Toast.makeText(Registration.this, "errorin Authentication", Toast.LENGTH_SHORT).show();
                        } else if (error instanceof ParseError) {
                            progressDialog.dismiss();
                            Toast.makeText(Registration.this, "error while parsing", Toast.LENGTH_SHORT).show();
                        } else if (error instanceof ServerError) {
                            progressDialog.dismiss();
                            Toast.makeText(Registration.this, "error  in server", Toast.LENGTH_SHORT).show();
                        } else if (error instanceof ClientError) {
                            progressDialog.dismiss();
                            Toast.makeText(Registration.this, "error with Client", Toast.LENGTH_SHORT).show();
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(Registration.this, "error while loading", Toast.LENGTH_SHORT).show();
                        }

                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("username", f);
                        params.put("idno", i);
                        params.put("phone", ph);
                        return params;
                    }
                };
                mycommand.add(stringRequest);
                progressDialog.show();
                mycommand.execute();
                mycommand.remove(stringRequest);
            }

        }
    }
    public static boolean isphone(CharSequence target){
        return !TextUtils.isEmpty(target) && Patterns.PHONE.matcher(target).matches();
    }
}
