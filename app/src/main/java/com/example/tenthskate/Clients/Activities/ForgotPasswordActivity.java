package com.example.tenthskate.Clients.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tenthskate.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class ForgotPasswordActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        mAuth = FirebaseAuth.getInstance();

        final EditText forgotPasswordEmailInput=(EditText)findViewById(R.id.forgot_user_email);
        Button resetPasswordButton=(Button)findViewById(R.id.reset_password_user_button);
        TextView goBackToLoginActivity=(TextView)findViewById(R.id.forgot_password_page_go_back);
        loadingBar = new ProgressDialog(ForgotPasswordActivity.this,R.style.MyAlertDialogStyle);

        goBackToLoginActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent forgotPassword = new Intent(ForgotPasswordActivity.this,LoginActivity.class);
                startActivity(forgotPassword);
                finish();
            }
        });

        resetPasswordButton.setOnClickListener(v -> {
            mAuth=FirebaseAuth.getInstance();

            String forgotPasswordEmail=forgotPasswordEmailInput.getText().toString();

            if (forgotPasswordEmail.isEmpty()){
                forgotPasswordEmailInput.setError("Email required!");
                forgotPasswordEmailInput.requestFocus();
                return;
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(forgotPasswordEmail).matches()){
                forgotPasswordEmailInput.setError("Enter a valid Email!");
                forgotPasswordEmailInput.requestFocus();
                return;
            }
            reset_password(forgotPasswordEmail);
        });
    }

    private void reset_password(String forgotPasswordEmail) {

        loadingBar.setTitle("Reset Password");
        loadingBar.setMessage("Please wait, while we are sending Reset Password Link to your email address");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        mAuth.sendPasswordResetEmail(forgotPasswordEmail)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        loadingBar.dismiss();
                        Toast.makeText(ForgotPasswordActivity.this,"Check your email to reset password!!!",Toast.LENGTH_LONG).show();
                        startActivity(new Intent(ForgotPasswordActivity.this,LoginActivity.class));
                        finish();
                    }else {
                        loadingBar.dismiss();
                        String error = Objects.requireNonNull(task.getException()).getMessage();
                        Toast.makeText(ForgotPasswordActivity.this,"Error: "+error, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onBackPressed() {
        Intent forgotPassword = new Intent(ForgotPasswordActivity.this,LoginActivity.class);
        startActivity(forgotPassword);
        finish();
    }
}