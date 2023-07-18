package gr.uth.cardshop;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import java.io.Serializable;
import java.util.ArrayList;
import gr.uth.cardshop.adapter.PaymentRecyclerAdapter;
import gr.uth.cardshop.domain.Items;

public class PaymentActivity extends AppCompatActivity {
    private TextView mSubTotal;
    private TextView mDiscount;
    private TextView mTotalAmount;
    private double amount=0.0;
    private String address="";
    private ArrayList<Items> itemsList;
    private String fullName="";
    private String email="";
    private String phone="";
    private double discount = 0.0;
    private double total_amount = 0.0;
    private MaterialToolbar mToolbar;
    private RecyclerView paymentRecyclerView;
    private PaymentRecyclerAdapter paymentRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        amount = getIntent().getDoubleExtra("amount",0.0);
        address = getIntent().getStringExtra("address");
        itemsList = (ArrayList<Items>) getIntent().getSerializableExtra("itemsList");
        mSubTotal = findViewById(R.id.sub_total);
        mSubTotal.setText("$ "+amount +"");
        mDiscount = findViewById(R.id.payment_discount);
        mDiscount.setText("$ "+discount+"");
        mTotalAmount = findViewById(R.id.total_amt);
        mTotalAmount.setText("$ "+total_amount+"");
        mToolbar=findViewById(R.id.payment_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        fullName = getIntent().getStringExtra("fName");
        phone = getIntent().getStringExtra("phone");
        email = getIntent().getStringExtra("email");
        paymentRecyclerView = findViewById(R.id.payment_item_container);
        paymentRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        paymentRecyclerView.setHasFixedSize(true);
        paymentRecyclerAdapter = new PaymentRecyclerAdapter(itemsList);
        paymentRecyclerView.setAdapter(paymentRecyclerAdapter);

        if(itemsList != null && itemsList.size()>0) {
            amount = 0.0;
            for(Items item: itemsList) {
                amount += item.getPrice()*item.getQuantity();
            }
            mSubTotal.setText("$ "+amount+"");
        }

        if(itemsList != null && itemsList.size() > 0) {
            discount = 0.0;
            total_amount = 0.0;

            if(amount >= 50.0 && amount <= 100.0) {
                discount = discount - 5.0;
                total_amount = amount + discount;
            }
            else if(amount >= 101.0 && amount <= 150.0) {
                discount = discount - 10.0;
                total_amount = amount + discount;
            }
            else if(amount >= 151.0 && amount <= 200.0) {
                discount = discount - 15.0;
                total_amount = amount + discount;
            }
            else if(amount >= 201.0 && amount <= 250) {
                discount = discount - 20.0;
                total_amount = amount + discount;
            }
            else if(amount >= 251.0 && amount <= 300) {
                discount = discount - 25.0;
                total_amount = amount + discount;
            }
            else if(amount >= 301.0 && amount <= 350) {
                discount = discount - 30.0;
                total_amount = amount + discount;
            }
            else if(amount >= 351.0 && amount <= 400) {
                discount = discount - 35.0;
                total_amount = amount + discount;
            }
            else if(amount >= 401.0 && amount <= 450) {
                discount = discount - 40.0;
                total_amount = amount + discount;
            }
            else if(amount >= 451.0 && amount <= 500) {
                discount = discount - 45.0;
                total_amount = amount + discount;
            }
            else if(amount >= 501.0) {
                discount = discount - 50.0;
                total_amount = amount + discount;
            }
            else {
                total_amount = total_amount + amount;
            }
            mDiscount.setText("$ " + discount + "");
            mTotalAmount.setText("$ " + total_amount + "");
        }
    }

    public void payWithStripe(View view) {
        if (itemsList != null && itemsList.size() > 0) {
            Intent intent = new Intent(PaymentActivity.this, CheckoutActivity.class);
            intent.putExtra("itemsList", (Serializable) itemsList);
            intent.putExtra("amount",total_amount);
            intent.putExtra("address",address);
            intent.putExtra("fName",fullName);
            intent.putExtra("email",email);
            intent.putExtra("phone",phone);
            startActivity(intent);
        }
    }
}