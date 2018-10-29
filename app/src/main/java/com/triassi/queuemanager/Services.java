package com.triassi.queuemanager;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class Services extends AppCompatActivity {

    private String companyName;
    private TextView servicesListTextView;
    private ListView servicesListView;
    private ArrayList<String> servicesList = new ArrayList<String>();
    private ArrayAdapter arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services);

        servicesListTextView = findViewById(R.id.servicesListTextView);
        servicesListView = findViewById(R.id.servicesListView);
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, servicesList);
        servicesListView.setAdapter(arrayAdapter);

        Intent intent = getIntent();
        companyName = intent.getStringExtra("company_tapped");

        updateServicesList();

        servicesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(servicesList.size() > position)
                {
                    String serviceName = servicesList.get(position);

                    Intent intent = new Intent(getApplicationContext(), Queue.class);
                    intent.putExtra("service_tapped", serviceName);
                    startActivity(intent);
                }
            }
        });
    }

    private void updateServicesList()
    {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Company");
        query.whereEqualTo("COMPANY_NAME", companyName);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e == null)
                {
                    if(objects.size() > 0)
                    {
                        for (final ParseObject object : objects)
                        {
                            ParseQuery<ParseObject> query = ParseQuery.getQuery("Service");
                            query.whereEqualTo("COMPANY", object);
                            query.findInBackground(new FindCallback<ParseObject>() {
                                @Override
                                public void done(List<ParseObject> services, ParseException e) {
                                    if(e == null)
                                    {
                                        if(services.size() > 0)
                                        {
                                            for (ParseObject service : services)
                                            {
                                                Log.i("SERVICE INFO", service.getString("SERVICE_NAME"));
                                                String serviceName = service.getString("SERVICE_NAME");
                                                servicesList.add(serviceName);
                                            }
                                        }
                                        arrayAdapter.notifyDataSetChanged();
                                    }
                                }
                            });
                        }
                    }
                }
            }
        });
    }
}
