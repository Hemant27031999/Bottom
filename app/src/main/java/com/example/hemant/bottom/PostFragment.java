package com.example.hemant.bottom;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


/**
 * A simple {@link Fragment} subclass.
 */
public class PostFragment extends Fragment {

    private TextView textViewSignin;
    private Button buttonRegister;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private EditText editTextName;
    private FirebaseUser user;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference mreference;

    private ProgressDialog progressDialog;

    public PostFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_post, null);


        textViewSignin=(TextView) view.findViewById(R.id.textViewSignin);
        textViewSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginFragment lf=new LoginFragment();
                FragmentManager manager=getFragmentManager();
                FragmentTransaction transaction=manager.beginTransaction();
                transaction.replace(R.id.fragment_container, lf);
                transaction.commit();
            }
        });

        firebaseAuth= FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser()!=null){
            //start the profile activity
            ProfileFragment pf=new ProfileFragment();
            FragmentManager manager=getFragmentManager();
            FragmentTransaction transaction=manager.beginTransaction();
            transaction.replace(R.id.fragment_container, pf);
            transaction.commit();
        }

        buttonRegister = (Button) view.findViewById(R.id.buttonRegister);
        editTextEmail=(EditText) view.findViewById(R.id.editTextEmail);
        editTextPassword=(EditText) view.findViewById(R.id.editTextPassword);
        editTextName=(EditText) view.findViewById(R.id.editTextName);
        progressDialog=new ProgressDialog(getContext());

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email=editTextEmail.getText().toString().trim();
                final String password=editTextPassword.getText().toString().trim();
                mreference=FirebaseDatabase.getInstance().getReference();
                if(TextUtils.isEmpty(email)){
                    Toast.makeText(getContext(), "Please enter E-mail first", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(password)){
                    Toast.makeText(getContext(), "Please enter password first", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressDialog.setMessage("Registerring User......");
                progressDialog.show();

                firebaseAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressDialog.dismiss();
                                if(task.isSuccessful()){
                                    String nme=editTextName.getText().toString().trim();
                                    user = FirebaseAuth.getInstance().getCurrentUser();
                                    String phn="";

                                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                            .setDisplayName(nme)
                                            .build();

                                    user.updateProfile(profileUpdates)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                    }
                                                }
                                            });
                                    EmailPhone emailPhone=new EmailPhone(nme,email,phn);

                                    mreference.child("Users").child(nme).setValue(emailPhone).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(getContext(), "Successfully registered.", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    ProfileFragment pf=new ProfileFragment();
                                    FragmentManager manager=getFragmentManager();
                                    FragmentTransaction transaction=manager.beginTransaction();
                                    transaction.add(R.id.fragment_container, pf);
                                    transaction.commit();
                                }
                                else{
                                    Toast.makeText(getContext(), "Unable to Register! Please try again.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        return view;
    }

}
