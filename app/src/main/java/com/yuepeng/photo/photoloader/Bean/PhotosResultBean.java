package com.yuepeng.photo.photoloader.Bean;

import java.io.File;
import java.util.List;

public class PhotosResultBean {
    private List<FolderBean> folderBeans;
    private int mMaxCount ;
    private File mCurrentFile;

    public List<FolderBean> getFolderBeans() {
        return folderBeans;
    }

    public void setFolderBeans(List<FolderBean> folderBeans) {
        this.folderBeans = folderBeans;
    }

    public int getmMaxCount() {
        return mMaxCount;
    }

    public void setmMaxCount(int mMaxCount) {
        this.mMaxCount = mMaxCount;
    }

    public File getmCurrentFile() {
        return mCurrentFile;
    }

    public void setmCurrentFile(File mCurrentFile) {
        this.mCurrentFile = mCurrentFile;
    }
}
