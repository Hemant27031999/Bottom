package com.example.hemant.bottom;


import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private Statistics statistics1;
    private Statistics statistics2;
    private TextView subscribersPreviewPewdiepie;
    private TextView subscribersPreviewtseries;
    private Button livecount;
    private TextView difference;
    private LinearLayout youtubetseries;
    private LinearLayout youtubepewdiepie;
    private int s1,s2;
    private String differ;
    String API_Key="AIzaSyAyON6YdgkFrtNHrGGs3IFS4groadJhhts";

    public HomeFragment() {
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
        inflater.inflate(R.menu.homemenu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        FragmentManager manager=getFragmentManager();
        FragmentTransaction transaction=manager.beginTransaction();
        switch (item.getItemId()) {
            case R.id.ocs:
                OtherChannelFragment pf=new OtherChannelFragment();
                transaction.replace(R.id.fragment_container, pf);
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
        View view=inflater.inflate(R.layout.fragment_home, null);

        subscribersPreviewPewdiepie=(TextView) view.findViewById(R.id.pewdiepiesubscribers);
        subscribersPreviewtseries=(TextView) view.findViewById(R.id.tseriessubscribers);
        difference=(TextView) view.findViewById(R.id.Difference);
        youtubetseries=(LinearLayout) view.findViewById(R.id.subscrbertseries);
        s1=0;
        s2=0;

        youtubetseries.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/user/tseries")));
            }
        });

        youtubepewdiepie=(LinearLayout) view.findViewById(R.id.subscrberpewdiepie);

        youtubepewdiepie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/user/pewdiepie")));
            }
        });

        livecount=(Button) view.findViewById(R.id.livestats);
        livecount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApiInterface service = ApiClient.getClient().create(ApiInterface.class);

                Call<Mainjson> call = service.getMainJson("statistics", "pewdiepie", API_Key);

                call.enqueue(new Callback<Mainjson>() {
                    @Override
                    public void onResponse(Call<Mainjson> call, Response<Mainjson> response) {
                        List<Items> items = response.body().getItems();
                        statistics1 = items.get(0).getStatistics();
                        String s = statistics1.getSubscriberCount();
                        s1=Integer.parseInt(statistics1.getSubscriberCount());
                        subscribersPreviewPewdiepie.setText(statistics1.getSubscriberCount());
                    }

                    @Override
                    public void onFailure(Call<Mainjson> call, Throwable t) {
                        Toast.makeText(getContext(), "Failed to retrieve data", Toast.LENGTH_SHORT).show();
                    }
                });

                Call<Mainjson> call1 = service.getMainJson("statistics", "tseries", API_Key);

                call1.enqueue(new Callback<Mainjson>() {
                    @Override
                    public void onResponse(Call<Mainjson> call, Response<Mainjson> response) {
                        List<Items> items = response.body().getItems();
                        statistics2 = items.get(0).getStatistics();
                        s2=Integer.parseInt(statistics2.getSubscriberCount());
                        subscribersPreviewtseries.setText(statistics2.getSubscriberCount());
                        differ=Integer.toString(s1-s2);
                        difference.setText(differ);
                    }

                    @Override
                    public void onFailure(Call<Mainjson> call, Throwable t) {
                        Toast.makeText(getContext(), "Failed to retrieve data", Toast.LENGTH_SHORT).show();
                    }
                });

            }

        });

        return view;
    }
}
