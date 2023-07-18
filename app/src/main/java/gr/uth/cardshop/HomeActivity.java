package gr.uth.cardshop;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import gr.uth.cardshop.adapter.ItemsRecyclerAdapter;
import gr.uth.cardshop.domain.Items;
import gr.uth.cardshop.fragment.HomeFragment;

public class HomeActivity extends AppCompatActivity {
    private Fragment homeFragment;
    private EditText mSearchText;
    private MaterialToolbar mToolbar;
    private FirebaseAuth mAuth;
    private FirebaseFirestore mStore;
    private List<Items> mItemsList;
    private RecyclerView mItemRecyclerView;
    private ItemsRecyclerAdapter itemsRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        homeFragment = new HomeFragment();
        loadFragment(homeFragment);
        mAuth = FirebaseAuth.getInstance();
        mToolbar = findViewById(R.id.home_toolbar);
        setSupportActionBar(mToolbar);
        mSearchText = findViewById(R.id.search_text);
        mStore = FirebaseFirestore.getInstance();
        mItemsList = new ArrayList<>();
        mItemRecyclerView = findViewById(R.id.search_recycler);
        mItemRecyclerView.setLayoutManager(new GridLayoutManager(this,2));
        itemsRecyclerAdapter = new ItemsRecyclerAdapter(this,mItemsList);
        mItemRecyclerView.setAdapter(itemsRecyclerAdapter);

        mSearchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                searchItem(s.toString());
            }
        });
    }

    private void searchItem(String text) {
        if(!text.isEmpty()){
            mStore.collection("Stock").whereGreaterThanOrEqualTo("name",text).get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful() && task.getResult() != null) {
                                mItemsList.clear();
                                for (DocumentSnapshot doc : task.getResult().getDocuments()) {
                                    Items items = doc.toObject(Items.class);
                                    if (!mItemsList.contains(items)) {
                                        mItemsList.add(items);
                                        items.setDocId(doc.getId());
                                    }
                                }
                                itemsRecyclerAdapter = new ItemsRecyclerAdapter(HomeActivity.this.getApplicationContext(), mItemsList);
                                mItemRecyclerView.setAdapter(itemsRecyclerAdapter);
                                mItemRecyclerView.setVisibility(View.VISIBLE);
                            }
                        }
                    });
        }else{
            mItemsList.clear();
            itemsRecyclerAdapter = new ItemsRecyclerAdapter(getApplicationContext(),new ArrayList<>());
            mItemRecyclerView.setAdapter(itemsRecyclerAdapter);
            itemsRecyclerAdapter.notifyDataSetChanged();
            mItemRecyclerView.setVisibility(View.GONE);
        }
    }
    //replace FrameLayout with fragment_home layout
    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.home_container,homeFragment);
        transaction.commit();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.logout_btn){
            mAuth.signOut();
            Intent intent = new Intent(HomeActivity.this,MainActivity.class);
            startActivity(intent);
            finish();
        }
        if(item.getItemId() == R.id.cart){
            Intent intent=new Intent(HomeActivity.this,CartActivity.class);
            startActivity(intent);
        }
        if(item.getItemId() == R.id.user){
            Intent intent=new Intent(HomeActivity.this,UsersProfileActivity.class);
            startActivity(intent);
        }
        if(item.getItemId() == R.id.myOrders){
            Intent intent=new Intent(HomeActivity.this,OrderHistoryActivity.class);
            startActivity(intent);
        }
        if(item.getItemId() == R.id.contact){
            Intent intent=new Intent(HomeActivity.this,ContactActivity.class);
            startActivity(intent);
        }
        if(item.getItemId() == R.id.wishlist){
            Intent intent=new Intent(HomeActivity.this, WishlistActivity.class);
            startActivity(intent);
        }
        return true;
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
