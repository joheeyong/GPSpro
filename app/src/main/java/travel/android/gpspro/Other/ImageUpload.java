package travel.android.gpspro.Other;


import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.ImageView;

import travel.android.gpspro.Activity.MainActivity;
import travel.android.gpspro.Fragment.FragmentPage4;

public class ImageUpload {

    public static final int PICK_FROM_ALBUM = 1;

    public static void goToAlbum(FragmentPage4 mainActivity) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        mainActivity.startActivityForResult(intent, ImageUpload.PICK_FROM_ALBUM);
    }

    public static void setImage(String profileURL, ImageView ivProfile) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap originalBm = BitmapFactory.decodeFile(profileURL, options);
        ivProfile.setImageBitmap(originalBm);
    }

    public static String getRealPathFromURI(Uri contentURI, MainActivity mainActivity) {
        String result;
        Cursor cursor = mainActivity.getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }

}

