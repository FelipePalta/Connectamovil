package com.example.connectamovil;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.google.firebase.auth.FirebaseUser;

import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TimerTask tarea = new TimerTask() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        };

        Timer tiempo = new Timer();
        tiempo.schedule(tarea,5000);


        // verifica si el usuario está autenticado
        if (usuarioAutenticado()) {
            iniciarActividad(ContactsActivity.class);
        } else {
            iniciarActividad(LoginActivity.class);
        }
        finish();
    }

    private boolean usuarioAutenticado() {
        // implementa lógica de Firebase Authentication
        FirebaseUser currentUser = mAuth.getCurrentUser();

        // retorna true si el usuario está autenticado, false de lo contrario
        return false;
    }

    private void iniciarActividad(Class<?> clase) {
        Intent intent = new Intent(this, clase);
        startActivity(intent);
    }
    }
