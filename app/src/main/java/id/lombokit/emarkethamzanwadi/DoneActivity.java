package id.lombokit.emarkethamzanwadi;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;



public class DoneActivity extends Activity {
    private TextView next;
    String latestVersion;
    TextView textViewbayar,textViewKd_order;
    String bayar,kode_order;
    TextView btn_selesai;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_done);
        bayar = getIntent().getStringExtra("total_bayar");
        kode_order = getIntent().getStringExtra("kode_order");
        textViewbayar = findViewById(R.id.bayar);
        textViewKd_order = findViewById(R.id.kd_order);
        btn_selesai = findViewById(R.id.btn_selesai);

        textViewKd_order.setText(""+kode_order);
        textViewbayar.setText("Rp."+bayar);
        btn_selesai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),ListSembakoActivity.class));
            }
        });

    }




}
