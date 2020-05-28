package com.example.myapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Model.One_line_message;
import Model.User;
import de.hdodenhof.circleimageview.CircleImageView;
import utils.ImageConverter;


public class My_message_window extends AppCompatActivity {

    ListView lsv;
    NameAdapter nameAdapter;
    CircleImageView btn_dangxuat;
    FirebaseUser currentUser;
    DatabaseReference reference;
    private List<One_line_message> mUsers;
    private TextView tenhienthi;
    private FirebaseAuth mAuth;
    private String avatar;
    private CircleImageView img_myavatar;
    public My_message_window context = this;
    private String resetPass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);;
        setContentView(R.layout.activity_my_message_window);
        mUsers = new ArrayList<One_line_message>();
        mAuth = FirebaseAuth.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("List of members!!!");
        Intent it_reset = getIntent();
        resetPass = it_reset.getStringExtra("resetPass");
        Anhxa();

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUsers.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    User user = snapshot.getValue(User.class);
                    assert user != null;
                    assert currentUser != null;

                    if (!user.getEmail().equals(currentUser.getEmail())){
                        mUsers.add(new One_line_message(user.getEmail(),user.getName(),user.getAvatar(),"",user.getStatus(),user.getUid()));
                    }
                    else {
                        tenhienthi.setText(user.getName());
                        byte[] mangHinh = Base64.decode(user.getAvatar(),Base64.DEFAULT);
                        Bitmap bmp = BitmapFactory.decodeByteArray(mangHinh, 0 , mangHinh.length);
                        img_myavatar.setImageBitmap(bmp);
                        if( resetPass==null ){} else if(   !resetPass.equals(user.getPass()))
                        {
                            HashMap<String, Object> result = new HashMap<>();
                            result.put("pass", resetPass);
                            snapshot.child("pass").getRef().updateChildren(result);
                        }

                    }
                }

                nameAdapter = new NameAdapter(My_message_window.this,R.layout.oneline_message,mUsers);
                lsv.setAdapter(nameAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        lsv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent it;
                it = new Intent(My_message_window.this, oneTabChatActivity.class);
                it.putExtra("friend",mUsers.get(position).getUID());
//                Toast.makeText(My_message_window.this,mUsers.get(position).getStatus(),Toast.LENGTH_SHORT).show();
                startActivity(it);
            }
        });

        btn_dangxuat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                finish();
                startActivity(new Intent(My_message_window.this,MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });
        img_myavatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(My_message_window.this,ProfileActivity.class));
            }
        });
    }
    
    public void Anhxa(){
        lsv =  findViewById(R.id.lsv_user) ;
        img_myavatar = findViewById(R.id.img_myavatar);
        btn_dangxuat = findViewById(R.id.btn_dangxuat);
        tenhienthi =  findViewById(R.id.edt_tenhienthi);



        Bitmap my_avatar_bitmap = BitmapFactory.decodeResource(this.getResources(),R.drawable.default_avatar);
        //Todo: thay R.drawable.default_avatar bằng ảnh lấy từ bundle

        Bitmap my_avatar_circularBitmap = ImageConverter.getRoundedCornerBitmap(my_avatar_bitmap, 500);
        img_myavatar.setImageBitmap(my_avatar_circularBitmap);

    }

    private void updateStatus(String stt){
        reference = FirebaseDatabase.getInstance().getReference("List of members!!!").child(currentUser.getUid());
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("status",stt);
        reference.updateChildren(hashMap);
    }

    @Override
    protected void onPause() {
        super.onPause();
        updateStatus("offline");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        updateStatus("online");
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateStatus("online");
    }

    @Override
    protected void onStop() {
        super.onStop();
        updateStatus("offline");
    }
    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }
}
