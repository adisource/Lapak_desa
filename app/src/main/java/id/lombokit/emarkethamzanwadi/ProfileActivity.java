package id.lombokit.emarkethamzanwadi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import id.lombokit.emarkethamzanwadi.SessionManager.SessionManager;

public class ProfileActivity extends AppCompatActivity {

    ImageView kembali, pemberitahuan;
    TextView judul;
    TextView textViewnama,textViewkecamatan,textViewtgl_lahir,textViewStatus_nikah,textViewKab,textViewAlamat,textViewEmail,textViewNo_hp;
    String url_profil = "http://192.168.43.153/e_comm_covid/profil_user.php?nik=";
    SessionManager sessionManager;
    String nik;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        kembali = findViewById(R.id.back);
        judul = findViewById(R.id.title);
        pemberitahuan = findViewById(R.id.notif);
        textViewnama = findViewById(R.id.nama_user);
        textViewkecamatan =findViewById(R.id.kecamatan);
        textViewtgl_lahir = findViewById(R.id.tgl_lahir);
        textViewStatus_nikah = findViewById(R.id.status_nikah);
        textViewKab = findViewById(R.id.kab);
        textViewAlamat = findViewById(R.id.alamat);
        textViewEmail = findViewById(R.id.email);
        textViewNo_hp = findViewById(R.id.no_hp);

        sessionManager = new SessionManager(this);
        nik = sessionManager.getSpNikwali();
        //Toast.makeText(getApplicationContext(),""+nik,Toast.LENGTH_SHORT).show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, (url_profil + nik), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i<jsonArray.length();i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String nama_user= jsonObject.getString("nama");
                        String kec = jsonObject.getString("kecamatan");
                        String tgl_lahir = jsonObject.getString("tgl_lahir");
                        String status_nikah  = jsonObject.getString("status_nikah");
                        String kab = jsonObject.getString("kabupaten");
                        String email = jsonObject.getString("email");
                        String no_hp = jsonObject.getString("telpon");
                        String alamat = jsonObject.getString("alamat");
                        textViewnama.setText(nama_user);
                        textViewkecamatan.setText(kec);
                        textViewtgl_lahir.setText(tgl_lahir);
                        textViewStatus_nikah.setText(status_nikah);
                        textViewKab.setText(kab);
                        textViewAlamat.setText(alamat);
                        textViewEmail.setText(email);
                        textViewNo_hp.setText(no_hp);
                        //Toast.makeText(getApplicationContext(),""+na,Toast.LENGTH_SHORT).show();

                    }
                }catch (JSONException e){
                    e.printStackTrace();

                }





            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),""+error,Toast.LENGTH_SHORT).show();
            }
        });
        Volley.newRequestQueue(this).add(stringRequest);


        kembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
            }
        });

        pemberitahuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), NotificationActivity.class));
            }
        });

        judul.setText((CharSequence) "Profil");


    }
}
