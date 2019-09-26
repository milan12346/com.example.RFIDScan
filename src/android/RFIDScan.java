package com.example.plugin;

import org.apache.cordova.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

// import com.android.hdhe.uhf.UhfAdapter;
// import com.android.hdhe.uhf.UhfManager;
import com.android.hdhe.uhf.ISO18000_6C;
import com.android.hdhe.uhf.MainActivity;

// import com.android.hdhe.uhf.tech.IUhfCallback;
import com.android.hdhe.uhf.reader.Tools;
import com.android.hdhe.uhf.reader.UhfReader;


import android.util.Log;
import com.olc.mode.readmode;
import com.olc.uint.TxtReader;
import com.olc.ScanResultActivity;

import android.view.KeyEvent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.text.TextUtils;
import java.io.Serializable;

import java.text.SimpleDateFormat;
import java.text.MessageFormat;

import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;
import android.widget.Toast;

public class RFIDScan extends CordovaPlugin {

	private static final boolean IS_AT_LEAST_LOLLIPOP = Build.VERSION.SDK_INT >= 21;
	//private ISO18000_6C uhf_6c;
	private boolean isLoop =false;
    private int Index = 1;
	//int allcount = 0;
	public JSONArray EPCName = new JSONArray();
	//public static final String EPCName1 = "";
	//public static UhfManager mService;
	//public Context context = null;
	//private Handler mHandler = new MainHandler();

	public static final String EXTRA_SCAN_FOR_EDIT = "EXTRA_SCAN_FOR_EDIT";
	public static final String EXTRA_SCANFOREDIT = "EXTRA_SCANFOREDIT";
	public static final String EXTRA_EPC = "EXTRA_EPC";
	public static final String EXTRA_EPC_LIST = "EXTRA_EPC_LIST";

	public static final int REQUEST_CODE_CHOOSER = 1001;
	private final String TAG = RFIDScan.class.getCanonicalName();
	private final int RFID_POWER = 26;
	private final int TIMER_INTERVAL = 5000;
	private UhfReader reader;
	private TextView topText;
	private TextView bottomText;

	private List<String> epcStringList = new ArrayList<String>();
	private InventoryThread workerThread;
	private boolean stopScanning;
	private boolean scanForEdit;

	public Context context = null;
	public Context context1 = null;

	private Handler mHandler = new MainHandler();
	  String javaScriptEventTemplate =
        "var e = document.createEvent(''Events'');\n" +
        "e.initEvent(''{0}'');\n" +
        "e.tag = ''{1}'';\n" +
        "document.dispatchEvent(e);";


	public void initialize(CordovaInterface cordova, CordovaWebView webView) {
		super.initialize(cordova, webView);
	}


	@Override
    public boolean execute(String action, JSONArray data, CallbackContext callbackContext) throws JSONException {
		Log.d("wyt", "execute");
		try {
			context1 = cordova.getActivity().getApplicationContext();

			Log.d("wyt", "ReadTIDStart start");

			if (action.equals("ReadTIDStart")) {
				Log.d("wyt", "ReadTIDStart called");
				this.ReadTIDStart();
				callbackContext.success();
				return true;
			}
			else if (action.equals("ReadTIDStop")) {
				this.ReadTIDStop(callbackContext);
				return true;
			}
			else {
				return false;
			}
		} catch (Exception e) {
			Log.d("wyt", "error");
			return false;
		}
    }

	public static void start(Activity context, boolean scanForEdit, int request_code ) {
		Log.d("wyt", "start method");
		Intent intent = new Intent(context, RFIDScan.class);
		intent.putExtra(EXTRA_SCAN_FOR_EDIT, scanForEdit);
		if ( scanForEdit)
			context.startActivityForResult(intent, request_code);
		else
			context.startActivity(intent);
	}

	public void getStatusDescribe1(CallbackContext callbackContext) {
			Log.d("wyt", "getStatusDescribe");
			//String str = uhf_6cuhf_6c.getVersion();
			//callbackContext.success(str);
    }

	public void ReadTIDStart() {
		stopScanning = false;
		Log.d("wyt", "set context");
		reader = UhfReader.getInstance();
		if(reader == null){
			Log.d("wyt", "RFID scanning not supported by this device");
			//callbackContext.error("RFID scanning not supported by this device");
			//return false;
		}


		Log.d("wyt", "Start RFID");
		reader.setOutputPower(RFID_POWER);
		workerThread = new InventoryThread();
		workerThread.start();
		Log.d("wyt", "after workerThread Start");
		//stopScanning = false;
    }

	private void ReadTIDStop(CallbackContext callbackContext) {
		callbackContext.success(EPCName.toString());
		EPCName = new JSONArray();
	}

