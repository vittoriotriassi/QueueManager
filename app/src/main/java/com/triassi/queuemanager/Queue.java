package com.triassi.queuemanager;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

public class Queue extends AppCompatActivity {

    private String serviceName;
    private TextView serviceNameTextView;
    private TextView currentNumberTextView;
    private Button resetQueueButton;
    private Button nextTurnButton;
    private int currentNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_queue);

        serviceNameTextView = findViewById(R.id.serviceNameTextView);
        currentNumberTextView = findViewById(R.id.currentNumberTextView);
        resetQueueButton = findViewById(R.id.resetQueueButton);
        nextTurnButton = findViewById(R.id.nextTurnButton);

        Intent intent = getIntent();
        serviceName = intent.getStringExtra("service_tapped");
        serviceNameTextView.setText(serviceName);

        updateQueue();
    }

    private void updateQueue()
    {
        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run()
            {
                ParseQuery<ParseObject> query = ParseQuery.getQuery("Service");
                query.whereEqualTo("SERVICE_NAME", serviceName);
                query.findInBackground(new FindCallback<ParseObject>()
                {
                    public void done(List<ParseObject> services, ParseException e)
                    {
                        if (e == null)
                        {
                            if(services.size() > 0)
                            {
                                for (ParseObject service : services)
                                {
                                    currentNumber = service.getInt("CURRENT_NUMBER");
                                    currentNumberTextView.setText(String.valueOf(currentNumber));
                                }
                            }
                        }
                    }
                });


                handler.postDelayed(this, 1000);
            }
        };

        handler.postDelayed(runnable, 1000);
    }

    public void onResetQueueClick(View view)
    {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Service");
        query.whereEqualTo("SERVICE_NAME", serviceName);
        query.findInBackground(new FindCallback<ParseObject>()
        {
            public void done(List<ParseObject> services, ParseException e)
            {
                if (e == null)
                {
                    if(services.size() > 0)
                    {
                        for (ParseObject service : services)
                        {
                            service.put("CURRENT_NUMBER", -1);
                            service.put("MY_NUMBER", 0);
                            service.saveInBackground();

                            currentNumberTextView.setText(String.valueOf(service.getInt("CURRENT_NUMBER")));
                        }
                    }
                }
            }
        });
    }

    public void onNextTurnClick(View view)
    {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Service");
        query.whereEqualTo("SERVICE_NAME", serviceName);
        query.findInBackground(new FindCallback<ParseObject>()
        {
            public void done(List<ParseObject> services, ParseException e)
            {
                if (e == null)
                {
                    if(services.size() > 0)
                    {
                        for (ParseObject service : services)
                        {
                            service.increment("CURRENT_NUMBER");
                            service.saveInBackground();
                            currentNumberTextView.setText(String.valueOf(service.getInt("CURRENT_NUMBER")));
                        }
                    }
                }
            }
        });
    }
}
