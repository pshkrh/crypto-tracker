package com.pshkrh.cryptopricetracker;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by pshkr on 30-01-2018.
 */

public class BitcoinFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bitcoin, container, false);
    }

    // Constants:
    private final String BASE_URL = "https://apiv2.bitcoinaverage.com/indices/global/ticker/BTC";

    //String locale = getActivity().getResources().getConfiguration().locale.getCountry();

    // Member Variables:
    TextView mPriceTextView;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)  {
        //super.onActivityCreated(savedInstanceState);
        super.onViewCreated(view, savedInstanceState);
        //setContentView(R.layout.activity_main);

        mPriceTextView = (TextView)view.findViewById(R.id.priceLabel);
        Spinner spinner = (Spinner)view.findViewById(R.id.currency_spinner);


        // Create an ArrayAdapter using the String array and a spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.currency_array, R.layout.spinner_item);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);

        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        //Set default currency to INR (for now)
        spinner.setSelection(7);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d("Crypto", "" + parent.getItemAtPosition(position));
                Log.d("Crypto", "Position is: " + position);
                String finalUrl = BASE_URL + parent.getItemAtPosition(position);
                Log.d("Crypto", "Final URL is: " + finalUrl);
                //Log.d("Crypto", "Location is: " + locale);
                letsDoSomeNetworking(finalUrl);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.d("Crypto", "Nothing selected");
            }
        });

    }

    private void letsDoSomeNetworking(String url) {

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // called when response HTTP status is "200 OK"
                Log.d("Crypto", "JSON: " + response.toString());

                try{
                    String price = response.getString("last");
                    mPriceTextView.setText(price);
                }
                catch(JSONException je){
                    je.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject response) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                Log.d("Crypto", "Request fail! Status code: " + statusCode);
                Log.d("Crypto", "Fail response: " + response);
                Log.e("ERROR", e.toString());
            }
        });
    }
}
