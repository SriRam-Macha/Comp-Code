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

public class Long extends Fragment {

    List<Contests_api> long_contests;
    RecyclerView recycler_long;
    MyAdapter adapter;
    CardView not_found;

    public Long(List<Contests_api> long_contests) {
        this.long_contests = long_contests;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_long, container, false);

        recycler_long = view.findViewById(R.id.recycler_long);

        not_found = view.findViewById(R.id.not_found);

        recycler_long.setLayoutManager(new LinearLayoutManager(view.getContext()));
        adapter = new MyAdapter(view.getContext(), long_contests);
        recycler_long.setAdapter(adapter);

        if (!(long_contests.size() == 0)) {
            ((ViewGroup) not_found.getParent()).removeView(not_found);
        }

        return view;
    }
}