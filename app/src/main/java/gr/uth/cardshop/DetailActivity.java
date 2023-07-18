package gr.uth.cardshop;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

import gr.uth.cardshop.domain.BestSell;
import gr.uth.cardshop.domain.Feature;
import gr.uth.cardshop.domain.Items;

public class DetailActivity extends AppCompatActivity {
    private ImageView mImage;
    private TextView mItemName;
    private TextView mPrice;
    private Button mItemRarity;
    private TextView mItemQnt;
    private TextView mItemDesc;
    private TextView mItemCode;
    private Button mAddToCart;
    private Button mAddToWishlist;
    private MaterialToolbar mToolbar;
    private int counter = 0;
    private Feature feature = null;
    private BestSell bestSell = null;
    private Items items = null;
    private FirebaseFirestore mStore;
    private FirebaseAuth mAuth;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        mToolbar = findViewById(R.id.detail_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mImage = findViewById(R.id.item_img);
        mItemName = findViewById(R.id.item_name);
        mPrice = findViewById(R.id.item_price);
        mItemRarity = findViewById(R.id.item_rating);
        mItemQnt = findViewById(R.id.item_quantity);
        mItemCode =findViewById(R.id.item_Pid_code);
        mItemDesc = findViewById(R.id.item_des);
        mAddToCart = findViewById(R.id.item_add_cart);
        mAddToWishlist = findViewById(R.id.item_add_wishlist);
        mStore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        final Object obj = getIntent().getSerializableExtra("detail");
        if (obj instanceof Feature) {
            feature = (Feature) obj;
        } else if (obj instanceof BestSell) {
            bestSell = (BestSell) obj;
        } else if (obj instanceof Items) {
            items = (Items) obj;
        }
        if (feature != null) {
            Glide.with(getApplicationContext()).load(feature.getImg_url()).into(mImage);
            mItemName.setText(feature.getName());
            mPrice.setText(feature.getPrice() + "$");
            mItemRarity.setText(feature.getRarity() + "");
            mItemDesc.setText(feature.getDescription());
            if(feature.getQuantity() > 0) {
                mItemQnt.setText(feature.getQuantity() + "");
            }else{
                mItemQnt.setText("Out of Stock");
            }
            mItemCode.setText(""+feature.getProductId());
        }
        if (bestSell != null) {
            Glide.with(getApplicationContext()).load(bestSell.getImg_url()).into(mImage);
            mItemName.setText(bestSell.getName());
            mPrice.setText(bestSell.getPrice() + "$");
            mItemRarity.setText(bestSell.getRarity() + "");
            mItemDesc.setText(bestSell.getDescription());
            if(bestSell.getQuantity() > 0) {
                mItemQnt.setText(bestSell.getQuantity() + "");
            }else{
                mItemQnt.setText("Out of Stock");
            }
            mItemCode.setText("" + bestSell.getProductId());
        }
        if (items != null) {
            Glide.with(getApplicationContext()).load(items.getImg_url()).into(mImage);
            mItemName.setText(items.getName());
            mPrice.setText(items.getPrice() + "$");
            mItemRarity.setText(items.getRarity());
            mItemDesc.setText(items.getDescription());
            if(items.getQuantity() > 0) {
                mItemQnt.setText(items.getQuantity() + "");
            }else{
                mItemQnt.setText("Out of Stock");
            }
            mItemCode.setText("" + items.getProductId());
        }

        mAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (user.isEmailVerified()) {
                    counter++;
                    if (feature != null) {
                        int qnt = feature.getQuantity();
                        qnt = qnt - counter;
                        if(qnt >= 0) {
                            if(qnt != 0) {
                                mItemQnt.setText(qnt + "");
                            }else {
                                mItemQnt.setText("Out of Stock");
                            }
                            Map<String, Object> mMap = new HashMap<>();
                            mMap.put("name", feature.getName());
                            mMap.put("description", feature.getDescription());
                            mMap.put("price", feature.getPrice());
                            mMap.put("rarity", feature.getRarity());
                            mMap.put("img_url", feature.getImg_url());
                            mMap.put("productId", feature.getProductId());
                            mMap.put("quantity", counter);
                            mMap.put("currentQuantity", qnt);
                            mMap.put("docRef", feature.getDocId());

                            DocumentReference userCartDocRef = mStore.collection("Users")
                                    .document(mAuth.getCurrentUser().getUid())
                                    .collection("Cart")
                                    .document(String.valueOf(feature.getProductId()));

                            userCartDocRef.set(mMap, SetOptions.merge())
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(DetailActivity.this, "Item Added to Cart", Toast.LENGTH_SHORT).show();

                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(DetailActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                            DocumentReference documentReference2 = mStore.collection("Feature").document(feature.getDocId());
                            Map<String, Object> mMap2 = new HashMap<>();
                            mMap2.put("quantity", qnt);
                            documentReference2.update(mMap2).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    //Toast.makeText(DetailActivity.this, "Updated", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(DetailActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }else{
                            mItemQnt.setText("Out of Stock");
                            Toast.makeText(DetailActivity.this, "Sorry item out of stock", Toast.LENGTH_SHORT).show();
                        }
                    }
                    if (bestSell != null) {
                        int qnt = bestSell.getQuantity();
                        qnt = qnt - counter;
                        if(qnt >= 0) {
                            if(qnt != 0) {
                                mItemQnt.setText(qnt + "");
                            }else {
                                mItemQnt.setText("Out of Stock");
                            }

                            Map<String, Object> mMap = new HashMap<>();
                            mMap.put("name", bestSell.getName());
                            mMap.put("description", bestSell.getDescription());
                            mMap.put("price", bestSell.getPrice());
                            mMap.put("rarity", bestSell.getRarity());
                            mMap.put("img_url", bestSell.getImg_url());
                            mMap.put("productId", bestSell.getProductId());
                            mMap.put("quantity", counter);
                            mMap.put("currentQuantity", qnt);
                            mMap.put("docRef", bestSell.getDocId());

                            DocumentReference userCartDocRef = mStore.collection("Users")
                                    .document(mAuth.getCurrentUser().getUid())
                                    .collection("Cart")
                                    .document(String.valueOf(bestSell.getProductId()));

                            userCartDocRef.set(mMap, SetOptions.merge())
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(DetailActivity.this, "Item Added to Cart", Toast.LENGTH_SHORT).show();

                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(DetailActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                            DocumentReference documentReference2 = mStore.collection("BestSell").document(bestSell.getDocId());
                            Map<String, Object> mMap2 = new HashMap<>();
                            mMap2.put("quantity", qnt);
                            documentReference2.update(mMap2).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    //Toast.makeText(DetailActivity.this, "Updated", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(DetailActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                                }
                            });

                        }else{
                            mItemQnt.setText("Out of Stock");
                            Toast.makeText(DetailActivity.this, "Sorry item out of stock", Toast.LENGTH_SHORT).show();
                        }
                    }
                    if (items != null) {
                        int qnt = items.getQuantity();
                        qnt = qnt - counter;
                        if(qnt >= 0) {
                            if(qnt != 0) {
                                mItemQnt.setText(qnt + "");
                            }else {
                                mItemQnt.setText("Out of Stock");
                            }

                            Map<String, Object> mMap = new HashMap<>();
                            mMap.put("name", items.getName());
                            mMap.put("description", items.getDescription());
                            mMap.put("price", items.getPrice());
                            mMap.put("rarity", items.getRarity());
                            mMap.put("img_url", items.getImg_url());
                            mMap.put("productId", items.getProductId());
                            mMap.put("quantity", counter);
                            mMap.put("currentQuantity", qnt);
                            mMap.put("docRef", items.getDocId());
                            DocumentReference userCartDocRef = mStore.collection("Users")
                                    .document(mAuth.getCurrentUser().getUid())
                                    .collection("Cart")
                                    .document(String.valueOf(items.getProductId()));

                            userCartDocRef.set(mMap, SetOptions.merge())
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(DetailActivity.this, "Item Added to Cart", Toast.LENGTH_SHORT).show();

                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(DetailActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                            DocumentReference stockDocRef = mStore.collection("Stock").document(items.getDocId());

                            Map<String, Object> stockMap = new HashMap<>();
                            stockMap.put("quantity", qnt);

                            stockDocRef.update(stockMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            //Toast.makeText(DetailActivity.this, "Updated", Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(DetailActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } else {
                            mItemQnt.setText("Out of Stock");
                            Toast.makeText(DetailActivity.this, "Sorry item out of stock", Toast.LENGTH_SHORT).show();
                        }
                    }
                }else{
                    Toast.makeText(DetailActivity.this, "Please verify the email to add a product to your cart", Toast.LENGTH_SHORT).show();
                }
            }
        });
        mAddToWishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (user.isEmailVerified()) {
                    if (feature != null) {
                        Map<String, Object> mMap = new HashMap<>();
                        mMap.put("name", feature.getName());
                        mMap.put("price", feature.getPrice());
                        mMap.put("rarity", feature.getRarity());
                        mMap.put("img_url", feature.getImg_url());

                        DocumentReference documentReference = mStore.collection("Users").document(mAuth.getCurrentUser().getUid())
                                .collection("Wishlist").document(String.valueOf(feature.getProductId()));
                        documentReference.set(mMap, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {

                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(DetailActivity.this, "Item Added to wishlist", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(DetailActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    if (bestSell != null) {
                        Map<String, Object> mMap = new HashMap<>();
                        mMap.put("name", bestSell.getName());
                        mMap.put("price", bestSell.getPrice());
                        mMap.put("rarity", bestSell.getRarity());
                        mMap.put("img_url", bestSell.getImg_url());

                        DocumentReference documentReference = mStore.collection("Users").document(mAuth.getCurrentUser().getUid())
                                .collection("Wishlist").document(String.valueOf(bestSell.getProductId()));
                        documentReference.set(mMap, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {

                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(DetailActivity.this, "Item Added to wishlist", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(DetailActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    if (items != null) {
                        Map<String, Object> mMap = new HashMap<>();
                        mMap.put("name", items.getName());
                        mMap.put("price", items.getPrice());
                        mMap.put("rarity", items.getRarity());
                        mMap.put("img_url", items.getImg_url());

                        DocumentReference documentReference = mStore.collection("Users").document(mAuth.getCurrentUser().getUid())
                                .collection("Wishlist").document(String.valueOf(items.getProductId()));
                        documentReference.set(mMap, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {

                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(DetailActivity.this, "Item Added to Wishlist", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(DetailActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }else{
                    Toast.makeText(DetailActivity.this, "Please verify the email to add a product to your wishlist", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(DetailActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }
}