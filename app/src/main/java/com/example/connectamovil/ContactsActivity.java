package com.example.connectamovil;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ContactsActivity extends AppCompatActivity {

    private EditText editTextContactName;
    private Button buttonAddContact;
    private ListView listViewContacts;

    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;

    private ArrayList<String> contactList;
    private ContactsAdapter contactsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("contacts").child(firebaseAuth.getUid());

        editTextContactName = findViewById(R.id.editTextContactName);
        buttonAddContact = findViewById(R.id.buttonAddContact);
        listViewContacts = findViewById(R.id.listViewContacts);

        contactList = new ArrayList<>();
        contactsAdapter = new ContactsAdapter(this, contactList);
        listViewContacts.setAdapter(contactsAdapter);

        buttonAddContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addContact();
            }
        });

        // escucha los cambios en la base de datos cuando se agregan nuevos contactos
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, String s) {
                String contactName = dataSnapshot.getValue(String.class);
                contactList.add(contactName);
                contactsAdapter.notifyDataSetChanged();
            }

            // implementa otros métodos según tus necesidades (onChildChanged, onChildRemoved, onChildMoved)
            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, String s) {}

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {}

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, String s) {}

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }

    private void addContact() {
        String contactName = editTextContactName.getText().toString().trim();

        if (!contactName.isEmpty()) {
            // Agrega el nuevo contacto a la base de datos
            databaseReference.push().setValue(contactName);
            editTextContactName.setText("");
        } else {
            Toast.makeText(ContactsActivity.this, "Ingrese un nombre de contacto", Toast.LENGTH_SHORT).show();
        }
    }
}
