package com.yuepeng.photo.photoloader;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.CursorLoader;

public class ImageLoader  extends CursorLoader  {
   static Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
    static String selection = MediaStore.Images.Media.MIME_TYPE + "=? or "
            + MediaStore.Images.Media.MIME_TYPE + "=?";
    static String[] selectionArgs=   new String[] { "image/jpeg", "image/png" };
    static String order= MediaStore.Images.Media.DATE_MODIFIED;

    public ImageLoader(@NonNull Context context) {
        super(context);
    }

    public ImageLoader(@NonNull Context context, @NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        super(context, uri, projection, selection, selectionArgs, sortOrder);
    }

    public static CursorLoader newInstance(Context context) {

        return new ImageLoader(context,mImageUri,null,selection, selectionArgs,order);
    }
    @Override
    public Cursor loadInBackground() {
        return super.loadInBackground();
    }
}
