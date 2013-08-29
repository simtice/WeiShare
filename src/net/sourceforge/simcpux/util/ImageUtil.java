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
		// ��Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
		// ����ͼ�����Ƶ�ı�׼����λ�ã����Ӧ��Ҳ���Է��ʣ�������Ӧ�ñ�ж���ˣ����·���µ��ļ��ǻᱣ���ġ�
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
		options.inJustDecodeBounds = true;// ���Բ���ͼƬ�����ڴ���,����Ȼ���Լ����ͼƬ�Ĵ�С
		BitmapFactory.decodeFile(filePath, options);
		options.inSampleSize = calculateInSampleSize(options, screenWidth,
				screenHeight);
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(filePath, options);
	}

	/**
	 * ����ѹ������ ���ͼƬ��ԭʼ�߶Ȼ��߿����Լ���������Ŀ���͸߶ȣ�������Ҫ��������ű�������ֵ������Ͳ����š�
	 * heightRatio��ͼƬԭʼ�߶���ѹ����߶ȵı���
	 * ��widthRatio��ͼƬԭʼ�����ѹ�����ȵı�����inSampleSizeΪheightRatio��widthRatio����С���Ǹ�
	 * ��inSampleSize��������ֵ�� inSampleSizeΪ1��ʾ��Ⱥ͸߶Ȳ����ţ�Ϊ2��ʾѹ����Ŀ����߶�Ϊԭ����1/2
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
	 * ����·�����ͻ�Ʋ�ѹ������bitmap������ʾ
	 * 
	 * @return
	 */
	public static Bitmap createPhotos(Bitmap bitmap) {
		if (bitmap != null) {
			Matrix m = new Matrix();
			try {
				m.setRotate(90, bitmap.getWidth() / 2, bitmap.getHeight() / 2);// 90����������Ҫѡ���90��
				Bitmap bmp2 = Bitmap.createBitmap(bitmap, 0, 0,
						bitmap.getWidth(), bitmap.getHeight(), m, true);
				bitmap.recycle();
				bitmap = bmp2;
			} catch (Exception ex) {
				ex.printStackTrace();
				System.out.print("����ͼƬʧ�ܣ�" + ex);
			}
		}
		return bitmap;
	}

	/**
	 * ��ȡͼƬ���ԣ���ת�ĽǶ�
	 * 
	 * @param path
	 *            ͼƬ����·��
	 * @return degree��ת�ĽǶ�
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
	 * ��תͼƬ
	 * 
	 * @param angle
	 * 
	 * @param bitmap
	 * 
	 * @return Bitmap
	 */
	public static Bitmap rotaingImageView(int angle, Bitmap bitmap) {
		// ��תͼƬ ����
		Matrix matrix = new Matrix();
		;
		matrix.postRotate(angle);
		// �����µ�ͼƬ
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
