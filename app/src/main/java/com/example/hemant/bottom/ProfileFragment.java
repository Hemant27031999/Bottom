package com.example.hemant.bottom;


import android.app.Notification;
import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

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
    private TextView profilename;
    private ImageView profilepic;
    private List<PostRetrieval> mPost;
    private FloatingActionButton mfloatingbutton;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private EditText searchbar;
    private FirebaseUser mUser;
    private NotificationManagerCompat notificationManager;
    private DatabaseReference basicinfo;
    private StorageReference retrievingimage;

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
        mUser=FirebaseAuth.getInstance().getCurrentUser();
        retrievingimage=FirebaseStorage.getInstance().getReference("uploads");
        profilename=(TextView) view.findViewById(R.id.currentusername);
        profilepic=(ImageView) view.findViewById(R.id.profilepic);
        basicinfo=FirebaseDatabase.getInstance().getReference();
        notificationManager=NotificationManagerCompat.from(getContext());
        firebaseAuth=FirebaseAuth.getInstance();
        progressDialog=new ProgressDialog(getContext());
        progressDialog.setMessage("Loading posts, wait for a sec......");
        progressDialog.show();
        searchbar=(EditText) view.findViewById(R.id.searchbox);
        searchbar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });

        profilename.setText(mUser.getDisplayName());
        retrievingimage.child(mUser.getDisplayName()+".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(getContext()).load(uri).into(profilepic);
            }
        });

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

        final Notification notification=new NotificationCompat.Builder(getContext(),CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_post)
                .setContentTitle("Posts")
                .setContentText("New post has been made.")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build();


        mRecyclerView=view.findViewById(R.id.recycler_view_posts);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mPost=new ArrayList<>();

        mDatareference=FirebaseDatabase.getInstance().getReference("Posts");

        mDatareference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                notificationManager.notify(1,notification);
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

        mDatareference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mPost.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    PostRetrieval postRetrieval=postSnapshot.getValue(PostRetrieval.class);
                    mPost.add(postRetrieval);
                }

                Collections.reverse(mPost);
                mpostAdapter=new PostAdapter(getContext(),mPost);
                mRecyclerView.setAdapter(mpostAdapter);
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(),databaseError.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private void filter(String text){
        ArrayList<PostRetrieval> filteredposts=new ArrayList<>();

        for(PostRetrieval searchedpost : mPost){
            if(searchedpost.getPosts().toLowerCase().contains(text.toLowerCase())){
                filteredposts.add(searchedpost);
            }
        }
        mpostAdapter.filteredlist(filteredposts);
    }

}
