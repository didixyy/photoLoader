package com.yuepeng.photo.photoloader.Bean;


public class FolderBean {
    private String dir;
    private String firstImgDir;
    private String name;
    private int count;

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
        int lastIndex =this. dir.lastIndexOf("/");
        this.name = this.dir.substring(lastIndex+1);
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getName() {
        return name;
    }


    public String getFirstImgDir() {
        return firstImgDir;
    }

    public void setFirstImgDir(String firstImgDir) {
        this.firstImgDir = firstImgDir;
    }
}
