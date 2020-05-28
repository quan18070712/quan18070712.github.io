package com.example.myapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import Model.One_line_message;


import java.util.List;

import utils.ImageConverter;

public class NameAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private List<One_line_message> lst;
    private TextView txtTen;
    private TextView txtmessage;
    private ImageView avatar;
    private ImageView stt;

    public NameAdapter(Context context, int layout, List<One_line_message> lst) {
        this.context = context;
        this.layout = layout;
        this.lst = lst;
    }
    @Override
    public int getCount() {
        return lst.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    private class ViewHolder{
        TextView txt_Ten;
        TextView txt_message;
        ImageView avatar;
        ImageView stt;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null){
            LayoutInflater layoutInf = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInf.inflate(layout,null);
            holder = new ViewHolder();

            //ánh xạ
            holder.txt_Ten = (TextView) convertView.findViewById(R.id.ten);
            holder.txt_message = (TextView) convertView.findViewById(R.id.lastmess);
            holder.avatar = (ImageView) convertView.findViewById(R.id.img1);

            holder.stt = (ImageView) convertView.findViewById(R.id.status);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        One_line_message one = lst.get(position);
        holder.txt_Ten.setText(one.getName());
        holder.txt_message.setText(one.getLastMessage());

        byte[] mangHinh = Base64.decode(one.getImage(),Base64.DEFAULT);
        Bitmap bmp = BitmapFactory.decodeByteArray(mangHinh, 0 , mangHinh.length);
        
        holder.avatar.setImageBitmap(bmp);
        if (one.getStatus().equals("online")) {
            holder.stt.setImageResource(R.drawable.status_icon);
        }
        else {
            holder.stt.setImageResource(R.drawable.offline_status);
        }
        return convertView;

        
    }
}
