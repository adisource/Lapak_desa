package id.lombokit.emarkethamzanwadi;

import android.app.DownloadManager;
import android.content.Intent;
import android.content.pm.PackageInstaller;
import android.os.Bundle;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import id.lombokit.emarkethamzanwadi.SessionManager.SessionManager;

public class LoginActivity extends AppCompatActivity {

    ImageView selebihnya;
    TextView judul;
    TextView login;
    TextView textViewDaftar;
    EditText inputNik,inputNama;

    SessionManager session;


    String url = "http://192.168.43.153/e_comm_covid/login.php";
    private String NIK_USER = "nik";
    //private String NAMA = "nama";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        judul = findViewById(R.id.title);
        selebihnya = findViewById(R.id.more);
        login = findViewById(R.id.login);
        inputNik = findViewById(R.id.nik);
        textViewDaftar = findViewById(R.id.daftar);
        //inputNama = findViewById(R.id.nama);
        session = new SessionManager(this);

        textViewDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Register.class));
            }
        });
        selebihnya.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), NotificationActivity.class));
            }
        });

        judul.setText((CharSequence) "Login Pengguna");

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
                ceklogin();
            }

            private void ceklogin() {
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String nik =inputNik.getText().toString();
                        //String nama = inputNama.getText().toString();

                        if (nik.isEmpty()) {
                            inputNik.setError("Tidak boleh kosong");
                        } else {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String status = jsonObject.getString("status");
                                if (status.equals("success")){
                                    String nama_desa = jsonObject.getString("desa");
                                    String id_user = jsonObject.getString("id_user");
                                    session.saveString(SessionManager.SP_IDUSER,id_user);
                                    session.saveString(SessionManager.SP_NIKWALI,nik);
                                    session.saveString(SessionManager.SP_NAMADESA,nama_desa);
                                    session.saveBoolean(SessionManager.SP_LOGINED,true);
                                    setStatus();
                                    Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                                    //intent.putExtra("no_hp",nama);
                                    startActivity(intent);
                                    Log.v("cek","berhasil");
                                }else if(status.equals("filed")){
                                    Toast.makeText(getApplicationContext(),"NIK tidak di temukan",Toast.LENGTH_SHORT).show();
                                   // alertDialog2.show();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();

                            }
                        }


                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),"Error"+error,Toast.LENGTH_SHORT).show();

                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> map = new HashMap<>();
                        map.put(NIK_USER,inputNik.getText().toString());
                        return map;
                    }
                };
                Volley.newRequestQueue(LoginActivity.this).add(stringRequest);

            }
        });
    }
    private void  setStatus(){
        

    }
    @Override
    protected void onStart() {
        super.onStart();
        if (session.getSpLogined()==true){
            Intent intent = new Intent(this, DashboardActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

        }
    }

}
