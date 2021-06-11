package com.oscarlolero.firebaserealtimechat.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.oscarlolero.firebaserealtimechat.fragments.chatsFragment;
import com.oscarlolero.firebaserealtimechat.fragments.myRequestsFragment;
import com.oscarlolero.firebaserealtimechat.fragments.requestsFragment;
import com.oscarlolero.firebaserealtimechat.fragments.usersFragment;

import org.jetbrains.annotations.NotNull;

public class PagesAdapter extends FragmentStateAdapter {
    public PagesAdapter(@NonNull @NotNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @NotNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new usersFragment();
            case 1:
                return new chatsFragment();
            case 2:
                return new requestsFragment();
            case 3:
                return new myRequestsFragment();
            default:
                return new usersFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
