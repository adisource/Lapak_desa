package id.lombokit.emarkethamzanwadi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

public class Proses_checout extends AppCompatActivity {

    TextView textViewNama_barang,textViewHarga,textViewtoko,textViewstok,textViewTotalharga;
    ImageView gambar_produk;
    String nama_barang,harga,toko,stok,gambar,idbarang;
    ImageView kembali, pemberitahuan;
    TextView judul;
    Button btn_proses;
    RelativeLayout layout_cart;
    private LinearLayout llMinus, llPlus;
    TextView quantityTxt;
    int quantity=1;
    int c_stok;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proses_checout);
        kembali = findViewById(R.id.back);
        judul = findViewById(R.id.title);
        pemberitahuan = findViewById(R.id.notif);
        layout_cart  = findViewById(R.id.layout_cart);
        btn_proses = findViewById(R.id.proses);
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

        gambar_produk = findViewById(R.id.gambar_produk);
        idbarang = getIntent().getStringExtra("id_barang");
        nama_barang = getIntent().getStringExtra("nama_barang");
        harga = getIntent().getStringExtra("harga");
        toko = getIntent().getStringExtra("toko");
        stok = getIntent().getStringExtra("stok");
        gambar= getIntent().getStringExtra("gambar");
        c_stok = Integer.parseInt(stok);

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

            btn_proses.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (c_stok !=0 ) {
                        Intent intent = new Intent(getApplicationContext(), DetailPageActivity.class);
                        intent.putExtra("id_barang", idbarang);
                        intent.putExtra("qty", quantityTxt.getText().toString());
                        startActivity(intent);

                    }else {
                        Toast.makeText(getApplicationContext(), "Stok habis", Toast.LENGTH_SHORT).show();
                    }
                }
            });





    }
}
