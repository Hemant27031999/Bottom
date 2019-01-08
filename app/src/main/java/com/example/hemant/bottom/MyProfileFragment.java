package com.example.hemant.bottom;


import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class MyProfileFragment extends Fragment {
    private static final int PICK_IMAGE_REQUEST=1;

    private ImageView mProfileImage;
    private FirebaseAuth firebaseAuth;
    private TextView mName,mPhn,mEmail;
    private Button mSave;
    private DatabaseReference mdbrefer;
    private DatabaseReference readrefer;
    private DatabaseReference databaseReference;
    private FirebaseUser mDBUser;
    private Uri mImageURI;
    private FloatingActionButton flbtn2;
    private StorageReference firebaseStorage;
    private StorageReference retrivingImage;

    public MyProfileFragment() {
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
                return true;
            case R.id.item2:
                firebaseAuth.signOut();
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
        View view= inflater.inflate(R.layout.fragment_my_profile, container, false);
        firebaseAuth=FirebaseAuth.getInstance();
        mName=(TextView) view.findViewById(R.id.username);
        mEmail=(TextView) view.findViewById(R.id.useremail);
        mPhn=(TextView) view.findViewById(R.id.userphn);
        mSave=(Button) view.findViewById(R.id.savebtn);
        mProfileImage=(ImageView) view.findViewById(R.id.profileimage);
        databaseReference=FirebaseDatabase.getInstance().getReference("uploads");
        flbtn2=(FloatingActionButton) view.findViewById(R.id.fab1);

        flbtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager=getFragmentManager();
                FragmentTransaction transaction=manager.beginTransaction();
                EditProfileFragment epf=new EditProfileFragment();
                transaction.replace(R.id.fragment_container, epf);
                transaction.commit();
            }
        });

        mProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, PICK_IMAGE_REQUEST);
            }
        });

        mdbrefer=FirebaseDatabase.getInstance().getReference();
        readrefer=FirebaseDatabase.getInstance().getReference();
        mDBUser=firebaseAuth.getCurrentUser();

        firebaseStorage=FirebaseStorage.getInstance().getReference("uploads");
        retrivingImage=FirebaseStorage.getInstance().getReference("uploads");
        retrivingImage.child(mDBUser.getDisplayName()+".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(getContext()).load(uri).into(mProfileImage);
            }
        });
        String s = mDBUser.getDisplayName();

        DatabaseReference ref = readrefer.child("Users").child(s);

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Userinforetrieval userinforetrieval=dataSnapshot.getValue(Userinforetrieval.class);
                try{
                    mName.setText(userinforetrieval.getUser_Name());
                    mEmail.setText(userinforetrieval.getUser_Email());
                    mPhn.setText(userinforetrieval.getUser_Phn());}
                catch (Exception e){
                    Log.d("hee",e.getMessage());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(),"Failed to retrieve data",Toast.LENGTH_SHORT).show();
            }
        });

        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name=mName.getText().toString().trim();
                String email=mEmail.getText().toString().trim();
                String phnumber=mPhn.getText().toString().trim();

                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                        .setDisplayName(name)
                        .build();
                mDBUser.updateEmail(email);
                mDBUser.updateProfile(profileUpdates);

                final Userinfo userinfo=new Userinfo(name,email,phnumber);

                uploadfile();

                mdbrefer.child("Users").child(mDBUser.getDisplayName()).setValue(userinfo);
                Toast.makeText(getContext(),"Information saved", Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==PICK_IMAGE_REQUEST && resultCode==RESULT_OK
                && data!=null && data.getData()!=null){
            mImageURI=data.getData();
            Picasso.with(getContext()).load(mImageURI).into(mProfileImage);
        }
    }

    private String getFileExtension(Uri uri){
        ContentResolver cR=getContext().getContentResolver();
        MimeTypeMap mime=MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadfile(){
        if(mImageURI!=null){
            StorageReference fileReference=firebaseStorage.child(mDBUser.getDisplayName()+"."+getFileExtension(mImageURI));
            fileReference.putFile(mImageURI)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Upload upload=new Upload(mDBUser.getDisplayName(),
                                    taskSnapshot.getMetadata().getReference().getDownloadUrl().toString());
                            String dataId=databaseReference.push().getKey();
                            databaseReference.child(dataId).setValue(upload);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    });
        }
        else{
            Toast.makeText(getContext(),"File not choosen.",Toast.LENGTH_SHORT).show();
        }
    }

}
