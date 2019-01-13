package com.example.hemant.bottom;


import android.app.Notification;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static com.example.hemant.bottom.App.CHANNEL_1_ID;


/**
 * A simple {@link Fragment} subclass.
 */
public class PostingFragment extends Fragment {


    private ImageView mImageUser;
    private TextView mNameUser;
    private EditText mPostText;
    private Button mPost;
    private Button mCancel;
    private DatabaseReference mReference;
    private DatabaseReference mdbReference;
    private FirebaseAuth mAuth;
//    private NotificationManagerCompat notificationManager;
    private FirebaseUser user;
    private StorageReference imgstr;

    public PostingFragment() {
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
                ProfileFragment pf=new ProfileFragment();
                transaction.replace(R.id.fragment_container, pf);
                transaction.commit();
                return true;
            case R.id.item1:
                MyProfileFragment mf=new MyProfileFragment();
                transaction.replace(R.id.fragment_container, mf);
                transaction.commit();
                return true;
            case R.id.item2:
                mAuth.signOut();
                LoginFragment lf=new LoginFragment();
                transaction.replace(R.id.fragment_container, lf);
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
        View view= inflater.inflate(R.layout.fragment_posting, container, false);

//        notificationManager=NotificationManagerCompat.from(getContext());
        mImageUser=(ImageView) view.findViewById(R.id.image_profile);
        mNameUser=(TextView) view.findViewById(R.id.name_user);
        mPostText=(EditText) view.findViewById(R.id.text_post_user);
        mPost=(Button) view.findViewById(R.id.button_post);
        mCancel=(Button) view.findViewById(R.id.button_cancel);
        mReference = FirebaseDatabase.getInstance().getReference();
        mdbReference = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        imgstr=FirebaseStorage.getInstance().getReference("uploads");
        imgstr.child(user.getDisplayName()+".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(getContext()).load(uri).into(mImageUser);
            }
        });

//        final Notification notification=new NotificationCompat.Builder(getContext(),CHANNEL_1_ID)
//                .setSmallIcon(R.drawable.ic_post)
//                .setContentTitle("Posts")
//                .setContentText("New post has been made.")
//                .setPriority(NotificationCompat.PRIORITY_HIGH)
//                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
//                .build();


        mNameUser.setText(user.getDisplayName());

        mPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String post=mPostText.getText().toString().trim();
                String name=user.getDisplayName();
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Calendar cal = Calendar.getInstance();
                String userId = dateFormat.format(cal.getTime());
                Posts myPost=new Posts(post,name,userId);

                mReference.child("Posts").child(userId).setValue(myPost);
//                mdbReference.child("NumberofPosts").child("number").addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        k=dataSnapshot.getValue(Integer.class);
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                    }
//                });
//
//                mSavingReference.child("NumberofPosts").addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        mSavingReference.child("number").setValue(k+1);
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                    }
//                });

//                mReference.child("Posts").addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        notificationManager.notify(1,notification);
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                    }
//                });


                Toast.makeText(getContext(),"Posted", Toast.LENGTH_SHORT).show();

                Handler mHandler = new Handler();
                mHandler.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        //send to profile fragment after making the post
                        ProfileFragment pf=new ProfileFragment();
                        FragmentManager manager=getFragmentManager();
                        FragmentTransaction transaction=manager.beginTransaction();
                        transaction.replace(R.id.fragment_container, pf);
                        transaction.commit();
                    }
                }, 1000L);

            }
        });
        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfileFragment pf=new ProfileFragment();
                FragmentManager manager=getFragmentManager();
                FragmentTransaction transaction=manager.beginTransaction();
                transaction.replace(R.id.fragment_container, pf);
                transaction.commit();
            }
        });

        return view;
    }

}
