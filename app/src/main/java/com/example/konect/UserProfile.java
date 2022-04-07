package com.example.konect;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.konect.fragment.ProfileFragment;
import com.example.konect.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;

import java.io.IOException;
import java.util.List;

public class UserProfile extends AppCompatActivity {
    private ImageView imgAvatar, imgBack;
    private EditText edtName,edtEmail;
    private Button btnUpdateProfile;

    private Uri photoURL;

    private ActivityResultLauncher<String> mTakeImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);


        initUI();
        getUserInformation();
        initListener();

        mTakeImage = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                imgAvatar.setImageURI(result);
                photoURL = result;
            }
        });
    }

    private void initUI() {
        imgAvatar = findViewById(R.id.img_avatar_edit);
        imgBack =findViewById(R.id.img_back_user_profile);
        edtName = findViewById(R.id.edt_name_user);
        edtEmail = findViewById(R.id.edt_email_user);
        btnUpdateProfile = findViewById(R.id.btn_update_profile);
    }

    private void initListener() {
        imgAvatar.setOnClickListener(view -> {
            onClickRequestPermission();

        });

        imgBack.setOnClickListener(view -> {
            finish();
        });

        btnUpdateProfile.setOnClickListener(view -> {
            onClickUpdateProfile();
            onClickSaveProfile();
        });

    }

    private void getUserInformation() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            return;
        }
        String userID = user.getUid();
        edtEmail.setText(user.getEmail());
        edtName.setText(user.getDisplayName());
        Glide.with(this).load(user.getPhotoUrl()).error(R.drawable.ic_avatar).into(imgAvatar);
    }

    private void onClickRequestPermission() {
        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                openGallery();
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                Toast.makeText(UserProfile.this,"xin hay cấp quyền cho ứng dụng",Toast.LENGTH_SHORT).show();
            }
        };

        TedPermission.create()
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("nếu bạn từ chối cấp quyền,bạn sẽ không thể sử dụng dịch vụ\n\nbạn có thể cấp quyền tại [Setting] > [Permission]")
                .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE)
                .check();
    }

    private void openGallery() {
        mTakeImage.launch("image/*");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void onClickSaveProfile() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            return;
        }
        String userID = user.getUid();
        String userEmail = user.getEmail();
        String username = user.getDisplayName();


        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        storageRef.child("ImageProfile/"+userID.toString()).putFile(photoURL).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                storageRef.child("ImageProfile/"+userID.toString()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        User mUser = new User(userEmail,username,uri.toString());
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference myRef = database.getReference("Users");
                        myRef.child(userID).setValue(mUser);
                    }
                });
            }
        });
    }

    private void onClickUpdateProfile() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            return;
        }
        String strName = edtName.getText().toString().trim();

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(strName)
                .setPhotoUri(photoURL)
                .build();

        user.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(UserProfile.this,"Đã cập nhật thông tin",Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
}