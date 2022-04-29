package com.example.petamigo.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.petamigo.Search.User;
import com.example.petamigo.Search.UserAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.example.petamigo.R;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment implements View.OnClickListener {

    private RecyclerView recyclerView;
    private List<User> mUsers;
    private UserAdapter userAdapter;
    private ImageView searchBut;
    private EditText search_bar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_find, container, false);

        recyclerView = view.findViewById(R.id.recycler_view_users);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        mUsers = new ArrayList<>();
        userAdapter = new UserAdapter(getContext() , mUsers , true);
        recyclerView.setAdapter(userAdapter);

        search_bar = view.findViewById(R.id.search_bar);
        searchBut = view.findViewById(R.id.search_button);
        searchBut.setOnClickListener(this);

        readUsers();
        return view;
    }



    private void readUsers() {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("myUsers");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (TextUtils.isEmpty(search_bar.getText().toString())){
                    mUsers.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                        User user = snapshot.getValue(User.class);
                        mUsers.add(user);
                    }

                    userAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void searchUser (String s) {
        Query query = FirebaseDatabase.getInstance().getReference().child("myUsers")
                .orderByChild("username").startAt(s).endAt(s + "\uf8ff");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                mUsers.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);
                    mUsers.add(user);
                }
                userAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.search_button) {
            searchUser(search_bar.getText().toString());
        }
    }
}
