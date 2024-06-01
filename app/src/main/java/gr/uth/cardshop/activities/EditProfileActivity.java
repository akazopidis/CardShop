package gr.uth.cardshop.activities;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import gr.uth.cardshop.R;
import gr.uth.cardshop.forms.ValidationForm;

public class EditProfileActivity extends AppCompatActivity {

    public static final  String TAG = "TAG";
    private EditText profileFullName, profileEmail, profilePhone;
    private ImageView profileImageView;
    private Button saveBtn;
    private FirebaseAuth fAuth;
    private FirebaseFirestore fStore;
    private FirebaseUser user;
    private StorageReference storageReference;
    private MaterialToolbar mToolbar;
    LoadingDialogActivity loadingDialog = new LoadingDialogActivity(EditProfileActivity.this);
    ValidationForm vf = new ValidationForm();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        Intent data = getIntent();
        String fullName = data.getStringExtra("fullName");
        String email = data.getStringExtra("email");
        String phone = data.getStringExtra("phone");
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        user = fAuth.getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference();
        profileFullName = findViewById(R.id.profileFullName);
        profileEmail = findViewById(R.id.profileEmailAddress);
        profilePhone = findViewById(R.id.profilePhoneNo);
        profileImageView = findViewById(R.id.profileImageView);
        saveBtn = findViewById(R.id.saveProfileInfo);
        mToolbar = findViewById(R.id.editProfile_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Edit Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Files with profile photos for users
        StorageReference profileRef = storageReference.child("users/"+fAuth.getCurrentUser().getUid()+"/profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {

                Picasso.get().load(uri).into(profileImageView);
            }
        });
        profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent openGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(openGalleryIntent,1000);
            }
        });
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pName = profileFullName.getText().toString();
                String pEmail = profileEmail.getText().toString();
                String pPhone = profilePhone.getText().toString();
                if(user.isEmailVerified()) {
                    if (TextUtils.isEmpty(pName) || TextUtils.isEmpty(profileEmail.getText().toString()) || TextUtils.isEmpty(pPhone)) {
                        Toast.makeText(EditProfileActivity.this, "One or Many fields are empty", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (!vf.isValidFullName(pName)) {
                        profileFullName.setError("Username not Valid");
                        return;
                    }
                    if (!vf.isValidEmail(pEmail)) {
                        profileEmail.setError("Email not valid");
                        return;
                    }
                    if (!vf.isValidPhone(pPhone)) {
                        profilePhone.setError("Phone not valid");
                        return;
                    }
                    loadingDialog.startLoadingDialog();
                    user.updateEmail(pEmail).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            DocumentReference docRef = fStore.collection("UserProfile").document(user.getUid())
                                    .collection("Information").document(user.getUid());
                            Map<String, Object> edited = new HashMap<>();
                            edited.put("email", pEmail);
                            edited.put("fName", pName);
                            edited.put("phone", pPhone);
                            docRef.update(edited).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    loadingDialog.dismissDialog();
                                    Toast.makeText(EditProfileActivity.this, "Profile Updated", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(getApplicationContext(), UsersProfileActivity.class));
                                    finish();
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    loadingDialog.dismissDialog();
                                }
                            },3000);
                            Toast.makeText(EditProfileActivity.this, "Error! " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }else{
                    Toast.makeText(EditProfileActivity.this, "You cant change profile because user is not verified", Toast.LENGTH_SHORT).show();
                }
            }
        });
        profileEmail.setText(email);
        profileFullName.setText(fullName);
        profilePhone.setText(phone);
        Log.d(TAG, "onCreate: " + fullName +" "+ email + " "+ phone);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1000){
            if(resultCode == Activity.RESULT_OK){
                Uri imageUri = data.getData();//save image  uri to imageUri
                uploadImageToFirebase(imageUri);
            }
        }
    }
    private void uploadImageToFirebase(Uri imageUri) {
        // Check if the selected image is less than 10MB
        if (imageUri != null && getImageSize(imageUri) <= 10 * 1024 * 1024) {
            // Upload image to Firebase storage
            loadingDialog.startLoadingDialog();
            StorageReference fileRef = storageReference.child("users/"+fAuth.getCurrentUser().getUid()+"/profile.jpg");
            fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Picasso.get().load(uri).into(profileImageView);
                            loadingDialog.dismissDialog();
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(EditProfileActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            loadingDialog.dismissDialog();
                        }
                    },3000);

                }
            });
        } else {
            Toast.makeText(EditProfileActivity.this, "Please select an image less than 10MB.", Toast.LENGTH_SHORT).show();
        }
    }

    // Helper method to get the size of an image URI
    private long getImageSize(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        int sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE);
        cursor.moveToFirst();
        long size = cursor.getLong(sizeIndex);
        cursor.close();
        return size;
    }
}