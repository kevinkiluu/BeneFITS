package com.example.ddk.benefits;

import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class HomeFragment extends Fragment {
    private RelativeLayout layout;
    private DrawerLayout drawerLayout;
    private SimpleDateFormat dateFormat;

    private TextView todayView;
    private Button logButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, null);//container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        todayView = getView().findViewById(R.id.todayView);
        logButton = getView().findViewById(R.id.logButton);

        dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        Date todayDate = new Date();
        String dateView = dateFormat.format(todayDate);
        todayView.setText(dateView);

        view.findViewById(R.id.logButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment f = new DiaryFragment();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame, f);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
    }
}
