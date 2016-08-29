/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lansyncserver;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Kazi
 */
public class FileInfo {

    private String fileAbsulotePath;
    private String rootPath;
    private long fileSize;
    private long lastModifiedDate;

    public FileInfo(String filePath, String rootPath) {
        fileAbsulotePath = filePath;
        this.rootPath = rootPath;
        File file = new File(fileAbsulotePath);
        fileSize = file.length();
        lastModifiedDate = file.lastModified();
    }

    public FileInfo(JSONObject obj, String rootPath) throws JSONException {
        this.rootPath = rootPath;
        fileAbsulotePath = rootPath + obj.getString("path");
        fileSize = obj.getLong("size");
        lastModifiedDate = obj.getLong("modifieddate");
    }

    public String getFileInfoMessage() {
        try {
            JSONObject obj = new JSONObject();
            obj.put("path", fileAbsulotePath.substring(rootPath.length()));
            obj.put("size", fileSize);
            obj.put("modifieddate", lastModifiedDate);
            return obj.toString();
        } catch (JSONException ex) {
            Logger.getLogger(FileInfo.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public String getFileAbsulotePath() {
        return fileAbsulotePath;
    }

    public long getFileSize() {
        return fileSize;
    }

    public long getLastModifiedDate() {
        return lastModifiedDate;
    }
}
