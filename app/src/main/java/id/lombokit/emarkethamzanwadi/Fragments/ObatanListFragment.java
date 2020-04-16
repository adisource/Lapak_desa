package id.lombokit.emarkethamzanwadi.Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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
import id.lombokit.emarkethamzanwadi.Detail_product;
import id.lombokit.emarkethamzanwadi.Models.Categories;
import id.lombokit.emarkethamzanwadi.Models.Products;
import id.lombokit.emarkethamzanwadi.Proses_checout;
import id.lombokit.emarkethamzanwadi.R;
import id.lombokit.emarkethamzanwadi.SessionManager.SessionManager;


public class ObatanListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private LinearLayout linear_progressbar;

    private Toolbar toolbar;
    private TextView toolBarTxt;

    private RecyclerView recyclerView;
    private RecycleAdapter_AddProduct mAdapter;
    private int status_code;
    private String token, totalPriceOfProducts;
    String url_nama = "http://192.168.43.153/e_comm_covid/List_obatan.php";
    SwipeRefreshLayout swipeRefreshLayout;
    SessionManager sessionManager;


//    private ProductArrayList productsArrayList;

    private TextView quantityOfTotalProduct, priceOfTotalProduct, next;
    private Categories categories;

    //private int[] IMAGES = {R.drawable.paramex, R.drawable.antalgin, R.drawable.paracetamol, R.drawable.amoxilin, R.drawable.vitacimin};
    //private String[] NamES = {"Paramex", "Antalgin", "Paracetamol", "Amoxilin", "Vitacimin"};
    //private String[] PRICE = {"2000", "4000", "6000", "8000", "6000"};


    private View view;


    Animation startAnimation;

    @SuppressLint("ResourceAsColor")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_list_obat, container, false);
        startAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.bounce);

        initComponent(view);
        sessionManager = new SessionManager(getContext());
        swipeRefreshLayout = view.findViewById(R.id.swipe);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeColors(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);

        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                if (swipeRefreshLayout != null){
                    swipeRefreshLayout.setRefreshing(true);
                }
                loadData();
            }
        });

        categories = new Categories();
        categories.productsArrayList = new ArrayList<>();


        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);



        mAdapter = new RecycleAdapter_AddProduct(getActivity(), categories);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        recyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), DetailPageActivity.class);
                startActivity(i);
            }
        });


        return view;

    }

    private void initComponent(View view) {


    }

    @Override
    public void onRefresh() {
        loadData();
    }
    private void  loadData(){
        if (categories.productsArrayList !=null){
            categories.productsArrayList.clear();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url_nama, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i = 0;i<jsonArray.length();i++){
                            JSONObject data = jsonArray.getJSONObject(i);
                            String id = data.getString("id_barang");
                            String nama_barang = data.getString("nama_barang");
                            String harga = data.getString("harga");
                            String gambar = data.getString("gambar");
                            //String ket = data.getString("keterangan");
                            String stok = data.getString("stok");
                            //String status = data.getString("status");
                            String toko = data.getString("nama");
                            Products productsm = new Products(
                                    id,
                                    nama_barang,
                                    harga,
                                    gambar,
                                    toko,
                                    stok
                            );
                            categories.productsArrayList.add(productsm);
                            //Toast.makeText(getContext(),""+nama,Toast.LENGTH_SHORT).show();
                        }
                        recyclerView.setAdapter(mAdapter);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    swipeRefreshLayout.setRefreshing(false);

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> map = new HashMap<>();
                    map.put("desa", sessionManager.getSpNamadesa());
                    return map;
                }


            };

            Volley.newRequestQueue(getContext()).add(stringRequest);
            swipeRefreshLayout.setRefreshing(false);


        }
    }

    public class RecycleAdapter_AddProduct extends RecyclerView.Adapter<RecycleAdapter_AddProduct.MyViewHolder> {

        Context context;
        boolean showingFirst = true;
        private Categories categories;
        int recentPos = -1;

        public class MyViewHolder extends RecyclerView.ViewHolder {

            ImageView image;
            TextView title;
            TextView price;
            TextView quantityTxt;
            TextView toko;
            ImageView clik_add;
            Button btn_beli;
            private LinearLayout llMinus, llPlus;
            int quantity;


            public MyViewHolder(View view) {
                super(view);
                btn_beli = view.findViewById(R.id.beli);
                clik_add = view.findViewById(R.id.add_cart);
                image = (ImageView) view.findViewById(R.id.image);
                title = (TextView) view.findViewById(R.id.title);
                price = (TextView) view.findViewById(R.id.price);
                toko = view.findViewById(R.id.toko);
                quantityTxt = (TextView) view.findViewById(R.id.quantityTxt);
                llPlus = (LinearLayout) view.findViewById(R.id.llPlus);
                llMinus = (LinearLayout) view.findViewById(R.id.llMinus);
            }
        }


        public RecycleAdapter_AddProduct(Context context, Categories categories) {
            this.categories = categories;
            this.context = context;
        }

        @Override
        public RecycleAdapter_AddProduct.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_add_product, parent, false);

            return new RecycleAdapter_AddProduct.MyViewHolder(itemView);
        }


        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void onBindViewHolder(final RecycleAdapter_AddProduct.MyViewHolder holder, final int position) {
//            Products movie = productsList.get(position);


            holder.title.setText(categories.getProductsArrayList().get(position).getNama_barang());
            holder.price.setText(categories.getProductsArrayList().get(position).getHarga());
            //holder.quantityTxt.setText(categories.getProductsArrayList().get(position).getQuantity() + "");
            holder.toko.setText(categories.getProductsArrayList().get(position).getNama());
            Glide.with(getContext()).load("http://192.168.43.153/e_comm_covid/assets/img/"+categories.getProductsArrayList().get(position).getGambar()).into(holder.image);

            /*if (position == recentPos) {
                Log.e("pos", "" + recentPos);
                // start animation
                holder.quantityTxt.startAnimation(startAnimation);
            } else {
                holder.quantityTxt.clearAnimation();

            }

            if (holder.quantity > 0) {
                holder.quantityTxt.setVisibility(View.VISIBLE);
                holder.llMinus.setVisibility(View.VISIBLE);
            } else {
                holder.quantityTxt.setVisibility(View.GONE);
                holder.llMinus.setVisibility(View.GONE);
            }


            categories.getProductsArrayList().get(position).setPriceAsPerQuantity("" + totalPrice);


            holder.llPlus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String stok_barang = categories.getProductsArrayList().get(position).getStok();
                    int covert_stok = Integer.parseInt(stok_barang);

                    if (holder.quantity < covert_stok) {

                        recentPos = position;
                        holder.quantity = holder.quantity + 1;
                        //categories.getProductsArrayList().get(position).setQuantity(holder.quantity);
                        //categories.getProductsArrayList().get(position).setPriceAsPerQuantity("" + holder.quantity * Integer.parseInt(categories.getProductsArrayList().get(position).getPrice()));
                        holder.quantityTxt.setText("" + holder.quantity);
                        /*String qty=holder.quantityTxt.getText().toString();
                        String hrg = categories.getProductsArrayList().get(position).getHarga();
                        int c_qty = Integer.parseInt(qty);
                        int c_hrg = Integer.parseInt(hrg);
                        int total_belanja =c_qty*c_hrg;
                        String s_total = String.valueOf(total_belanja);
                        textViewTotal = getActivity().findViewById(R.id.value);
                        textViewTotal.setText(s_total);
                    }
                    notifyDataSetChanged();


                }
            });
            holder.llMinus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String stok_barang = categories.getProductsArrayList().get(position).getStok();
                    int covert_stok = Integer.parseInt(stok_barang);
                    if (holder.quantity > 0 && holder.quantity <=covert_stok) {

                        recentPos = position;

                        holder.quantity = holder.quantity - 1;
                        //categories.getProductsArrayList().get(position).setQuantity(holder.quantity);
                        //categories.getProductsArrayList().get(position).setPriceAsPerQuantity("" + holder.quantity * Integer.parseInt(categories.getProductsArrayList().get(position).getPrice()));

                        holder.quantityTxt.setText("" + holder.quantity);



                    }

                    notifyDataSetChanged();

                }
            });*/
            holder.clik_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), Detail_product.class);
                    intent.putExtra("id_barang",categories.getProductsArrayList().get(position).getId_barang());
                    intent.putExtra("nama_barang",categories.getProductsArrayList().get(position).getNama_barang());
                    intent.putExtra("gambar",categories.getProductsArrayList().get(position).getGambar());
                    intent.putExtra("harga",categories.getProductsArrayList().get(position).getHarga());
                    intent.putExtra("stok",categories.getProductsArrayList().get(position).getStok());
                    intent.putExtra("toko",categories.getProductsArrayList().get(position).getNama());
                    startActivity(intent);
                }
            });
            holder.btn_beli.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), Proses_checout.class);
                    intent.putExtra("id_barang",categories.getProductsArrayList().get(position).getId_barang());
                    intent.putExtra("nama_barang",categories.getProductsArrayList().get(position).getNama_barang());
                    intent.putExtra("gambar",categories.getProductsArrayList().get(position).getGambar());
                    intent.putExtra("harga",categories.getProductsArrayList().get(position).getHarga());
                    intent.putExtra("stok",categories.getProductsArrayList().get(position).getStok());
                    intent.putExtra("toko",categories.getProductsArrayList().get(position).getNama());
                    startActivity(intent);

                }
            });


        }

        @Override
        public int getItemCount() {
            return categories.getProductsArrayList().size();
        }

    }

}

