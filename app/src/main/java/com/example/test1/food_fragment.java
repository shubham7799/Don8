package com.example.test1;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test1.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

public class food_fragment extends Fragment {
    Button btn;
    RecyclerView recyclerView;
    DatabaseReference dr;
    ProgressBar p;
    ArrayList<uploadinfo> uploads=new ArrayList<uploadinfo>();
    int state=0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.fragment_food,container,false);
       btn=(Button)view.findViewById(R.id.btn);
       p=(ProgressBar)view.findViewById(R.id.foodprogress);
        recyclerView = (RecyclerView) view.findViewById(R.id.food_recyclerview);
        dr= FirebaseDatabase.getInstance().getReference("Food Donations");
        dr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                uploads=new ArrayList<uploadinfo>();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    uploadinfo u = postSnapshot.getValue(uploadinfo.class);
                    uploads.add(u);
                }
                load(uploads);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        dr.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                uploadinfo u = dataSnapshot.getValue(uploadinfo.class);
                load(uploads);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

       btn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent i = new Intent(getActivity(),upload_data.class);
               startActivity(i);
           }
       });

       return view;
    }

    void load(ArrayList<uploadinfo> uploads){
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        p.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        RAdapter adapter = new RAdapter(getActivity(),uploads);
        recyclerView.setAdapter(adapter);
    }
}
