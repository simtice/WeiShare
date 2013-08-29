package net.sourceforge.simcpux.wxapi;

import net.sourceforge.simcpux.Constant;
import net.sourceforge.simcpux.R;
import net.sourceforge.simcpux.ui.PhotoShareActivity;
import net.sourceforge.simcpux.util.DialogUtil;
import net.sourceforge.simcpux.util.Util;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.tencent.mm.sdk.openapi.BaseReq;
import com.tencent.mm.sdk.openapi.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.SendMessageToWX;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.mm.sdk.openapi.WXMediaMessage;
import com.tencent.mm.sdk.openapi.WXTextObject;

public class WXEntryActivity extends SherlockActivity implements
		IWXAPIEventHandler {

	private IWXAPI api;
	private EditText editText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setTheme(Constant.THEME); // Used for theme switching in samples
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		init();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		getSupportMenuInflater().inflate(R.menu.main, menu);

		return true;
	}

	private void init() {
		api = WXAPIFactory.createWXAPI(this, Constant.APP_ID, false);
		editText = (EditText) this.findViewById(R.id.edit);
		api.registerApp(Constant.APP_ID);
//		api.handleIntent(getIntent(), this);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// This uses the imported MenuItem from ActionBarSherlock
		switch (item.getItemId()) {
		case R.id.share_photo:
			startActivity(new Intent(this, PhotoShareActivity.class));
			break;
		case R.id.share_text:
			textShare();
			break;
		case R.id.forward:
			api.openWXApp();
			break;
		}
		return true;
	}

	private void textShare() {
		String text = editText.getText().toString();
		if (!TextUtils.isEmpty(text.trim())) {
			// 初始化一个WXTextObject对象
			WXTextObject textObj = new WXTextObject();
			textObj.text = text;

			// 用WXTextObject对象初始化一个WXMediaMessage对象
			WXMediaMessage msg = new WXMediaMessage();
			msg.mediaObject = textObj;
			// 发送文本类型的消息时，title字段不起作用
			// msg.title = "Will be ignored";
			msg.description = text;

			// 构造一个Req
			final SendMessageToWX.Req req = new SendMessageToWX.Req();

			req.transaction = Util.buildTransaction("text"); // transaction字段用于唯一标识一个请求
			req.message = msg;

			DialogInterface.OnClickListener yes = new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					req.scene = SendMessageToWX.Req.WXSceneTimeline;
					api.sendReq(req);
				}
			};

			DialogInterface.OnClickListener no = new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					req.scene = SendMessageToWX.Req.WXSceneSession;
					api.sendReq(req);
				}
			};

			DialogUtil.showDialog(this, yes, no);

		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		// ungisterApp();

	}

	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);
		setIntent(intent);
		api.handleIntent(intent, this);
	}

	// private void ungisterApp() {
	// api.unregisterApp();
	// }

	// private void registerApp() {
	// api.registerApp(Constant.APP_ID);
	// // String str = "";
	// // if (flag) {
	// // str = "注册成功";
	// // } else {
	// // str = "注册失败";
	// // }
	// // Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
	// api.handleIntent(getIntent(), this);
	// }

	@Override
	public void onReq(BaseReq arg0) {

	}

	@Override
	public void onResp(BaseResp arg0) {
		switch (arg0.errCode) {
		case BaseResp.ErrCode.ERR_OK:
			Toast.makeText(this, "已经成功分享", Toast.LENGTH_SHORT).show();
			break;
		}

	}

}