	class InventoryThread extends Thread{
		private List<byte[]> epcList;
		private boolean firstEpc = true;

		@Override
		public void run() {
			Log.d("wyt", "InventoryThread");
			super.run();
			while(!stopScanning){

				try {
					if ( !stopScanning )
						epcList = reader.inventoryRealTime();
				} catch (Exception e) {
					e.printStackTrace();
				}

				Log.d("wyt", "epcList : " + epcList);
				if(epcList != null && !epcList.isEmpty()){
					for(byte[] epc:epcList){
						if ( firstEpc )
						{

							firstEpc = false;
							if(epcList != null)
							{
								stopScanning = true;
								Log.d("wyt", "stopScanning : " + stopScanning);
								//callbackContext.success(epcList);
								//if (!scanForEdit) {
									/*Log.d("wyt", "ScanResultActivity start called");
									Intent intent = new Intent(cordova.getActivity().getApplicationContext(), RFIDScan.class);
									intent.putExtra(EXTRA_SCANFOREDIT, scanForEdit);
									intent.putExtra(EXTRA_EPC_LIST, (Serializable) epcList);
									//if (scanForEdit)
										intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
									cordova.getActivity().getApplicationContext().startActivity(intent);*/
//									ScanResultActivity.start(context1, scanForEdit, epcStringList);
								//}
								//else {
									//ScanResultActivity.startForResult(cordova.getActivity(), scanForEdit, epcStringList, REQUEST_CODE_CHOOSER);
								//}
							}

							  /*runOnUiThread(new Runnable() {

								@Override
								public void run() {
									Log.d("wyt", "thread1");
									//topText.setVisibility(View.GONE);
									//bottomText.setText(R.string.scanning);

									new CountDownTimer(TIMER_INTERVAL,TIMER_INTERVAL) {
										@Override
										public void onTick(long l) {
											Log.d("wyt", "onTick");
										}

										@Override
										public void onFinish() {
											//context1 = this.cordova.getActivity().getApplicationContext();
											Log.d("wyt", "onFinish");
											if (!scanForEdit)
												ScanResultActivity.start(context1, scanForEdit, epcStringList);
											else
												ScanResultActivity.startForResult(cordova.getActivity(), scanForEdit, epcStringList,REQUEST_CODE_CHOOSER);
										}
									}.start();
								}
							});*/
//							//thread1.start();
						}

						boolean contains = false;
						String epcStr = Tools.Bytes2HexString(epc, epc.length);
						Log.e(TAG, epcStr);
						Log.d("wyt", "TAG : " + epcStr);
						//EPCName1 = epcStr;
						EPCName = new JSONArray();
						EPCName.put(epcStr);

						for ( String s : epcStringList )
						{
							if ( s.compareToIgnoreCase(epcStr) == 0 )
							{
								contains = true;
								break;
							}
						}

						if ( !contains ) {
							Log.d("wyt", "contains : " + epcStringList);
							epcStringList.add(epcStr);
						}

						if ( workerThread != null )
						{
							Log.d("wyt", "workerThread");
							stopScanning = true;
							workerThread.interrupt();
							workerThread = null;
							reader.close();
							fireEvent( "EPC"  , EPCName.toString());

							//RFIDScan.ReadTIDStop();
						}

					}
				}

				epcList = null ;
				try {
					Thread.sleep(40);
				} catch (InterruptedException e) {}
			}
		}
	}


	public void LoopReadTID() {
		//Thread thread = new Thread(new Runnable() {

        //});
        //thread.start();
    }
	
	 private class MainHandler extends Handler {
        @SuppressLint("HandlerLeak")
        @Override
        public void handleMessage(Message msg) {
		Log.d("wyt", "msg");
		if (msg.what == 1) 
			{			 		
                String readerdata = (String) msg.obj;
                {
					Log.d("wyt", "msg =" +readerdata );
					fireEvent( "EPC"  , readerdata);					
                }
           }
        }
    };

	private void fireEvent(String type, String messages) {

        String command = MessageFormat.format(javaScriptEventTemplate, type, messages);
        Log.v("wyt", command);
        this.webView.sendJavascript(command);
    }
		
	public static byte[] stringToBytes(String hexString) {
		if (hexString == null || hexString.equals("")) {
			return null;
		}
		hexString = hexString.toUpperCase();
		int length = hexString.length() / 2;
		char[] hexChars = hexString.toCharArray();
		byte[] d = new byte[length];
		for (int i = 0; i < length; i++) {
			int pos = i * 2;
			d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
		}
		return d;
	}

	private static byte charToByte(char c) {
		return (byte) "0123456789ABCDEF".indexOf(c);
	}
}
