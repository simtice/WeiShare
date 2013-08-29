package net.sourceforge.simcpux.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class DialogUtil {
	public static void showDialog(Context context,DialogInterface.OnClickListener yes,DialogInterface.OnClickListener no) {

		AlertDialog.Builder dialog = new AlertDialog.Builder(context);
		dialog.setTitle("正在分享至微信");
		dialog.setMessage("是否分享到朋友圈?");
		dialog.setPositiveButton("是", yes);
		dialog.setNegativeButton("否",no);
		dialog.create().show();
	}
}
