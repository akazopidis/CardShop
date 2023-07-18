package gr.uth.cardshop;

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
import gr.uth.cardshop.adapter.WishlistAdapter;
import gr.uth.cardshop.domain.Items;

public class WishlistActivity extends AppCompatActivity implements WishlistAdapter.ItemRemoved{
    private FirebaseFirestore mStore;
    private FirebaseAuth mAuth;
    private RecyclerView wishlistRecyclerView;
    private WishlistAdapter wishlistAdapter;
    private List<Items> itemsList;
    private MaterialToolbar mToolbar;
    private TextView emptyMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wishlist);
        mStore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        itemsList = new ArrayList<>();
        emptyMsg = findViewById(R.id.empty_msg_wishlist);
        wishlistRecyclerView = findViewById(R.id.wishlist_item_container);
        mToolbar=findViewById(R.id.wishlist_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Wishlist");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        wishlistRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        wishlistRecyclerView.setHasFixedSize(true);
        wishlistAdapter = new WishlistAdapter(itemsList,this);
        wishlistRecyclerView.setAdapter(wishlistAdapter);

        mStore.collection("Users").document(mAuth.getCurrentUser().getUid())
                .collection("Wishlist").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
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
                                wishlistAdapter.notifyDataSetChanged();
                                if(itemsList.isEmpty()){
                                    emptyMsg.setVisibility(View.VISIBLE);
                                }
                            }
                        } else {
                            Toast.makeText(WishlistActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onItemRemoved(List<Items> itemsList) {
    }
}