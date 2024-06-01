package gr.uth.cardshop.adapter;

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
import gr.uth.cardshop.R;
import gr.uth.cardshop.domain.Feature;

public class FeatureAdapter extends RecyclerView.Adapter<FeatureAdapter.ViewHolder> {
    private Context context;
    private List<Feature> mFeatureList;

    public FeatureAdapter(Context context, List<Feature> mFeatureList) {
        this.context = context;
        this.mFeatureList = mFeatureList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_feature_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.mFetName.setText(mFeatureList.get(position).getName());
        holder.mFetCost.setText(mFeatureList.get(position).getPrice()+" $");
        holder.mFetRarity.setText(mFeatureList.get(position).getRarity());
        Glide.with(context).load(mFeatureList.get(position).getImg_url()).into(holder.mFetImage);
        holder.mFetImage.setOnClickListener(view -> {
            Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtra("detail",mFeatureList.get(position));
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return mFeatureList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView mFetImage;
        private TextView mFetCost;
        private TextView mFetName;
        private TextView mFetRarity;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mFetImage = itemView.findViewById(R.id.f_img);
            mFetCost = itemView.findViewById(R.id.f_cost);
            mFetName = itemView.findViewById(R.id.f_name);
            mFetRarity = itemView.findViewById(R.id.f_rarity);
        }
    }
}
