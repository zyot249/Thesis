package zyot.shyn.healthcareapp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.bumptech.glide.Glide;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import zyot.shyn.healthcareapp.R;
import zyot.shyn.healthcareapp.base.BaseActivity;
import zyot.shyn.healthcareapp.base.Constants;
import zyot.shyn.healthcareapp.model.User;
import zyot.shyn.healthcareapp.service.SuperviseHumanActivityService;

public class MainActivity extends BaseActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    private FirebaseAuth mAuth;
    private DatabaseReference dbReference;
    private FirebaseUser firebaseUser;

    private AppBarConfiguration mAppBarConfiguration;

    private ImageView avaImg;
    private TextView displayNameTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_practice, R.id.nav_report, R.id.nav_family, R.id.nav_setting)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        View navHeader = navigationView.getHeaderView(0);
        avaImg = navHeader.findViewById(R.id.ava_img);
        displayNameTxt = navHeader.findViewById(R.id.display_name_txt);

        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();
        dbReference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());

        dbReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                displayNameTxt.setText(user.getDisplayName());
                if (user.getAvatar().equals("default"))
                    avaImg.setImageResource(R.mipmap.ic_launcher_round);
                else
                    Glide.with(getApplicationContext()).load(user.getAvatar()).into(avaImg);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Intent startIntent = new Intent(this, SuperviseHumanActivityService.class);
        startIntent.setAction(Constants.START_FOREGROUND);
        startService(startIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_logout) {
            MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(this);
            dialogBuilder
                    .setTitle("Logout")
                    .setMessage("Are you sure to logout?")
                    .setPositiveButton("YES", (dialog, which) -> {
                        mAuth.signOut();
                        Intent stopIntent = new Intent(this, SuperviseHumanActivityService.class);
                        stopIntent.setAction(Constants.STOP_FOREGROUND);
                        startService(stopIntent);

                        Intent intent = new Intent(this, SignInActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }).setNegativeButton("NO", (dialog, which) -> {

            }).show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        Intent stopIntent = new Intent(this, SuperviseHumanActivityService.class);
//        stopIntent.setAction(Constants.STOP_FOREGROUND);
//        startService(stopIntent);
    }
}