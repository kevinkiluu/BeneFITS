package com.example.ddk.benefits;

import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.content.Intent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FoodFragment extends Fragment {
    private RelativeLayout layout;
    private DrawerLayout drawerLayout;

    private Bundle info;
    private String foodId;
    private int ndbNumber;
    private ArrayList<Double> fatList;
    private ArrayList<Double> carbList;
    private ArrayList<Double> proteinList;
    private ArrayList<Integer> calorieList;

    protected TextView foodName, calories, calorie_count, fats, fat_count, carbohydrates, carb_count, proteins, protein_count;
    protected Button addFood;
    protected ListView foodMeasure;

    private ArrayAdapter<String> listAdapter;
    private List<String> listDataHeader;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        info = getArguments();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_food, null);//container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        foodId = info.getString("food");
        ndbNumber = info.getInt("ndbno");
        foodName = (TextView) getView().findViewById(R.id.foodName);
        foodName.setText(foodId);
        calorie_count = (TextView) getView().findViewById(R.id.calorie_count);
        fat_count = (TextView) getView().findViewById(R.id.fat_count);
        carb_count = (TextView) getView().findViewById(R.id.carb_count);
        protein_count = (TextView) getView().findViewById(R.id.protein_count);
        addFood = (Button) getView().findViewById(R.id.addFood);
        foodMeasure = (ListView) getView().findViewById(R.id.foodMeasure);

        fatList = new ArrayList<Double>();
        carbList = new ArrayList<Double>();
        proteinList = new ArrayList<Double>();
        calorieList = new ArrayList<Integer>();

        listDataHeader = new ArrayList<String>();
        listAdapter = new ArrayAdapter<String>(getActivity(), R.layout.item_results, listDataHeader);
        foodMeasure.setAdapter(listAdapter);

        RetrieveFoodInfo query = new RetrieveFoodInfo();
        query.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        foodMeasure.setClickable(true);
        foodMeasure.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                String text = null;

                if (arg1 instanceof TextView) {
                    TextView t = (TextView)arg1;
                    text = t.getText().toString();
                }

                if (text != null) {
                    Toast toast = Toast.makeText(getActivity().getApplicationContext(), text, Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });

        view.findViewById(R.id.addFood).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: subtract from user's daily macros
                Bundle args = new Bundle();
                getActivity().getSupportFragmentManager().popBackStack();
                //transaction.commit();
                Toast toast = Toast.makeText(getActivity().getApplicationContext(), "Added food", Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }

    class RetrieveFoodInfo extends AsyncTask<Void, Void, String> {
        private Exception exception;

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(Void... urls) {
            // Do some validation here
            //https://api.nal.usda.gov/ndb/nutrients/?format=json&api_key=O0np9WbTWxzKDzJPhWBAiU6NMh3NWH4J8jDv3qB3&nutrients=205&nutrients=204&nutrients=208&nutrients=203&ndbno=01009
            try {
                URL url = new URL("https://api.nal.usda.gov/ndb/nutrients/?format=json&api_key=O0np9WbTWxzKDzJPhWBAiU6NMh3NWH4J8jDv3qB3&nutrients=205&nutrients=204&nutrients=208&nutrients=203&ndbno=" + ndbNumber);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();
                    return stringBuilder.toString();
                }
                finally{
                    urlConnection.disconnect();
                }
            }
            catch(Exception e) {
                Log.e("ERROR", e.getMessage(), e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(String response) {
            if(response == null) {
                response = "THERE WAS AN ERROR";
            }
            Log.w("INFO", response);
            try {
                ArrayList<JSONObject> convertedItems = new ArrayList<JSONObject>();
                JSONObject object = new JSONObject(response);
                JSONObject report = object.getJSONObject("report");
                JSONArray foods = report.getJSONArray("foods");
                JSONArray nutrients = foods.getJSONObject(0).getJSONArray("nutrients"); // "nutrients"
                listDataHeader.clear();
                listAdapter.clear();

                String measures = foods.getJSONObject(0).getString("measure");
                listDataHeader.add(measures);
                listAdapter.notifyDataSetChanged();

                for (int i = 0; i < nutrients.length(); i++) {
                    JSONObject nextElmt = nutrients.getJSONObject(i);
                    if (nextElmt.getInt("nutrient_id") == 208) { //kcal
                        calorieList.add(nextElmt.getInt("value"));
                    } else if (nextElmt.getInt("nutrient_id") == 204) { //fat
                        fatList.add(nextElmt.getDouble("value"));
                    } else if (nextElmt.getInt("nutrient_id") == 205) { //carbs
                        carbList.add(nextElmt.getDouble("value"));
                    } else if (nextElmt.getInt("nutrient_id") == 203) { //protein
                        proteinList.add(nextElmt.getDouble("value"));
                    }
                }

                calorie_count.setText(Integer.toString(calorieList.get(0)));
                fat_count.setText(Double.toString(fatList.get(0)) + "g");
                carb_count.setText(Double.toString(carbList.get(0)) + "g");
                protein_count.setText(Double.toString(proteinList.get(0)) + "g");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
