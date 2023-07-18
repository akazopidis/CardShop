package gr.uth.cardshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class AddAddressActivity extends AppCompatActivity {
    private EditText mCountry;
    private EditText mCity;
    private EditText mAddress;
    private EditText mCode;
    private Button mAddAddressBtn;
    private FirebaseFirestore mStore;
    private FirebaseAuth mAuth;
    private MaterialToolbar mToolbar;
    ValidationForm vf = new ValidationForm();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);
        mCountry = findViewById(R.id.add_country);
        mCity = findViewById(R.id.add_city);
        mAddress = findViewById(R.id.add_address);
        mCode = findViewById(R.id.add_code);
        mAddAddressBtn = findViewById(R.id.add_add_address_btn);
        mStore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mToolbar = findViewById(R.id.add_address_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Add Address");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAddAddressBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String country = mCountry.getText().toString();
                String city = mCity.getText().toString();
                String address = mAddress.getText().toString();
                String code = mCode.getText().toString();
                String final_address = "";

                if(!vf.isValidAddress(address)) {
                    mAddress.setError("Please enter valid address.");
                    return;
                }

                if(!vf.isValidPostalCode(code)) {
                    mCode.setError("Please enter valid postal code.");
                    return;
                }

                if(!vf.isValidCountry(country)) {
                    mCountry.setError("Please enter valid country.");
                    return;
                }

                if(!vf.isValidCity(city)) {
                    mCity.setError("Please enter valid city.");
                    return;
                }

                if (!address.isEmpty()) {
                    final_address += address + ", ";
                } else {
                    mAddress.setError("Address is Required.");
                    return;
                }

                if (!city.isEmpty()) {
                    final_address += city + ", ";
                } else {
                    mCity.setError("City is Required.");
                    return;
                }

                if (!code.isEmpty()) {
                    final_address += code + ", ";
                } else {
                    mCode.setError("Postal code is Required.");
                    return;
                }

                if (!country.isEmpty()) {
                    final_address += country;
                } else {
                    mCountry.setError("Country is Required.");
                    return;
                }

                Map<String,String> mMap = new HashMap<>();
                mMap.put("address",final_address);

                mStore.collection("UserProfile").document(mAuth.getCurrentUser().getUid())
                        .collection("Address").add(mMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentReference> task) {
                                if(task.isSuccessful()) {
                                    Toast.makeText(AddAddressActivity.this, "Address Added", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(AddAddressActivity.this, CartActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        });
            }
        });
    }
}