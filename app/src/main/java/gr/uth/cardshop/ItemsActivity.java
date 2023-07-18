package gr.uth.cardshop;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.List;
import gr.uth.cardshop.adapter.ItemsRecyclerAdapter;
import gr.uth.cardshop.domain.Items;

public class ItemsActivity extends AppCompatActivity {
    private FirebaseFirestore mStore;
    private List<Items> mItemsList;
    private RecyclerView itemRecyclerView;
    private ItemsRecyclerAdapter itemsRecyclerAdapter;
    private MaterialToolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items);
        String type = getIntent().getStringExtra("type");
        mStore = FirebaseFirestore.getInstance();
        mItemsList = new ArrayList<>();
        mToolbar = findViewById(R.id.item_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Items");
        itemRecyclerView = findViewById(R.id.items_recycler);
        itemRecyclerView.setLayoutManager(new GridLayoutManager(this,2));
        itemsRecyclerAdapter = new ItemsRecyclerAdapter(this,mItemsList);
        itemRecyclerView.setAdapter(itemsRecyclerAdapter);

        if(type==null || type.isEmpty()) {
            mStore.collection("Stock").get().addOnCompleteListener(task -> {
                if(task.isSuccessful()) {
                    for(DocumentSnapshot doc:task.getResult().getDocuments()) {
                        Log.i("TAG","onComplete: "+doc.toString());
                        Items items = doc.toObject(Items.class);
                        mItemsList.add(items);
                        items.setDocId(doc.getId());
                        itemsRecyclerAdapter.notifyDataSetChanged();
                    }
                }
            });
        }
        if(type != null && type.equalsIgnoreCase("monster")) {
            mStore.collection("Stock").whereEqualTo("type","monster").get().addOnCompleteListener(task -> {
                if(task.isSuccessful()) {
                    for(DocumentSnapshot doc:task.getResult().getDocuments()) {
                        Log.i("TAG","onComplete: "+doc.toString());
                        Items items = doc.toObject(Items.class);
                        mItemsList.add(items);
                        items.setDocId(doc.getId());
                        itemsRecyclerAdapter.notifyDataSetChanged();
                    }
                }
            });
        }
        if(type != null && type.equalsIgnoreCase("trap")) {
            mStore.collection("Stock").whereEqualTo("type","trap").get().addOnCompleteListener(task -> {
                if(task.isSuccessful()) {
                    for(DocumentSnapshot doc:task.getResult().getDocuments()) {
                        Log.i("TAG","onComplete: "+doc.toString());
                        Items items = doc.toObject(Items.class);
                        mItemsList.add(items);
                        items.setDocId(doc.getId());
                        itemsRecyclerAdapter.notifyDataSetChanged();
                    }
                }
            });
        }
        if(type != null && type.equalsIgnoreCase("spell")) {
            mStore.collection("Stock").whereEqualTo("type","spell").get().addOnCompleteListener(task -> {
                if(task.isSuccessful()) {
                    for(DocumentSnapshot doc:task.getResult().getDocuments()) {
                        Log.i("TAG","onComplete: "+doc.toString());
                        Items items = doc.toObject(Items.class);
                        mItemsList.add(items);
                        items.setDocId(doc.getId());
                        itemsRecyclerAdapter.notifyDataSetChanged();
                    }
                }
            });
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu,menu);
        MenuItem item = menu.findItem(R.id.search_it);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchItem(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private void searchItem(String newText) {
        mItemsList.clear();
        mStore.collection("Stock").whereGreaterThanOrEqualTo("name",newText).get().addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                for(DocumentSnapshot doc:task.getResult().getDocuments()) {
                    Log.i("TAG","onComplete: "+doc.toString());
                    Items items = doc.toObject(Items.class);
                    mItemsList.add(items);
                    items.setDocId(doc.getId());
                    itemsRecyclerAdapter.notifyDataSetChanged();
                }
            }
        });
    }
}