package com.mrwhitehat.compcode;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class Upcoming extends Fragment {

    RecyclerView recycler_upcoming;
    List<Contests_api> upcoming_contests;
    MyAdapter adapter;
    CardView not_found;

    public Upcoming(List<Contests_api> upcoming_contests) {
        this.upcoming_contests = upcoming_contests;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_upcoming, container, false);

        recycler_upcoming = view.findViewById(R.id.recycler_upcoming);

        not_found = view.findViewById(R.id.not_found);

        recycler_upcoming.setLayoutManager(new LinearLayoutManager(view.getContext()));
        adapter = new MyAdapter(view.getContext(), upcoming_contests);
        recycler_upcoming.setAdapter(adapter);

        if (!(upcoming_contests.size() == 0)) {
            ((ViewGroup) not_found.getParent()).removeView(not_found);
        }

        return view;
    }
}