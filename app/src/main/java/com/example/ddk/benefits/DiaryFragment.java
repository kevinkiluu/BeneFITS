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
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
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
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class DiaryFragment extends Fragment {
    private LinearLayout searchLayout;
    private DrawerLayout drawerLayout;
    private ArrayAdapter<String> mAdapter;
    private List<String> itemList;
    private List<Integer> ndbnoList;

    protected ProgressBar progressBar;
    protected ListView responseView;
    protected TextView textView;
    protected AutoCompleteTextView searchBar;
    protected Button goButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_diary, null);//container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        progressBar = (ProgressBar) getView().findViewById(R.id.progressBar);
        responseView = (ListView) getView().findViewById(R.id.responseView);
        textView = (TextView) getView().findViewById(R.id.textView);
        searchBar = (AutoCompleteTextView) getView().findViewById(R.id.search);
        goButton = (Button) getView().findViewById(R.id.search_button);

        itemList = new ArrayList<String>();
        ndbnoList = new ArrayList<Integer>();
        mAdapter = new ArrayAdapter<String>(getActivity(), R.layout.item_results, itemList);
        responseView.setAdapter(mAdapter);

        view.findViewById(R.id.search_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RetrieveFeedTask query = new RetrieveFeedTask();
                query.execute();
            }
        });

        responseView.setClickable(true);
        responseView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                String text = null;
                int foodId;
                if (arg1 instanceof TextView) {
                    TextView t = (TextView)arg1;
                    text = t.getText().toString();
                }

                if (text != null) {
                    foodId = ndbnoList.get((int)arg3);
                    //Fragment f = new FoodFragment();
                    //fragment setup...
                }

                Toast toast = Toast.makeText(getActivity().getApplicationContext(), text, Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }

    class RetrieveFeedTask extends AsyncTask<Void, Void, String> {
        private Exception exception;

        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);

        }

        protected String doInBackground(Void... urls) {
            String query = searchBar.getText().toString();
            // Do some validation here
            //https://api.nal.usda.gov/ndb/search/?format=json&q=butter&sort=n&max=25&offset=0&api_key=DEMO_KEY
            try {
                URL url = new URL("https://api.nal.usda.gov/ndb/search/?format=json&q=" + query + "&api_key=" + "O0np9WbTWxzKDzJPhWBAiU6NMh3NWH4J8jDv3qB3");
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

        protected void onPostExecute(String response) {
            if(response == null) {
                response = "THERE WAS AN ERROR";
            }

            progressBar.setVisibility(View.INVISIBLE);
            Log.i("INFO", response);
            //textView.setText(response);

            searchLayout = (LinearLayout) getView().findViewById(R.id.search_results);
            //searchLayout.removeView(getView().findViewById(R.id.responseView));
            //responseView = new ListView(getActivity());
            //responseView.setAdapter(mAdapter);

            try {
                ArrayList<JSONObject> convertedItems = new ArrayList<JSONObject>();
                JSONObject object = new JSONObject(response);
                JSONObject list = object.getJSONObject("list");
                JSONArray jsonItems = list.getJSONArray("item");
                itemList.clear();
                ndbnoList.clear();
                mAdapter.clear();

                for (int i = 0; i < jsonItems.length(); i++) {
                    JSONObject nextItem = jsonItems.getJSONObject(i);
                    convertedItems.add(nextItem);
                }

                for (int i = 0; i < convertedItems.size(); i++) {
                    JSONObject nextItem = convertedItems.get(i);
                    String itemName = nextItem.getString("name");
                    Integer ndbNum = nextItem.getInt("ndbno");
                    itemList.add(itemName);
                    ndbnoList.add(ndbNum);
                }

                mAdapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
