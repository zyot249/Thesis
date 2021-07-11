package zyot.shyn.healthcareapp.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import zyot.shyn.healthcareapp.R;
import zyot.shyn.healthcareapp.base.BaseActivity;

public class SignInActivity extends BaseActivity implements View.OnClickListener {
    private EditText emailEt;
    private EditText passwordEt;
    private Button signInBtn;
    private Button signUpBtn;
    private TextView forgotPassText;

    private FirebaseAuth mAuth;

    @Override
    protected void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null){
            Intent intent = new Intent(SignInActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        emailEt = findViewById(R.id.email_et);
        passwordEt = findViewById(R.id.password_et);
        signInBtn = findViewById(R.id.sign_in_btn);
        signUpBtn = findViewById(R.id.sign_up_btn);
        forgotPassText = findViewById(R.id.forgot_pass_text);

        mAuth = FirebaseAuth.getInstance();

        // get data if after register
        Intent intent = getIntent();
        String action = intent.getAction();
        if (action != null){
            if (action.equals("LOGIN_AFTER_REGISTER") || action.equals("LOGIN_AFTER_RESET_PASSWORD")) {
                String email = intent.getStringExtra("user_email");
                if (!email.isEmpty())
                    emailEt.setText(email);
            }
        }

        signInBtn.setOnClickListener(this);
        signUpBtn.setOnClickListener(this);
        forgotPassText.setOnClickListener(this);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACTIVITY_RECOGNITION)
                != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACTIVITY_RECOGNITION}, 1);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_btn:
                String userEmail = emailEt.getText().toString();
                String userPass = passwordEt.getText().toString();

                if (userEmail.isEmpty())
                    Snackbar.make(signInBtn, "Email must be filled", Snackbar.LENGTH_SHORT).show();
                else if (userPass.isEmpty())
                    Snackbar.make(signInBtn, "Password must be filled", Snackbar.LENGTH_SHORT).show();
                else {
                    mAuth.signInWithEmailAndPassword(userEmail, userPass)
                            .addOnCompleteListener(SignInActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Snackbar.make(signInBtn, "Welcome", Snackbar.LENGTH_SHORT).show();
                                        Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    } else
                                        Snackbar.make(signInBtn, "Authentication failed", Snackbar.LENGTH_SHORT).show();
                                }
                            });
                }
                break;
            case R.id.sign_up_btn:
                startActivity(new Intent(SignInActivity.this, SignUpActivity.class));
                break;
            case R.id.forgot_pass_text:
                startActivity(new Intent(SignInActivity.this, ResetPasswordActivity.class));
                break;
        }
    }
}
