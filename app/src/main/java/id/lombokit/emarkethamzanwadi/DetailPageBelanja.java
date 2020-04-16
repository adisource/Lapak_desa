package id.lombokit.emarkethamzanwadi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import id.lombokit.emarkethamzanwadi.Fragments.Cart;
import id.lombokit.emarkethamzanwadi.Models.Categories;
import id.lombokit.emarkethamzanwadi.Models.List_pesanan;
import id.lombokit.emarkethamzanwadi.Models.Pesanan;
import id.lombokit.emarkethamzanwadi.Models.Products;
import id.lombokit.emarkethamzanwadi.SessionManager.SessionManager;

public class DetailPageBelanja extends AppCompatActivity {

    private LinearLayout linear_progressbar;

    private Toolbar toolbar;
    private TextView toolBarTxt,btn;

    private RecyclerView recyclerView;
    private RecycleAdapter_AddProduct mAdapter;

    private int status_code;
    private String token,totalPriceOfProducts;


//    private ProductArrayList productsArrayList;

    private TextView quantityOfTotalProduct,priceOfTotalProduct,next;
    private List_pesanan list_pesanan;



    Animation startAnimation,zoomOut, bounceAnimation;

    Runnable rr;
    Handler handler = new Handler();
    String url_pesan ="http://192.168.43.153/e_comm_covid/pesanan.php";
    String id_barang,qty;
    int ongkir=5000;
    String id_user;
    SessionManager sessionManager;
    String currentdate;
    String dateTime;
    String dateTime_todb;
    private  int callulate=0;


    TextView pickup,textViewjam_pesan;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_page_belanja);
        textViewjam_pesan = findViewById(R.id.jam_pesan);
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateNow = new SimpleDateFormat("yyyy/MM/dd h:m:s ");

        dateTime = dateNow.format(calendar.getTime());
        textViewjam_pesan.setText(dateTime);
        sessionManager = new SessionManager(this);

        list_pesanan = new List_pesanan();
        list_pesanan.pesananArrayList = new ArrayList<>();

        recyclerView = findViewById(R.id.list_ulasan);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new RecycleAdapter_AddProduct(getApplicationContext(), list_pesanan);



        loadData();
    }
    private  void  loadData() {
        if (list_pesanan.pesananArrayList != null) {
            list_pesanan.pesananArrayList.clear();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url_pesan, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject data = jsonArray.getJSONObject(i);
                            String id_barang = data.getString("id_barang");
                            String nama_barang = data.getString("nama_barang");
                            String harga = data.getString("harga");
                            String gambar = data.getString("gambar");
                            String toko = data.getString("nama");
                            String stok = data.getString("stok");
                            String qty = data.getString("qty");
                            Pesanan productsm = new Pesanan(
                                    id_barang,
                                    nama_barang,
                                    harga,
                                    gambar,
                                    toko,
                                    stok,
                                    qty
                            );
                            list_pesanan.pesananArrayList.add(productsm);
                            //Toast.makeText(getContext(),""+nama,Toast.LENGTH_SHORT).show();

                        }

                        recyclerView.setAdapter(mAdapter);


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
                    return map;
                }
            };

            Volley.newRequestQueue(this).add(stringRequest);
        }
    }

    public class RecycleAdapter_AddProduct extends RecyclerView.Adapter<RecycleAdapter_AddProduct.MyViewHolder> {

        Context context;
        boolean showingFirst = true;
        private List_pesanan list_pesanan;

        public class MyViewHolder extends RecyclerView.ViewHolder {

            TextView textViewTotalharga,total,textViewOngkir,textViewTotal_pembayaran;

            public MyViewHolder(View view) {
                super(view);
                textViewTotalharga= view.findViewById(R.id.bayar);
                total = view.findViewById(R.id.total_bayar);
                textViewOngkir = findViewById(R.id.ongkir);
                textViewTotal_pembayaran = findViewById(R.id.total_pembayaran);

            }

        }
        public RecycleAdapter_AddProduct(Context context, List_pesanan list_pesanan) {
            this.list_pesanan = list_pesanan;
            this.context = context;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_ulasan, parent, false);

            return new MyViewHolder(itemView);


        }


        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {
            String harga = list_pesanan.getPesananArrayList().get(position).getHarga();
            String qty = list_pesanan.getPesananArrayList().get(position).getQty();
            int c_h = Integer.parseInt(harga);
            int c_qty=Integer.parseInt(qty);
            int hasil = c_h*c_qty;
            holder.textViewTotalharga.setText("Rp."+harga+" x "+ qty + " = Rp."+hasil);
            holder.total.setText(""+hasil);
            int ongkir = 5000;
            holder.textViewOngkir.setText(""+ongkir);
            callulate = callulate+hasil+ongkir;
            if (position==list_pesanan.getPesananArrayList().size()-1){
                holder.textViewTotal_pembayaran.setText(""+callulate);
            }

            String get_data = list_pesanan.getPesananArrayList().get(position).getId_barang();


        }

        @Override
        public int getItemCount() {
            return list_pesanan.pesananArrayList.size();
        }

    }

}

