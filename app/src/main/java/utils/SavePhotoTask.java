package utils;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;

public class SavePhotoTask extends AsyncTask<byte[], String, String> {
    private String filePath;

    public String getFilePath() {
        return filePath;
    }

    @Override
    public String doInBackground(byte[]... jpeg) {
        String fileName = RandomString.getAlphaNumericString(10);
        File photo=new File(Environment.getExternalStorageDirectory(), fileName+".jpg");
        filePath = photo.getPath();
//        File photo=new File(Environment.DIRECTORY_PICTURES, fileName+".jpg");

        if (photo.exists()) {
            photo.delete();
        }

        try {
            FileOutputStream fos=new FileOutputStream(filePath);

            fos.write(jpeg[0]);
            fos.close();
        }
        catch (java.io.IOException e) {
            Log.e("PictureDemo", "Exception in photoCallback", e);
        }

        return(null);
    }



}
