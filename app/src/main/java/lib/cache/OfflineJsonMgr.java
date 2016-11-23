package lib.cache;

import android.content.Context;

import com.alibaba.fastjson.JSON;

import java.io.File;
import java.io.InputStream;

public class OfflineJsonMgr {

    private static OfflineJsonMgr sInstance;

    public static OfflineJsonMgr getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new OfflineJsonMgr(context);
        }
        return sInstance;
    }

    private String mDirPath;

    private OfflineJsonMgr(Context context) {
        mDirPath = context.getFilesDir().getAbsolutePath();
    }

    /**
     * 根据类名获取缓存的数据
     *
     * @param clazz
     * @return
     */
    public Object getJsonObject(Class<?> clazz) {
        return getJsonObject(clazz, clazz.getSimpleName());
    }

    /**
     * 根据类名及键名获取数据
     *
     * @param clazz
     * @param cacheKey
     * @return
     */
    public Object getJsonObject(Class<?> clazz, String cacheKey) {
        Object object = null;
        File file = FileUtil.newFile(mDirPath, cacheKey);
        if (file.exists()) {
            InputStream inputStream = FileUtil.getInputStreamFromFile(file);
            String jsonStr = FileUtil.getStringFromInputStream(inputStream);
            if (jsonStr == null) {
                return object;
            }
            try {
                if (jsonStr.startsWith("[")) {
                    object = JSON.parseArray(jsonStr, clazz);
                } else {
                    object = JSON.parseObject(jsonStr, clazz);
                }
            } catch (Exception e) {
                e.printStackTrace();
                object = null;
            }
        }
        return object;
    }

    public void saveJson(String jsonStr, String fileName) {
        if (jsonStr == null || jsonStr.isEmpty()) {
            return;
        }
        if (fileName == null || fileName.isEmpty()) {
            return;
        }
        File file = FileUtil.newFile(mDirPath, fileName);
        FileUtil.stringToFile(jsonStr, file);
    }

    public void delectJson(String fileName) {
        if (fileName == null || fileName.length() < 1) {
            return;
        }
        File file = FileUtil.newFile(mDirPath, fileName);
        FileUtil.delectFile(file);
    }
}
