package com.learn.zohky.expensemanager;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.widget.Button;

import com.google.gson.*;
import com.learn.zohky.expensemanager.DAOs.CurrenciesDAO;
import com.learn.zohky.expensemanager.DAOs.RatesDAO;
import com.learn.zohky.expensemanager.Handler.CurrenciesHandler;
import com.learn.zohky.expensemanager.Handler.RatesHandler;


import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class DownloadJsonTask extends AsyncTask<Integer,Long,Boolean> {

    private Activity activity;
    private CurrenciesHandler currenciesHandler;
    private RatesHandler ratesHandler;
    private String base;

    public DownloadJsonTask(Activity activity, CurrenciesHandler currenciesHandler, RatesHandler ratesHandler,String base) {
        this.activity = activity;
        this.currenciesHandler = currenciesHandler;
        this.ratesHandler = ratesHandler;
        this.base = base;
    }

    /**
     * Check if rates table contain the current rates.
     * if so, uses them. else, download the new rates.
     */
    private void checkExists(){
        ArrayList<RatesDAO> ratesDAOArrayList = (ArrayList<RatesDAO>) ratesHandler.getToday();
        //if the today date not exists in db get it from the server
        if(ratesDAOArrayList.isEmpty()){
//            System.out.println("--> Date don't Exists <--");
            saveInDb(getRestCall());
         //else get it from db
        } else {
//            System.out.println("--> Date Exists <--");
        }

    }

    /**
     * Try to download the current rates from the server.
     * If it's cant do this, get the rates from local file
     * @return json rates as string
     */
    @Nullable
    private String getRestCall() {
        try {
            String urlStr = "http://api.fixer.io/latest?base=" + base ;
//            System.out.println("--> urlStr: " + urlStr);
            URL url = new URL(urlStr);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setConnectTimeout(1000*4);
            con.setReadTimeout(1000*4);
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String str = in.readLine();
            in.close();
            return str;
        } catch (Exception e) {
//            System.out.println("--> can't download online file");
            e.printStackTrace();
            try {
//                System.out.println("--> Read offline file");
                InputStream in = activity.getAssets().open("offline_json.txt");
                return readInputStream(in);

            } catch (Exception e1) {
//                System.out.println("--> Could not read offline file");
                e1.printStackTrace();
            }
            return null;
        }
    }

    /**
     * Get the json string, convert it into json object, and save it into the rates table.
     * @param JsonStr The string represents the json rates.
     */
    private void saveInDb(String JsonStr){
        // transform to json
        // save in database
        JsonParser p = new JsonParser();
        JsonObject response = (JsonObject) p.parse(JsonStr);
        JsonPrimitive base = response.getAsJsonPrimitive("base");
        JsonPrimitive date = response.getAsJsonPrimitive("date");
        JsonObject ratesObjs = response.getAsJsonObject("rates");
//        System.out.println("--> base: " + base.getAsString());
//        System.out.println("--> date: " + date.getAsString());


        //fill the Def Base a.k.a ILS
        CurrenciesDAO currenciesDAO = new CurrenciesDAO("ILS");
        if (!currenciesHandler.checkIfExists(currenciesDAO)) {
            currenciesHandler.create(currenciesDAO);
        }
        int defCurrId = (currenciesHandler.getByName(currenciesDAO)).getId();
        String dateAsString = date.getAsString();
        Date dateFormat = null;
        try {
            dateFormat = new SimpleDateFormat("yyyy-MM-dd").parse(dateAsString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        RatesDAO ratesDao = new RatesDAO(defCurrId, 1, dateFormat);
        if (!ratesHandler.checkIfExists(ratesDao)){
//            System.out.println("--> Not Exists: ILS");
            ratesHandler.create(ratesDao);
        }


        //fill the curr Table
        Set<Map.Entry<String, JsonElement>> rates = ratesObjs.entrySet();
        for (Map.Entry<String, JsonElement> entry : rates) {
//            System.out.println("--> name " + entry.getKey());
//            System.out.println("--> rate " + entry.getValue());
            currenciesDAO = new CurrenciesDAO(entry.getKey());
            if (!currenciesHandler.checkIfExists(currenciesDAO)) {
                currenciesHandler.create(currenciesDAO);
            }

            //fill the rates Table
            int currId = (currenciesHandler.getByName(new CurrenciesDAO(entry.getKey()))).getId();
            ratesDao = new RatesDAO(currId, entry.getValue().getAsFloat(), dateFormat);
            if (!ratesHandler.checkIfExists(ratesDao)){
//                System.out.println("--> Not Exists: " + entry.getKey());
                ratesHandler.create(ratesDao);
            }
        }
    }

    private String readInputStream(InputStream in) throws IOException {
        String line;
        StringBuilder stringBuilder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
            while ((line = reader.readLine()) != null)
                stringBuilder.append(line);
            return stringBuilder.toString();
        }
    }

    @Override
    protected Boolean doInBackground(Integer... integers) {
        checkExists();
        return true;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        Button enter = (Button)activity.findViewById(R.id.buttonEnter);
        enter.setEnabled(true);
    }
}
