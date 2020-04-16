package id.lombokit.emarkethamzanwadi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
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

import id.lombokit.emarkethamzanwadi.Models.Categories;
import id.lombokit.emarkethamzanwadi.Models.Products;
import id.lombokit.emarkethamzanwadi.SessionManager.SessionManager;

public class DetailPageActivity extends AppCompatActivity {

    private LinearLayout linear_progressbar;

    private Toolbar toolbar;
    private TextView toolBarTxt,btn;

    private RecyclerView recyclerView;

    private int status_code;
    private String token,totalPriceOfProducts;


//    private ProductArrayList productsArrayList;

    private TextView quantityOfTotalProduct,priceOfTotalProduct,next;
    private Categories categories;



    Animation startAnimation,zoomOut, bounceAnimation;

    Runnable rr;
    Handler handler = new Handler();
    String url = "http://192.168.43.153/e_comm_covid/view_product.php";
    String url_pesan ="http://192.168.43.153/e_comm_covid/belanja_user.php";
    String id_barang,qty;
    int ongkir=5000;
    String id_user;
    SessionManager sessionManager;
    String currentdate;
    String dateTime;
    String dateTime_todb;


    TextView pickup,textViewjam_pesan,textViewbayar,textViewjumlahbarang,textViewTotal_bayar,textViewongkir;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_billing_details);

        pickup = findViewById(R.id.btn);
        textViewjam_pesan = findViewById(R.id.jam_pesan);
        textViewbayar = findViewById(R.id.bayar);
        textViewTotal_bayar = findViewById(R.id.total_bayar);
        textViewongkir = findViewById(R.id.ongkir);
        sessionManager = new SessionManager(this);

        id_barang = getIntent().getStringExtra("id_barang");
        qty = getIntent().getStringExtra("qty");

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateNow = new SimpleDateFormat("yyyy/MM/dd h:m:s ");
        dateTime = dateNow.format(calendar.getTime());

        SimpleDateFormat tgl = new SimpleDateFormat("y-d-m");
        currentdate = tgl.format(new Date());

        viewDatapesan();
        textViewjam_pesan.setText(dateTime);
        textViewongkir.setText("Rp."+ongkir);
        pickup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prosesPembelian();
            }
        });



    }



    private void viewDatapesan() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i =0;i<jsonArray.length();i++){
                        JSONObject data = jsonArray.getJSONObject(i);
                        String harga_barang = data.getString("harga");
                        int c_h= Integer.parseInt(harga_barang);
                        int c_qty = Integer.parseInt(qty);
                        int hasil = c_h*c_qty;
                        int o_hasil = hasil+ongkir;
                        textViewbayar.setText("Rp."+harga_barang+" x "+qty+ " = Rp."+hasil);
                        textViewTotal_bayar.setText(""+o_hasil);


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
                Map<String,String>map = new HashMap<>();
                map.put("id_barang",id_barang);
                return map;
            }
        };
        Volley.newRequestQueue(getApplicationContext()).add(stringRequest);
    }

    private void prosesPembelian() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat tgl = new SimpleDateFormat("yyyyMMdd");
        String kd= tgl.format(calendar.getTime());
        final String kode_order = "KB-"+kd+"-"+id_barang;

        SimpleDateFormat currenttime = new SimpleDateFormat("yyyy/MM/dd h:m:s");
        dateTime_todb = currenttime.format(calendar.getTime());



        StringRequest stringRequest = new StringRequest(Request.Method.POST, url_pesan, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.contains("success")){
                    Intent intent = new Intent(getApplicationContext(),DoneActivity.class);
                    intent.putExtra("kode_order",kode_order);
                    intent.putExtra("total_bayar",textViewTotal_bayar.getText().toString());
                    startActivity(intent);
                }else {
                    Toast.makeText(getApplicationContext(),"Anda Gagal melakukan pemesanan",Toast.LENGTH_SHORT).show();
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
                //map.put("tgl",currentDateandTime);
                map.put("jam",textViewjam_pesan.getText().toString());
                map.put("qty",qty);
                map.put("total",textViewTotal_bayar.getText().toString());
                map.put("id_user",sessionManager.getSpIduser());
                map.put("id_barang",id_barang);
                map.put("kode_order",kode_order);

                return map;
            }
        };
        Volley.newRequestQueue(this).add(stringRequest);




    }


    /*public class RecycleAdapter_AddProduct extends RecyclerView.Adapter<RecycleAdapter_AddProduct.MyViewHolder> {

        Context context;
        boolean showingFirst = true;
        private Categories categories;

        public class MyViewHolder extends RecyclerView.ViewHolder {


            ImageView image;
            TextView title;
            TextView price;
            TextView quantityTxt;
            private LinearLayout llMinus,llPlus;
            int quantity;


            public MyViewHolder(View view) {
                super(view);

                image = (ImageView) view.findViewById(R.id.image);
                title = (TextView) view.findViewById(R.id.title);
                price = (TextView) view.findViewById(R.id.price);
                quantityTxt = (TextView) view.findViewById(R.id.quantityTxt);
                llPlus = (LinearLayout)view.findViewById(R.id.llPlus);
                llMinus = (LinearLayout)view.findViewById(R.id.llMinus);
            }

        }



        public RecycleAdapter_AddProduct(Context context, Categories categories) {
            this.categories = categories;
            this.context = context;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_add_product, parent, false);



            return new MyViewHolder(itemView);


        }


        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {
//            Products movie = productsList.get(position);

            /*holder.title.setText(categories.getProductsArrayList().get(position).getName());
            holder.price.setText(categories.getProductsArrayList().get(position).getPrice());
            holder.quantityTxt.setText(categories.getProductsArrayList().get(position).getQuantity() + "");


              holder.quantity = categories.getProductsArrayList().get(position).getQuantity();
            int totalPrice = holder.quantity * Integer.parseInt(categories.getProductsArrayList().get(position).getPrice());


            if (categories.getProductsArrayList().get(position).getQuantity() > 0){
                holder.quantityTxt.setVisibility(View.VISIBLE);
                holder.llMinus.setVisibility(View.VISIBLE);
            }else {
                holder.quantityTxt.setVisibility(View.GONE);
                holder.llMinus.setVisibility(View.GONE);
            }


            categories.getProductsArrayList().get(position).setPriceAsPerQuantity(""+ totalPrice);


            holder.llPlus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (holder.quantity <10){

                        holder.quantity = holder.quantity + 1;
                        categories.getProductsArrayList().get(position).setQuantity(holder.quantity);
                        categories.getProductsArrayList().get(position).setPriceAsPerQuantity(""+holder.quantity * Integer.parseInt(categories.getProductsArrayList().get(position).getPrice()));

                        holder.quantityTxt.setText("" + holder.quantity);
                    }


                    notifyDataSetChanged();

                }
            });


            holder.llMinus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (holder.quantity > 0 && holder.quantity <= 10){

                        holder.quantity = holder.quantity - 1;
                        categories.getProductsArrayList().get(position).setQuantity(holder.quantity);
                        categories.getProductsArrayList().get(position).setPriceAsPerQuantity(""+holder.quantity * Integer.parseInt(categories.getProductsArrayList().get(position).getPrice()));

                        holder.quantityTxt.setText("" + holder.quantity);


                    }

                    notifyDataSetChanged();

                }
            });



        }

        @Override
        public int getItemCount() {
            return categories.getProductsArrayList().size();
        }

    }*/

}

