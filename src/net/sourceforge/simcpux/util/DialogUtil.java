package net.sourceforge.simcpux.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class DialogUtil {
	public static void showDialog(Context context,DialogInterface.OnClickListener yes,DialogInterface.OnClickListener no) {

		AlertDialog.Builder dialog = new AlertDialog.Builder(context);
		dialog.setTitle("���ڷ�����΢��");
		dialog.setMessage("�Ƿ��������Ȧ?");
		dialog.setPositiveButton("��", yes);
		dialog.setNegativeButton("��",no);
		dialog.create().show();
	}
}
