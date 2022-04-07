package com.example.konect.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.konect.LoginActivity;
import com.example.konect.MainActivity;
import com.example.konect.R;
import com.example.konect.UserProfile;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class ProfileFragment extends Fragment {
    private View mView;

    private Button btnSignOut;
    private ImageView imgAvatar, imgEditProfile;
    private TextView txtName, txtEmail;

    public ProfileFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_profile, container, false);

        initUI();
        initListener();
        getUserInfomation();

        return mView;
    }

    private void initUI() {
        btnSignOut = mView.findViewById(R.id.btn_sign_out);
        imgAvatar = mView.findViewById(R.id.img_avatar_profile);
        txtName = mView.findViewById(R.id.txt_name_profile);
        txtEmail = mView.findViewById(R.id.txt_email_profile);
        imgEditProfile = mView.findViewById(R.id.img_edit_profile);
    }

    private void initListener() {
        btnSignOut.setOnClickListener(view -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent =new Intent(view.getContext(),LoginActivity.class);
            startActivity(intent);
            getActivity().finish();
        });


        imgEditProfile.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), UserProfile.class);
            startActivity(intent);
        });
    }

    public void getUserInfomation(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null){
            return;
        }
        String name = user.getDisplayName();
        Uri photoUrl = user.getPhotoUrl();
        String email = user.getEmail();
        txtEmail.setText(email);
        if (name == null) {
            txtName.setVisibility(View.GONE);
        }else {
            txtName.setVisibility(View.VISIBLE);
            txtName.setText(name);
        }
        Glide.with(this).load(photoUrl).error(R.drawable.ic_avatar).into(imgAvatar);
    }

    @Override
    public void onResume() {
        super.onResume();
        getUserInfomation();
    }
}