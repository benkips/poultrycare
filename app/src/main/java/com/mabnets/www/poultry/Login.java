package com.mabnets.www.poultry;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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

public class Login extends AppCompatActivity {
    private TextView signupref;
    private EditText usidno;
    private EditText usphone;
    private CheckBox cb;
    private Button lgnbtn;
    private ProgressDialog progressDialog;
    private Mycommand mycommand;
    private Boolean checked;
    private SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        signupref=(TextView)findViewById(R.id.signupref);
        lgnbtn=(Button) findViewById(R.id.usbtnlogin);
        usidno=(EditText)findViewById(R.id.usidlog);
        usphone=(EditText)findViewById(R.id.usphonelog);
        cb=(CheckBox)findViewById(R.id.cbL);

        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("loading..");
        mycommand=new Mycommand(this);

        preferences=getSharedPreferences("logininfo.conf",MODE_PRIVATE);
        String phones=preferences.getString("phone","");
        String idno=preferences.getString("idno","");

        if(!phones.equals("") && !idno.equals("")){
            /*startActivity here*/
            startActivity(new Intent(Login.this,home.class));
            Login.this.finish();
        }
        checked=cb.isChecked();
        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean b) {
                checked=b;

            }
        });
        lgnbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String idnumber= usidno.getText().toString().trim();
                String phonee=usphone.getText().toString().trim();

                userlogin(idnumber,phonee);

            }
        });
        signupref.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this,Registration.class));
                CustomIntent.customType(Login.this,"left-to-right");
            }
        });

    }
    private void userlogin(final String i,final String ph){
       if(i.isEmpty()){
            usidno.setError("idno is invalid");
            usidno.requestFocus();
            return;
        }else if(ph.isEmpty()){
            usphone.setError("phone is invalid");
            usphone.requestFocus();
            return;
        }else {
            if (!isphone(ph) || (ph.length() != 10 || !ph.startsWith("07"))) {
                usphone.setError("phone is invalid");
                usphone.requestFocus();
                return;
            } else {
                String url = "http://lewis.mabnets.com/login.php";
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        if(!response.equals("Wrong details,try again")){
                            if(checked){
                                preferences=getSharedPreferences("logininfo.conf",MODE_PRIVATE);
                                SharedPreferences.Editor editor=preferences.edit();
                                editor.putString("phone",ph);
                                editor.putString("idno",i);
                                editor.apply();
                            }
                          Toast.makeText(Login.this, response, Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(Login.this,home.class));
                            Login.this.finish();
                            CustomIntent.customType(Login.this,"left-to-right");
                        }else{
                            Toast.makeText(Login.this, response, Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {


                        if (error instanceof TimeoutError) {
                            progressDialog.dismiss();
                            Toast.makeText(Login.this, "error time out ", Toast.LENGTH_SHORT).show();
                        } else if (error instanceof NoConnectionError) {
                            progressDialog.dismiss();
                            Toast.makeText(Login.this, "error no connection", Toast.LENGTH_SHORT).show();
                        } else if (error instanceof NetworkError) {
                            progressDialog.dismiss();
                            Toast.makeText(Login.this, "error network error", Toast.LENGTH_SHORT).show();
                        } else if (error instanceof AuthFailureError) {
                            progressDialog.dismiss();
                            Toast.makeText(Login.this, "errorin Authentication", Toast.LENGTH_SHORT).show();
                        } else if (error instanceof ParseError) {
                            progressDialog.dismiss();
                            Toast.makeText(Login.this, "error while parsing", Toast.LENGTH_SHORT).show();
                        } else if (error instanceof ServerError) {
                            progressDialog.dismiss();
                            Toast.makeText(Login.this, "error  in server", Toast.LENGTH_SHORT).show();
                        } else if (error instanceof ClientError) {
                            progressDialog.dismiss();
                            Toast.makeText(Login.this, "error with Client", Toast.LENGTH_SHORT).show();
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(Login.this, "error while loading", Toast.LENGTH_SHORT).show();
                        }

                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
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
    @Override
    public void finish() {
        super.finish();
        CustomIntent.customType(Login.this,"right-to-left");
    }
}

