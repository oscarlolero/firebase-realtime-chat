package com.oscarlolero.firebaserealtimechat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.oscarlolero.firebaserealtimechat.adapters.PagesAdapter;
import com.oscarlolero.firebaserealtimechat.pojos.User;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class HomeActivity extends AppCompatActivity {

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference defaultUserRef = database.getReference("Users").child(user.getUid());
    DatabaseReference defaultUserRequestsCountRef = database.getReference("Users").child(user.getUid()).child("requests");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_FirebaseRealtimeChatWithActionBar);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        setupTabLayout();
        singleUser();

    }

    private void singleUser() {
        defaultUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    User userFirebase = new User(
                            user.getUid(),
                            user.getDisplayName(),
                            user.getEmail(),
                            Objects.requireNonNull(user.getPhotoUrl()).toString(),
                            "OFFLINE",
                            "11/06/2021",
                            "01:15pm",
                            0,
                            0
                    );
                    defaultUserRef.setValue(userFirebase);
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

    }

    private void setupTabLayout() {
        ViewPager2 viewPager2 = findViewById(R.id.viewPager);
        viewPager2.setAdapter(new PagesAdapter(this));

        TabLayout tabLayout = findViewById(R.id.tabLayout);
        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(tabLayout, viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull @NotNull TabLayout.Tab tab, int position) {
                switch (position) {
                    case 0:
                        tab.setText("Users");
                        tab.setIcon(R.drawable.ic_users);
                        break;
                    case 1:
                        tab.setText("Chats");
                        tab.setIcon(R.drawable.ic_chats);
                        break;
                    case 2:
                        tab.setText("Requests");
                        tab.setIcon(R.drawable.ic_requests);

                        defaultUserRequestsCountRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                if(snapshot.exists()) {
                                    BadgeDrawable badgeDrawable = tab.getOrCreateBadge();
                                    Integer count = snapshot.getValue(Integer.class);
                                    if(count == null || count == 0) {
                                        badgeDrawable.setVisible(false);
                                    } else {
                                        badgeDrawable.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent));
                                        badgeDrawable.setNumber(count);
                                        badgeDrawable.setVisible(true);
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull @NotNull DatabaseError error) {

                            }
                        });
                        break;
                    case 3:
                        tab.setText("My requests");
                        tab.setIcon(R.drawable.ic_my_requests);
                        break;
                }
            }
        });
        tabLayoutMediator.attach();

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                if(position == 2) {
                    BadgeDrawable badgeDrawable = Objects.requireNonNull(tabLayout.getTabAt(position)).getOrCreateBadge();
                    resetPendingRequests(badgeDrawable);
                }
            }
        });
    }

    private void resetPendingRequests(BadgeDrawable badgeDrawable) {
        defaultUserRequestsCountRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    badgeDrawable.setVisible(false);
                    defaultUserRequestsCountRef.setValue(0);
                    Toast.makeText(HomeActivity.this, "Counter resetted", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_close:
                AuthUI.getInstance()
                        .signOut(this)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull @NotNull Task<Void> task) {
                                finish();
                                Toast.makeText(HomeActivity.this, "Signing out...", Toast.LENGTH_SHORT).show();
                                goToLogin();

                            }
                        });
        }

        return super.onOptionsItemSelected(item);
    }

    private void goToLogin() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}