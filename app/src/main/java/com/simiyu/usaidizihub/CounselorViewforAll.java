package com.simiyu.usaidizihub;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.androidstudy.daraja.Daraja;
import com.androidstudy.daraja.DarajaListener;
import com.androidstudy.daraja.model.AccessToken;
import com.androidstudy.daraja.model.LNMExpress;
import com.androidstudy.daraja.model.LNMResult;
import com.androidstudy.daraja.util.Env;
import com.androidstudy.daraja.util.TransactionType;
import com.google.android.material.snackbar.Snackbar;

public class CounselorViewforAll extends Fragment {

    public static final String CONSUMER_KEY = "fcS3KzI2LwvMR3I9m23W60wRlntXCehX";
    public static final String CONSUMER_SECRET = "LP9Nv96jKBbyAydz";
    //Don't use this...jk
    //public static final String PHONE_NUMBER = "254715598801";
    //public static final String TRANSACTION_DESCRIPTION = "Counselor card payment";
    //public static final String ACCOUNT_REFERENCE = "UH101";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // TODO: 1/17/2021 M-pesa STK push
        //For Production Mode
        final Daraja daraja = Daraja.with(CONSUMER_KEY, CONSUMER_SECRET, new DarajaListener<AccessToken>() {
            @Override
            public void onResult(@NonNull AccessToken accessToken) {
                Log.i(this.getClass().getSimpleName(), accessToken.getAccess_token());
            }

            @Override
            public void onError(String error) {
                Log.e(this.getClass().getSimpleName(), error);
            }
        });
        /**
         * Generating the token
         * Code ends here: M-Pesa
         */

        //on btn click__STK Push
        Button buyCard = getView().findViewById(R.id.buyCard);

        buyCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    LNMExpress lnmExpress = new LNMExpress(
                            "174379",
                            "bfb279f9aa9bdbcf158e97dd71a467cd2e0c893059b10f78e6b72ada1ed2c919",
                            TransactionType.CustomerBuyGoodsOnline, // TransactionType.CustomerPayBillOnline
                            "100",
                            "254708374149",
                            "174379",
                            "0707159659",
                            "http://mycallbackurl.com/checkout.php",
                            "001ABC",
                            "Counselor Payment");
                    daraja.requestMPESAExpress(lnmExpress, new DarajaListener<LNMResult>() {
                        @Override
                        public void onResult(@NonNull LNMResult lnmResult) {
                            Log.i(this.getClass().getSimpleName(), lnmResult.ResponseDescription);
                        }

                        @Override
                        public void onError(String error) {
                            Log.i(this.getClass().getSimpleName(), error);
                        }
                    });
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_counselor_viewfor_all, container, false);
    }
}