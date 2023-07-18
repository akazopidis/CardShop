package gr.uth.cardshop.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.List;
import gr.uth.cardshop.R;
import gr.uth.cardshop.domain.Orders;


public class OrdersHistoryRecyclerAdapter extends RecyclerView.Adapter<OrdersHistoryRecyclerAdapter.ViewHolder> {
    FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private List<Orders> ordersList;

    public OrdersHistoryRecyclerAdapter(List<Orders> ordersList) {
        this.ordersList = ordersList;
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = firebaseAuth.getInstance();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_order_history_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.orderName.setText(ordersList.get(position).getName());
        holder.orderAddress.setText(ordersList.get(position).getAddress());
        holder.orderEmail.setText(ordersList.get(position).getEmail());
        holder.orderRarity.setText("Rarity: "+ordersList.get(position).getRarity());
        Glide.with(holder.itemView.getContext()).load(ordersList.get(position).getImg_url()).into(holder.orderImage);
        holder.orderPrice.setText("Price:$ "+ordersList.get(position).getAmount());
        holder.orderQuantity.setText("Quantity: "+ordersList.get(position).getQuantity());
    }

    @Override
    public int getItemCount() {
        return ordersList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView orderImage;
        private TextView orderName;
        private TextView orderPrice;
        private TextView orderEmail;
        private TextView orderAddress;
        private TextView orderRarity;
        private TextView orderQuantity;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            orderImage = itemView.findViewById(R.id.order_image);
            orderName = itemView.findViewById(R.id.order_name);
            orderPrice = itemView.findViewById(R.id.order_price);
            orderEmail = itemView.findViewById(R.id.order_email);
            orderAddress = itemView.findViewById(R.id.order_address);
            orderRarity = itemView.findViewById(R.id.order_rarity);
            orderQuantity = itemView.findViewById(R.id.order_quantity);
        }
    }
}
