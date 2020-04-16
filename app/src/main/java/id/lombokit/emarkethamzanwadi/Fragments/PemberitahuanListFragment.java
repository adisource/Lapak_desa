package id.lombokit.emarkethamzanwadi.Fragments;

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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import id.lombokit.emarkethamzanwadi.DetailPageActivity;
import id.lombokit.emarkethamzanwadi.Models.Categories;
import id.lombokit.emarkethamzanwadi.Models.Products;
import id.lombokit.emarkethamzanwadi.R;


public class PemberitahuanListFragment extends Fragment {

    private LinearLayout linear_progressbar;

    private Toolbar toolbar;
    private TextView toolBarTxt;

    private RecyclerView recyclerView;
    private RecycleAdapter_AddProduct mAdapter;
    private int status_code;
    private String token, totalPriceOfProducts;


//    private ProductArrayList productsArrayList;

    private TextView quantityOfTotalProduct, priceOfTotalProduct, next;
    private Categories categories;

    private int[] IMAGES = {R.drawable.circle_blue, R.drawable.circle_green, R.drawable.circleyellowfil};
    private String[] NamES = {"Penyemprotan Massal", "Jumlah PDP Berkurang 5", "Sembako Gratis"};
    private String[] PRICE = {"Agenda yang akan dilaksanakan 11 Maret 2020", "Menurut hasil observasi dan pengecekan RSUD", "PemDes membagikan sembako gratis"};


    private View view;


    Animation startAnimation;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_list_pemberitahuan, container, false);
        startAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.bounce);

        initComponent(view);

        categories = new Categories();
        categories.productsArrayList = new ArrayList<>();


        /*for (int i = 0; i < NamES.length; i++) {
            Products products = new Products();
            products.setName(NamES[i]);
            products.setPrice(PRICE[i]);
            products.setImage(IMAGES[i]);

            categories.productsArrayList.add(products);

        }*/


        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);

        mAdapter = new RecycleAdapter_AddProduct(getActivity(), categories);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

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
            private LinearLayout llMinus, llPlus;
            int quantity;


            public MyViewHolder(View view) {
                super(view);
                image = (ImageView) view.findViewById(R.id.image);
                title = (TextView) view.findViewById(R.id.title);
                price = (TextView) view.findViewById(R.id.price);
            }
        }


        public RecycleAdapter_AddProduct(Context context, Categories categories) {
            this.categories = categories;
            this.context = context;
        }

        @Override
        public RecycleAdapter_AddProduct.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_list_notify, parent, false);

            return new RecycleAdapter_AddProduct.MyViewHolder(itemView);
        }


        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void onBindViewHolder(final RecycleAdapter_AddProduct.MyViewHolder holder, final int position) {
//            Products movie = productsList.get(position);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("ind", "" + position);
                    Intent i = new Intent(getContext(), DetailPageActivity.class);
                    startActivity(i);
                }
            });

            

        }

        @Override
        public int getItemCount() {
            return categories.getProductsArrayList().size();
        }

    }

}

