package com.oscarlolero.firebaserealtimechat.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.oscarlolero.firebaserealtimechat.R;
import com.oscarlolero.firebaserealtimechat.adapters.UsersAdapter;
import com.oscarlolero.firebaserealtimechat.pojos.User;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class usersFragment extends Fragment {

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        View view = inflater.inflate(R.layout.fragment_users, container, false);

        TextView tvUser = view.findViewById(R.id.tvUser);
        ImageView imgUser = view.findViewById(R.id.imgUser);

        assert user != null;
        tvUser.setText(user.getDisplayName() + " (you)");

        Glide.with(this).load(user.getPhotoUrl()).into(imgUser);

        RecyclerView recyclerView;
        ArrayList<User> userArrayList;
        UsersAdapter usersAdapter;
        LinearLayoutManager linearLayoutManager;

        linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(linearLayoutManager);
        userArrayList = new ArrayList<>();
        usersAdapter = new UsersAdapter(userArrayList, getContext());
        recyclerView.setAdapter(usersAdapter);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("Users");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    userArrayList.removeAll(userArrayList);
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        User user = snapshot.getValue(User.class);
                        userArrayList.add(user);
                    }
                    usersAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getContext(), "No users found!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        return view;
    }
}