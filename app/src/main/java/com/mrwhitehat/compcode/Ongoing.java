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

public class Ongoing extends Fragment {

    List<Contests_api> ongoing_contests;
    RecyclerView recycler_ongoing;
    MyAdapter adapter;
    CardView not_found;

    public Ongoing(List<Contests_api> ongoing_contests) {
        this.ongoing_contests = ongoing_contests;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ongoing, container, false);

        recycler_ongoing = view.findViewById(R.id.recycler_ongoing);

        not_found = view.findViewById(R.id.not_found);

        recycler_ongoing.setLayoutManager(new LinearLayoutManager(view.getContext()));
        adapter = new MyAdapter(view.getContext(), ongoing_contests);
        recycler_ongoing.setAdapter(adapter);

        if (!(ongoing_contests.size() == 0)) {
            ((ViewGroup) not_found.getParent()).removeView(not_found);
        }

        return view;
    }
}