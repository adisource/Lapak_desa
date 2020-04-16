package id.lombokit.emarkethamzanwadi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import id.lombokit.emarkethamzanwadi.SessionManager.SessionManager;

public class Detail_product extends AppCompatActivity {

    TextView textViewNama_barang,textViewHarga,textViewtoko,textViewstok,textViewTotalharga;
    ImageView gambar_produk;
    String nama_barang,harga,toko,stok,gambar,id_barang;
    ImageView kembali, pemberitahuan;
    TextView judul;
    RelativeLayout layout_cart;
    private LinearLayout llMinus, llPlus;
    TextView quantityTxt;
    int quantity=1;
    Button add_cart;
    String url_savetoCart="http://192.168.43.153/e_comm_covid/save_toCart.php";
    SessionManager sessionManager;
    Calendar calendar;
    int c_stok;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_product);
        kembali = findViewById(R.id.back);
        judul = findViewById(R.id.title);
        pemberitahuan = findViewById(R.id.notif);
        layout_cart  = findViewById(R.id.layout_cart);
        add_cart = findViewById(R.id.add_cart);

        layout_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Shopping_cart.class));
            }
        });

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
        judul.setText("Detail Order");

        textViewNama_barang = findViewById(R.id.nama_barang);
        textViewHarga = findViewById(R.id.harga);
        textViewtoko = findViewById(R.id.toko);
        textViewstok = findViewById(R.id.v_stok);
        llPlus = findViewById(R.id.llPlus);
        llMinus =findViewById(R.id.llMinus);
        quantityTxt = findViewById(R.id.quantityTxt);
        textViewTotalharga = findViewById(R.id.value);
        sessionManager = new SessionManager(this);

        gambar_produk = findViewById(R.id.gambar_produk);
        id_barang = getIntent().getStringExtra("id_barang");
        nama_barang = getIntent().getStringExtra("nama_barang");
        harga = getIntent().getStringExtra("harga");
        toko = getIntent().getStringExtra("toko");
        stok = getIntent().getStringExtra("stok");
        gambar= getIntent().getStringExtra("gambar");

        textViewNama_barang.setText(nama_barang);
        textViewHarga.setText(harga);
        textViewtoko.setText("Dari toko "+toko);
        textViewstok.setText(stok);
        Glide.with(getApplicationContext()).load("http://192.168.43.153/e_comm_covid/assets/img/"+gambar).into(gambar_produk);

        String q = quantityTxt.getText().toString();
        int c_q = Integer.parseInt(q);
        int c_harga = Integer.parseInt(harga);
        final int total_h = c_q*c_harga;
        String s_total = String.valueOf(total_h);
        textViewTotalharga.setText(s_total);
        c_stok=Integer.parseInt(stok);

        llPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (quantity < c_stok){
                    quantity = quantity+1;
                    quantityTxt.setText(""+quantity);
                    int h = total_h*quantity;
                    String s_total = String.valueOf(h);
                    textViewTotalharga.setText(s_total);


                }

            }
        });
        llMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int c_stok=Integer.parseInt(stok);
                if (quantity > 1){
                    quantity = quantity-1;
                    quantityTxt.setText(""+quantity);
                    int h = total_h*quantity;
                    String s_total = String.valueOf(h);
                    textViewTotalharga.setText(s_total);

                }
            }
        });
        add_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(0, 0);
                startActivity(getIntent());
                overridePendingTransition(0, 0);
                if (c_stok !=0){
                    save_tocart();
                }else{
                    Toast.makeText(getApplicationContext(),"Stok barang habis",Toast.LENGTH_SHORT).show();
                }




            }
        });




    }

    private void save_tocart() {
        calendar = Calendar.getInstance();
        SimpleDateFormat tgl = new SimpleDateFormat("yyyyMMdd");
        String kd= tgl.format(calendar.getTime());
        final String kode_order = "KC-"+kd+"-"+sessionManager.getSpIduser();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url_savetoCart, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String cek = jsonObject.getString("cek");
                    if (cek.equals("exsits")){
                        Toast.makeText(getApplicationContext(),"sudah ditambah di cart",Toast.LENGTH_SHORT).show();
                    }else if(cek.equals("success")){
                        startActivity(new Intent(getApplicationContext(),ListSembakoActivity.class));

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<>();
                map.put("id_user",sessionManager.getSpIduser());
                map.put("id_barang",id_barang);
                map.put("qty",quantityTxt.getText().toString());
                map.put("kode_order",kode_order);
                return map;
            }
        };
        Volley.newRequestQueue(getApplicationContext()).add(stringRequest);

    }


}
