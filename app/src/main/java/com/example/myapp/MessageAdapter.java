//package com.example.myapp;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import java.util.List;
//
//import PeerToPeer.MyMessage;
//
//public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
//    public static final int MSG_TYPE_LEFT = 0;
//    public static final int MSG_TYPE_RIGHT = 1;
//
//
//
//    private Context mContext;
//    private List<MyMessage> myMessages;
//
//    public MessageAdapter(Context mContext, List<MyMessage> myMessages) {
//        this.mContext = mContext;
//        this.myMessages = myMessages;
//    }
//
//    @NonNull
//    @Override
//    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(mContext).inflate(R.layout.my_message,parent,false);
//
//
//        return new MessageAdapter.ViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        MyMessage one_message = myMessages.get(position);
//        holder.message.setText(one_message.getContent());
//        holder.time.setText(one_message.getTime());
//
//
//
//    }
//
//    @Override
//    public int getItemCount() {
//        return myMessages.size();
//    }
//
//    public class ViewHolder extends RecyclerView.ViewHolder{
//        public TextView message;
//        public TextView time;
//        public ViewHolder(View itemView){
//            super(itemView);
//            message = itemView.findViewById(R.id.txtMyMessage);
//            time = itemView.findViewById(R.id.txtMyMessageTime);
//        }
//
//    }
//
//    @Override
//    public int getItemViewType(int position) {
//        if(myMessages.get(position).getSender().equals())
//
//    }
//}
package com.example.myapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import Model.Message;
import utils.RandomString;
import utils.SavePhotoTask;

import static androidx.core.app.ActivityCompat.startActivityForResult;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    public static final int MSG_FROM_FRIEND = 0;
    public static final int MSG_FROM_ME = 1;
    public static final int IMG_MSG_ME = 2;
    public static final int IMG_MSG_FRIEND = 3;
    private Context mContext;
    private List<Message> messageList;
    FirebaseUser fuser;
    int REQUEST_CODE_IMAGE = 1;
    private final int PICK_IMAGE_REQUEST = 71;

    public MessageAdapter(Context mContext, List<Message> messageList) {
        this.mContext = mContext;
        this.messageList = messageList;
    }


    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == MSG_FROM_ME) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.my_message,parent, false);
            MessageAdapter.ViewHolder holder = new MessageAdapter.ViewHolder(view);
            holder.setShow_message(view);
            return holder;

        }
        else if(viewType == MSG_FROM_FRIEND) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.friend_message,parent, false);
            MessageAdapter.ViewHolder holder = new MessageAdapter.ViewHolder(view);
            holder.setShow_message(view);
            return holder;
        }
        else if(viewType == IMG_MSG_FRIEND){
            View view = LayoutInflater.from(mContext).inflate(R.layout.friend_image_message,parent,false);
            MessageAdapter.ViewHolder holder = new MessageAdapter.ViewHolder(view);
            holder.setShow_IMG(view);
            return  holder;
        }
        else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.my_image_message,parent,false);
            MessageAdapter.ViewHolder holder = new MessageAdapter.ViewHolder(view);
            holder.setShow_IMG(view);
            return holder;
        }

    }

    @SuppressLint("ResourceType")
    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.ViewHolder holder, int position) {
        final Message message = messageList.get(position);
        if(message.getType().equals("normal")){
            holder.show_message.setText(message.getMessage());
            holder.show_time.setText(message.getTime());
        }
        else if(message.getType().equals("image")){
            final byte[] mangHinh = Base64.decode(message.getMessage(),Base64.DEFAULT);
            Bitmap bmp = BitmapFactory.decodeByteArray(mangHinh, 0 , mangHinh.length);
            holder.show_image.setImageBitmap(bmp);
            holder.show_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    SavePhotoTask sv=new SavePhotoTask();
                    sv.doInBackground(mangHinh);
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setTitle("Save Image at "+sv.getFilePath());
                    final CharSequence[] items={"Ok"};
                    builder.setItems(items, new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    if (items[i].equals("Ok")) {
                                        dialogInterface.dismiss();
                                    }
                                }
                            });
                    builder.show();

                }
            });
            holder.show_time.setText(message.getTime());
        }

    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView show_message;
        public TextView show_time;
        public ImageView show_image;


        public ViewHolder(View view) {
            super(view);
            show_time = (TextView) view.findViewById(R.id.txt_Mtime);
        }

        public void setShow_IMG(View view){
            show_image = (ImageView)view.findViewById(R.id.img_message);
        }
        public void setShow_message(View view){
            show_message = (TextView)view.findViewById(R.id.txtmyMessage);
        }
    }

    @Override
    public int getItemViewType(int position) {
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        if(messageList.get(position).getType().equals("normal")){
            if (messageList.get(position).getSender().equals(fuser.getEmail())) {
                return MSG_FROM_ME;
            }
            else return MSG_FROM_FRIEND;
        }
        else {
            if (messageList.get(position).getSender().equals(fuser.getEmail())) {
                return IMG_MSG_ME;
            }
            else return IMG_MSG_FRIEND;

        }
    }


}
