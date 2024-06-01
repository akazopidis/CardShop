package gr.uth.cardshop.activities;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.stripe.android.ApiResultCallback;
import com.stripe.android.PaymentIntentResult;
import com.stripe.android.Stripe;
import com.stripe.android.model.ConfirmPaymentIntentParams;
import com.stripe.android.model.PaymentIntent;
import com.stripe.android.model.PaymentMethodCreateParams;
import com.stripe.android.view.CardMultilineWidget;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import gr.uth.cardshop.R;
import gr.uth.cardshop.domain.Items;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CheckoutActivity extends AppCompatActivity {
    // 10.0.2.2 is the Android emulator's alias to localhost
    private static final String BACKEND_URL = "http://10.0.2.2:5001/";//Localhost backend
    //private static final String BACKEND_URL = "https://stripe-payment-backend.herokuapp.com/"; //Heroku backend
    private OkHttpClient httpClient = new OkHttpClient();
    private String paymentIntentClientSecret;
    private Stripe stripe;
    private MaterialToolbar mToolbar;
    private Double amountDouble = null;
    private String address = "";
    private String fullName = "";
    private String Email = "";
    private String Phone = "";
    private FirebaseAuth mAuth;
    private FirebaseFirestore mStore;
    private ArrayList<Items> itemsList;
    LoadingDialogActivity loadingDialog = new LoadingDialogActivity(CheckoutActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        itemsList = (ArrayList<Items>) getIntent().getSerializableExtra("itemsList");
        mAuth = FirebaseAuth.getInstance();
        mStore = FirebaseFirestore.getInstance();
        amountDouble = getIntent().getDoubleExtra("amount",0.0);
        address = getIntent().getStringExtra("address");
        fullName = getIntent().getStringExtra("fName");
        Email = getIntent().getStringExtra("email");
        Phone = getIntent().getStringExtra("phone");
        mToolbar = findViewById(R.id.checkout_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Checkout");
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        stripe = new Stripe(getApplicationContext(),Objects.requireNonNull("pk_test_51LtBusLWr7TnFjBIztpEPkVVsntRHPUfQO69IAvc0ODxixGIhF12OTQk57mKCwpamWMyumFMyOsCKqq4g3zJPzmc00wiE9yfVi"));
        startCheckout();
    }

    private void startCheckout() {
        // Create a PaymentIntent by calling the server's endpoint.
        MediaType mediaType = MediaType.get("application/json; charset=utf-8");
        double amount = amountDouble * 100;
        Map<String, Object> payMap = new HashMap<>();
        Map<String, Object> itemMap = new HashMap<>();
        List<Map<String, Object>> itemList = new ArrayList<>();
        payMap.put("currency", "usd");
        itemMap.put("id", "photo_subscription");
        itemMap.put("amount", amount);
        itemList.add(itemMap);
        payMap.put("items", itemList);
        String json = new Gson().toJson(payMap);
        RequestBody body = RequestBody.create(json, mediaType);
        Request request = new Request.Builder()
                .url(BACKEND_URL + "create-payment-intent")
                .post(body)
                .build();
        httpClient.newCall(request)
                .enqueue(new PayCallback(this));

        // Hook up the pay button to the card widget and stripe instance
        Button payButton = findViewById(R.id.payButton);
        payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CardMultilineWidget cardMultilineWidget = CheckoutActivity.this.findViewById(R.id.cardInputWidget);
                PaymentMethodCreateParams params = cardMultilineWidget.getPaymentMethodCreateParams();
                if (params != null) {
                    loadingDialog.startLoadingDialog();//animation loadingDialog
                    ConfirmPaymentIntentParams confirmParams = ConfirmPaymentIntentParams
                            .createWithPaymentMethodCreateParams(params, paymentIntentClientSecret);
                    //This value must be greater than or equal to 1.
                    stripe.confirmPayment(CheckoutActivity.this, confirmParams);
                }
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Handle the result of stripe.confirmPayment
        stripe.onPaymentResult(requestCode, data, new PaymentResultCallback(this));
    }
    private static final class PayCallback implements Callback {
        @NonNull private final WeakReference<CheckoutActivity> activityRef;
        PayCallback(@NonNull CheckoutActivity activity) {
            activityRef = new WeakReference<>(activity);
        }
        @Override
        public void onFailure(@NonNull Call call, @NonNull IOException e) {
            final CheckoutActivity activity = activityRef.get();
            if (activity == null) {
                return;
            }
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(activity, "Error: " + e.toString(), Toast.LENGTH_LONG).show();
                }
            });
        }
        @Override
        public void onResponse(@NonNull Call call, @NonNull final Response response)
                throws IOException {
            final CheckoutActivity activity = activityRef.get();
            if (activity == null) {
                return;
            }
            if (!response.isSuccessful()) {activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(activity, "Error: " + response.toString(), Toast.LENGTH_LONG).show();
                }
            });
            } else {
                activity.onPaymentSuccess(response);
            }
        }
    }
    private void onPaymentSuccess(@NonNull final Response response) throws IOException {
        Gson gson = new Gson();
        Type type = new TypeToken<Map<String, String>>(){}.getType();
        Map<String, String> responseMap = gson.fromJson(
                Objects.requireNonNull(response.body()).string(),
                type
        );
        paymentIntentClientSecret = responseMap.get("clientSecret");
        Log.i("TAG", "onPaymentSuccess: "+paymentIntentClientSecret);
    }
    private final class PaymentResultCallback
            implements ApiResultCallback<PaymentIntentResult> {
        @NonNull private final WeakReference<CheckoutActivity> activityRef;
        PaymentResultCallback(@NonNull CheckoutActivity activity) {
            activityRef = new WeakReference<>(activity);

        }
        @Override
        public void onSuccess(@NonNull PaymentIntentResult result) {
            final CheckoutActivity activity = activityRef.get();
            if (activity == null) {
                return;
            }
            PaymentIntent paymentIntent = result.getIntent();
            PaymentIntent.Status status = paymentIntent.getStatus();
            if (status == PaymentIntent.Status.Succeeded) {

                // Payment completed successfully
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                //for multiple items in cart//
                if(itemsList != null && itemsList.size()>0){
                    for(Items item: itemsList) {
                        Map<String, Object> mMap = new HashMap<>();
                        mMap.put("fName", fullName);
                        mMap.put("email", Email);
                        mMap.put("phone", Phone);
                        mMap.put("address", address);
                        mMap.put("img_url", item.getImg_url());
                        mMap.put("name", item.getName());
                        mMap.put("rarity", item.getRarity());
                        mMap.put("payment_id", result.getIntent().getId());
                        mMap.put("amount", amountDouble);
                        mMap.put("order_status", status);
                        mMap.put("payment_method", result.getIntent().getPaymentMethodTypes());
                        mMap.put("productId", item.getProductId());
                        mMap.put("quantity", item.getQuantity());

                        mStore.collection("Users").document(mAuth.getCurrentUser().getUid())
                                .collection("Orders")
                                .add(mMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentReference> task) {
                                        loadingDialog.dismissDialog();
                                        Intent intent = new Intent(getApplicationContext(), ConfirmationActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                });
                    }

                    for(Items item: itemsList)  {
                        mStore.collection("Users").document(mAuth.getCurrentUser().getUid())
                                .collection("Cart").document(item.getDocId()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(activity, "Cart clear", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(activity, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }
                }
            } else if (status == PaymentIntent.Status.RequiresPaymentMethod) {
                // Payment failed – allow retrying using a different payment method
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadingDialog.dismissDialog();
                    }
                },3000);
                activity.displayAlert("Payment failed", Objects.requireNonNull(paymentIntent.getLastPaymentError()).getMessage()
                );
            }
        }
        @Override
        public void onError(@NonNull Exception e) {
            final CheckoutActivity activity = activityRef.get();
            if (activity == null) {
                return;
            }
            // Payment request failed – allow retrying using the same payment method
            activity.displayAlert("Error", e.toString());
        }
    }
    private void displayAlert(@NonNull String title, @Nullable String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message);
        builder.setPositiveButton("Ok", null);
        builder.create().show();
    }
}
