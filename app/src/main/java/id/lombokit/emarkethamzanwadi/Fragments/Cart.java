package id.lombokit.emarkethamzanwadi.Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import id.lombokit.emarkethamzanwadi.DetailPageActivity;
import id.lombokit.emarkethamzanwadi.DetailPageBelanja;
import id.lombokit.emarkethamzanwadi.Models.List_pesanan;
import id.lombokit.emarkethamzanwadi.Models.Pesanan;

import id.lombokit.emarkethamzanwadi.Proses_checout;
import id.lombokit.emarkethamzanwadi.R;
import id.lombokit.emarkethamzanwadi.SessionManager.SessionManager;


public class Cart extends Fragment  {
    private LinearLayout linear_progressbar;

    private Toolbar toolbar;
    private TextView toolBarTxt;
    SwipeRefreshLayout swipeRefreshLayout;

    private RecyclerView recyclerView;
    private RecycleAdapter_AddProduct mAdapter;
    private int status_code;
    private String token, totalPriceOfProducts;
    String url = "http://192.168.43.153/e_comm_covid/pesanan.php";
    String url_update = "http://192.168.43.153/e_comm_covid/update_cart.php";
    String url_delete = "http://192.168.43.153/e_comm_covid/delete_cart.php";
    private TextView quantityOfTotalProduct, priceOfTotalProduct, next;
    private List_pesanan list_pesanan;
    private  int callulate=0;
    SessionManager sessionManager;
    TextView textViewTotal;
    TextView textViewCount_cart;
    TextView textViewTotal_cart;
    CoordinatorLayout coordinatorLayout;




    private View view;


    Animation startAnimation;


