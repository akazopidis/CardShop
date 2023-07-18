package gr.uth.cardshop.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.List;
import gr.uth.cardshop.R;
import gr.uth.cardshop.domain.Address;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.ViewHolder> {
    private Context applicationContext;
    private List<Address> mAddressList;
    private RadioButton mSelectedRadioButton;
    private SelectedAddress selectedAddress;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private ItemRemoved itemRemoved;

    public AddressAdapter(Context applicationContext, List<Address> mAddressList, SelectedAddress selectedAddress, ItemRemoved itemRemoved) {
        this.applicationContext = applicationContext;
        this.mAddressList = mAddressList;
        this.selectedAddress = selectedAddress;
        this.itemRemoved = itemRemoved;
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = firebaseAuth.getInstance();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(applicationContext).inflate(R.layout.single_address_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        holder.mAddress.setText(mAddressList.get(position).getAddress());
        holder.mRadio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(Address address: mAddressList){
                    address.setSelected(false);
                }
                mAddressList.get(position).setSelected(false);
                if(mSelectedRadioButton != null){
                    mSelectedRadioButton.setChecked(false);
                }
                mSelectedRadioButton = (RadioButton) view;
                mSelectedRadioButton.setChecked(true);
                selectedAddress.setAddress(mAddressList.get(position).getAddress());
            }
        });

        holder.removeAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseFirestore.collection("UserProfile").document(firebaseAuth.getCurrentUser().getUid())
                        .collection("Address").document(mAddressList.get(position).getDocId()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()) {
                                    mAddressList.remove(mAddressList.get(position));
                                    notifyDataSetChanged();
                                    itemRemoved.onAddressRemoved(mAddressList);
                                    Toast.makeText(holder.itemView.getContext(), "Address Removed", Toast.LENGTH_SHORT).show();
                                } else{
                                    Toast.makeText(holder.itemView.getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }

    @Override
    public int getItemCount() {
        return mAddressList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView mAddress;
        private RadioButton mRadio;
        private ImageView removeAddress;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mAddress = itemView.findViewById(R.id.address_add);
            mRadio = itemView.findViewById(R.id.select_address);
            removeAddress = itemView.findViewById(R.id.remove_address);
        }
    }

    public interface SelectedAddress{
        void setAddress(String s);
    }
    public interface ItemRemoved{
        void onAddressRemoved(List<Address> itemsList);
    }
}

