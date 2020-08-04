package com.mrwhitehat.compcode;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerOptions;

public class Custom extends Fragment {

    RecyclerView recycler_custom;
    CardView not_found;

    ProgressDialog pd;

    FirebaseRecyclerOptions<Api_custom> options;
    private FirebaseAdapter adapter;

    public Custom(FirebaseRecyclerOptions options) {

        this.options = options;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_custom, container, false);

        pd = new ProgressDialog(getActivity());
        pd.setMessage("Fetching live data...");
        pd.setCancelable(false);
        pd.show();

        recycler_custom = view.findViewById(R.id.recycler_custom);

        not_found = view.findViewById(R.id.not_found);

        recycler_custom.setLayoutManager(new LinearLayoutManager(view.getContext()));

        adapter = new FirebaseAdapter(view.getContext(), options);

        recycler_custom.setAdapter(adapter);

        pd.dismiss();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}