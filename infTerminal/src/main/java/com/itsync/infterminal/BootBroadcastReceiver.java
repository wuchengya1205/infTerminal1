package com.itsync.infterminal;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class BootBroadcastReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(final Context arg0, final Intent arg1) {
		
		if (arg1.getAction().equals("android.intent.action.BOOT_COMPLETED")) {

			Intent intent = new Intent(arg0, MainActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
			arg0.startActivity(intent);
			FileUtils.addTxtToFileBuffered("捕捉到开机广播并启动程序");
		}else if ( arg1.getAction().equals("itsync.intent.action.reload")) {
			Toast.makeText(arg0, "程序发生异常，稍后重启", Toast.LENGTH_LONG).show();
		}

	}
}
