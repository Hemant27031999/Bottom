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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthEmailException;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {

    private Button buttonSignUp;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private TextView textViewSignUp;
    private TextView resetpassword;

    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;

    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        textViewSignUp=(TextView) view.findViewById(R.id.textViewSignup);
        textViewSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PostFragment pf=new PostFragment();
                FragmentManager manager=getFragmentManager();
                FragmentTransaction transaction=manager.beginTransaction();
                transaction.replace(R.id.fragment_container, pf);
                transaction.commit();
            }
        });

        firebaseAuth = FirebaseAuth.getInstance();

        //if the objects getcurrentuser method is not null
        //means user is already logged in
        if(firebaseAuth.getCurrentUser() != null){
            //profile activity to be started
            ProfileFragment pf=new ProfileFragment();
            FragmentManager manager=getFragmentManager();
            FragmentTransaction transaction=manager.beginTransaction();
            transaction.replace(R.id.fragment_container, pf);
            transaction.commit();
        }

        editTextEmail = (EditText) view.findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) view.findViewById(R.id.editTextPassword);
        buttonSignUp = (Button) view.findViewById(R.id.buttonSignUp);
        textViewSignUp  = (TextView) view.findViewById(R.id.textViewSignup);
        resetpassword=(TextView) view.findViewById(R.id.passwordreset);

        progressDialog = new ProgressDialog(getContext());

        //attaching click listener

        resetpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ResetPasswordFragment rpf=new ResetPasswordFragment();
                FragmentManager manager=getFragmentManager();
                FragmentTransaction transaction=manager.beginTransaction();
                transaction.replace(R.id.fragment_container, rpf);
                transaction.commit();
            }
        });

        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = editTextEmail.getText().toString();
                final String password  = editTextPassword.getText().toString();


                //checking if email and passwords are empty
                if(TextUtils.isEmpty(email)){
                    Toast.makeText(getContext(),"Please enter email",Toast.LENGTH_LONG).show();
                    return;
                }

                if(TextUtils.isEmpty(password)){
                    Toast.makeText(getContext(),"Please enter password",Toast.LENGTH_LONG).show();
                    return;
                }

                //if the email and password are not empty
                //displaying a progress dialog

                progressDialog.setMessage("Logging in, Please wait ......");
                progressDialog.show();

                //logging in the user
                firebaseAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(getActivity(),new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressDialog.dismiss();
                                //if the task is successfull
                                if(task.isSuccessful()){
                                    //start the profile fragment
                                    ProfileFragment pf=new ProfileFragment();
                                    FragmentManager manager=getFragmentManager();
                                    FragmentTransaction transaction=manager.beginTransaction();
                                    transaction.replace(R.id.fragment_container, pf);
                                    transaction.commit();
                                }
                                else {
                                    try {
                                        throw task.getException();
                                    }
                                    catch (FirebaseAuthInvalidCredentialsException e) {
                                        Toast.makeText(getContext(), "Invalid Password", Toast.LENGTH_LONG).show();
                                    }
                                    catch (FirebaseAuthEmailException e){
                                        Toast.makeText(getContext(), "Invalid Email", Toast.LENGTH_LONG).show();
                                    }
                                    catch (FirebaseAuthException e){
                                        Toast.makeText(getContext(), "Invalid Credentials", Toast.LENGTH_LONG).show();
                                    }
                                    catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        });
            }
        });

        return view;
    }

}
