package com.hulldiscover.zeus.locationdistancecalulation.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hulldiscover.zeus.locationdistancecalulation.Model.Event;
import com.hulldiscover.zeus.locationdistancecalulation.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Zeus on 07/05/16.
 */
public class ListAdapter extends ArrayAdapter {

    private Context mContext;

    public ListAdapter(Context context, int resource, ArrayList<Event> eventItem) {
        super(context, resource, eventItem);
        this.mContext = context;
    }


   /* public void add(VendingItem object) {
        super.add(object);
        // add all objects into list
        list.add(object);
    }

    @Override
    public int getCount() {
        return list.size();
    }*/

    /*@Override
    public Object getItem(int position) {
        return list.get(position); // get each object from the list
    }*/

    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {

        View row;
        row = convertView;
        final ViewHolder viewHolder;

        if(row == null) {
            LayoutInflater layoutInflater = (LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.listview_event_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.eventID = (TextView) row.findViewById(R.id.txt_eventID);
            viewHolder.eventTitle = (TextView) row.findViewById(R.id.txt_eventTitle);
            viewHolder.eventPrice = (TextView) row.findViewById(R.id.txt_eventPrice);
            viewHolder.eventLocation = (TextView) row.findViewById(R.id.txt_eventLocation);
            viewHolder.eventDisplayImage = (ImageView) row.findViewById(R.id.display_image);

            row.setTag(viewHolder);
        }
        else { // if row is already available
            viewHolder = (ViewHolder)row.getTag();
        }

        Event item = (Event)this.getItem(position);
        // set resource for TextViews
        //final VendingItem vendingItems = (VendingItem)this.getItem(position);

        //final String newPictureId = Integer.toString(downloadedPuzzles.getPictureId());
        // set the resource for the DownloadedPuzzlesHolder
        viewHolder.eventID.setText(getContext().getText(R.string.eventID_tag) + " " + String.valueOf(item.getId()));
        viewHolder.eventTitle.setText(item.getTitle());
        //viewHolder.eventLocation.setText(item.getLocation().toString());
        viewHolder.eventPrice.setText(getContext().getText(R.string.price_tag) + " " + "£" + item.getPrice().toString());
        String[] items = item.getLocation().toString().replaceAll("[()]", "").split("\\s*,\\s*");
        viewHolder.eventLocation.setText(getContext().getText(R.string.location_tag) + " " + items[0].toString().replace("Point", "") + ", " + items[1].toString());
        Picasso.with(mContext)
                .load(item.getEvent_image())
                .fit()
                .into(viewHolder.eventDisplayImage);
        return row;
    }


    public class ViewHolder {
        private TextView eventID, eventPrice, eventLocation, eventTitle;
        ImageView eventDisplayImage;

    }

}
