package com.example.hemant.bottom;


import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;


/**
 * A simple {@link Fragment} subclass.
 */
public class ResetPasswordFragment extends Fragment {

    private EditText emailobtain;
    private Button sendverification;
    Context context;
    public ResetPasswordFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reset_password, container, false);
        context=view.getContext();
        emailobtain=(EditText) view.findViewById(R.id.emailsend);
        sendverification=(Button) view.findViewById(R.id.actionbutton);

            sendverification.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String email=emailobtain.getText().toString().trim();
                    if (email == null) {
                        Toast.makeText(context, "Enter the E-mail first !!!", Toast.LENGTH_SHORT).show();
                    } else {
                        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(context, "Password reset email sent to the given email.", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });

                        Handler mHandler = new Handler();
                        mHandler.postDelayed(new Runnable() {

                            @Override
                            public void run() {
                                //send to login fragment after sending password reset email
                                LoginFragment lf = new LoginFragment();
                                FragmentManager manager = getFragmentManager();
                                FragmentTransaction transaction = manager.beginTransaction();
                                transaction.replace(R.id.fragment_container, lf);
                                transaction.commit();
                            }
                        }, 1000L);

                    }
                }
            });
        return view;
    }

}
