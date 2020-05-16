package com.example.covid19tracker;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ArrayList<String> allCountries, bebe, newcof, newde, dea, newRe, re, da, cList;
    private ListView listView;
    private RequestQueue queue;
    private ArrayAdapter arrayAdapter;
    public static final String URL = "https://api.covid19api.com/summary";
//    private List<EarthQuake> quakeList;

    private AlertDialog.Builder dialogBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        listView = (ListView) findViewById(R.id.listView);
        queue = Volley.newRequestQueue(this);
        allCountries = new ArrayList<>();
        bebe = new ArrayList<>();

        newcof = new ArrayList<>();
        newde = new ArrayList<>();
        dea = new ArrayList<>();
        newRe = new ArrayList<>();
        re = new ArrayList<>();
        da = new ArrayList<>();
        cList= new ArrayList<>();

        getAllData(URL);
    }

    void getAllData(String url){


        JsonObjectRequest jsonObjectRequest = new
                JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            final JSONArray countries = response.getJSONArray("Countries");

                           final JSONObject glob =response.getJSONObject("Global");
//
                            String global = glob.getString("NewConfirmed");
                            String tots = glob.getString("TotalConfirmed");
                            String nede = glob.getString("NewDeaths");
                            String dets = glob.getString("TotalDeaths");
                            String nrecov = glob.getString("NewRecovered");
                            String recov = glob.getString("TotalRecovered");


                            cList.add(0, "Global");
                            allCountries.add(0,"Global");
                            bebe.add(0, tots);
                            newcof.add(0,global);
                            newde.add(0,nede);
                            dea.add(0, dets);
                            newRe.add(0, nrecov);
                            re.add(0, recov);


                            for (int i = 0; i<countries.length(); i++){

                                String country = countries.getJSONObject(i).getString("Country");
                                String newConfirmed = countries.getJSONObject(i).getString("NewConfirmed");
                                String totalConfirmed = String.valueOf(countries.getJSONObject(i).getInt("TotalConfirmed"));
                                String newDeaths = countries.getJSONObject(i).getString("NewDeaths");
                                String totalDeaths = countries.getJSONObject(i).getString("TotalDeaths");
                                String newRecovered = countries.getJSONObject(i).getString("NewRecovered");
                                String totalRecovered = countries.getJSONObject(i).getString("TotalRecovered");
                                String date = countries.getJSONObject(i).getString("Date");

                                cList.add(+(i+1) +". "+country);
                                allCountries.add(country);
                                bebe.add(totalConfirmed);
                                newcof.add(newConfirmed);
                                newde.add(newDeaths);
                                dea.add(totalDeaths);
                                newRe.add(newRecovered);
                                re.add(totalRecovered);
                                da.add(date);




                            }


//                           Log.d("kiimba", "onResponse: " +da);

                            arrayAdapter = new ArrayAdapter<>(MainActivity.this,
                                    android.R.layout.simple_list_item_1, android.R.id.text1,cList);

                            listView.setAdapter(arrayAdapter);

                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    String country = allCountries.get(i);
                                   int totConf= Integer.parseInt(bebe.get(i));

                                    int newConf= Integer.parseInt(newcof.get(i));
                                    int newDed= Integer.parseInt(newde.get(i));
                                    int ded= Integer.parseInt(dea.get(i));
                                    int newRec= Integer.parseInt(newRe.get(i));
                                    int rec= Integer.parseInt(re.get(i));
                                   String dat= da.get(i);

//                                   int tot =Integer.decode(totConf);

                                    Toast.makeText(MainActivity.this, "We are fine", Toast.LENGTH_LONG).show();


                                    dialogBuilder = new AlertDialog.Builder(MainActivity.this);
                                    view= getLayoutInflater().inflate(R.layout.popup,
                                            null);


                                    Button dismissButtonTop = (Button) view.findViewById(R.id.dismissPopTop);
                                    TextView popList = (TextView) view.findViewById(R.id.popList);
                                    TextView popListTitle = (TextView) view.findViewById(R.id.popListTitle);

                                    TextView newCases = (TextView) view.findViewById(R.id.newconfN);
                                    TextView newDeaths = (TextView) view.findViewById(R.id.newDeathsN);
                                    TextView deaths = (TextView) view.findViewById(R.id.deathsN);
                                    TextView newRecovered = (TextView) view.findViewById(R.id.newRecoveredN);
                                    TextView recovered = (TextView) view.findViewById(R.id.recoveredN);
                                    TextView date = (TextView) view.findViewById(R.id.date);




                                        popListTitle.setText(country);
                                        popList.setText(""+totConf);

                                        newCases.setText(""+newConf);
                                        newDeaths.setText(""+newDed);
                                        deaths.setText(""+ded);
                                        newRecovered.setText(""+newRec);
                                        recovered.setText(""+rec);
                                        date.setText("Date: "+dat);

                                        if (newConf !=0) {

                                            if (totConf > 1000 || totConf / newConf < 22) {

                                                popList.setTextColor(Color.RED);
                                                newCases.setTextColor(Color.RED);
                                                popListTitle.setTextColor(Color.RED);

                                            } else {
                                                popList.setTextColor(Color.GREEN);
                                                newCases.setTextColor(Color.GREEN);
                                                popListTitle.setTextColor(Color.GREEN);
                                            }
                                        }else{
                                            popList.setTextColor(Color.GREEN);
                                            newCases.setTextColor(Color.GREEN);
                                            popListTitle.setTextColor(Color.GREEN);

                                        }

                                        if (ded!=0) {

                                            if (newDed != 0) {

                                                if (ded > 0 || ded / newDed < 22) {
                                                    deaths.setTextColor(Color.RED);
                                                    newDeaths.setTextColor(Color.RED);
                                                } else {
                                                    deaths.setTextColor(Color.GREEN);
                                                    newDeaths.setTextColor(Color.GREEN);
                                                }
                                            } else {
                                                deaths.setTextColor(Color.RED);
                                                newDeaths.setTextColor(Color.GREEN);

                                            }
                                        }else{
                                            deaths.setTextColor(Color.GREEN);

                                        }

                                            if (newRec != 0) {
                                                newRecovered.setTextColor(Color.GREEN);
                                                recovered.setTextColor(Color.GREEN);
                                            } else {
                                                recovered.setTextColor(Color.GREEN);

                                            }









                                        dialogBuilder.setView(view);
                                        final AlertDialog dialog = dialogBuilder.create();
                                        dialog.show();

                                        dismissButtonTop.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                dialog.dismiss();
                                            }
                                        });




                                }
                            });
                            arrayAdapter.notifyDataSetChanged();


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        queue.add(jsonObjectRequest);


    }



    @Override
    public void onClick(View view) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}

