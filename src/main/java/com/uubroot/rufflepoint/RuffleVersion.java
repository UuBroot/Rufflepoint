package com.uubroot.rufflepoint;

import java.util.List;

public class RuffleVersion {
    private String name;
    private List files;
    private String uploadDate;

    public RuffleVersion(String name,List files,String uploadDate){
        this.name = name;
        this.files = files;
        this.uploadDate = uploadDate;
    }

    public String getName() {
        return name;
    }

    public List getFiles() {
        return files;
    }

    public String getUploadDate() {
        return uploadDate;
    }
}
