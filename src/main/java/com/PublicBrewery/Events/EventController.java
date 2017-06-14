package com.PublicBrewery.Events;

import com.Common.AbstractController;
import com.Common.Event;

import java.util.HashMap;

/**
 * Created by alexanderkleinhans on 6/13/17.
 */
public class EventController extends AbstractController {
    public String loadEvents(
            int brewery_id,
            int limit,
            int offset
    ) throws Exception {
        // Validate input parameters.
        this.validateID(brewery_id, "brewery_id");
        // Initialize model and create the data.
        EventModel eventModel = new EventModel();
        // Load the data structure we're loading.
        HashMap<Integer, Event> eventHashMap = new HashMap<Integer, Event>();
        // Load the data using the model.
        eventHashMap = eventModel.loadEvents(
                brewery_id,
                limit,
                offset
        );
        // Return JSON or data structure returned by model.
        return this.returnJSON(eventHashMap);
    }
}
