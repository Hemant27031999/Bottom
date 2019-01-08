package com.example.hemant.bottom;


import android.app.Notification;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.example.hemant.bottom.App.CHANNEL_1_ID;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment{

    private RecyclerView mRecyclerView;
    private DatabaseReference mDatareference;
    private PostAdapter mpostAdapter;
    private List<PostRetrieval> mPost;
    private FloatingActionButton mfloatingbutton;
    private FirebaseAuth firebaseAuth;
    private NotificationManagerCompat notificationManager;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void
    onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.my_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        FragmentManager manager=getFragmentManager();
        FragmentTransaction transaction=manager.beginTransaction();
        switch (item.getItemId()) {
            case R.id.item0:
                LoginFragment lf=new LoginFragment();
                transaction.replace(R.id.fragment_container, lf);
                transaction.commit();
                return true;
            case R.id.item1:
                MyProfileFragment mf=new MyProfileFragment();
                transaction.replace(R.id.fragment_container, mf);
                transaction.commit();
                return true;
            case R.id.item2:
                firebaseAuth.signOut();
                LoginFragment kf=new LoginFragment();
                transaction.replace(R.id.fragment_container, kf);
                transaction.commit();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_profile, container, false);

        notificationManager=NotificationManagerCompat.from(getContext());
        firebaseAuth=FirebaseAuth.getInstance();

        mfloatingbutton=(FloatingActionButton) view.findViewById(R.id.fab);
        mfloatingbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PostingFragment pf=new PostingFragment();
                FragmentManager manager=getFragmentManager();
                FragmentTransaction transaction=manager.beginTransaction();
                transaction.replace(R.id.fragment_container, pf);
                transaction.commit();
            }
        });


        mRecyclerView=view.findViewById(R.id.recycler_view_posts);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mPost=new ArrayList<>();

        mDatareference=FirebaseDatabase.getInstance().getReference("Posts");
        final Notification notification=new NotificationCompat.Builder(getContext(),CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_post)
                .setContentTitle("Posts")
                .setContentText("New notification from War Tube")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build();


        mDatareference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mPost.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    PostRetrieval postRetrieval=postSnapshot.getValue(PostRetrieval.class);
                    mPost.add(postRetrieval);
                }

                notificationManager.notify(1,notification);
                Collections.reverse(mPost);
                mpostAdapter=new PostAdapter(getContext(),mPost);
                mRecyclerView.setAdapter(mpostAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(),databaseError.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

}
