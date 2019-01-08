package com.example.hemant.bottom;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


/**
 * A simple {@link Fragment} subclass.
 */
public class EditProfileFragment extends Fragment {

    private EditText useremail;
    private EditText userphone;
    private Button btnsaveinfo;
    private DatabaseReference databaseReference;
    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;

    public EditProfileFragment() {
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
                MyProfileFragment mpf=new MyProfileFragment();
                transaction.replace(R.id.fragment_container, mpf);
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
        View view=inflater.inflate(R.layout.fragment_edit_profile, container, false);

        useremail=(EditText) view.findViewById(R.id.editTextuseremail);
        userphone=(EditText) view.findViewById(R.id.editTextuserphnno);
        btnsaveinfo=(Button) view.findViewById(R.id.buttonsaveinfo);
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();

        databaseReference=FirebaseDatabase.getInstance().getReference();

        btnsaveinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EmailPhone emailPhone=new EmailPhone(firebaseUser.getDisplayName(),useremail.getText().toString().trim(),userphone.getText().toString().trim());

                databaseReference.child("Users").child(firebaseUser.getDisplayName()).setValue(emailPhone).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getContext(),"Information Saved", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        return view;
    }
}
