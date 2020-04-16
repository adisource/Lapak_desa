package id.lombokit.emarkethamzanwadi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
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
import com.google.android.material.tabs.TabLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import id.lombokit.emarkethamzanwadi.Adapters.ProductsPagerAdapter;
import id.lombokit.emarkethamzanwadi.Adapters.TimingsAdapter;
import id.lombokit.emarkethamzanwadi.Fragments.ObatanListFragment;
import id.lombokit.emarkethamzanwadi.Fragments.SembakoListFragment;
import id.lombokit.emarkethamzanwadi.SessionManager.SessionManager;

public class ListSembakoActivity extends AppCompatActivity {

    private ProductsPagerAdapter productsPagerAdapter;
    private TabLayout customTab;
    private ViewPager pager;

    Typeface mTypeface;
    ImageView kembali, pemberitahuan;
    TextView judul;
    RelativeLayout layout_cart;


    private Dialog bottomDialog;
    private TextView tvToday,tvTomorrow;
    private LinearLayout linearDialog;
    private FrameLayout frameOk;
    private RecyclerView rvTimings;
    private TimingsAdapter timingsAdapter;
    TextView textViewCount_cart;
    String url = "http://192.168.43.153/e_comm_covid/cart_count.php";
    SessionManager sessionManager;

    private String[] NAMES_TIMMING = {"9 TO 10 am","10 to 11 am","11 to 12 am","12 to 1 pm","1 to 2 pm","2 to 3 pm","3 to 4 pm"};
    private ArrayList<String> stringArrayList = new ArrayList<>();
    private ImageView btn;

    Animation slideUpAnimation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_sembako);

        kembali = findViewById(R.id.back);
        judul = findViewById(R.id.title);
        pemberitahuan = findViewById(R.id.notif);
        layout_cart  = findViewById(R.id.layout_cart);
        textViewCount_cart = findViewById(R.id.count_cart);
        sessionManager = new SessionManager(this);
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

        judul.setText((CharSequence) "Sembako");


        slideUpAnimation= AnimationUtils.loadAnimation(ListSembakoActivity.this, R.anim.slide_up_linear);

        for (int i=0; i<NAMES_TIMMING.length ; i++){

            stringArrayList.add(NAMES_TIMMING[i]);
        }


        bottomDialog  = new Dialog(ListSembakoActivity.this,R.style.BottomDialog);
        bottomDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        bottomDialog.getWindow().getAttributes().windowAnimations = R.style.CustomDialogAnimation;
        layoutParams.copyFrom(bottomDialog.getWindow().getAttributes());
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.gravity = Gravity.BOTTOM;
        bottomDialog.getWindow().setAttributes(layoutParams);
        bottomDialog.setCancelable(true);
        bottomDialog.setContentView(R.layout.dilaog_bottom);
        tvToday = bottomDialog.findViewById(R.id.tvToday);
        tvTomorrow= bottomDialog.findViewById(R.id.tvTomorrow);
        frameOk= (FrameLayout) bottomDialog.findViewById(R.id.frameOk);
        linearDialog= (LinearLayout) bottomDialog.findViewById(R.id.linearDialog);
        rvTimings = (RecyclerView) bottomDialog.findViewById(R.id.rvTimings);
        RecyclerView.LayoutManager mLayoutManagerHappyHours = new LinearLayoutManager(ListSembakoActivity.this,
                LinearLayoutManager.HORIZONTAL, false);
        rvTimings.setLayoutManager(mLayoutManagerHappyHours);
        rvTimings.setNestedScrollingEnabled(false);
        rvTimings.setItemAnimator(new DefaultItemAnimator());
        rvTimings.setHasFixedSize(false);

        timingsAdapter = new TimingsAdapter(ListSembakoActivity.this, stringArrayList);
        rvTimings.setAdapter(timingsAdapter);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String count = jsonObject.getString("count");
                    textViewCount_cart.setText(count);

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



        frameOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                bottomDialog.dismiss();
                Intent it = new Intent(ListSembakoActivity.this, MainActivity.class);
                startActivity(it);
            }
        });


        tvToday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvToday.setBackgroundResource(R.drawable.round_button_dark);
                tvToday.setTextColor(Color.parseColor("#ffffff"));
                tvTomorrow.setBackgroundResource(R.drawable.round_button_light);
                tvTomorrow.setTextColor(Color.parseColor("#28a7fc"));


            }
        });


        tvTomorrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                tvTomorrow.setBackgroundResource(R.drawable.round_button_dark);
                tvTomorrow.setTextColor(Color.parseColor("#ffffff"));
                tvToday.setBackgroundResource(R.drawable.round_button_light);
                tvToday.setTextColor(Color.parseColor("#28a7fc"));

            }
        });



        pager = (ViewPager)findViewById(R.id.pager);
        customTab=  findViewById(R.id.customTab);


        customTab.addTab(customTab.newTab().setText("MAKANAN"));
        customTab.addTab(customTab.newTab().setText("MINUMAN"));
        customTab.addTab(customTab.newTab().setText("KESEHATAN"));

        mTypeface = Typeface.createFromAsset(this.getAssets(), "roboto_regular.ttf");
        ViewGroup vg = (ViewGroup) customTab.getChildAt(0);
        int tabsCount = vg.getChildCount();
        for (int j = 0; j < tabsCount; j++) {
            ViewGroup vgTab = (ViewGroup) vg.getChildAt(j);
            int tabChildsCount = vgTab.getChildCount();
            for (int i = 0; i < tabChildsCount; i++) {
                View tabViewChild = vgTab.getChildAt(i);
                if (tabViewChild instanceof TextView) {
                    ((TextView) tabViewChild).setTypeface(mTypeface, Typeface.NORMAL);
                }
            }
        }



        productsPagerAdapter = new ProductsPagerAdapter
                (getSupportFragmentManager(), customTab.getTabCount());
        pager.setAdapter(productsPagerAdapter);
        pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(customTab));
        pager.setOffscreenPageLimit(customTab.getTabCount());

        customTab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                pager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

    }

    void addPager() {
        SembakoListFragment sembakoListFragment = new SembakoListFragment();
        ObatanListFragment obatanListFragment = new ObatanListFragment();

        productsPagerAdapter.addFragment(sembakoListFragment, "SEMBAKO");
        productsPagerAdapter.addFragment(obatanListFragment, "OBAT");
    }
}
