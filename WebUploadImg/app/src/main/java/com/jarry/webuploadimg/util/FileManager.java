package com.jarry.webuploadimg.util;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import android.text.TextUtils;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class FileManager {

    public static final String ROOT_NAME = "RongYifu";
    public static final String LOG_NAME = "UserLog";
    public static final String CACHE_NAME = "Cache";
    public static final String IMAGE_NAME = "Image";
    public static final String RECORD_NAME = "Voice";

    public static final String ROOT_PATH = File.separator + ROOT_NAME
            + File.separator;
    public static final String LOG_PATH_NAME = File.separator + LOG_NAME
            + File.separator;
    public static final String CACHE_PATH_NAME = File.separator + CACHE_NAME
            + File.separator;
    public static final String IMAGE_PATH_NAME = File.separator + IMAGE_NAME
            + File.separator;
    public static final String RECORD_PATH_NAME = File.separator + RECORD_NAME
            + File.separator;

    public static final String ACTION_DEL_ALL_IMAGE_CACHE = "com.citic21.user_delImageCache";
    public static final String CODE_ENCODING = "utf-8";

    public static String getRootPath(Context appContext) {

        String rootPath = null;
        if (checkMounted()) {
            rootPath = getRootPathOnSdcard();
        } else {
            rootPath = getRootPathOnPhone(appContext);
        }
        return rootPath;
    }

    public static String getRootPathOnSdcard() {
        File sdcard = Environment.getExternalStorageDirectory();
        String rootPath = sdcard.getAbsolutePath() + ROOT_PATH;
        return rootPath;
    }

    public static String getRootPathOnPhone(Context appContext) {
        File phoneFiles = appContext.getFilesDir();
        String rootPath = phoneFiles.getAbsolutePath() + ROOT_PATH;
        return rootPath;
    }

    public static String getSdcardPath() {
        File sdDir = null;
        boolean sdCardExist = checkMounted(); // 判断sd卡是否存在
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();// 获取跟目录
            return sdDir.getPath();
        }
        return "/";
    }

    // SD卡剩余空间
    public long getSDFreeSize() {
        // 取得SD卡文件路径
        File path = Environment.getExternalStorageDirectory();
        StatFs sf = new StatFs(path.getPath());
        // 获取单个数据块的大小(Byte)
        long blockSize = sf.getBlockSize();
        // 空闲的数据块的数量
        long freeBlocks = sf.getAvailableBlocks();
        // 返回SD卡空闲大小
        // return freeBlocks * blockSize; //单位Byte
        // return (freeBlocks * blockSize)/1024; //单位KB
        return (freeBlocks * blockSize) / 1024 / 1024; // 单位MB
    }

    public static boolean checkMounted() {
        return Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState());
    }

    public static String getUserLogDirPath(Context appContext) {

        String logPath = getRootPath(appContext) + LOG_PATH_NAME;
        return logPath;
    }

    // 缓存整体路径
    public static String getCacheDirPath(Context appContext) {

        String imagePath = getRootPath(appContext) + CACHE_PATH_NAME;
        return imagePath;
    }

    // 图片缓存路径
    public static String getImageCacheDirPath(Context appContext) {

        String imagePath = getCacheDirPath(appContext) + IMAGE_PATH_NAME;
        return imagePath;
    }

    // 创建一个图片文件
    public static File getImgFile(Context context) {
        File file = new File(getImageCacheDirPath(context));
        if (!file.exists()) {
            file.mkdirs();
        }
        String imgName = new SimpleDateFormat("yyyyMMdd_HHmmss")
                .format(new Date());
        File imgFile = new File(file.getAbsolutePath() + File.separator
                + "IMG_" + imgName + ".jpg");
        return imgFile;
    }


    // 创建拍照处方单路径
    public static File initCreatImageCacheDir(Context appContext) {
        String rootPath = getImageCacheDirPath(appContext);
        File dir = new File(rootPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir;
    }
 

    public static final String getFileSize(File file) {
        String fileSize = "0.00K";
        if (file.exists()) {
            fileSize = FormetFileSize(file.length());
            return fileSize;
        }
        return fileSize;
    }

    public static String FormetFileSize(long fileS) {// 转换文件大小
        DecimalFormat df = new DecimalFormat("0.00");
        String fileSizeString = "";
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "K";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "M";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "G";
        }
        return fileSizeString;
    }

    public static boolean writeStringToFile(String text, File file) {
        try {
            return writeStringToFile(text.getBytes(CODE_ENCODING), file);
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
            return false;
        }
    }

    static void close(Closeable c) {
        if (c != null) {
            try {
                c.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public static boolean writeStringToFile(byte[] datas, File file) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            fos.write(datas);
            fos.flush();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            close(fos);
        }
        return false;
    }

    /**
     * @param oldpath URL 的 md5+"_tmp"
     * @param newpath URL 的 md5+
     * @return
     */
    public static boolean renameFileName(String oldpath, String newpath) {
        try {
            File file = new File(oldpath);
            if (file.exists()) {
                file.renameTo(new File(newpath));
            }
            return true;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }

    public static final boolean isFileExists(File file) {
        if (file.exists()) {

            return true;
        }
        return false;
    }

    public static final long getFileSizeByByte(File file) {
        long fileSize = 0l;
        if (file.exists()) {
            fileSize = file.length();
            return fileSize;
        }
        return fileSize;
    }


    public static boolean checkCachePath(Context appContext) {
        String path = getCacheDirPath(appContext);
        File file = new File(path);
        if (!file.exists()) {
            return false;
        }
        return true;
    }


    public static String getUrlFileName(String resurlt) {
        if (!TextUtils.isEmpty(resurlt)) {
            int nameIndex = resurlt.lastIndexOf("/");
            String loacalname = "";
            if (nameIndex != -1) {
                loacalname = resurlt.substring(nameIndex + 1);
            }

            int index = loacalname.indexOf("?");
            if (index != -1) {
                loacalname = loacalname.substring(0, index);
            }
            return loacalname;
        } else {
            return resurlt;
        }
    }

    // 存储map类型数据 转换为Base64进行存储
    public static String SceneList2String(Map<String, String> SceneList)
            throws IOException {
        ByteArrayOutputStream toByte = new ByteArrayOutputStream();

        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(toByte);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        try {
            oos.writeObject(SceneList);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // 对byte[]进行Base64编码

        String SceneListString = new String(Base64.encode(toByte.toByteArray(),
                Base64.DEFAULT));
        return SceneListString;

    }
}
