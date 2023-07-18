package gr.uth.cardshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import gr.uth.cardshop.adapter.CartItemAdapter;
import gr.uth.cardshop.domain.Items;

public class CartActivity extends AppCompatActivity implements CartItemAdapter.ItemRemoved {
    private FirebaseFirestore mStore;
    private FirebaseAuth mAuth;
    private List<Items> itemsList;
    private RecyclerView cartRecyclerView;
    private CartItemAdapter cartItemAdapter;
    private Button buyItemButton;
    private TextView totalAmount;
    private MaterialToolbar mToolbar;
    private TextView emptyMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        mStore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        itemsList = new ArrayList<>();
        emptyMsg = findViewById(R.id.empty_msg_cart);
        cartRecyclerView = findViewById(R.id.cart_item_container);
        buyItemButton = findViewById(R.id.cart_item_buy_now);
        totalAmount = findViewById(R.id.total_amount);
        cartRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        cartRecyclerView.setHasFixedSize(true);

        mToolbar=findViewById(R.id.cart_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Cart");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        buyItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!itemsList.isEmpty()) {
                    Intent intent = new Intent(CartActivity.this,AddressActivity.class);
                    intent.putExtra("itemList", (Serializable) itemsList);
                    startActivity(intent);
                } else {
                    Toast.makeText(CartActivity.this, "Sorry, your cart is empty", Toast.LENGTH_SHORT).show();
                }

            }
        });
        cartItemAdapter = new CartItemAdapter(itemsList,this);
        cartRecyclerView.setAdapter(cartItemAdapter);

        mStore.collection("Users").document(mAuth.getCurrentUser().getUid())
                .collection("Cart").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            if (task.getResult() != null) {
                                for(DocumentChange doc :task.getResult().getDocumentChanges()) {
                                    String documentId = doc.getDocument().getId();
                                    Items item = doc.getDocument().toObject(Items.class);
                                    item.setDocId(documentId);
                                    itemsList.add(item);
                                }
                                calculateAmount(itemsList);
                                cartItemAdapter.notifyDataSetChanged();
                                if(itemsList.isEmpty()){
                                    emptyMsg.setVisibility(View.VISIBLE);
                                }
                            }

                        } else {
                            Toast.makeText(CartActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void calculateAmount(List<Items> itemsList) {
        double totalAmountInDouble = 0.0;
        for(Items item: itemsList) {
            totalAmountInDouble += item.getPrice()* item.getQuantity();
        }
        totalAmount.setText("Total Amount : "+totalAmountInDouble);
    }

    @Override
    public void onItemRemoved(List<Items> itemsList) {
        calculateAmount(itemsList);
    }
}