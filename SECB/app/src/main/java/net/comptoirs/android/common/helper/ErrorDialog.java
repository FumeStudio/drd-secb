package net.comptoirs.android.common.helper;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

public class ErrorDialog
{
	public static AlertDialog alert;
	
	public static void showMessageDialog(final String title, final String message, final Activity activity){
		showMessageDialog(title, message, activity, null);
	}
	
	public static void showMessageDialog(final String title, final String message, final Activity activity, final Runnable runnable)
	{
		if (activity == null)
			return;
		activity.runOnUiThread(new Runnable()
		{
			@Override
			public void run()
			{
				final AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
				dialog.setTitle(title);
				dialog.setMessage(message);
				dialog.setCancelable(false);

				dialog.setPositiveButton(activity.getString(android.R.string.ok), new DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface dialog, int whichButton)
					{
						if(runnable!= null){
							runnable.run();
						}
					}
				});

				if(alert != null && alert.isShowing())
					alert.dismiss();
				alert = dialog.create();
				alert.show();
			}
		});
	}
}
