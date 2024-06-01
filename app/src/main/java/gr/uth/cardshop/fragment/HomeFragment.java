package gr.uth.cardshop.fragment;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.List;

import gr.uth.cardshop.activities.ItemsActivity;
import gr.uth.cardshop.R;
import gr.uth.cardshop.adapter.BestSellAdapter;
import gr.uth.cardshop.adapter.CategoryAdapter;
import gr.uth.cardshop.adapter.FeatureAdapter;
import gr.uth.cardshop.domain.BestSell;
import gr.uth.cardshop.domain.Category;
import gr.uth.cardshop.domain.Feature;

public class HomeFragment extends Fragment {
    private FirebaseFirestore mStore;
    //Category Tab
    private List<Category> mCategoryList;
    private CategoryAdapter mCategoryAdapter;
    private RecyclerView mCatRecyclerView;
    //Feature Tab
    private List<Feature> mFeatureList;
    private FeatureAdapter mFeatureAdapter;
    private RecyclerView mFeatureRecyclerView;
    //BestSell Tab
    private List<BestSell> mBestSellList;
    private BestSellAdapter mBestSellAdapter;
    private RecyclerView mBestSellRecyclerView;
    private TextView mSeeAll;
    private TextView mFeature;
    private TextView mBestSell;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        mStore = FirebaseFirestore.getInstance();
        mSeeAll = view.findViewById(R.id.see_all);
        mFeature = view.findViewById(R.id.feat_see_all);
        mBestSell = view.findViewById(R.id.best_sell);
        mCatRecyclerView = view.findViewById(R.id.category_recycler);
        mFeatureRecyclerView = view.findViewById(R.id.feature_recycler);
        mBestSellRecyclerView = view.findViewById(R.id.bestsell_recycler);

        //For Category
        mCategoryList = new ArrayList<>();
        mCategoryAdapter = new CategoryAdapter(getContext(),mCategoryList);
        mCatRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.HORIZONTAL,false));
        mCatRecyclerView.setAdapter(mCategoryAdapter);

        //For Feature
        mFeatureList = new ArrayList<>();
        mFeatureAdapter = new FeatureAdapter(getContext(), mFeatureList);
        mFeatureRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.HORIZONTAL,false));
        mFeatureRecyclerView.setAdapter(mFeatureAdapter);

        //For BestSell
        mBestSellList = new ArrayList<>();
        mBestSellAdapter = new BestSellAdapter(getContext(), mBestSellList);
        mBestSellRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.HORIZONTAL,false));
        mBestSellRecyclerView.setAdapter(mBestSellAdapter);

        mStore.collection("Category")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Category category = document.toObject(Category.class);
                                mCategoryList.add(category);
                                mCategoryAdapter.notifyDataSetChanged();
                            }
                        } else {
                            Log.w("TAG", "Error getting documents.", task.getException());
                        }
                    }
                });

        mStore.collection("Feature")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Feature feature = document.toObject(Feature.class);
                                mFeatureList.add(feature);
                                feature.setDocId(document.getId());
                                mFeatureAdapter.notifyDataSetChanged();
                            }
                        } else {
                            Log.w("TAG", "Error getting documents.", task.getException());
                        }
                    }
                });

        mStore.collection("BestSell")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                BestSell bestSell = document.toObject(BestSell.class);
                                mBestSellList.add(bestSell);
                                bestSell.setDocId(document.getId());
                                mBestSellAdapter.notifyDataSetChanged();
                            }
                        } else {
                            Log.w("TAG", "Error getting documents.", task.getException());
                        }
                    }
                });

        mSeeAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ItemsActivity.class);
                startActivity(intent);
            }
        });

        mBestSell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ItemsActivity.class);
                startActivity(intent);
            }
        });

        mFeature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ItemsActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }
}



