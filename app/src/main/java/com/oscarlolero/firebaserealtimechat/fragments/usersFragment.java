package com.oscarlolero.firebaserealtimechat.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.oscarlolero.firebaserealtimechat.R;

public class usersFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        View view = inflater.inflate(R.layout.fragment_users, container, false);

        TextView tvUser = view.findViewById(R.id.tvUser);
        ImageView imgUser = view.findViewById(R.id.imgUser);

        assert user != null;
        tvUser.setText(user.getDisplayName());

        Glide.with(this).load(user.getPhotoUrl()).into(imgUser);

        return view;
    }
}