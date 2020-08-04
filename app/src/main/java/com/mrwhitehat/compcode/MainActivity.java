package com.mrwhitehat.compcode;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    BottomNavigationView bottomNavigationView;

    ProgressDialog pd;

    ExtendedFloatingActionButton floating;

    List<Contests_api> upcoming_contests = new ArrayList<>();
    List<Contests_api> ongoing_contests = new ArrayList<>();
    List<Contests_api> long_contests = new ArrayList<>();
    List<Contests_api> long_ongoing = new ArrayList<>();

    int check_runner = 0;
    private RequestQueue result_queue;
    private static String JSON_URL = "https://coding-notify.herokuapp.com/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pd = new ProgressDialog(this);
        pd.setMessage("Fetching live data...");
        pd.setCancelable(false);
        pd.show();

        floating = (ExtendedFloatingActionButton) findViewById(R.id.floating);

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.navigation_icon2);

        result_queue = Volley.newRequestQueue(this);

        extractContests();

        floating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddContest.class);
                startActivity(intent);
            }
        });


    }

    @Override
    public boolean onCreatePanelMenu(int featureId, @NonNull Menu menu) {

        MenuInflater menus = getMenuInflater();
        menus.inflate(R.menu.toolbar, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {

            case R.id.developers:
                startActivity(new Intent(this, Developers.class));
                return true;

            case R.id.shareApp:
                String text = "I'm using this Comp Code app and didn't miss even a single contest. You can also download this app.";
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT, text);
                intent.setType("text/plain");
                startActivity(Intent.createChooser(intent, "Share to: "));
                return true;

            case R.id.logoutApp:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void extractContests() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, JSON_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            DateFormat simple = new SimpleDateFormat(" HH:mm");
                            DateFormat dateFormat = new SimpleDateFormat(" dd-M-yy");
                            JSONObject result = response.getJSONObject("results");

                            JSONArray jsonArray1 = result.getJSONArray("ongoing");

                            for (int i = 0; i < jsonArray1.length(); i++) {

                                JSONObject contestObject = jsonArray1.getJSONObject(i);

                                Contests_api ongoing = new Contests_api();
                                ongoing.setName(contestObject.getString("name").toString());
                                ongoing.setUrl(contestObject.getString("url").toString());
                                ongoing.setPlatform(contestObject.getString("platform").toString());

                                long startTime = contestObject.getLong("startTime");
                                startTime = startTime * 1000;
                                Date Stime = new Date(startTime);
                                long endTime = contestObject.getLong("endTime");
                                endTime = endTime * 1000;
                                Date Etime = new Date(endTime);

                                ongoing.setStart_time((simple.format(Stime)).toString() + " hrs");
                                ongoing.setStart_date((dateFormat.format(Stime)).toString());
                                ongoing.setEnd_time((simple.format(Etime)).toString() + " hrs");
                                ongoing.setEnd_date((dateFormat.format(Etime)).toString());

                                if (endTime - startTime > 432000000) {
                                    long_ongoing.add(ongoing);
                                } else {
                                    ongoing_contests.add(ongoing);
                                }

                            }

                            for (int i = 0; i < long_ongoing.size(); i++) {
                                ongoing_contests.add(long_ongoing.get(i));
                            }

                            JSONArray jsonArray2 = result.getJSONArray("upcoming");

                            for (int i = 0; i < jsonArray2.length(); i++) {

                                JSONObject contestObject = jsonArray2.getJSONObject(i);

                                Contests_api upcoming = new Contests_api();
                                upcoming.setName(contestObject.getString("name").toString());
                                upcoming.setUrl(contestObject.getString("url").toString());
                                upcoming.setPlatform(contestObject.getString("platform").toString());

                                long startTime = contestObject.getLong("startTime");
                                startTime = startTime * 1000;
                                Date Stime = new Date(startTime);
                                long endTime = contestObject.getLong("endTime");
                                endTime = endTime * 1000;
                                Date Etime = new Date(endTime);

                                upcoming.setStart_time((simple.format(Stime)).toString() + " hrs");
                                upcoming.setStart_date((dateFormat.format(Stime)).toString());
                                upcoming.setEnd_time((simple.format(Etime)).toString() + " hrs");
                                upcoming.setEnd_date((dateFormat.format(Etime)).toString());

                                if (endTime - startTime > 432000000) {
                                    long_contests.add(upcoming);
                                } else {
                                    upcoming_contests.add(upcoming);
                                }
                            }
                            pd.dismiss();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("tag", "onErrorResponse: " + error.getMessage());
                if (check_runner < 3) {
                    check_runner++;
                    pd.show();
                    extractContests();
                } else {
                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle("Network Error")
                            .setMessage("Poor connection detected, please make sure that you are connected to the Internet!")
                            .setIcon(R.drawable.error)
                            .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    check_runner = 0;
                                    extractContests();
                                }
                            })
                            .setNegativeButton("Close App", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    finish();
                                    System.exit(0);
                                }
                            })
                            .setNeutralButton("Dismiss", null)
                            .show();

                }
            }
        });
        result_queue.add(jsonObjectRequest);
    }

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Contests");

    FirebaseRecyclerOptions<Api_custom> options = new FirebaseRecyclerOptions.Builder<Api_custom>().setQuery(databaseReference, Api_custom.class).build();

    final Contests contests_frag = new Contests(ongoing_contests, upcoming_contests, long_contests);
    final Profile profile_frag = new Profile();
    final Custom custom_frag = new Custom(options);

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        ActionBar bar = getSupportActionBar();

        switch (item.getItemId()) {

            case R.id.navigation_icon1:
                bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#77DD77")));
                floating.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#77DD77")));
                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right).replace(R.id.container, contests_frag).commit();
                return true;

            case R.id.navigation_icon2:
                bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FF6961")));
                floating.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF6961")));
                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right).replace(R.id.container, profile_frag).commit();
                return true;

            case R.id.navigation_icon3:
                bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#89CFF0")));
                floating.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#89CFF0")));
                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right).replace(R.id.container, custom_frag).commit();
                return true;
        }

        return false;
    }
}