package com.olc;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

//import com.tagologic.rfid.R;
//import com.tagologic.rfid.RfidApplication;
//import com.tagologic.rfid.api.ApiAuthorization;
//import com.tagologic.rfid.model.request.GetTTagByNubmerRequest;
//import com.tagologic.rfid.model.response.GetTTagByNumberResponse;

import java.io.Serializable;
import java.net.ConnectException;
import java.net.UnknownHostException;
import java.util.List;

//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;

/**
 * Created by macbookpro on 28.05.15.
 */
public class ScanResultActivity extends Activity {

    private List<String> epcList;
    private final String TAG = ScanResultActivity.class.getCanonicalName();
    public static final String EXTRA_SCANFOREDIT = "EXTRA_SCANFOREDIT";
    public static final String EXTRA_EPC = "EXTRA_EPC";
    public static final String EXTRA_EPC_LIST = "EXTRA_EPC_LIST";
    private boolean scanForEdit;

    public static void start(Context context, boolean scanForEdit, List<String> epcList) {
        Log.d("wyt", "ScanResultActivity start called");
        Intent intent = new Intent(context, ScanResultActivity.class);
        intent.putExtra(EXTRA_SCANFOREDIT, scanForEdit);
        intent.putExtra(EXTRA_EPC_LIST, (Serializable) epcList);
        //if (scanForEdit)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static void startForResult(Activity context, boolean scanForEdit, List<String> epcList, int resultCode) {
        Log.d("wyt", "ScanResultActivity startForResult called");
        Intent intent = new Intent(context, ScanResultActivity.class);
        intent.putExtra(EXTRA_SCANFOREDIT, scanForEdit);
        intent.putExtra(EXTRA_EPC_LIST, (Serializable) epcList);
        context.startActivityForResult(intent, resultCode);
    }
}
