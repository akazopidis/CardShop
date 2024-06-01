package gr.uth.cardshop.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.List;

import gr.uth.cardshop.activities.DetailActivity;
import gr.uth.cardshop.activities.HomeActivity;
import gr.uth.cardshop.R;
import gr.uth.cardshop.domain.Items;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ViewHolder> {
    private Context applicationContext;
    private List<Items> mItemsList;

    public ItemsAdapter(Context applicationContext, List<Items> mItemsList) {
        this.applicationContext = applicationContext;
        this.mItemsList = mItemsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(applicationContext).inflate(R.layout.single_item_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.mCost.setText("$ "+mItemsList.get(position).getPrice());
        holder.mName.setText(mItemsList.get(position).getName());
        holder.mRarity.setText(mItemsList.get(position).getRarity());
        if(!(applicationContext instanceof HomeActivity)){
            Glide.with(applicationContext).load(mItemsList.get(position).getImg_url()).into(holder.mItemImage);
        }else {
            holder.mItemImage.setVisibility(View.GONE);
        }

        holder.mItemImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(applicationContext, DetailActivity.class);
                intent.putExtra("detail",mItemsList.get(position));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                applicationContext.startActivity(intent);
            }
        });
        holder.mName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(applicationContext, DetailActivity.class);
                intent.putExtra("detail",mItemsList.get(position));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                applicationContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mItemsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView mItemImage;
        private TextView mCost;
        private TextView mName;
        private TextView mRarity;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mItemImage = itemView.findViewById(R.id.item_image);
            mCost = itemView.findViewById(R.id.item_cost);
            mName = itemView.findViewById(R.id.item_name);
            mRarity = itemView.findViewById(R.id.item_rarity);
        }
    }


}
