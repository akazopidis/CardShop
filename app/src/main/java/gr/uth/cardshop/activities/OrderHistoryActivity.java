package gr.uth.cardshop.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.List;

import gr.uth.cardshop.R;
import gr.uth.cardshop.adapter.OrdersHistoryAdapter;
import gr.uth.cardshop.domain.Orders;

public class OrderHistoryActivity extends AppCompatActivity {
    private FirebaseFirestore mStore;
    private FirebaseAuth mAuth;
    private RecyclerView orderRecyclerView;
    private OrdersHistoryAdapter ordersHistoryAdapter;
    private List<Orders> ordersList;
    private MaterialToolbar mToolbar;
    private TextView emptyMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);
        mStore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        emptyMsg = findViewById(R.id.empty_msg_orders);
        ordersList = new ArrayList<>();
        orderRecyclerView = findViewById(R.id.order_history_container);
        mToolbar=findViewById(R.id.order_history_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Orders History");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        orderRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        orderRecyclerView.setHasFixedSize(true);
        ordersHistoryAdapter = new OrdersHistoryAdapter(ordersList);
        orderRecyclerView.setAdapter(ordersHistoryAdapter);

        mStore.collection("Users").document(mAuth.getCurrentUser().getUid())
                .collection("Orders").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            if (task.getResult() != null) {
                                for(DocumentChange doc :task.getResult().getDocumentChanges()) {
                                    Orders order = doc.getDocument().toObject(Orders.class);
                                    ordersList.add(order);
                                }
                                ordersHistoryAdapter.notifyDataSetChanged();
                                if(ordersList.isEmpty()){
                                    emptyMsg.setVisibility(View.VISIBLE);
                                }
                            }
                        } else {
                            Toast.makeText(OrderHistoryActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}