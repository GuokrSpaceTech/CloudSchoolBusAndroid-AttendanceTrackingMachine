package com.cytx.timecard.utility;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.cytx.timecard.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

public class FileTools {

    /**
     * 得到SDcard路径
     *
     * @return
     */
    public static String getSDcardPath() {
        String SDcardPath = "";
        if (existSDcard()) {
            SDcardPath = Environment.getExternalStorageDirectory()
                    .getAbsolutePath();
        }
        return SDcardPath;
    }

    /**
     * 判断存储卡是否存在
     *
     * @return
     */
    public static boolean existSDcard() {
        if (android.os.Environment.MEDIA_MOUNTED.equals(android.os.Environment
                .getExternalStorageState())) {
            return true;
        } else
            return false;
    }

    /**
     * 讲字符串写到sdcard
     *
     * @param fileDir  文件目录
     * @param fileName 文件名
     * @param content  内容
     * @return
     */
    public static boolean save2SDCard(String fileDir, String fileName,
                                      String content) {
        try {
            File file = new File(fileDir);
            if (!file.exists()) {
                file.mkdirs();
            }

            File fileAbsolute = new File(fileDir, fileName);
            if (fileAbsolute.exists()) {
                fileAbsolute.delete();
            }
            fileAbsolute.createNewFile();

            // 得到文件输出流
            FileOutputStream outStream = new FileOutputStream(fileAbsolute);
            outStream.write(content.getBytes());
            outStream.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    /**
     * 修改bitmap的尺寸
     *
     * @param bitmap
     * @param w
     * @param h
     * @return
     */
    public static Bitmap resizeImage(Bitmap bitmap, int w, int h) {
        Bitmap BitmapOrg = bitmap;
        int width = BitmapOrg.getWidth();
        int height = BitmapOrg.getHeight();
        int newWidth = w;
        int newHeight = h;

        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        // 旋转180度
        matrix.postRotate(90);
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap resizedBitmap = Bitmap.createBitmap(BitmapOrg, 0, 0, width,
                height, matrix, true);
        return resizedBitmap;
    }

    /**
     * bitmap转为base64
     *
     * @param bitmap
     * @return
     */
    public static String bitmapToBase64(Bitmap bitmap) {

        String result = null;
        ByteArrayOutputStream baos = null;
        try {
            if (bitmap != null) {
                baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

                baos.flush();
                baos.close();

                byte[] bitmapBytes = baos.toByteArray();
                result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.flush();
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * bitmap转为byte[]
     *
     * @param bm
     * @return
     */
    public static byte[] Bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    /**
     * 以行为单位读取文件，常用于读面向行的格式化文件
     */
    public static String readFileByLines(String filePath, String defaultValue) {

        File file = new File(filePath);
        if (!file.exists()) {
            return defaultValue;
        }
        StringBuffer sBuffer = new StringBuffer();
        BufferedReader reader = null;
        try {
            System.out.println("以行为单位读取文件内容，一次读一整行：");
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                sBuffer.append(tempString);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
        return sBuffer.toString();
    }

    /**
     * 将数据保存的文件中
     *
     * @param path
     */
    public static void writeFileByLines(String jsonData, String fileName) {
        Log.d("writeByLines", jsonData + "=====" + fileName);
        File file = new File(fileName);
        if (file.exists()) {
            file.delete();
        }
        BufferedWriter writer = null;
        try {
            file.createNewFile();
            Log.d("writeByLines", file.getName());
            writer = new BufferedWriter(new FileWriter(file));
            writer.write(jsonData);
        } catch (FileNotFoundException e) {

            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 将图片缓存到本地
     *
     * @param fileDir
     * @param fileName
     * @param mBitmap
     */
    public static void saveBitmap2Cache(Bitmap mBitmap, String dir,
                                        String fileName) {

        // 创建目录
        File fileDir = new File(dir);
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }

        // 创建文件
        File f = new File(dir, fileName);
        try {
            if (f.exists()) {
                f.delete();
            }
            f.createNewFile();
        } catch (IOException e) {
        }
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // 保存图片
        mBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
        try {
            fOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 加载图片，并将图片缓存到本地
     */
    public static void loadImage(final ImageView imageView, String url,
                                 final String dir, final String fileName,
                                 final ProgressBar progressBar, final boolean isBigPicture) {

        // 如果本地有图片，那么加载本地的
        Bitmap bitmap = Utils.getOptionBitmap(dir + "/" + fileName);
        if (bitmap != null) {
            // 将图片设置为圆角
            imageView.setImageBitmap(Utils.toRoundCorner(bitmap, 30));
        } else {
            // 否则，开始加载网络图片
            if (url != null) {
                if (!url.startsWith("http://")) {
                    url = "http://" + url;
                }
                ImageLoader.getInstance().displayImage(url, imageView,
                        new ImageLoadingListener() {

                            @Override
                            public void onLoadingStarted(String imageUri, View view) {
                                progressBar.setVisibility(View.VISIBLE);

                            }

                            @Override
                            public void onLoadingFailed(String imageUri, View view,
                                                        FailReason failReason) {
                                progressBar.setVisibility(View.GONE);
                                if (isBigPicture) {
                                    imageView
                                            .setImageResource(R.drawable.portrait_big);
                                } else {
                                    imageView
                                            .setImageResource(R.drawable.portrait_small);
                                }
                            }

                            @Override
                            public void onLoadingComplete(String imageUri,
                                                          View view, Bitmap loadedImage) {
                                progressBar.setVisibility(View.GONE);
                                // 将图片保存到本地
                                if (loadedImage != null) {
                                    imageView.setImageBitmap(Utils.toRoundCorner(
                                            loadedImage, 30));
                                    FileTools.saveBitmap2Cache(loadedImage, dir,
                                            fileName);
                                } else {

                                    // 若为学生头像，则加载大图背景；否则为接送人头像，则加载小图背景
                                    if (isBigPicture) {
                                        imageView
                                                .setImageResource(R.drawable.portrait_big);
                                    } else {
                                        imageView
                                                .setImageResource(R.drawable.portrait_small);
                                    }

                                }
                            }

                            @Override
                            public void onLoadingCancelled(String imageUri,
                                                           View view) {

                            }

                        });
            }
        }
    }

    /**
     * 加载图片，并将图片缓存到本地
     */
    public static void loadAvdImage(final ImageView imageView, String url,
                                    final String dir, final String fileName) {

        // 否则，开始加载网络图片
        if (!url.startsWith("http://")) {
            url = "http://" + url;
        }
        ImageLoader.getInstance().displayImage(url, imageView,
                new ImageLoadingListener() {

                    @Override
                    public void onLoadingStarted(String imageUri, View view) {

                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view,
                                                FailReason failReason) {
                        // 如果本地有图片，那么加载本地的
                        Bitmap bitmap = Utils.getOptionBitmap(dir + "/"
                                + fileName);
                        if (bitmap != null) {
                            // 将图片设置为圆角
                            imageView.setImageBitmap(Utils.toRoundCorner(
                                    bitmap, 30));
                        }
                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view,
                                                  Bitmap loadedImage) {
                        // 将图片保存到本地
                        if (loadedImage != null) {
                            imageView.setImageBitmap(Utils.toRoundCorner(
                                    loadedImage, 30));
                            FileTools.saveBitmap2Cache(loadedImage, dir,
                                    fileName);
                        } else {
                            // 如果本地有图片，那么加载本地的
                            Bitmap bitmap = Utils.getOptionBitmap(dir + "/"
                                    + fileName);
                            if (bitmap != null) {
                                // 将图片设置为圆角
                                imageView.setImageBitmap(Utils.toRoundCorner(
                                        bitmap, 30));
                            }
                        }
                    }

                    @Override
                    public void onLoadingCancelled(String imageUri, View view) {

                    }

                });

    }

    /**
     * 根据路径删除指定的目录或文件，无论存在与否
     *
     * @param sPath
     * 要删除的目录或文件
     * @return 删除成功返回 true，否则返回 false。
     */
    private static boolean flag = false;
    private static File file = null;

    public static boolean DeleteFolder(String sPath) {
        flag = false;
        file = new File(sPath);
        // 判断目录或文件是否存在
        if (!file.exists()) { // 不存在返回 false
            return flag;
        } else {
            // 判断是否为文件
            if (file.isFile()) { // 为文件时调用删除文件方法
                return deleteFile(sPath);
            } else { // 为目录时调用删除目录方法
                return deleteDirectory(sPath);
            }
        }
    }

    /**
     * 删除单个文件
     *
     * @param sPath 被删除文件的文件名
     * @return 单个文件删除成功返回true，否则返回false
     */
    public static boolean deleteFile(String sPath) {
        flag = false;
        file = new File(sPath);
        // 路径为文件且不为空则进行删除
        if (file.isFile() && file.exists()) {
            file.delete();
            flag = true;
        }
        return flag;
    }

    /**
     * 删除目录（文件夹）以及目录下的文件
     *
     * @param sPath 被删除目录的文件路径
     * @return 目录删除成功返回true，否则返回false
     */
    public static boolean deleteDirectory(String sPath) {
        // 如果sPath不以文件分隔符结尾，自动添加文件分隔符
        if (!sPath.endsWith(File.separator)) {
            sPath = sPath + File.separator;
        }
        File dirFile = new File(sPath);
        // 如果dir对应的文件不存在，或者不是一个目录，则退出
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            return false;
        }
        flag = true;
        // 删除文件夹下的所有文件(包括子目录)
        File[] files = dirFile.listFiles();
        for (int i = 0; i < files.length; i++) {
            // 删除子文件
            if (files[i].isFile()) {
                flag = deleteFile(files[i].getAbsolutePath());
                if (!flag)
                    break;
            } // 删除子目录
            else {
                flag = deleteDirectory(files[i].getAbsolutePath());
                if (!flag)
                    break;
            }
        }
        if (!flag)
            return false;
        // 删除当前目录
        if (dirFile.delete()) {
            return true;
        } else {
            return false;
        }
    }

}
