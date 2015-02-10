package com.catchingnow.tinyclipboardmanager;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by heruoxin on 15/2/10.
 */
public class ImportAndExport {
    public final static String PACKAGE_NAME = "com.catchingnow.tinyclipboardmanager";

    private static boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state);
    }

    private static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private static File getBackupStorageDir(Context context, Date backupDate) {
        // Get the directory for the user's public pictures directory.
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOCUMENTS), context.getString(R.string.backup_file_name) + backupDate + ".txt");
        return file;
    }

    public static void exportDialog(Context context) {

    }

    public static boolean makeExport(Context context, Date startDate, Date endDate, boolean reverse) {
        if (!isExternalStorageWritable()) {
            return false;
        }
        List<ClipObject> clipObjects = Storage.getInstance(context).getClipHistory();
        if (reverse) { //reverse
            Collections.reverse(clipObjects);
        }
        List<String> backupStringList = new ArrayList<>();
        backupStringList.add(context.getString(R.string.backup_file_name));
        for (ClipObject clipObject : clipObjects) { //delete out of date clips
            Date clipObjectDate = clipObject.getDate();
            if (clipObjectDate.after(startDate) && clipObjectDate.before(endDate)) {
                backupStringList.add("\n" + clipObjectDate.toString() + "\n" + clipObject.getText());
            }
        }

        File backupFile = getBackupStorageDir(context, new Date());
        try {
            if (!backupFile.exists()) {
                backupFile.createNewFile();
            }
            BufferedWriter writer = new BufferedWriter(new FileWriter(backupFile, true /*append*/));
            for (String backupString : backupStringList) {
                writer.write(backupString);
            }
            writer.close();
            makeNotification(context, backupFile, true);
            return true;
        } catch (IOException e) {
            Log.e(PACKAGE_NAME, "Backup error: \n" + e.toString());
            makeNotification(context, backupFile, false);
            return false;
        }

    }

    private static void makeNotification(Context context, File file, boolean success) {
        if (success) {
            Toast.makeText(
                    context,
                    context.getString(R.string.export_success) + "\n" + file.getAbsolutePath(),
                    Toast.LENGTH_LONG
            ).show();
            Intent openIntent = new Intent()
                    .setAction(android.content.Intent.ACTION_VIEW)
                    .setDataAndType(Uri.fromFile(file), MimeTypeMap.getSingleton().getMimeTypeFromExtension("txt"))
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(openIntent);
        } else {
            Toast.makeText(
                    context,
                    context.getString(R.string.export_failed),
                    Toast.LENGTH_LONG
                    ).show();
        }
    }
}

