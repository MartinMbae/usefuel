package fr.esiea.ferre.usefuel.DeliveryActivities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import fr.esiea.ferre.usefuel.DeliveryActivities.fragmentDeliveryClasses.FragmentDeliveryProfile;
import fr.esiea.ferre.usefuel.R;

public class MainDeliveryActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_delivery);


        final Fragment profileDeliverFragment = new FragmentDeliveryProfile();
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationView);

        if (savedInstanceState == null) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragmentContainer, profileDeliverFragment).commit();
        }

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                FragmentManager fragmentManager = getSupportFragmentManager();

                if (item.getItemId() == R.id.profileItem) {
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragmentContainer, profileDeliverFragment).commit();
                }
                return true;
            }
        });
    }
}

