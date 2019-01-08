package com.example.hemant.bottom;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.MyViewHolder> {

    private DatabaseReference mReference;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private Context mContext;
    private List<PostRetrieval> mPosts;

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView post;
        public TextView name;
        public TextView likesprv;
        public Button likebtn;
        public TextView pstime;

        public MyViewHolder(View view){
            super(view);
            post=view.findViewById(R.id.post_preview);
            name=view.findViewById(R.id.nameofpost);
            likebtn=view.findViewById(R.id.RespondButton);
            pstime=view.findViewById(R.id.posttime);

            likesprv=view.findViewById(R.id.nooflikes);
            mReference = FirebaseDatabase.getInstance().getReference();
            mAuth = FirebaseAuth.getInstance();
            user = mAuth.getCurrentUser();

        }
    }

    public PostAdapter(Context context, List<PostRetrieval> postList){
        mContext=context;
        mPosts=postList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v=LayoutInflater.from(mContext).inflate(R.layout.postlayout, viewGroup, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, int i) {

        PostRetrieval postRetrieval=mPosts.get(i);
        myViewHolder.post.setText(postRetrieval.getPosts());
        myViewHolder.name.setText("By :"+postRetrieval.getPostby());
        final int k=postRetrieval.getLikes();
        final String tim=postRetrieval.getTimeofposting();
        myViewHolder.likesprv.setText(Integer.toString(k)+" Likes");
        myViewHolder.pstime.setText("Posted On : "+tim);

        myViewHolder.likebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mReference.child("Posts").child(tim).child("Likes").setValue(k+1);
            }
        });
    }


    @Override
    public int getItemCount() {
        return mPosts.size();
    }
}
