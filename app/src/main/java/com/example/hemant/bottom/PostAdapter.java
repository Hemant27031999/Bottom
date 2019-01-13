package com.example.hemant.bottom;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.MyViewHolder> {

    private DatabaseReference mReference;
    private DatabaseReference mReferenceLikes;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private Context mContext;
    private List<PostRetrieval> mPosts;
    private EditText commentretrieve;
    private boolean mProcessLike;

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
            commentretrieve=view.findViewById(R.id.comment);

            likesprv=view.findViewById(R.id.nooflikes);
            mReference = FirebaseDatabase.getInstance().getReference();
            mReferenceLikes=FirebaseDatabase.getInstance().getReference("Likes");
            mReference=FirebaseDatabase.getInstance().getReference("Likes");
            mAuth = FirebaseAuth.getInstance();
            user = mAuth.getCurrentUser();
            mProcessLike=false;
            mReference.keepSynced(true);

            mReferenceLikes.keepSynced(true);
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
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {

        final PostRetrieval postRetrieval=mPosts.get(i);
        myViewHolder.post.setText(postRetrieval.getPosts());
        myViewHolder.name.setText("By : "+postRetrieval.getPostby());
        final String tim=postRetrieval.getTimeofposting();
        myViewHolder.pstime.setText("Posted On : "+tim);

        myViewHolder.likebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProcessLike=true;
                mReferenceLikes.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (mProcessLike) {
                            if (dataSnapshot.child(tim).hasChild(user.getDisplayName())) {
                                mReferenceLikes.child(tim).child(user.getDisplayName()).removeValue();
                                mProcessLike = false;
                            } else {
                                mReferenceLikes.child(tim).child(user.getDisplayName()).setValue(user.getDisplayName());
                                mProcessLike = false;
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(tim).hasChild(user.getDisplayName())){
                    myViewHolder.likebtn.setText("Unlike");
                    myViewHolder.likebtn.setTextColor(ContextCompat.getColor(mContext,R.color.white));
                    myViewHolder.likebtn.setBackgroundResource(R.drawable.shape);
                }
                else {
                    myViewHolder.likebtn.setText("Like");
                    myViewHolder.likebtn.setTextColor(ContextCompat.getColor(mContext,R.color.black));
                    myViewHolder.likebtn.setBackgroundResource(R.drawable.shape1);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mReference.child(tim).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int size = (int) dataSnapshot.getChildrenCount();
                myViewHolder.likesprv.setText(size+" Likes");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    @Override
    public int getItemCount() {
        return mPosts.size();
    }

    public void filteredlist(ArrayList<PostRetrieval> filteredposts) {
        mPosts=filteredposts;
        notifyDataSetChanged();
    }
}
