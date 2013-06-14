package com.ws83.firebeam;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;

public class Main extends Activity {

	ImageButton btnSwitch;

	private Camera camera;
	private boolean isFlashOn;
	private boolean hasFlash;
	Parameters params;
	MediaPlayer mp;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		// flash switch button
		btnSwitch = (ImageButton) findViewById(R.id.btnSwitch);

		// First check if device is supporting flashlight or not
		hasFlash = getApplicationContext().getPackageManager()
				.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);

		if (!hasFlash) {
			// device doesn't support flash
			// Show alert message and close the application
			AlertDialog alert = new AlertDialog.Builder(Main.this).create();
			alert.setTitle("Error");
			alert.setMessage("Sorry, your device doesn't support Fire Beam!");
			alert.setButton("OK", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(final DialogInterface dialog,
						final int which) {
					// closing the application
					finish();
				}
			});
			alert.show();
			return;
		}

		// get the camera
		getCamera();

		// displaying button image
		toggleButtonImage();

		// Switch button click event to toggle flash on/off
		btnSwitch.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isFlashOn) {
					// turn off flash
					turnOffFlash();
				} else {
					// turn on flash
					turnOnFlash();
				}
			}
		});
	}

	// Get the camera
	private void getCamera() {
		if (camera == null) {
			try {
				camera = Camera.open();
				params = camera.getParameters();
			} catch (RuntimeException e) {
				Log.e("Camera Error. Failed to Open. Error: ", e.getMessage());
			}
		}
	}

	// Turning On flash
	private void turnOnFlash() {
		if (!isFlashOn) {
			if (camera == null || params == null) {
				return;
			}
			// play sound
			/* playSound(); */

			params = camera.getParameters();
			params.setFlashMode(Parameters.FLASH_MODE_TORCH);
			camera.setParameters(params);
			camera.startPreview();
			isFlashOn = true;

			// changing button/switch image
			toggleButtonImage();
		}

	}

	// Turning Off flash
	private void turnOffFlash() {
		if (isFlashOn) {
			if (camera == null || params == null) {
				return;
			}
			// play sound
			/* playSound(); */

			params = camera.getParameters();
			params.setFlashMode(Parameters.FLASH_MODE_OFF);
			camera.setParameters(params);
			camera.stopPreview();
			isFlashOn = false;

			// changing button/switch image
			toggleButtonImage();
		}

		// Playing sound
		Button btn1 = (Button) findViewById(R.id.btn_site);
		btn1.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent myWebLink = new Intent(
						android.content.Intent.ACTION_VIEW);
				myWebLink.setData(Uri.parse("http://mobile-overload.com"));
				startActivity(myWebLink);
			}
		});
		{
		}

		Button btn2 = (Button) findViewById(R.id.btn_donate);
		btn2.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent myWebLink = new Intent(
						android.content.Intent.ACTION_VIEW);
				myWebLink.setData(Uri
						.parse("https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=LAREM9E6D3SAW"));
				startActivity(myWebLink);
			}
		});
		{
		}

		Button btn3 = (Button) findViewById(R.id.btn_moreapps);
		btn3.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent myWebLink = new Intent(
						android.content.Intent.ACTION_VIEW);
				myWebLink.setData(Uri
						.parse("https://play.google.com/store/apps/developer?id=Joshua+Hale"));
				startActivity(myWebLink);
			}
		});
	}

	/*
	 * Toggle switch button images changing image states to on / off
	 */
	private void toggleButtonImage() {
		if (isFlashOn) {
			btnSwitch.setImageResource(R.drawable.btn_switch_on);
		} else {
			btnSwitch.setImageResource(R.drawable.btn_switch_off);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		super.onPause();

		// on pause turn off the flash
		turnOffFlash();
	}

	@Override
	protected void onRestart() {
		super.onRestart();
	}

	@Override
	protected void onResume() {
		super.onResume();

		// on resume turn on the flash
		if (hasFlash)
			turnOnFlash();
	}

	@Override
	protected void onStart() {
		super.onStart();

		// on starting the app get the camera params
		getCamera();
	}

	@Override
	protected void onStop() {
		super.onStop();

		// on stop release the camera
		if (camera != null) {
			camera.release();
			camera = null;
		}
		{

		}

	}
}