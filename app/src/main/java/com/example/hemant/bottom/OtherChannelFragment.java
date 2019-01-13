package com.example.hemant.bottom;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class OtherChannelFragment extends Fragment {

    private EditText getNameofChannel;
    private TextView showsubs;
    private Button btnSubs;
    private LinearLayout btnSubscribe;
    private Statistics statistics;
    private TextView subscriber;
    private String API_Key="AIzaSyAyON6YdgkFrtNHrGGs3IFS4groadJhhts";
    private ProgressBar progressBar;

    public OtherChannelFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_other_channel, container, false);

        progressBar=view.findViewById(R.id.progress);
        getNameofChannel=(EditText) view.findViewById(R.id.channelname);
        showsubs=(TextView) view.findViewById(R.id.subsprv);
        subscriber=(TextView) view.findViewById(R.id.sub);
        btnSubs=(Button)view.findViewById(R.id.action1);
        btnSubscribe=(LinearLayout) view.findViewById(R.id.subscrberochannel);

        btnSubscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/user/"+getNameofChannel.getText())));
            }
        });

        btnSubs.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                ApiInterface service = ApiClient.getClient().create(ApiInterface.class);

                Call<Mainjson> call = service.getMainJson("statistics", getNameofChannel.getText().toString().trim(), API_Key);

                call.enqueue(new Callback<Mainjson>() {
                    @Override
                    public void onResponse(Call<Mainjson> call, Response<Mainjson> response) {
                        PageInfo pageInfo = response.body().getPageInfo();
                        int i = pageInfo.getTotalResults();
                        if (i == 0) {
                            progressBar.setVisibility(View.INVISIBLE);
                            btnSubscribe.setEnabled(false);
                            showsubs.setText("No such channel exist.");
                            subscriber.setText("Check the Username");
                        } else {
                            List<Items> items = response.body().getItems();
                            statistics = items.get(0).getStatistics();
                            showsubs.setText(statistics.getSubscriberCount());
                            subscriber.setText("Subscribers");
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    }

                    @Override
                    public void onFailure(Call<Mainjson> call, Throwable t) {
                        Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        return view;
    }

}
