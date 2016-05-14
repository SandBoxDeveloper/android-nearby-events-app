package com.hulldiscover.zeus.locationdistancecalulation.Activity;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.hulldiscover.zeus.locationdistancecalulation.Adapter.ListAdapter;
import com.hulldiscover.zeus.locationdistancecalulation.Helper.XMLPullParserHandler;
import com.hulldiscover.zeus.locationdistancecalulation.Model.Event;
import com.hulldiscover.zeus.locationdistancecalulation.R;
import com.like.LikeButton;
import com.like.OnLikeListener;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Zeus on 13/05/16.
 */
public class EventListings extends AppCompatActivity implements OnLikeListener{
    //members
    ListView mListview;
    ListAdapter mListAdapter;

    private int niceCounterValue = 37;
    //LikeButton favourite;


    @BindView(R.id.star_button) LikeButton favourite;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_listview);
        ButterKnife.bind(this);

        // find listView layout
        mListview = (ListView)findViewById(R.id.listView);

        //favourite.setOnLikeListener(this);

        //favourite = (LikeButton) findViewById(R.id.star_button);

        // list of events
        final ArrayList<Event> eventListings;
        ArrayList<String> imageURLs = new ArrayList<String>();

        // gather vending items stock from XML file
        try {
            XMLPullParserHandler parser = new XMLPullParserHandler();
            eventListings = parser.parse(getAssets().open("event_listings.xml"));
            // add url of each vending item
            for(int i = 0; i < eventListings.size(); i++) {
                imageURLs.add(eventListings.get(i).getEvent_image());
                eventListings.get(i).setEvent_image(imageURLs.get(i));
            }

            //place event listings into adapter
            mListAdapter = new ListAdapter(this, R.layout.listview_event_item, eventListings);
            mListview.setAdapter(mListAdapter);
        } catch (IOException e) {
            e.printStackTrace();
        }




       /* favourite.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                Snackbar.make(likeButton, getString(R.string.toolbar_favorite_snack) + false,
                        Snackbar.LENGTH_SHORT).show();
            }

            @Override
            public void unLiked(LikeButton likeButton) {

            }
        });*/


    }

    @Override
    public void liked(LikeButton likeButton) {
        Snackbar.make(likeButton, getString(R.string.toolbar_favorite_snack) + false,
                Snackbar.LENGTH_SHORT).show();

    }

    @Override
    public void unLiked(LikeButton likeButton) {

    }


}
