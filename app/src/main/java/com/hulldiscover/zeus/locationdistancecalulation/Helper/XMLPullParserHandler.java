package com.hulldiscover.zeus.locationdistancecalulation.Helper;

import com.hulldiscover.zeus.locationdistancecalulation.Model.Event;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Created by Zeus on 07/05/16.
 */
public class XMLPullParserHandler {

    ArrayList<Event> events;

    private Event eventItem;
    private String text;

    public XMLPullParserHandler() {
        events = new ArrayList<Event>();
    }

    public ArrayList<Event> getEvents() {
        return events;
    }

    public ArrayList<Event> parse(InputStream inputStream) {
        XmlPullParserFactory factory = null;
        XmlPullParser parser = null;

        try {
            factory  = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);

            parser = factory.newPullParser();
            parser.setInput(inputStream, null);

            int eventType = parser.getEventType();
            while(eventType != XmlPullParser.END_DOCUMENT) {
                String tagName = parser.getName();
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        if (tagName.equalsIgnoreCase("event")) {
                            eventItem = new Event();
                        }
                        break;

                    case XmlPullParser.TEXT:
                        text = parser.getText();
                        break;

                    case XmlPullParser.END_TAG:
                        if (tagName.equalsIgnoreCase("event")) {
                            events.add(eventItem);
                        }
                        else if (tagName.equalsIgnoreCase("id")) {
                            eventItem.setId(Integer.parseInt(text));
                        }
                        else if (tagName.equalsIgnoreCase("title")) {
                            eventItem.setTitle(text);
                        }
                        else if (tagName.equalsIgnoreCase("event_img")) {
                            eventItem.setEvent_image(text);
                        }
                        else if (tagName.equalsIgnoreCase("ticket_price_catA")) {
                            eventItem.setPriceCatA(new BigDecimal(text));
                        }
                        else if (tagName.equalsIgnoreCase("ticket_price_catB")) {
                            eventItem.setPriceCatB(new BigDecimal(text));
                        }
                        else if (tagName.equalsIgnoreCase("quantity")) {
                            eventItem.setNoOfTickets(Integer.parseInt(text));
                        }
                        else if (tagName.equalsIgnoreCase("location")) {
                            String[] latlong =  text.split(",");
                            int x, y;
                            x = Integer.parseInt(latlong[0]);
                            y = Integer.parseInt(latlong[1]);
                            eventItem.setLocation(x, y);
                        }
                        break;

                    default:
                        break;
                }
                    eventType = parser.next();

                }
        } catch (Exception e){
            e.printStackTrace();
        }

        return events;
    }

}
