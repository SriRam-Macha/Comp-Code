package com.mrwhitehat.compcode;

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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    LayoutInflater inflater;
    List<Contests_api> contests;
    Context context;

    public MyAdapter(Context context, List<Contests_api> contests) {
        this.inflater = LayoutInflater.from(context);
        this.contests = contests;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.contest_cards, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        holder.v_name.setText(contests.get(position).getName());
        holder.v_platform.setText(contests.get(position).getPlatform());
        holder.v_start.setText(contests.get(position).getStart_time());
        holder.v_startDate.setText(contests.get(position).getStart_date());
        holder.v_end.setText(contests.get(position).getEnd_time());
        holder.v_endDate.setText(contests.get(position).getEnd_date());

        if (holder.v_platform.getText().equals("AtCoder")) {
            holder.card_view.setCardBackgroundColor(Color.parseColor("#E2F0CB"));
            holder.image.setImageResource(R.drawable.atcoder);
        } else if (holder.v_platform.getText().equals("CodeChef")) {
            holder.card_view.setCardBackgroundColor(Color.parseColor("#B5EAD7"));
            holder.image.setImageResource(R.drawable.codechef);
        } else if (holder.v_platform.getText().equals("Codeforces")) {
            holder.card_view.setCardBackgroundColor(Color.parseColor("#FFB7B2"));
            holder.image.setImageResource(R.drawable.codeforces);
        } else if (holder.v_platform.getText().equals("COJ")) {
            holder.card_view.setCardBackgroundColor(Color.parseColor("#DDA0DC"));
            holder.image.setImageResource(R.drawable.coj);
        } else if (holder.v_platform.getText().equals("CS Academy")) {
            holder.card_view.setCardBackgroundColor(Color.parseColor("#EF5350"));
            holder.image.setImageResource(R.drawable.csacademy);
        } else if (holder.v_platform.getText().equals("HackerEarth")) {
            holder.image.setImageResource(R.drawable.hackerearth);
            holder.card_view.setCardBackgroundColor(Color.parseColor("#C7CEEA"));
        } else if (holder.v_platform.getText().equals("LeetCode")) {
            holder.card_view.setCardBackgroundColor(Color.parseColor("#CFCFC4"));
            holder.image.setImageResource(R.drawable.leetcode);
        } else if (holder.v_platform.getText().equals("Topcoder")) {
            holder.card_view.setCardBackgroundColor(Color.parseColor("#AB47BC"));
            holder.image.setImageResource(R.drawable.topcoder);
        }

        holder.open_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(String.valueOf(contests.get(position).getUrl())));
                context.startActivity(intent);
            }
        });

        holder.remind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String s_time = contests.get(position).getStart_time().substring(0, contests.get(position).getStart_time().length() - 4);
                String[] s_date = contests.get(position).getStart_date().split("-");

                if (Integer.parseInt(s_date[1]) < 10) {
                    s_date[1] = "0" + s_date[1];
                }

                String e_time = contests.get(position).getEnd_time().substring(0, contests.get(position).getEnd_time().length() - 4);
                String[] e_date = contests.get(position).getEnd_date().split("-");

                if (Integer.parseInt(e_date[1]) < 10) {
                    e_date[1] = "0" + e_date[1];
                }

                String start = "20" + s_date[2] + "/" + s_date[1] + "/" + s_date[0].trim() + " " + s_time.trim() + ":00";
                String end = "20" + e_date[2] + "/" + e_date[1] + "/" + e_date[0].trim() + " " + e_time.trim() + ":00";

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
                        .putExtra(CalendarContract.Events.TITLE, contests.get(position).getName())
                        .putExtra(CalendarContract.Events.DESCRIPTION, "A competitive coding contest held by " + contests.get(position).getPlatform() + ". \n\nThis reminder is generated via Comp Code. \nComp Code - Never miss a contest!")
                        .putExtra(CalendarContract.Events.EVENT_LOCATION, contests.get(position).getUrl())
                        .putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY);
                context.startActivity(intent);

            }
        });

        holder.share_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = "Hello, Let's compete together in this " + contests.get(position).getName() + " contest." + "\n" + "Link: " + contests.get(position).getUrl() + "\n" + "I'm using this Comp Code app and didn't miss even a single contest. You can also download this app.";
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT, text);
                intent.setType("text/plain");
                context.startActivity(Intent.createChooser(intent, "Share to: "));
            }
        });

    }

    @Override
    public int getItemCount() {
        return contests.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView v_name, v_url, v_platform, v_start, v_end, v_startDate, v_endDate;
        com.google.android.material.imageview.ShapeableImageView image;
        com.google.android.material.button.MaterialButton open_link, remind, share_link;
        CardView card_view;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            v_name = itemView.findViewById(R.id.v_name);
            v_platform = itemView.findViewById(R.id.v_platform);
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