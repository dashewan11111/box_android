package com.adult.android.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.graphics.Bitmap;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

public class FileUtil {

	private static final String AGENT_NAME = "box";
	private String SDCardRoot;

	public FileUtil() {
		// 得到当前外部存储设备目录
		SDCardRoot = Environment.getExternalStorageDirectory()
				.getAbsolutePath();
	}

	/**
	 * 在SDCard上创建文件
	 * 
	 * @return
	 * @throws Exception
	 */
	public File createFileInSDCard(String path) throws Exception {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss"); // 格式化时间
		String fileName = format.format(new Date()) + ".jpg";
		File fileFolder = createSDDir(SDCardRoot + "/" + AGENT_NAME + "/"
				+ path + "/");
		if (!fileFolder.exists()) { // 如果目录不存在，则创建一个名为"CTC"的目录
			fileFolder.mkdirs();
		}
		File jpgFile = new File(fileFolder, fileName);
		if (!jpgFile.exists()) {
			jpgFile.createNewFile();
		}
		return jpgFile;
	}

	/**
	 * 在SDCard上创建OfficeFile
	 * 
	 * @return
	 * @throws Exception
	 */
	public File createOfficeFileInSDCard(String path, String fileName)
			throws Exception {
		File fileFolder = createSDDir(SDCardRoot + "/" + path + "/");
		if (!fileFolder.exists()) { // 如果目录不存在，则创建一个名为"CTC"的目录
			fileFolder.mkdirs();
		}
		File jpgFile = new File(fileFolder, fileName);
		if (!jpgFile.exists()) {
			jpgFile.createNewFile();
		}
		return jpgFile;
	}

	/**
	 * 在SDCard上创建目录
	 * 
	 * @param dir
	 * @return
	 */
	public File createSDDir(String dir) {
		File file = new File(dir);
		return file;
	}

	/**
	 * 判断SDCard上的文件是否存在
	 * 
	 * @param fileName
	 * @param path
	 * @return
	 */
	public boolean isFileExist(String fileName, String path) {
		File file = new File(SDCardRoot + "/" + path + "/" + fileName);
		return file.exists();
	}

	/**
	 * 获取文件
	 * 
	 * @param fileName
	 * @param path
	 * @return
	 */
	public File getFile(String fileName, String path) {
		File file = new File(SDCardRoot + "/" + path + "/" + fileName);
		return file;
	}

	/**
	 * 删除文件
	 * 
	 * @param fileName
	 * @param path
	 */
	public void deleteFile(String fileName, String path) {
		System.out.println("delete");
		File file = new File(SDCardRoot + "/" + path + "/" + fileName);
		file.delete();
	}

	/**
	 * 将一个InputStream写入SDCard
	 * 
	 * @param path
	 * @param inputStream
	 * @return
	 */
	public File writeToSDCardFromInput(String path, InputStream inputStream,
			String imageScallPath) {
		File file = null;
		OutputStream outputStream = null;

		try {
			createSDDir(path);
			file = createFileInSDCard(imageScallPath);

			outputStream = new FileOutputStream(file);
			byte data[] = new byte[4 * 1024];
			int tmp;
			while ((tmp = inputStream.read(data)) != -1) {
				outputStream.write(data, 0, tmp);
			}
			outputStream.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				outputStream.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return file;
	}

	/**
	 * 将一个InputStream写入SDCard
	 * 
	 * @param path
	 * @param inputStream
	 * @return
	 */
	public File writeToSDCardFromInputFile(String path,
			InputStream inputStream, String fileName) {
		File file = null;
		OutputStream outputStream = null;

		try {
			createSDDir(path);
			file = createOfficeFileInSDCard(path, fileName);

			outputStream = new FileOutputStream(file);
			byte data[] = new byte[4 * 1024];
			int tmp;
			while ((tmp = inputStream.read(data)) != -1) {
				outputStream.write(data, 0, tmp);
			}
			outputStream.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				outputStream.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return file;
	}

	/**
	 * @param file
	 * @return
	 */
	public static byte[] getBytesFromFile(File file) {
		byte[] bytes = null;
		if (file != null) {
			InputStream is = null;
			try {
				is = new FileInputStream(file);
				int length = (int) file.length();
				if (length > Integer.MAX_VALUE) {
					is.close();
					return null;
				}
				bytes = new byte[length];
				int offset = 0;
				int numRead = 0;
				while (offset < bytes.length
						&& (numRead = is.read(bytes, offset, bytes.length
								- offset)) >= 0) {
					offset += numRead;
				}
				if (offset < bytes.length) {
					is.close();
					return null;
				}
				is.close();
			} catch (FileNotFoundException e) {
				Log.e("AgentUtils", "getBytesFromFile", e);
			} catch (IOException e) {
				Log.e("AgentUtils", "getBytesFromFile", e);
			} finally {
				if (is != null) {
					try {
						is.close();
					} catch (IOException e) {
						Log.e("AgentUtils", "getBytesFromFile", e);
					}
				}
			}
		}
		return bytes;
	}

	/**
	 * 将bigmap保存为文件
	 * 
	 * @param bp
	 * @param imageScallPath
	 * @throws IOException
	 */
	public String saveBitmap(Bitmap bp, String imageScallPath) {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss"); // 格式化时间
		String fileName = format.format(new Date()) + ".jpg";
		File fileFolder = createSDDir(SDCardRoot + "/" + AGENT_NAME + "/"
				+ imageScallPath + "/");
		if (!fileFolder.exists()) { // 如果目录不存在，则创建一个名为"CTC"的目录
			fileFolder.mkdirs();
		}
		File f = new File(fileFolder, fileName);
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(f);
			// 每当执行到这时就抛出异常FileNotFoundException
		} catch (FileNotFoundException e) {
			fileName = "";
			e.printStackTrace();
		}
		bp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
		try {
			fos.flush();
			fos.close();
		} catch (IOException e) {
			fileName = "";
			e.printStackTrace();
		}

		return fileFolder + "/" + fileName;
	}

	/**
	 * 获取文件夹大小
	 * 
	 * @param file
	 *            File实例
	 * @return long 单位为M
	 * @throws Exception
	 */
	public static float getFolderSize(java.io.File file) throws Exception {
		float size = 0;
		java.io.File[] fileList = file.listFiles();
		for (int i = 0; i < fileList.length; i++) {
			if (fileList[i].isDirectory()) {
				size = size + getFolderSize(fileList[i]);
			} else {
				size = size + fileList[i].length();
			}
		}
		return size / 1048576;
	}

	/**
	 * 删除指定目录下文件及目录
	 * 
	 * @param deleteThisPath
	 * @param filepath
	 * @return
	 */
	public static void deleteFolderFile(String filePath, boolean deleteThisPath)
			throws IOException {
		if (!TextUtils.isEmpty(filePath)) {
			File file = new File(filePath);

			if (file.isDirectory()) {// 处理目录
				File files[] = file.listFiles();
				for (int i = 0; i < files.length; i++) {
					deleteFolderFile(files[i].getAbsolutePath(), true);
				}
			}
			if (deleteThisPath) {
				if (!file.isDirectory()) {// 如果是文件，删除
					file.delete();
				} else {// 目录
					if (file.listFiles().length == 0) {// 目录下没有文件或者目录，删除
						file.delete();
					}
				}
			}
		}
	}

}
