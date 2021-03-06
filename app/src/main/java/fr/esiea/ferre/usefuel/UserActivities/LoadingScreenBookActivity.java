package fr.esiea.ferre.usefuel.UserActivities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import fr.esiea.ferre.usefuel.DeliveryActivities.BookedActivity;
import fr.esiea.ferre.usefuel.R;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by rob on 7/14/2017.
 */


public class LoadingScreenBookActivity extends AppCompatActivity {

    Button button_cancel;
    TextView text;
    ImageView logo_circle;
    AnimationSet animSet;

    private DatabaseReference mDatabase;
    private FirebaseAuth firebaseAuth;
    String status;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_book_screen);

        button_cancel = (Button)findViewById(R.id.button_cancel);
        text = (TextView)findViewById(R.id.text);

        // Set up of the Calligraphy dependencies, that allows us to use a custom font in a .xml layout file
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Roboto-RobotoRegular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

        mDatabase = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();

        FirebaseUser FireUser = firebaseAuth.getCurrentUser();
        final String uid = FireUser.getUid().toString();

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        mDatabase.child("orders").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                status = dataSnapshot.child("status").getValue(String.class);

                final String d_uid;
                d_uid = dataSnapshot.child("deliverer").getValue(String.class);

                Log.d("firebase","data changed");
                if (status == null)
                    return;
                if (status.equals("booked"))
                {
                    text.setText("");
                    final AlertDialog myDialog;
                    builder.setTitle("Yay ! we found a deliverer for you !");
                    builder.setIcon(R.drawable.ic_menu_fuel);
                    builder.setCancelable(false);
                    //Button to decide what to do next
                    builder.setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(getApplicationContext(),UserBookedActivity.class);
                            intent.putExtra("value1",d_uid);
                            startActivity(intent);
                            finish();
                        }
                    });
                    myDialog = builder.create();
                    myDialog.show();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Failed to read value
            }
        });

        displayAnim();


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                button_cancel.setVisibility(View.VISIBLE);
                text.setText(getString(R.string.really_no_deliverer));
                mDatabase.child("orders").child(uid).child("status").setValue("choosing");
            }
        },(5)*20000 + (6)*4000);


    }

    // Block the return button
    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }


    public void onCancel(View view){
        mDatabase = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();

        FirebaseUser FireUser = firebaseAuth.getCurrentUser();
        final String uid = FireUser.getUid().toString();
        mDatabase.child("orders").child(uid).child("status").setValue("choosing");
        finish();
    }

    // Calligraphy dependencies required that
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    private void displayAnim(){
        // Animation to wait for deliverer
        logo_circle = (ImageView)findViewById(R.id.logo);
        button_cancel = (Button)findViewById(R.id.button_cancel);
        text = (TextView)findViewById(R.id.text);

        animSet = new AnimationSet(true);

        animSet.setInterpolator(new AccelerateDecelerateInterpolator());
        animSet.setFillAfter(true);
        animSet.setFillEnabled(true);

        final RotateAnimation animRotate = new RotateAnimation(0.0f, 360.0f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);

        animRotate.setDuration(2000);
        animRotate.setFillAfter(true);
        animRotate.setRepeatCount(61);
        animSet.addAnimation(animRotate);
        logo_circle.startAnimation(animSet);

        for (int i=0; i<=4; i++){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    button_cancel.setVisibility(View.VISIBLE);
                    text.setText(getString(R.string.no_deliverer));
                }
            }, (i+1)*20000 + i*4000);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    button_cancel.setVisibility(View.GONE);
                    text.setText(getString(R.string.wait_deliverer));
                }
            }, (i+1)*20000 + (i+1)*4000);
        }
    }


}
