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

public class MainActivity extends AppCompatActivity {

    private TextView companiesListTextView;
    private ListView companiesListView;
    private ArrayList<String> companiesList = new ArrayList<String>();
    private ArrayAdapter arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        companiesListTextView = findViewById(R.id.companiesListTextView);
        companiesListView = findViewById(R.id.companiesListView);
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, companiesList);
        companiesListView.setAdapter(arrayAdapter);

        updateCompaniesList();

        companiesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(companiesList.size() > position)
                {
                    String companyName = companiesList.get(position);

                    Intent intent = new Intent(getApplicationContext(), Services.class);
                    intent.putExtra("company_tapped", companyName);
                    startActivity(intent);
                }
            }
        });
    }

    private void updateCompaniesList()
    {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Company");
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> companies, ParseException e) {
                if (e == null)
                {
                    if(companies.size() > 0)
                    {
                        for(ParseObject company : companies)
                        {
                            Log.i("COMPANY_NAME", company.getString("COMPANY_NAME"));
                            companiesList.add(company.getString("COMPANY_NAME"));
                        }
                    }
                    arrayAdapter.notifyDataSetChanged();
                }
            }
        });
    }
}
