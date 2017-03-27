package com.tianer.ch.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;

/**
 * file util
 * 
 * @author Thinkpad
 *
 */
public class FileUtil {
	/**
	 * 保存文件
	 * 
	 * @param b
	 *            数据源
	 * @param dir
	 *            文件夹
	 * @param filename
	 *            文件名称
	 * @return
	 */
	public static File saveFile(byte[] b, String dir, String filename) {

		File dirs = new File(dir);
		FileOutputStream output = null;
		if (!dirs.exists()) {
			dirs.mkdirs();
		}
		String SDCard = Environment.getExternalStorageDirectory() + "";
		File file = new File(SDCard + "/" + dir + "/" + filename);
		// 如果文件存在 删除文件
		if (file.exists()) {
			file.delete();
		}
		try {
			file.createNewFile();// 新建文件
			output = new FileOutputStream(file);
			output.write(b);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				output.flush();
				if (output != null) {
					output.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return file;
	}

	/**
	 * 打开apk
	 * 
	 * @param context
	 * @param pathName
	 *            文件路径
	 */
	public static void openFile(Context context, String pathName) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(new File(pathName)), "application/vnd.android.package-archive");
		context.startActivity(intent);
	}
}
