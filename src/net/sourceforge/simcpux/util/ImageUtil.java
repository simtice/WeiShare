package net.sourceforge.simcpux.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Environment;
import android.util.Base64;

public class ImageUtil {

	public static File getAlbumDir() {
		// 　Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
		// 返回图像和视频的标准共享位置，别的应用也可以访问，如果你的应用被卸载了，这个路径下的文件是会保留的。
		File dir = new File(
				Environment
						.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
				"test");
		if (!dir.exists()) {
			dir.mkdirs();
		}
		return dir;
	}

	public static Bitmap getSmallBitmap(String filePath, int screenWidth,
			int screenHeight) {
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;// 可以不把图片读到内存中,但依然可以计算出图片的大小
		BitmapFactory.decodeFile(filePath, options);
		options.inSampleSize = calculateInSampleSize(options, screenWidth,
				screenHeight);
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(filePath, options);
	}

	/**
	 * 计算压缩比例 如果图片的原始高度或者宽带大约我们期望的宽带和高度，我们需要计算出缩放比例的数值。否则就不缩放。
	 * heightRatio是图片原始高度与压缩后高度的倍数
	 * ，widthRatio是图片原始宽度与压缩后宽度的倍数。inSampleSize为heightRatio与widthRatio中最小的那个
	 * ，inSampleSize就是缩放值。 inSampleSize为1表示宽度和高度不缩放，为2表示压缩后的宽度与高度为原来的1/2
	 * 
	 * @param options
	 * @param resWidth
	 * @param resHeight
	 * @return
	 */
	private static int calculateInSampleSize(BitmapFactory.Options options,
			int resWidth, int resHeight) {
		int height = options.outHeight;
		int width = options.outWidth;
		int inSampleSize = 1;

		System.out.println("height-->" + height);
		System.out.println("width-->" + width);
		System.out.println("resWidth-->" + resWidth);
		System.out.println("resHeight-->" + resHeight);

		if (height > resHeight || width > resWidth) {
			// final int heightRatio = Math.round((float) height/ (float)
			// resHeight);
			// final int widthRatio = Math.round((float) width / (float)
			// resWidth);

			final int heightRatio = Math.round((float) height
					/ (float) resHeight);
			final int widthRatio = Math.round((float) width / (float) resWidth);

			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}
		return inSampleSize;
	}

	/**
	 * 根据路径获得突破并压缩返回bitmap用于显示
	 * 
	 * @return
	 */
	public static Bitmap createPhotos(Bitmap bitmap) {
		if (bitmap != null) {
			Matrix m = new Matrix();
			try {
				m.setRotate(90, bitmap.getWidth() / 2, bitmap.getHeight() / 2);// 90就是我们需要选择的90度
				Bitmap bmp2 = Bitmap.createBitmap(bitmap, 0, 0,
						bitmap.getWidth(), bitmap.getHeight(), m, true);
				bitmap.recycle();
				bitmap = bmp2;
			} catch (Exception ex) {
				ex.printStackTrace();
				System.out.print("创建图片失败！" + ex);
			}
		}
		return bitmap;
	}

	/**
	 * 读取图片属性：旋转的角度
	 * 
	 * @param path
	 *            图片绝对路径
	 * @return degree旋转的角度
	 */
	public static int readPictureDegree(String path) {
		int degree = 0;
		try {
			ExifInterface exifInterface = new ExifInterface(path);
			int orientation = exifInterface.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				degree = 90;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				degree = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				degree = 270;
				break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return degree;
	}

	/*
	 * 旋转图片
	 * 
	 * @param angle
	 * 
	 * @param bitmap
	 * 
	 * @return Bitmap
	 */
	public static Bitmap rotaingImageView(int angle, Bitmap bitmap) {
		// 旋转图片 动作
		Matrix matrix = new Matrix();
		;
		matrix.postRotate(angle);
		// 创建新的图片
		Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
				bitmap.getWidth(), bitmap.getHeight(), matrix, true);
		return resizedBitmap;
	}

	public static String bitmapToString(Bitmap bit) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bit.compress(Bitmap.CompressFormat.JPEG, 40, baos);
		byte[] b = baos.toByteArray();
		return Base64.encodeToString(b, Base64.DEFAULT);
	}

	
}
