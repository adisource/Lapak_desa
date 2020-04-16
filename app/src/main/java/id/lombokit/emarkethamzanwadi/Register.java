package id.lombokit.emarkethamzanwadi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
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

import java.util.ArrayList;
import java.util.List;

import id.lombokit.emarkethamzanwadi.Models.Domisili;

public class Register extends AppCompatActivity {

    TextView textViewLogin;
    String url_kab = "http://192.168.43.153/e_comm_covid/view_kab.php";
    String url_kec = "http://192.168.43.153/e_comm_covid/view_kec.php?id_kab=";
    String url_desa = "http://192.168.43.153/e_comm_covid/view_desa.php";

    Spinner spinnerDesa;
    Spinner spinnerKec;
    Spinner spinnerKab;
    List<Domisili> domisilis = new ArrayList<Domisili>();
    List<String> list_desa = new ArrayList<String>();
    List<String> list_kabupaten= new ArrayList<>();
    List<String> list_kec= new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        textViewLogin  = findViewById(R.id.pageLogin);
        spinnerDesa = findViewById(R.id.desa);
        spinnerKab= findViewById(R.id.kab);
        spinnerKec = findViewById(R.id.kec);

        textViewLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
            }
        });
        loadKab();
        //loadKec("1");

        //loadDesa();

    }
    private  void loadKab(){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url_kab, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                list_kabupaten.add(0,getString(R.string.select_kab));
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0;i<jsonArray.length();i++){
                        JSONObject data = jsonArray.getJSONObject(i);
                        String id_kab = data.getString("id_kab");
                        String kab = data.getString("kabupaten");
                        Domisili domisili  = new Domisili();
                        domisili.setId_kab(id_kab);
                        domisili.setKabupaten(kab);
                        domisilis.add(domisili);
                    }

                    for (int i=0;i<domisilis.size();i++){
                        list_kabupaten.add(domisilis.get(i).getKabupaten());
                    }

                    ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getApplicationContext(),
                            R.layout.spiner_item, list_kabupaten);

                    spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerKab.setAdapter(spinnerAdapter);
                    spinnerKab.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            String seleted = (String) parent.getItemAtPosition(position);
                            if (!spinnerKab.getSelectedItem().toString().equals(getString(R.string.select_kab))){
                                String idkab = domisilis.get(position-1).getId_kab();

                                //Toast.makeText(getApplicationContext(),""+idkab,Toast.LENGTH_LONG).show()

                            }

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });


                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        Volley.newRequestQueue(this).add(stringRequest);
    }
    private void loadKec(String idkab) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, (url_kec+idkab), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                list_kec.add(0,getString(R.string.select_kec));
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0;i<jsonArray.length();i++){
                        JSONObject data = jsonArray.getJSONObject(i);
                        String kec = data.getString("kecamatan");


                        Domisili domisili  = new Domisili();
                        domisili.setKecamatan(kec);
                        domisilis.add(domisili);

                    }

                    for (int i=0;i<domisilis.size();i++){
                        list_kec.add(domisilis.get(i).getKecamatan());
                        Toast.makeText(getApplicationContext(),""+domisilis.get(i).getKecamatan(),Toast.LENGTH_LONG).show();
                    }


                    ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getApplicationContext(),
                            R.layout.spiner_item, list_kec);

                    spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerKec.setAdapter(spinnerAdapter);
                    spinnerKec.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            //String seleted = (String) parent.getItemAtPosition(position);



                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        Volley.newRequestQueue(this).add(stringRequest);
    }

    private void loadDesa() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url_desa, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0;i<jsonArray.length();i++){
                        JSONObject data = jsonArray.getJSONObject(i);
                        String desa = data.getString("desa");
                        Domisili domisili  = new Domisili();
                        domisili.setNama_desa(desa);
                        domisilis.add(domisili);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //listDesa();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        Volley.newRequestQueue(this).add(stringRequest);
    }





    private void lisKec() {

    }
    private void listDesa() {
        list_desa.add(0,getString(R.string.select_desa));
        for (int i=0;i<domisilis.size();i++){
            list_desa.add(domisilis.get(i).getNama_desa());
        }

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getApplicationContext(),
                R.layout.spiner_item, list_desa);

        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDesa.setAdapter(spinnerAdapter);
        spinnerDesa.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String seleted = (String) parent.getItemAtPosition(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}
