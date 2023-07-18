package gr.uth.cardshop.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gr.uth.cardshop.R;
import gr.uth.cardshop.domain.Items;

public class CartItemAdapter extends RecyclerView.Adapter<CartItemAdapter.ViewHolder> {
    private List<Items> itemsList;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private ItemRemoved itemRemoved;

    public CartItemAdapter(List<Items> itemsList, ItemRemoved itemRemoved) {
        this.itemsList = itemsList;
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = firebaseAuth.getInstance();
        this.itemRemoved = itemRemoved;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_cart_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        holder.cartName.setText(itemsList.get(position).getName());
        holder.cartRarity.setText("Rarity: "+itemsList.get(position).getRarity());
        holder.cartPrice.setText("Price: $"+itemsList.get(position).getPrice());
        holder.cartQuantity.setText("Quantity: " + itemsList.get(position).getQuantity());
        Glide.with(holder.itemView.getContext()).load(itemsList.get(position).getImg_url()).into(holder.cartImage);
        holder.removeItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseFirestore.collection("Users").document(firebaseAuth.getCurrentUser().getUid())
                        .collection("Cart").document(itemsList.get(position).getDocId()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()) {
                                    if(!itemsList.isEmpty()) {
                                        itemsList.remove(itemsList.get(position));
                                        notifyDataSetChanged();
                                        itemRemoved.onItemRemoved(itemsList);
                                        Toast.makeText(holder.itemView.getContext(), "Item Removed", Toast.LENGTH_SHORT).show();
                                    }else{
                                        Toast.makeText(holder.itemView.getContext(), "No items to remove", Toast.LENGTH_SHORT).show();
                                    }
                                } else{
                                    Toast.makeText(holder.itemView.getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                //if a customer removes an item from the cart, the quantity is restored//
                Map<String, Object> fMap = new HashMap<>();
                fMap.put("quantity", itemsList.get(position).getQuantity() + itemsList.get(position).getCurrentQuantity());

                DocumentReference featureDocumentReference = firebaseFirestore.collection("Feature").document(itemsList.get(position).getDocRef());
                featureDocumentReference.update(fMap).addOnSuccessListener(new OnSuccessListener<Void>() {

                    @Override
                    public void onSuccess(Void unused) {
                        //Toast.makeText(holder.itemView.getContext(), "Restore Quantity", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //Toast.makeText(holder.itemView.getContext(), "Fail feature restore", Toast.LENGTH_SHORT).show();
                    }
                });
                Map<String, Object> bMap = new HashMap<>();
                bMap.put("quantity", itemsList.get(position).getQuantity() + itemsList.get(position).getCurrentQuantity());

                DocumentReference bestSellDocumentReference = firebaseFirestore.collection("BestSell").document(itemsList.get(position).getDocRef());
                bestSellDocumentReference.update(bMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        //Toast.makeText(holder.itemView.getContext(), "Restore Quantity", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //Toast.makeText(holder.itemView.getContext(), "Fail bestSell restore", Toast.LENGTH_SHORT).show();
                    }
                });
                Map<String, Object> sMap = new HashMap<>();
                sMap.put("quantity", itemsList.get(position).getQuantity() + itemsList.get(position).getCurrentQuantity());

                DocumentReference stockDocumentReference = firebaseFirestore.collection("Stock").document(itemsList.get(position).getDocRef());
                stockDocumentReference.update(sMap).addOnSuccessListener(new OnSuccessListener<Void>() {

                    @Override
                    public void onSuccess(Void unused) {
                        //Toast.makeText(holder.itemView.getContext(), "Restore Quantity", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //Toast.makeText(holder.itemView.getContext(), "Fail stock restore", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
    @Override
    public int getItemCount() {
        return itemsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView cartImage;
        private TextView cartName;
        private TextView cartRarity;
        private TextView cartPrice;
        private TextView cartQuantity;
        private ImageView removeItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cartImage = itemView.findViewById(R.id.cart_image);
            cartName = itemView.findViewById(R.id.cart_name);
            cartPrice = itemView.findViewById(R.id.cart_price);
            removeItem = itemView.findViewById(R.id.remove_item);
            cartRarity = itemView.findViewById(R.id.cart_rarity);
            cartQuantity = itemView.findViewById(R.id.cart_quantity);
        }
    }
    public interface ItemRemoved{
        void onItemRemoved(List<Items> itemsList);
    }
}