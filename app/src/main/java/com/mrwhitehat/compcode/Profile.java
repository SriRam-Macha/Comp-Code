package com.mrwhitehat.compcode;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.provider.Settings;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class Profile extends Fragment {

    ShapeableImageView DP;

    MaterialTextView AtCoder_rank, AtCoder_rating, AtCoder_high_rating;
    MaterialTextView name, mail, star, rating, countryRank, globalRank;
    MaterialTextView codeforces_rating, codeforces_contribution, codeforces_friends;
    MaterialTextView LeetCode_Rating, LeetCode_global_rank, LeetCode_finished_contests;

    TextInputEditText AtCoder_username, CodeChef_username, Codeforces_username, LeetCode_username;
    MaterialButton AtCoder_Go, CodeChef_g, Codeforces_go, LeetCode_Go;
    SharedPreferences preferences;

    String user_name;
    String user_email;
    Uri profile_dp;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        DP = (ShapeableImageView) view.findViewById(R.id.DP);
        name = (MaterialTextView) view.findViewById(R.id.name);
        mail = (MaterialTextView) view.findViewById(R.id.mail);

        preferences = view.getContext().getSharedPreferences("User Stats", Context.MODE_PRIVATE);

        AtCoder_username = (TextInputEditText) view.findViewById(R.id.AtCoder_username);
        AtCoder_Go = (MaterialButton) view.findViewById(R.id.AtCoder_Go);
        AtCoder_rank = (MaterialTextView) view.findViewById(R.id.AtCoder_rank);
        AtCoder_rating = (MaterialTextView) view.findViewById(R.id.AtCoder_rating);
        AtCoder_high_rating = (MaterialTextView) view.findViewById(R.id.AtCoder_high_rating);

        CodeChef_username = (TextInputEditText) view.findViewById(R.id.CodeChef_username);
        CodeChef_g = (MaterialButton) view.findViewById(R.id.CodeChef_g);
        star = (MaterialTextView) view.findViewById(R.id.star);
        rating = (MaterialTextView) view.findViewById(R.id.rating);
        countryRank = (MaterialTextView) view.findViewById(R.id.countryRank);
        globalRank = (MaterialTextView) view.findViewById(R.id.globalRank);

        Codeforces_username = (TextInputEditText) view.findViewById(R.id.Codeforces_username);
        Codeforces_go = (MaterialButton) view.findViewById(R.id.Codeforces_go);
        codeforces_rating = (MaterialTextView) view.findViewById(R.id.codeforces_rating);
        codeforces_contribution = (MaterialTextView) view.findViewById(R.id.codeforces_contribution);
        codeforces_friends = (MaterialTextView) view.findViewById(R.id.codeforces_friends);

        LeetCode_username = (TextInputEditText) view.findViewById(R.id.LeetCode_username);
        LeetCode_Go = (MaterialButton) view.findViewById(R.id.LeetCode_Go);
        LeetCode_Rating = (MaterialTextView) view.findViewById(R.id.LeetCode_Rating);
        LeetCode_global_rank = (MaterialTextView) view.findViewById(R.id.LeetCode_global_rank);
        LeetCode_finished_contests = (MaterialTextView) view.findViewById(R.id.LeetCode_finished_contests);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        user_name = user.getDisplayName();
        user_email = user.getEmail();
        profile_dp = user.getPhotoUrl();
        Picasso
                .with(getContext())
                .load(profile_dp)
                .into(DP);

        name.setText(user_name);
        mail.setText(user_email);


        if (preferences != null) {
            String atcoder_username = preferences.getString("atcoder_username", "");
            String codechef_username = preferences.getString("codechef_username", "");
            String codefrces_username = preferences.getString("codeforces_username", "");
            String leetcode_username = preferences.getString("leetcode_username", "");

            AtCoder_username.setText(atcoder_username);
            CodeChef_username.setText(codechef_username);
            Codeforces_username.setText(codefrces_username);
            LeetCode_username.setText(leetcode_username);

        }

        AtCoder_Go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = String.valueOf(AtCoder_username.getText());
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("atcoder_username", user.trim());
                editor.apply();
                if (!user.isEmpty()) {
                    new getAtCoderData(user).execute();
                }
            }
        });


        CodeChef_g.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = String.valueOf(CodeChef_username.getText());
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("codechef_username", user.trim());
                editor.apply();
                if (!user.isEmpty()) {
                    new getCodeChefData(user).execute();
                }
            }
        });

        Codeforces_go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = String.valueOf(Codeforces_username.getText());
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("codeforces_username", user.trim());
                editor.apply();
                if (!user.isEmpty()) {
                    new getCodeForcesData(user).execute();
                }
            }
        });

        LeetCode_Go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = String.valueOf(LeetCode_username.getText());
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("leetcode_username", user.trim());
                editor.apply();
                if (!user.isEmpty()) {
                    new getLeetCodeData(user).execute();
                }
            }
        });

        if (!AtCoder_username.getText().toString().isEmpty()) {
            AtCoder_Go.performClick();
        }

        if (!CodeChef_username.getText().toString().isEmpty()) {
            CodeChef_g.performClick();
        }

        if (!Codeforces_username.getText().toString().isEmpty()) {
            Codeforces_go.performClick();
        }

        if (!LeetCode_username.getText().toString().isEmpty()) {
            LeetCode_Go.performClick();
        }

        AtCoder_username.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        CodeChef_username.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        Codeforces_username.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        LeetCode_username.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        return view;
    }

    public class getAtCoderData extends AsyncTask<Void, Void, Void> {

        String Rank;
        String Rating;
        String Highest_Rating;
        String user;

        public getAtCoderData(String user) {
            this.user = user;
        }

        @Override
        protected Void doInBackground(Void... voids) {

            try {

                Document doc = Jsoup.connect("https://atcoder.jp/users/" + user).get();
                Rank = doc.select("#main-container > div.row > div.col-md-9.col-sm-12 > table > tbody > tr:nth-child(1) > td").html();
                Rating = doc.select("#main-container > div.row > div.col-md-9.col-sm-12 > table > tbody > tr:nth-child(2) > td > span:nth-child(1)").html();
                Highest_Rating = doc.select("#main-container > div.row > div.col-md-9.col-sm-12 > table > tbody > tr:nth-child(3) > td > span:nth-child(1)").html();


            } catch (IOException e) {

                e.printStackTrace();

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            AtCoder_rank.setText("Rank: " + Rank);
            AtCoder_rating.setText("Rating: " + Rating);
            AtCoder_high_rating.setText("Highest Rating: " + Highest_Rating);
        }

    }

    public class getCodeChefData extends AsyncTask<Void, Void, Void> {

        String user;
        String stars;
        String codeChef_Rank;
        String global_Rank;
        String country_Rank;
        String username;

        public getCodeChefData(String user) {

            this.user = user;

        }

        @Override
        protected Void doInBackground(Void... voids) {

            try {

                Document doc = Jsoup.connect("https://www.codechef.com/users/" + user.trim()).get();
                stars = doc.select("body > main > div > div > div > div > div > section.user-details > ul > li:nth-child(1) > span > span.rating").html();
                codeChef_Rank = doc.select("body > main > div > div > div > aside > div.widget.pl0.pr0.widget-rating > div > div.rating-header.text-center > div.rating-number").html();
                global_Rank = doc.select("body > main > div > div > div > aside > div.widget.pl0.pr0.widget-rating > div > div.rating-ranks > ul > li:nth-child(1) > a > strong").html();
                country_Rank = doc.select("body > main > div > div > div > aside > div.widget.pl0.pr0.widget-rating > div > div.rating-ranks > ul > li:nth-child(2) > a > strong").html();
                username = doc.select("body > main > div > div > div > div > div > section.user-details > ul > li:nth-child(1) > span > span:nth-child(2)").html();

            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            star.setText(stars);
            rating.setText("Rating: " + codeChef_Rank);
            countryRank.setText("Country Rank: " + country_Rank);
            globalRank.setText("Global Rank: " + global_Rank);

        }
    }

    public class getCodeForcesData extends AsyncTask<Void, Void, Void> {

        String user;
        String rating, contribution, friends;

        public getCodeForcesData(String user) {
            this.user = user;
        }

        @Override
        protected Void doInBackground(Void... voids) {

            try {

                Document doc = Jsoup.connect("https://codeforces.com/profile/" + user.trim()).get();
                rating = doc.select("#pageContent > div:nth-child(3) > div.userbox > div.info > ul > li:nth-child(1) > span.user-legendary").html();
                contribution = doc.select("#pageContent > div:nth-child(3) > div.userbox > div.info > ul > li:nth-child(2) > span").html();
                friends = doc.select("#pageContent > div:nth-child(3) > div.userbox > div.info > ul > li:nth-child(3)").html();

            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            codeforces_rating.setText("Rating: " + rating);
            codeforces_contribution.setText("Contribution: " + contribution);
            codeforces_friends.setText("Friend of: " + friends.substring(134));

        }
    }

    public class getLeetCodeData extends AsyncTask<Void, Void, Void> {

        String Global_Rank;
        String Rating;
        String Finished_contests;
        String user;
        String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/42.0.2311.135 Safari/537.36 Edge/12.246";

        public getLeetCodeData(String user) {
            this.user = user;
        }

        @Override
        protected Void doInBackground(Void... voids) {

            try {
                Document doc = Jsoup.connect("https://leetcode.com/" + user + "/").userAgent(userAgent).get();
                Rating = doc.select("#base_content > div > div > div.col-sm-5.col-md-4 > div:nth-child(2) > ul > li:nth-child(2) > span").html();
                Global_Rank = doc.select("#base_content > div > div > div.col-sm-5.col-md-4 > div:nth-child(2) > ul > li:nth-child(3) > span").html();
                Finished_contests = doc.select("#base_content > div > div > div.col-sm-5.col-md-4 > div:nth-child(2) > ul > li:nth-child(1) > span").html();

            } catch (IOException e) {
                e.printStackTrace();

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            LeetCode_Rating.setText("Rating: " + Rating);
            LeetCode_global_rank.setText("Global Rank: " + Global_Rank);
            LeetCode_finished_contests.setText("Finished Contests: " + Finished_contests);

        }

    }


    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}