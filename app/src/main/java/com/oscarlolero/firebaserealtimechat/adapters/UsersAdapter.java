package com.oscarlolero.firebaserealtimechat.adapters;

import android.content.Context;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.oscarlolero.firebaserealtimechat.R;
import com.oscarlolero.firebaserealtimechat.pojos.User;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.viewHolderAdapter> {

    List<User> userList;
    Context context;
    FirebaseUser defaultUser = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    public UsersAdapter(List<User> userList, Context context) {
        this.userList = userList;
        this.context = context;
    }

    @NonNull
    @NotNull
    @Override
    public viewHolderAdapter onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_users, parent, false);
        return new viewHolderAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull UsersAdapter.viewHolderAdapter holder, int position) {
        User userFromList = userList.get(position);
        final Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);

        Glide.with(context).load(userFromList.getPicture()).into(holder.imgUser);
        holder.tvUser.setText(userFromList.getName());

        if (defaultUser.getUid().equals(userFromList.getId())) {
            holder.cardView.setVisibility(View.GONE);
        } else {
            holder.cardView.setVisibility(View.VISIBLE);
        }

        setupDefaultUserButtons(holder, userFromList);
        updateSendRequestAction(holder, userFromList, vibrator);
    }

    private void setupDefaultUserButtons(@NotNull viewHolderAdapter holder, User userFromList) {
        DatabaseReference buttonsRef = database.getReference("Users").child(defaultUser.getUid()).child("pendingRequests").child(userFromList.getId());
        buttonsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                String status = snapshot.child("state").getValue(String.class);
                if (snapshot.exists()) {
                    assert status != null;
                    if (status.equals("SENT")) {
                        holder.btnRequestSent.setVisibility(View.VISIBLE);
                        holder.btnIncomingRequest.setVisibility(View.GONE);
                        holder.btnFriends.setVisibility(View.GONE);
                        holder.btnSendRequest.setVisibility(View.GONE);
                        holder.progressBar.setVisibility(View.GONE);
                    }
                    if (status.equals("FRIENDS")) {
                        holder.btnRequestSent.setVisibility(View.GONE);
                        holder.btnIncomingRequest.setVisibility(View.GONE);
                        holder.btnFriends.setVisibility(View.VISIBLE);
                        holder.btnSendRequest.setVisibility(View.GONE);
                        holder.progressBar.setVisibility(View.GONE);
                    }
                    if (status.equals("INCOMING_REQUEST")) {
                        holder.btnRequestSent.setVisibility(View.GONE);
                        holder.btnIncomingRequest.setVisibility(View.VISIBLE);
                        holder.btnFriends.setVisibility(View.GONE);
                        holder.btnSendRequest.setVisibility(View.GONE);
                        holder.progressBar.setVisibility(View.GONE);
                    }
                } else {
                    holder.btnRequestSent.setVisibility(View.GONE);
                    holder.btnIncomingRequest.setVisibility(View.GONE);
                    holder.btnFriends.setVisibility(View.GONE);
                    holder.btnSendRequest.setVisibility(View.VISIBLE);
                    holder.progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    private void updateSendRequestAction(@NotNull viewHolderAdapter holder, User userFromList, Vibrator vibrator) {
        holder.btnSendRequest.setOnClickListener(v -> {
            DatabaseReference defaultAppUser = database.getReference("Users").child(defaultUser.getUid()).child("pendingRequests");
            defaultAppUser.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                    defaultAppUser.child(userFromList.getId()).child("state").setValue("SENT");
                }

                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                }
            });

            DatabaseReference newFriendUser = database.getReference("Users").child(userFromList.getId()).child("pendingRequests");
            newFriendUser.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                    newFriendUser.child(defaultUser.getUid()).child("state").setValue("INCOMING_REQUEST");
                }

                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                }
            });

            DatabaseReference requestsCount = database.getReference("Users").child(userFromList.getId()).child("requests");
            requestsCount.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        Integer currentCount = snapshot.getValue(Integer.class);
                        if (currentCount == null || currentCount == 0) {
                            requestsCount.setValue(1);
                        } else {
                            requestsCount.setValue(currentCount + 1);
                        }

                    }
                }

                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                }
            });

            vibrator.vibrate(300);
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class viewHolderAdapter extends RecyclerView.ViewHolder {
        TextView tvUser;
        ImageView imgUser;
        CardView cardView;
        Button btnRequestSent, btnFriends, btnIncomingRequest;
        ImageButton btnSendRequest;
        ProgressBar progressBar;

        public viewHolderAdapter(@NonNull @NotNull View itemView) {
            super(itemView);
            tvUser = itemView.findViewById(R.id.tvUser);
            imgUser = itemView.findViewById(R.id.imgUser);
            cardView = itemView.findViewById(R.id.cardView);
            btnSendRequest = itemView.findViewById(R.id.btnSendRequest);
            btnRequestSent = itemView.findViewById(R.id.btnRequestSent);
            btnFriends = itemView.findViewById(R.id.btnFriend);
            btnIncomingRequest = itemView.findViewById(R.id.btnIncomingRequest);
            progressBar = itemView.findViewById(R.id.progress_bar);
        }
    }
}
