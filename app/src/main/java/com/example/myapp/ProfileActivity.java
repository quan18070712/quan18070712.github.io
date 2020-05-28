package com.example.myapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {
    private TextView name;
    private TextView email;
    private TextView id;
    private ImageButton btnBack;
    private CircleImageView imgProfile;
    FirebaseUser me;
    DatabaseReference ref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Anhxa();
        me = FirebaseAuth.getInstance().getCurrentUser();
        Toast.makeText(ProfileActivity.this,me != null ?me.getEmail():"NULL",Toast.LENGTH_SHORT).show();
        ref = FirebaseDatabase.getInstance().getReference("List of members!!!").child(me.getUid());
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                name.setText((String) dataSnapshot.child("name").getValue());
                email.setText((String)dataSnapshot.child("email").getValue());
                id.setText((String)dataSnapshot.child("uid").getValue());
                byte[] mangHinh = Base64.decode((String)dataSnapshot.child("avatar").getValue(),Base64.DEFAULT);
                Bitmap bmp = BitmapFactory.decodeByteArray(mangHinh, 0 , mangHinh.length);
                imgProfile.setImageBitmap(bmp);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it1 = new Intent(ProfileActivity.this,My_message_window.class);
                startActivity(it1);
            }
        });


    }
    private void Anhxa(){
        name            = findViewById(R.id.txtTenhienthi);
        email           = findViewById(R.id.txtEmailProfile);
        id              = findViewById(R.id.txtUserID);
        btnBack         = findViewById(R.id.btnBack);
        imgProfile      = findViewById(R.id.imgProfile);
    }
}
