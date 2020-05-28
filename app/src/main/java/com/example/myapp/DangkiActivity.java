package com.example.myapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

import utils.ImageViewToString;


public class DangkiActivity extends AppCompatActivity {
    Button btn_xacnhandangki, btnUpload;
    EditText editTK, editMK, editTen;
    ImageView imgHinh;
    private Uri filePath;
    int REQUEST_CODE_IMAGE = 1;
    private final int PICK_IMAGE_REQUEST = 71;

    FirebaseAuth mAuthencation;
    private DatabaseReference mData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dangki);

        mAuthencation = FirebaseAuth.getInstance();
        mData = FirebaseDatabase.getInstance().getReference();

        Anhxa1();

        btn_xacnhandangki.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Todo:  gửi thông tin đăng kí lên firebase
                DangKy();
            }
        });

        imgHinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectImage();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CODE_IMAGE && resultCode == RESULT_OK && data != null){
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            imgHinh.setImageBitmap(bitmap);
        }

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imgHinh.setImageBitmap(bitmap);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    private void DangKy(){
        final String name,email,password;


        if (editTen.getText().toString().length() == 0) {
            Toast.makeText(DangkiActivity.this, "Nhập Tên hiển thị", Toast.LENGTH_SHORT).show();
            return;

        } else if(editTen.getText().toString().contains("\\")
                    || editTen.getText().toString().contains("@")
                    || editTen.getText().toString().contains(",")
                    || editTen.getText().toString().contains("{")
                    || editTen.getText().toString().contains("}")
                    || editTen.getText().toString().contains("[")
                    || editTen.getText().toString().contains("]")
                    || editTen.getText().toString().contains("|")
                    || editTen.getText().toString().contains("\"")
                    || editTen.getText().toString().contains("#")
                    || editTen.getText().toString().contains("$")
                    || editTen.getText().toString().contains("%")
                    || editTen.getText().toString().contains("^")
                    || editTen.getText().toString().contains("&")
                    || editTen.getText().toString().contains("*")
                    || editTen.getText().toString().contains("!")
                    || editTen.getText().toString().contains("~")
                    || editTen.getText().toString().contains("+")
                    || editTen.getText().toString().contains("-")
                    || editTen.getText().toString().contains("=")
        ) {
            Toast.makeText(DangkiActivity.this, "Tên không hợp lệ!", Toast.LENGTH_SHORT).show();
            return;
        }
        else {
            name = editTen.getText().toString().trim();
        }


        if(editTK.getText().toString().length()==0){
            Toast.makeText(DangkiActivity.this, "Chưa nhập email!", Toast.LENGTH_SHORT).show();
            return;
        }else if(editTK.getText().toString().trim().contains("@gmail.com") == false && editTK.getText().toString().trim().contains("@hcmut.edu.vn")==false){
            Toast.makeText(DangkiActivity.this, "Email không hợp lệ", Toast.LENGTH_SHORT).show();
            return;
        }
        else {
            email = editTK.getText().toString();
        }

        if(editMK.getText().toString().length()==0){
            Toast.makeText(DangkiActivity.this, "Chưa nhập mật khẩu!", Toast.LENGTH_SHORT).show();
            return;
        }
        else {
            password = editMK.getText().toString();
        }
        mAuthencation.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser user = mAuthencation.getCurrentUser();
                            Toast.makeText(DangkiActivity.this, "Đăng Ký Thành Công", Toast.LENGTH_SHORT).show();
                            DangKyThanhVien dk = new DangKyThanhVien(name, email, password,user.getUid(),"offline");
                            assert user != null;
                            String userID = user.getUid();
                            final String  avatar_string = new ImageViewToString(imgHinh).getString();
                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("List of members!!!").child(userID);


                            HashMap<String,String> hashMap = new HashMap<>();
                            hashMap.put("email",email);
                            hashMap.put("name",name);
                            hashMap.put("pass",password);
                            hashMap.put("status","offline");
                            hashMap.put("uid",user.getUid());
                            hashMap.put("avatar",avatar_string);


                            reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Intent intent_to_message = new Intent(DangkiActivity.this,My_message_window.class);
                                        startActivity(intent_to_message);
                                    }
                                    else {
                                        editTen.setText(Objects.requireNonNull(task.getException()).toString());
                                    }

                                }
                            });

                        }
                        else {
                            Toast.makeText(DangkiActivity.this, "Đăng kí thất bại!", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

//    private void uploadImage() {
//
//        if(filePath != null)
//        {
//            final ProgressDialog progressDialog = new ProgressDialog(this);
//            progressDialog.setTitle("Uploading...");
//            progressDialog.show();
//
//            StorageReference ref = storageReference.child("images/"+ UUID.randomUUID().toString());
//            ref.putFile(filePath)
//                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                        @Override
//                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                            progressDialog.dismiss();
//                            Toast.makeText(DangkiActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
//                        }
//                    })
//                    .addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//                            progressDialog.dismiss();
//                            Toast.makeText(DangkiActivity.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
//                        }
//                    })
//                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
//                        @Override
//                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
//                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
//                                    .getTotalByteCount());
//                            progressDialog.setMessage("Uploaded "+(int)progress+"%");
//                        }
//                    });
//        }
//    }

    private void SelectImage(){

        final CharSequence[] items={"Camera","Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(DangkiActivity.this);
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

    public void Anhxa1(){
        btn_xacnhandangki = (Button) findViewById(R.id.btn_xacnhandangki);
        editTen           = (EditText) findViewById(R.id.edt_tenhienthi);
        editTK            = (EditText) findViewById(R.id.edt_emaildangki);
        editMK            = (EditText) findViewById(R.id.edt_passdangki);
        imgHinh           = (ImageView) findViewById(R.id.img_avatardangki);
    }
}