    @SuppressLint("ResourceAsColor")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        startAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.bounce);

        initComponent(view);
        sessionManager = new SessionManager(getContext());


        swipeRefreshLayout = view.findViewById(R.id.swipe);
        swipeRefreshLayout.setRefreshing(false);
        //swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeColors(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);

        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                loadData();

            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
            }
        });


        list_pesanan = new List_pesanan();
        list_pesanan.pesananArrayList = new ArrayList<>();



        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new RecycleAdapter_AddProduct(getActivity(), list_pesanan);
        textViewCount_cart = getActivity().findViewById(R.id.count_cart);


        return view;
    }
    private void initComponent(View view) {

    }

    private  void  loadData() {
        if (list_pesanan.pesananArrayList != null) {
            list_pesanan.pesananArrayList.clear();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
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

            Volley.newRequestQueue(getContext()).add(stringRequest);
        }
    }


    public class RecycleAdapter_AddProduct extends RecyclerView.Adapter<RecycleAdapter_AddProduct.MyViewHolder> {

        Context context;
        boolean showingFirst = true;
        private List_pesanan list_pesanan;


        public class MyViewHolder extends RecyclerView.ViewHolder {


            ImageView image;
            TextView title;
            TextView price;
            TextView quantityTxt;
            TextView textViewtoko;
            ImageView clik_add;
            ImageView delete_cart;
            private LinearLayout llMinus, llPlus;
            int quantity;
            Button btn_update,btn_beli;



            public MyViewHolder(View view) {
                super(view);

                delete_cart = view.findViewById(R.id.delete);
                image = (ImageView) view.findViewById(R.id.image);
                clik_add = view.findViewById(R.id.add_cart);
                title = (TextView) view.findViewById(R.id.title);
                price = (TextView) view.findViewById(R.id.price);
                quantityTxt = (TextView) view.findViewById(R.id.quantityTxt);
                textViewtoko = view.findViewById(R.id.toko);
                llPlus = (LinearLayout) view.findViewById(R.id.llPlus);
                llMinus = (LinearLayout) view.findViewById(R.id.llMinus);
                btn_update = getActivity().findViewById(R.id.update);
                btn_beli = getActivity().findViewById(R.id.beli);
            }
        }


        public RecycleAdapter_AddProduct(Context context, List_pesanan list_pesanan) {
            this .list_pesanan = list_pesanan;
            this.context = context;
        }

        @Override
        public RecycleAdapter_AddProduct.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_cart, parent, false);

            return new RecycleAdapter_AddProduct.MyViewHolder(itemView);
        }


        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void onBindViewHolder(final RecycleAdapter_AddProduct.MyViewHolder holder, final int position) {
//            Products movie = productsList.get(position);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(getContext(), ""+categories.getProductsArrayList().get(position).getId_barang(), Toast.LENGTH_SHORT).show();
                }
            });

            holder.title.setText(list_pesanan.getPesananArrayList().get(position).getNama_barang());
            holder.price.setText(list_pesanan.getPesananArrayList().get(position).getHarga());
            holder.textViewtoko.setText(list_pesanan.getPesananArrayList().get(position).getNama());
            Glide.with(getContext()).load("http://192.168.43.153/e_comm_covid/assets/img/"+ list_pesanan.getPesananArrayList().get(position).getGambar()).into(holder.image);

            holder.quantityTxt.setText(list_pesanan.getPesananArrayList().get(position).getQty());

            String harga = list_pesanan.getPesananArrayList().get(position).getHarga();
            String qty = list_pesanan.getPesananArrayList().get(position).getQty();
            int c_h = Integer.parseInt(harga);
            int c_q= Integer.parseInt(qty);

            int hasil_kali = c_h*c_q;
            callulate = callulate+hasil_kali;

            if (position==list_pesanan.getPesananArrayList().size()-1){
                textViewTotal_cart = getActivity().findViewById(R.id.total_bayar);
                textViewTotal_cart.setText(""+callulate);
            }



            holder.llPlus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String stok_barang = list_pesanan.getPesananArrayList().get(position).getStok();
                    int covert_stok = Integer.parseInt(stok_barang);
                    String s_qty = holder.quantityTxt.getText().toString();
                    int c_qty = Integer.parseInt(s_qty);

                    if (holder.quantity < covert_stok) {
                        holder.quantity = 1 + c_qty;
                        holder.quantityTxt.setText("" + holder.quantity);
                        String qty=holder.quantityTxt.getText().toString();
                        String id_barang = list_pesanan.getPesananArrayList().get(position).getId_barang();

                        update_qty(qty,id_barang);
                        //Toast.makeText(getContext(),""+id_barang,Toast.LENGTH_LONG).show();

                    }

                }
            });
            holder.llMinus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String stok_barang = list_pesanan.getPesananArrayList().get(position).getStok();
                    int covert_stok = Integer.parseInt(stok_barang);
                    String s_qty = holder.quantityTxt.getText().toString();
                    int c_qty = Integer.parseInt(s_qty);

                    if (c_qty > 0 && c_qty <= covert_stok) {
                        holder.quantity = c_qty-1;
                        holder.quantityTxt.setText("" + holder.quantity);

                        String qty=holder.quantityTxt.getText().toString();
                        String id_barang = list_pesanan.getPesananArrayList().get(position).getId_barang();
                        update_qty(qty,id_barang);



                    }


                }
            });
            holder.btn_update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().finish();
                    getActivity().overridePendingTransition(0, 0);
                    getActivity().startActivity(getActivity().getIntent());
                    getActivity().overridePendingTransition(0, 0);
                }
            });
            holder.delete_cart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    delete_cart(position);
                }
            });
            holder.btn_beli.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), DetailPageBelanja.class);
                    startActivity(intent);
                }
            });





        }

        @Override
        public int getItemCount() {
            return list_pesanan.getPesananArrayList().size();
        }




    }

    private void delete_cart(int position) {
        final String id_barang = list_pesanan.getPesananArrayList().get(position).getId_barang();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url_delete, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.contains("success")){
                    getActivity().finish();
                    getActivity().overridePendingTransition(0, 0);
                    getActivity().startActivity(getActivity().getIntent());
                    getActivity().overridePendingTransition(0, 0);
                }else{
                    Toast.makeText(getContext(),"Gagal",Toast.LENGTH_LONG).show();
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
                map.put("id_barang",id_barang);
                map.put("id_user",sessionManager.getSpIduser());
                return map;
            }
        };
        Volley.newRequestQueue(getContext()).add(stringRequest);

    }

    private void update_qty(final String qty, final String id_barang) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url_update, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.contains("success")){

                }else{
                    Toast.makeText(getContext(),"Gagal",Toast.LENGTH_LONG).show();
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
                map.put("qty",qty);
                map.put("id_barang",id_barang);
                return map;
            }
        };
        Volley.newRequestQueue(getContext()).add(stringRequest);

    }

}
