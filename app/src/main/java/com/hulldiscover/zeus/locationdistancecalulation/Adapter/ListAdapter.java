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

        // set ID text
        viewHolder.eventID.setText(getContext().getText(R.string.eventID_tag)
                + " "
                + String.valueOf(item.getId()));

        // set Title text
        viewHolder.eventTitle.setText(item.getTitle());
        //viewHolder.eventLocation.setText(item.getLocation().toString());

        // set Price text
        viewHolder.eventPrice.setText(getContext().getText(R.string.price_tag)
                + " " + getContext().getText(R.string.pound_sterling_symbol)
                + item.getPrice().toString());

        // grab event item location, and replace "() - brackets" punctuation with empty character
        // and split on commas and consume any spaces either side
        String[] items = item.getLocation().toString().replaceAll("[()]", "").split("\\s*,\\s*");

        // set Location text
        viewHolder.eventLocation.setText(getContext().getText(R.string.location_tag)
                + " "
                + items[0].toString().replace("Point", "")
                + getContext().getText(R.string.comma) + items[1].toString());

        // set Image
        Picasso.with(mContext)
                // TODO: what if there is no image available ? Should display a placeholder image
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
