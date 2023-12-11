package com.example.connectamovil;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {

    private TextView textViewUsername, textViewEmail;
    private EditText editTextNewUsername;
    private Button buttonUpdateProfile;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user == null) {
            // no hay usuario autenticado, redirige a la pantalla de inicio de sesión
            startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
            finish();
        }

        databaseReference = FirebaseDatabase.getInstance().getReference("users").child(user.getUid());

        textViewUsername = findViewById(R.id.textViewUsername);
        textViewEmail = findViewById(R.id.textViewEmail);
        editTextNewUsername = findViewById(R.id.editTextNewUsername);
        buttonUpdateProfile = findViewById(R.id.buttonUpdateProfile);

        buttonUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfile();
            }
        });

        loadUserProfile();
    }

    private void loadUserProfile() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            textViewEmail.setText("Email: " + user.getEmail());

            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        String username = snapshot.child("username").getValue(String.class);
                        textViewUsername.setText("Username: " + username);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(ProfileActivity.this, "Error al cargar el perfil", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void updateProfile() {
        String newUsername = editTextNewUsername.getText().toString().trim();

        if (!TextUtils.isEmpty(newUsername)) {
            databaseReference.child("username").setValue(newUsername);
            Toast.makeText(ProfileActivity.this, "Perfil actualizado exitosamente", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(ProfileActivity.this, "Ingrese un nuevo nombre de usuario", Toast.LENGTH_SHORT).show();
        }
    }
}
