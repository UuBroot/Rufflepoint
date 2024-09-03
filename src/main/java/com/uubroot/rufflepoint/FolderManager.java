package com.uubroot.rufflepoint;

import java.io.File;

public class FolderManager {
    public static String folderPath;

    public static void setupFolder(){

        switch (System.getProperty("os.name")){
            case "Linux":
                String homeDir = System.getProperty("user.home");
                String fullPath = homeDir + File.separator+".local"+ File.separator + "share" + File.separator + "Rufflepoint";//TODO:Fix weird full path
                System.out.println("fp:"+fullPath);

                folderPath = fullPath;

                File dir = new File(fullPath + "/Rufflepoint").getParentFile();
                System.out.println(dir);
                if (dir != null && !dir.exists()) {

                    if (dir.mkdirs()) {
                        System.out.println("Directory created: " + dir.getAbsolutePath());
                        //Ruffle Directory
                        File ruffleDir = new File(fullPath+"/ruffle");
                        if(ruffleDir.mkdirs()){
                            System.out.println("Directory created: " + ruffleDir.getAbsolutePath());
                        }else {
                            System.err.println("Failed to create directory");
                        }
                    } else {
                        System.err.println("Failed to create directory");
                    }
                }else {
                    System.err.println("directory exists");
                }

                break;
            default:
                System.err.println(System.getProperty("os.name")+" not supported");
                System.exit(1);

        }
    }
}
