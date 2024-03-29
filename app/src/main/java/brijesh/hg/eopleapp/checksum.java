package brijesh.hg.eopleapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class checksum extends AppCompatActivity implements PaytmPaymentTransactionCallback{
    String custid="", orderId="", mid="", txnamount="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
        //initOrderId();


        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        Intent intent = getIntent();
        orderId = intent.getExtras().getString("orderid");
        custid = intent.getExtras().getString("custid");
        txnamount = intent.getExtras().getString("txnamount");
        mid = "HaWNyx92022787507029"; /// your marchant key
        sendUserDetailTOServerdd dl = new sendUserDetailTOServerdd();
        dl.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
// vollye , retrofit, asynch
    }
    public class sendUserDetailTOServerdd extends AsyncTask<ArrayList<String>, Void, String> {
        private ProgressDialog dialog = new ProgressDialog(checksum.this);
        //private String orderId , mid, custid, amt;
        //String url ="https://hgpaytm.000webhostapp.com/generateChecksum.php";

        //String url ="https://www.blueappsoftware.com/payment/payment_paytm/generateChecksum.php";

        String url ="https://ruchapaytmhost.000webhostapp.com/generateChecksum.php";
        //String url ="http://nav.mypressonline.com/generateChecksum.php";
        String varifyurl = "https://securegw.paytm.in/theia/paytmCallback?ORDER_ID="+orderId;

        //String varifyurl = "https://pguat.paytm.com/paytmchecksum/paytmCallback.jsp";

        //String varifyurl = "https://securegw-stage.paytm.in/theia/paytmCallback?ORDER_ID=1234567";
        // "https://securegw-stage.paytm.in/theia/paytmCallback?ORDER_ID"+orderId;
        String CHECKSUMHASH ="";
        @Override
        protected void onPreExecute() {
            this.dialog.setMessage("Please wait");
            this.dialog.show();
        }

        protected String doInBackground(ArrayList<String>... alldata) {
            Log.e("CheckSum result >>","inside do in back");
            JSONParser jsonParser = new JSONParser(checksum.this);
            String param=
                    "MID="+mid+
                            "&ORDER_ID=" + orderId +
                            "&CUST_ID="+custid+
                            "&CHANNEL_ID=WAP"+"&TXN_AMOUNT="+txnamount+"&WEBSITE=DEFAULT"+
                            "&CALLBACK_URL="+ varifyurl+"&INDUSTRY_TYPE_ID=Retail";
            JSONObject jsonObject = jsonParser.makeHttpRequest(url,"POST",param);
            // yaha per checksum ke saht order id or status receive hoga..
            Log.e("CheckSum result >>",jsonObject.toString());
            if(jsonObject != null){
                Log.e("CheckSum result >>",jsonObject.toString());
                try {
                    CHECKSUMHASH=jsonObject.has("CHECKSUMHASH")?jsonObject.getString("CHECKSUMHASH"):"";
                    Log.e("CheckSum result >>",CHECKSUMHASH);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return CHECKSUMHASH;
        }
        @Override
        protected void onPostExecute(String result) {
            try {
                super.onPostExecute(result);
                Log.e(" setup acc ", "  signup result  " + result);

                }catch (Exception ex){
                    Toast.makeText(checksum.this, "Something is going wrong, please try again!", Toast.LENGTH_LONG).show();
                }

            if (dialog.isShowing()) {
                dialog.dismiss();


            }



            //PaytmPGService Service = PaytmPGService.getStagingService();
            PaytmPGService Service = PaytmPGService.getProductionService();
            // when app is ready to publish use production service
            // PaytmPGService  Service = PaytmPGService.getProductionService();
            // now call paytm service here
            //below parameter map is required to construct PaytmOrder object, Merchant should replace below map values with his own values
            HashMap<String, String> paramMap = new HashMap<String, String>();
            //these are mandatory parameters
            paramMap.put("MID", "HaWNyx92022787507029");
            paramMap.put("ORDER_ID", orderId);
            paramMap.put("CUST_ID", custid);
            paramMap.put("INDUSTRY_TYPE_ID", "Retail");
            paramMap.put("CHANNEL_ID", "WAP");
            paramMap.put("TXN_AMOUNT", txnamount);
            paramMap.put("WEBSITE", "DEFAULT");
            paramMap.put("CALLBACK_URL" ,varifyurl);
            //paramMap.put( "EMAIL" , "ashishghadi27@gmail.com");   // no need
            //paramMap.put( "MOBILE_NO" , "9664359034");  // no need
            paramMap.put("CHECKSUMHASH" ,CHECKSUMHASH);
            //paramMap.put("PAYMENT_TYPE_ID" ,"CC");    // no need

            PaytmOrder Order = new PaytmOrder(paramMap);
            Log.e("checksum ", "param "+ paramMap.toString());
            Service.initialize(Order,null);
            // start payment service call here
            Service.startPaymentTransaction(checksum.this, true, true,
                    checksum.this);
        }
    }
    @Override
    public void onTransactionResponse(Bundle bundle) {
        Log.e("checksum ", " respon true " + bundle.toString());

        if(bundle.toString().contains("STATUS=TXN_SUCCESS")){
            Intent intent = new Intent(checksum.this, Transaction_status.class);
            startActivity(intent);
            Toast.makeText(this,"Transaction Succss",Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(checksum.this, "Transaction Failed", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(checksum.this, Main.class);
            startActivity(intent);
            finish();
        }
    }
    @Override
    public void networkNotAvailable() {

    }
    @Override
    public void clientAuthenticationFailed(String s) {
    }
    @Override
    public void someUIErrorOccurred(String s) {
        Log.e("checksum ", " ui fail respon  "+ s );
    }
    @Override
    public void onErrorLoadingWebPage(int i, String s, String s1) {
        Log.e("checksum ", " error loading pagerespon true "+ s + "  s1 " + s1);
    }
    @Override
    public void onBackPressedCancelTransaction() {
        Intent intent = new Intent(checksum.this, Main.class);
        startActivity(intent);
        finish();
    }
    @Override
    public void onTransactionCancel(String s, Bundle bundle) {
        Log.e("checksum ", "  transaction cancel " );
    }





}