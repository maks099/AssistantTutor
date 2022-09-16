package com.example.assistanttutor;

import android.content.Intent;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import com.example.assistanttutor.databinding.ActivityLoginBinding;
import com.example.assistanttutor.helpers.PreferencesHelper;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private boolean isRegistration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            this.getSupportActionBar().hide();
        } catch (NullPointerException ex){

        }
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if(PreferencesHelper.get(this, "login").equals("")){
            binding.btnInputAction.setText(getString(R.string.registration));
            isRegistration = true;
        } else {
            binding.btnInputAction.setText(getString(R.string.authorization));
            isRegistration = false;
        }

        onActionButtonClick();
    }

    private void onActionButtonClick() {
        binding.btnInputAction.setOnClickListener(e ->{
            String login = binding.edtLogin.getText().toString().trim();
            String password = binding.edtPass.getText().toString().trim();
            System.out.println(password + " ///////////////");
            if(login.length() == 0){
                Toast.makeText(getApplicationContext(), getString(R.string.inputLogin), Toast.LENGTH_SHORT).show();
                return;
            } else if(password.length() == 0){
                Toast.makeText(getApplicationContext(), getString(R.string.inputPassword), Toast.LENGTH_SHORT).show();
                return;
            }
            if(isRegistration){
                PreferencesHelper.save(this, "login", login);
                PreferencesHelper.save(this, "password", hash(password));

            } else{
                String trueLogin = PreferencesHelper.get(this, "login"),
                        truePass = PreferencesHelper.get(this, "password");

                if (!trueLogin.equals(login) || !truePass.equals(hash(password))) {
                    Toast.makeText(this, getString(R.string.checkCredentials), Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            ActivityCompat.finishAffinity(LoginActivity.this);
        });
    }

    private String hash(String pass){
        try{
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] bytes = md.digest(pass.getBytes());
            StringBuilder sb = new StringBuilder();
            for(byte b : bytes){
                sb.append(Integer.toHexString((b & 0xFF) | 0x100).substring(1,3));
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException ex){
            ex.printStackTrace();
            return pass;
        }
    }


}