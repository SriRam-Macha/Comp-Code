package com.mrwhitehat.compcode;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.TimeZone;

public class FirebaseAdapter extends FirebaseRecyclerAdapter<Api_custom, FirebaseAdapter.FirebaseViewHolder> {

    Context context;
    List<String> colors = Arrays.asList("#FFD6A4", "#FFEFAA", "#FFB19D", "#FDC7CF", "#FEDFE5", "#DFD7CC", "#F2EEE2", "#ABDFED", "#D2EFF5", "#EDEFBE", "#C7F0E0", "#DEDFE1");


    public FirebaseAdapter(Context context, @NonNull FirebaseRecyclerOptions<Api_custom> options) {
        super(options);

        this.context = context;

    }

    @Override
    protected void onBindViewHolder(@NonNull final FirebaseViewHolder firebaseViewHolder, final int i, @NonNull final Api_custom Api_custom) {

        firebaseViewHolder.v_name.setText(Api_custom.getName());
        firebaseViewHolder.v_platform.setText(Api_custom.getPlatform());
        firebaseViewHolder.v_start.setText(Api_custom.getStart_time() + " hrs");
        firebaseViewHolder.v_startDate.setText(Api_custom.getStart_date());
        firebaseViewHolder.v_end.setText(Api_custom.getEnd_time() + " hrs");
        firebaseViewHolder.v_endDate.setText(Api_custom.getEnd_date());

        firebaseViewHolder.card_view.setCardBackgroundColor(Color.parseColor(colors.get(new Random().nextInt(colors.size()))));

        firebaseViewHolder.open_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(String.valueOf(Api_custom.getUrl())));
                context.startActivity(intent);
            }
        });

        firebaseViewHolder.remind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] s_date = Api_custom.getStart_date().split("-");
                String[] e_date = Api_custom.getEnd_date().split("-");

                String start = "20" + s_date[2] + "/" + s_date[1] + "/" + s_date[0].trim() + " " + Api_custom.getStart_time().trim() + ":00";
                String end = "20" + e_date[2] + "/" + e_date[1] + "/" + e_date[0].trim() + " " + Api_custom.getEnd_time().trim() + ":00";

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                Date start_date = null;
                Date end_date = null;
                try {
                    start_date = sdf.parse(start);

                    end_date = sdf.parse(end);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                Intent intent = new Intent(Intent.ACTION_INSERT)
                        .setData(CalendarContract.Events.CONTENT_URI)
                        .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, start_date.getTime())
                        .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, end_date.getTime())
                        .putExtra(CalendarContract.Events.TITLE, Api_custom.getName())
                        .putExtra(CalendarContract.Events.DESCRIPTION, "A competitive coding contest held by " + Api_custom.getPlatform() + ". \n\nThis reminder is generated via Comp Code. \nComp Code - Never miss a contest!")
                        .putExtra(CalendarContract.Events.EVENT_LOCATION, Api_custom.getUrl())
                        .putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY);
                context.startActivity(intent);

            }
        });

        firebaseViewHolder.share_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = "Hello, Let's compete together in this " + Api_custom.getName() + " contest." + "\n" + "Link: " + Api_custom.getUrl() + "\n" + "I'm using this Comp Code app and didn't miss even a single contest. You can also download this app.";
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT, text);
                intent.setType("text/plain");
                context.startActivity(Intent.createChooser(intent, "Share to: "));
            }
        });

        firebaseViewHolder.remove_data.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+5:30"));
                int currentHour = cal.get(Calendar.HOUR_OF_DAY);
                int currentMinutes = cal.get(Calendar.MINUTE);

                int currentYear = cal.get(Calendar.YEAR) - 2000;
                int currentMonth = cal.get(Calendar.MONTH) + 1;
                int currentDay = cal.get(Calendar.DAY_OF_MONTH);

                String checkTime = Api_custom.getEnd_time();
                String[] checkTimeArray = checkTime.split(":");

                String checkDate = Api_custom.getEnd_date();
                String[] checkDateArray = checkDate.split("-");

                int Hour = Integer.parseInt(checkTimeArray[0]);
                int Minutes = Integer.parseInt(checkTimeArray[1]);

                int Year = Integer.parseInt(checkDateArray[2]);
                int Month = Integer.parseInt(checkDateArray[1]);
                int Day = Integer.parseInt(checkDateArray[0]);

                if (currentYear > Year) {

                    FirebaseDatabase.getInstance().getReference().child("Contests").child(Api_custom.getUid() + "   " + Api_custom.getName() + Api_custom.getPlatform() + Api_custom.getStart_date() + Api_custom.getStart_time()).removeValue();

                } else if (currentYear >= Year & currentMonth > Month) {

                    FirebaseDatabase.getInstance().getReference().child("Contests").child(Api_custom.getUid() + "   " + Api_custom.getName() + Api_custom.getPlatform() + Api_custom.getStart_date() + Api_custom.getStart_time()).removeValue();

                } else if (currentYear >= Year & currentMonth >= Month & currentDay > Day) {

                    FirebaseDatabase.getInstance().getReference().child("Contests").child(Api_custom.getUid() + "   " + Api_custom.getName() + Api_custom.getPlatform() + Api_custom.getStart_date() + Api_custom.getStart_time()).removeValue();

                } else if (currentYear >= Year & currentMonth >= Month & currentDay >= Day & currentHour > Hour) {

                    FirebaseDatabase.getInstance().getReference().child("Contests").child(Api_custom.getUid() + "   " + Api_custom.getName() + Api_custom.getPlatform() + Api_custom.getStart_date() + Api_custom.getStart_time()).removeValue();

                } else if (currentYear >= Year & currentMonth >= Month & currentDay >= Day & currentHour >= Hour & currentMinutes > Minutes) {

                    FirebaseDatabase.getInstance().getReference().child("Contests").child(Api_custom.getUid() + "   " + Api_custom.getName() + Api_custom.getPlatform() + Api_custom.getStart_date() + Api_custom.getStart_time()).removeValue();

                }

                return false;
            }
        });

        firebaseViewHolder.remove_data.performLongClick();

    }

    @NonNull
    @Override
    public FirebaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_contest_cards, parent, false);

        return new FirebaseViewHolder(view);
    }

    class FirebaseViewHolder extends RecyclerView.ViewHolder {

        TextView v_name, v_url, v_platform, v_start, v_end, v_startDate, v_endDate, remove_data;
        com.google.android.material.imageview.ShapeableImageView image;
        com.google.android.material.button.MaterialButton open_link, remind, share_link;
        CardView card_view;

        public FirebaseViewHolder(@NonNull View itemView) {
            super(itemView);

            v_name = itemView.findViewById(R.id.v_name);
            v_platform = itemView.findViewById(R.id.v_platform);
            remove_data = itemView.findViewById(R.id.remove_data);
            v_start = itemView.findViewById(R.id.v_start);
            v_startDate = itemView.findViewById(R.id.v_startDate);
            v_end = itemView.findViewById(R.id.v_end);
            v_endDate = itemView.findViewById(R.id.v_endDate);
            image = itemView.findViewById(R.id.image);
            open_link = itemView.findViewById(R.id.open_link);
            remind = itemView.findViewById(R.id.remind);
            share_link = itemView.findViewById(R.id.share_link);
            card_view = itemView.findViewById(R.id.card_view);

        }
    }
}
