package net.sourceforge.simcpux.ui;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import net.sourceforge.simcpux.Constant;
import net.sourceforge.simcpux.R;
import net.sourceforge.simcpux.util.DialogUtil;
import net.sourceforge.simcpux.util.ImageUtil;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.SendMessageToWX;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.mm.sdk.openapi.WXImageObject;
import com.tencent.mm.sdk.openapi.WXMediaMessage;
import com.tencent.mm.sdk.platformtools.Util;

public class PhotoShareActivity extends SherlockActivity {

	private static final int REQUEST_TAKE_PHOTO = 1;
	private Bitmap bit;
	private String mCurrentPhotoPath;// ԭʼͼƬ·��
	private ImageView image;
	private IWXAPI api;
	private int THUMB_SIZE = 150;

	protected void onCreate(android.os.Bundle savedInstanceState) {
		setTheme(Constant.THEME);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.photo_layout);
		init();

	}

	private void init() {
		image = (ImageView) this.findViewById(R.id.image);
		api = WXAPIFactory.createWXAPI(this, Constant.APP_ID, false);

		image.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				sharePhoto();
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		getSupportMenuInflater().inflate(R.menu.photo, menu);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// This uses the imported MenuItem from ActionBarSherlock
		switch (item.getItemId()) {
		case R.id.camera:
			takePhoto();
			break;
		}
		return true;
	}

	private void sharePhoto() {
		if (!TextUtils.isEmpty(mCurrentPhotoPath)) {

			WXImageObject imgObj = new WXImageObject();
			imgObj.setImagePath(mCurrentPhotoPath);

			WXMediaMessage msg = new WXMediaMessage();
			msg.mediaObject = imgObj;

			Bitmap bmp = BitmapFactory.decodeFile(mCurrentPhotoPath);
			Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, THUMB_SIZE,
					THUMB_SIZE, true);

			thumbBmp = ImageUtil.rotaingImageView(
					ImageUtil.readPictureDegree(mCurrentPhotoPath), thumbBmp);

			bmp.recycle();
			msg.thumbData = Util.bmpToByteArray(thumbBmp, true);

			final SendMessageToWX.Req req = new SendMessageToWX.Req();
			req.transaction = net.sourceforge.simcpux.util.Util
					.buildTransaction("img");
			req.message = msg;
			DialogInterface.OnClickListener yes = new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {

					req.scene = SendMessageToWX.Req.WXSceneTimeline;
					api.sendReq(req);
					PhotoShareActivity.this.finish();
				}
			};
			DialogInterface.OnClickListener no = new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					req.scene = SendMessageToWX.Req.WXSceneSession;
					api.sendReq(req);
					PhotoShareActivity.this.finish();
				}
			};
			DialogUtil.showDialog(this, yes, no);
		}
	}

	private void takePhoto() {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		try {
			File f = createImageFile();
			intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
			startActivityForResult(intent, REQUEST_TAKE_PHOTO);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case REQUEST_TAKE_PHOTO:
			switch (resultCode) {
			case Activity.RESULT_OK:
				DisplayMetrics dm = new DisplayMetrics();
				getWindowManager().getDefaultDisplay().getMetrics(dm);
				bit = ImageUtil.getSmallBitmap(mCurrentPhotoPath,
						dm.widthPixels, dm.widthPixels);

				bit = ImageUtil.rotaingImageView(
						ImageUtil.readPictureDegree(mCurrentPhotoPath), bit);

				image.setImageBitmap(bit);
				break;
			}
			break;

		}
	}

	private File createImageFile() throws Exception {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		String timeStamp = formatter.format(new Date());
		String fileName = timeStamp + ".jpg";
		File file = new File(ImageUtil.getAlbumDir(), fileName);
		mCurrentPhotoPath = file.getAbsolutePath();
		return file;
	}

}
