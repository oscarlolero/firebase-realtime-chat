package com.oscarlolero.firebaserealtimechat.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.oscarlolero.firebaserealtimechat.R;
import com.oscarlolero.firebaserealtimechat.pojos.User;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.viewHolderAdapter> {

    List<User> userList;
    Context context;
    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

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
        User user = userList.get(position);

        Glide.with(context).load(user.getPicture()).into(holder.imgUser);
        holder.tvUser.setText(user.getName());

        if(firebaseUser.getUid().equals(user.getId())) {
            holder.cardView.setVisibility(View.GONE);
        } else {
            holder.cardView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class viewHolderAdapter extends RecyclerView.ViewHolder {
        TextView tvUser;
        ImageView imgUser;
        CardView cardView;

        public viewHolderAdapter(@NonNull @NotNull View itemView) {
            super(itemView);
            tvUser = itemView.findViewById(R.id.tvUser);
            imgUser = itemView.findViewById(R.id.imgUser);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }
}
