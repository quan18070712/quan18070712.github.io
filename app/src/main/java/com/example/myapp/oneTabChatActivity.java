package com.example.myapp;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import Model.Message;
import Model.One_line_message;
import Model.User;
import de.hdodenhof.circleimageview.CircleImageView;


public class oneTabChatActivity extends AppCompatActivity {
    ImageButton btnsend;
    CircleImageView img_avatar_OneTabChat;
    ImageButton btn_chooseImage;
    ImageButton btn_callVideo;
    EditText edt_messageContent;
    TextView txt_clientname;
    Context context = this;
    FirebaseUser me;
    String friendUid;
    One_line_message friend;
    public User _friend;
    List<Message> messageList;
    RecyclerView recyclerView;
    DatabaseReference reference;
    DatabaseReference reference2;
    MessageAdapter messageAdapter;
    ImageButton btn_backchat;
    String Fname;
    String Fimage;
    String Fmail;
    final int REQUEST_CODE_IMAGE = 1;
    private final int PICK_IMAGE_REQUEST = 71 ;
    private Uri filePath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_tab_chat);
        initPermission();
        Intent it = getIntent();
        friendUid =  it.getStringExtra("friend");

        me = FirebaseAuth.getInstance().getCurrentUser();
        if (me == null){
            Toast.makeText(oneTabChatActivity.this,"NULL",Toast.LENGTH_SHORT).show();
        }
        
//        Toast.makeText(oneTabChatActivity.this,_friend.getEmail(),Toast.LENGTH_SHORT).show();
        try {
            Anhxa();
        } catch (IOException e) {
            e.printStackTrace();
        }

        updateStatus("online");
        btnsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!edt_messageContent.getText().toString().equals("")){
                    sendMessage(me.getEmail(),_friend.getEmail(),edt_messageContent.getText().toString(),"normal");
                }
                else {
                    Toast.makeText(oneTabChatActivity.this,"Nhập tin nhắn",Toast.LENGTH_SHORT).show();
                }

            }
        });

        reference = FirebaseDatabase.getInstance().getReference("List of members!!!").child(friendUid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                readMessage();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btn_chooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Todo: sự kiện cho btn chọn ảnh từ điện thoại
                ImageView img_send = new ImageView(oneTabChatActivity.this);
                img_send.setId(R.id.img_message);
                SelectImage();


            }
        });
        btn_callVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Todo: sự kiện cho btn call video
            }
        });
        btn_backchat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it1 = new Intent(oneTabChatActivity.this,My_message_window.class);
                startActivity(it1);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CODE_IMAGE && resultCode == RESULT_OK && data != null){
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();
            String string = Base64.encodeToString(byteArray,Base64.DEFAULT);
            sendMessage(me.getEmail(),_friend.getEmail(),string,"image");
        }

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            try {
                Bitmap bmp = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();
                String string = Base64.encodeToString(byteArray,Base64.DEFAULT);
                sendMessage(me.getEmail(),_friend.getEmail(),string,"image");
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    public void Anhxa() throws IOException {
        reference2 = FirebaseDatabase.getInstance().getReference("List of members!!!");
        reference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    if(snapshot.getKey().equals(friendUid)){

                        _friend = snapshot.getValue(User.class);
                        assert  _friend != null;
//                        Toast.makeText(oneTabChatActivity.this,_friend.getName(),Toast.LENGTH_SHORT).show();

                        Fname = _friend.getName();
                        Fmail = _friend.getEmail();
                        Fimage= _friend.getAvatar();
                        txt_clientname.setText(Fname);
                        byte[] mangHinh = Base64.decode(_friend.getAvatar(),Base64.DEFAULT);
                        Bitmap bmp = BitmapFactory.decodeByteArray(mangHinh, 0 , mangHinh.length);
                        img_avatar_OneTabChat.setImageBitmap(bmp);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        btn_backchat = (ImageButton) findViewById(R.id.btn_backchat);
        btnsend = (ImageButton) findViewById(R.id.btnsend);
        btn_callVideo = (ImageButton) findViewById(R.id.btn_callVideol);
        btn_chooseImage = (ImageButton) findViewById(R.id.btn_chooseImage);
        img_avatar_OneTabChat = (CircleImageView) findViewById(R.id.img_avatar_OneTapChat);
        txt_clientname = (TextView) findViewById(R.id.txt_clientname);
        txt_clientname.setText(Fname);
        edt_messageContent = (EditText) findViewById(R.id.edt_messageContent);
        recyclerView = (RecyclerView) findViewById(R.id.RC_Compose);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);




    }
    private void sendMessage(String sender,String receiver, String message,String type){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("sender",sender);
        hashMap.put("receiver",receiver);
        hashMap.put("message",message);
        hashMap.put("type",type);
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        hashMap.put("time",formatter.format(date));
        reference.child("ChatsContent").push().setValue(hashMap);
        edt_messageContent.setText("");

    }
    private void readMessage(){
        messageList = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("ChatsContent");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                messageList.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Message mes = snapshot.getValue(Message.class);
                    if (mes == null) Toast.makeText(oneTabChatActivity.this,"Message is NULL",Toast.LENGTH_SHORT).show();
                    if((mes.getReceiver().equals(me.getEmail()) && mes.getSender().equals(_friend.getEmail())) || (mes.getReceiver().equals(_friend.getEmail()) && mes.getSender().equals(me.getEmail()))){
                        messageList.add(mes);
                    }
                    messageAdapter = new MessageAdapter(oneTabChatActivity.this,messageList);
                    recyclerView.setAdapter(messageAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

private void updateStatus(String stt){
    reference = FirebaseDatabase.getInstance().getReference("List of members!!!").child(me.getUid());
    HashMap<String,Object> hashMap = new HashMap<>();
    hashMap.put("status",stt);
    reference.updateChildren(hashMap);
}

//    @Override
//    protected void onPause() {
//        super.onPause();
//        updateStatus("offline");
//    }

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
    private void SelectImage(){

        final CharSequence[] items={"Camera","Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(oneTabChatActivity.this);
        builder.setTitle("Add Image");

        builder.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (items[i].equals("Camera")) {

                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_CODE_IMAGE);

                } else if (items[i].equals("Gallery")) {

                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);

                } else if (items[i].equals("Cancel")) {
                    dialogInterface.dismiss();
                }
            }
        });
        builder.show();

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults.length == 1 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(oneTabChatActivity.this, "Permision Write File is Granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(oneTabChatActivity.this, "Permision Write File is Denied", Toast.LENGTH_SHORT).show();

            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public void initPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                //Permisson don't granted
                if (shouldShowRequestPermissionRationale(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    Toast.makeText(oneTabChatActivity.this, "Permission isn't granted ", Toast.LENGTH_SHORT).show();
                }
                // Permisson don't granted and dont show dialog again.
                else {
                    Toast.makeText(oneTabChatActivity.this, "Permisson don't granted and dont show dialog again ", Toast.LENGTH_SHORT).show();
                }
                //Register permission
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

            }
        }
    }

}
