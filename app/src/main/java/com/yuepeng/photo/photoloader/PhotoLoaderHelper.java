/*
 * Copyright (C) 2014 nohana, Inc.
 * Copyright 2017 Zhihu Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an &quot;AS IS&quot; BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.yuepeng.photo.photoloader;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;


import com.yuepeng.photo.photoloader.Bean.FolderBean;
import com.yuepeng.photo.photoloader.Bean.PhotosResultBean;

import java.io.File;
import java.io.FilenameFilter;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PhotoLoaderHelper implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final int LOADER_ID = 2;
    private static final String ARGS_ALBUM = "args_album";
    private static final String ARGS_ENABLE_CAPTURE = "args_enable_capture";
    private WeakReference<Context> mContext;
    private LoaderManager mLoaderManager;
    private PhotoCallbacks mCallbacks;
    private Set<String> mDir = new HashSet<>();
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Context context = mContext.get();
        if (context == null) {
            return null;
        }


        return ImageLoader.newInstance(context);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, final Cursor cursor) {
        Context context = mContext.get();
        if (context == null) {
            return;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                List<FolderBean> mFolderBeanList = new ArrayList<>();
                PhotosResultBean photosResultBean = new PhotosResultBean();
                while (cursor.moveToNext()){
                    String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                    File parentFile = new File(path).getParentFile();
                    if (parentFile==null){
                        continue;
                    }
                    String dirPath = parentFile.getAbsolutePath();
                    FolderBean folderBean = null;
                    if (mDir.contains(dirPath)){
                        continue;
                    }else {
                        mDir.add(dirPath);
                        folderBean=new FolderBean();
                        folderBean.setDir(dirPath);
                        folderBean.setFirstImgDir(path);
                    }
                    if (parentFile.list()==null){
                        continue;
                    }
                    int picSize = parentFile.list(new FilenameFilter() {
                        @Override
                        public boolean accept(File dir, String name) {
                            if (name.endsWith(".png")||name.endsWith(".jpg")){
                                return  true;
                            }
                            return false;
                        }
                    }).length;
                    folderBean.setCount(picSize);
                    mFolderBeanList.add(folderBean);
                    photosResultBean.setFolderBeans(mFolderBeanList);

                    if (picSize>photosResultBean.getmMaxCount()){
                        photosResultBean.setmMaxCount(picSize) ;
                        photosResultBean.setmCurrentFile(parentFile);
                    }

                }
                mCallbacks.onAlbumMediaLoad(photosResultBean);
                cursor.close();
                mDir=null;
            }
        }).start();

    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        Context context = mContext.get();
        if (context == null) {
            return;
        }

        mCallbacks.onAlbumMediaReset();
    }

    public void onCreate(@NonNull FragmentActivity context, @NonNull PhotoCallbacks callbacks) {
        mContext = new WeakReference<Context>(context);
        mLoaderManager = context.getSupportLoaderManager();
        mCallbacks = callbacks;
    }

    public void onDestroy() {
        if (mLoaderManager != null) {
            mLoaderManager.destroyLoader(LOADER_ID);
        }
        mCallbacks = null;
    }


    public void load() {
        mLoaderManager.initLoader(LOADER_ID, null, this);
    }

    public interface PhotoCallbacks {

        void onAlbumMediaLoad(PhotosResultBean resultBean);

        void onAlbumMediaReset();
    }
}
