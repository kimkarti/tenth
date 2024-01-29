package com.example.tenthskate.Faq;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.example.tenthskate.R;

import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private FaqsAdapter faqsAdapter;
    private ArrayList<Faq> faqs;
    private ExpandableListView expandableListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.faq_activity);
        Toolbar mToolbar = (Toolbar) findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Floor decor Help & Faqs");
        findViewById();
    }

    private void findViewById() {
        expandableListView = findViewById(R.id.expandableListView);
        faqs = getFaqs();
        faqsAdapter = new FaqsAdapter(faqs);
        expandableListView.setAdapter(faqsAdapter);
        final int colorPrimary = ContextCompat.getColor(MainActivity.this, R.color.colorPrimary);

        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            int previousGroup = -1;

            @Override
            public void onGroupExpand(int groupPosition) {
                if (groupPosition != previousGroup)
                    expandableListView.collapseGroup(previousGroup);
                previousGroup = groupPosition;
            }
        });

        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            View previousGroupView = null;
            TextView currentHeaderTv, prevHeaderTv;

            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View currentView, int groupPosition, long l) {
                currentHeaderTv = currentView.findViewById(R.id.headerTv);
                if (currentView == previousGroupView) {
                    if (expandableListView.isGroupExpanded(groupPosition)) {
                        currentView.setBackgroundColor(Color.WHITE);
                        currentHeaderTv.setTextColor(colorPrimary);
                    } else {
                        currentView.setBackgroundColor(colorPrimary);
                        currentHeaderTv.setTextColor(Color.WHITE);
                    }
                } else {
                    currentView.setBackgroundColor(colorPrimary);
                    currentHeaderTv.setTextColor(Color.WHITE);
                    if (previousGroupView != null) {
                        previousGroupView.setBackgroundColor(Color.WHITE);
                        prevHeaderTv = previousGroupView.findViewById(R.id.headerTv);
                        prevHeaderTv.setTextColor(colorPrimary);
                    }
                }
                previousGroupView = currentView;
                return false;
            }
        });
    }

    private ArrayList<Faq> getFaqs() {
        ArrayList<Faq> faqs = new ArrayList<>();
        faqs.add(new Faq("How do I make an Order?",
                "Shopping through the shopping cart, select the items that will be purchased in accordance with your wishes.\n" +
                        "Continue by filling the form e-mail with details of the total price.\n" +
                        "after you place an order, we will immediately check the conditions, the availability (and prices if there are changes in the price), as well as the shipping of the product that you yourselves message, therefore DO NOT send / transfer money before there is confirmation from us via phone / SMS or email.\n" +
                        "Upon confirmation from us, please send / transfer payment to one of the following bank account\n" +
                        "We only accept cash payments by wire transfer to a bank account."));

        faqs.add(new Faq("Payments?",
                "Make orders with your phone number using the mpesa gateway" +
                        "floor decor will send information on the number of total expenditures " +
                        "and postage to your email address, for details please check your email!"));

        faqs.add(new Faq("Shipping?",
                "Product prices do not include shipping costs, shipping costs depend " +
                        "on the weight and volume of goods, destination address and courier used."));

        faqs.add(new Faq("Does our customer care respond?",
                "Yes, fast as well."));

        faqs.add(new Faq("Contact us?",
                "Do you still feel confused by the system that you need? Do not worry, " +
                        "please contact us now! Gladly we will help resolve your needs."));



        return faqs;
    }
}
