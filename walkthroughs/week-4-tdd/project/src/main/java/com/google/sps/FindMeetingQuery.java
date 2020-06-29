// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps;

import java.util.Collection;
import java.util.Collections;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Iterator;
import java.lang.Object;
import java.util.TreeMap;
import java.util.Set;
import java.util.Map;

public final class FindMeetingQuery {
  public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {

    //The TreeMap stores pairs of key and value. It sorts pairs in ascending order based on their key.
    //Key is start time of event and value is event. 
    //So Events will be sorted based on their start time when they get added to the TreeMap.   
    TreeMap<Integer, Event> eventsList = new TreeMap<Integer, Event>();
    
    //Go through events attendees and request attendees 
    //and add only the important events to TreeMap.
    //Important events are events that contain any of the attendees in the request attendees. 

    Iterator i = events.iterator();
    Iterator iter = request.getAttendees().iterator();

    while (i.hasNext()) {
        Event currEvent = (Event)i.next();
        //if meeting request only has one attendee
        if (request.getAttendees().size() == 1) { 
            Iterator iterAttendee = request.getAttendees().iterator();
            String oneAttendee = (String)iterAttendee.next();
            if (currEvent.getAttendees().contains(oneAttendee)) {
                eventsList.put(currEvent.getWhen().start(), currEvent);
            }
        }
        //if meeting request contains more than one attendee
        else { 
            while (iter.hasNext()) {
                String currentAttendee = (String)iter.next();
                if (currEvent.getAttendees().contains(currentAttendee)) {
                    eventsList.put(currEvent.getWhen().start(), currEvent);
                    break;
                }
            }
        }
    }
    
    //Need this in order to iterate through the TreeMap
    Set set = eventsList.entrySet();
    
    //finalTimes will contain all the times the meeting can happen
    ArrayList<TimeRange> finalTimes = new ArrayList<TimeRange>();
   
    //If meeting duration > 24 hours, return no options
    if (request.getDuration() > TimeRange.WHOLE_DAY.duration()) {
        finalTimes.clear();
    } 

    //If request has no attendees or there are no events already set up, return whole day
    else if (request.getAttendees().isEmpty() || events.isEmpty() || eventsList.isEmpty()) {
        finalTimes.add(TimeRange.WHOLE_DAY);
    }

    int limitTime = TimeRange.START_OF_DAY;
    boolean contained = false;
    boolean overlapped = false;
    Event nextEvent = null;

    //Iterates through sorted important events to find the meeting times
    for (Iterator iterator = set.iterator(); iterator.hasNext();) {
        
        Map.Entry mentry = (Map.Entry)iterator.next();
        
        //currentEvent is the event the loop is currently on
        Event currentEvent = (Event)mentry.getValue(); 
        
        //nextEvent is the event that comes after the currentEvent in the TreeMap
        if (iterator.hasNext()) { 
            nextEvent = (Event)eventsList.higherEntry((int)mentry.getKey()).getValue();
        }
        
        //in case two events overlap, and one contains the other, and there are no other events after those
        if (contained && !iterator.hasNext()) {
            //create new TimeRange
            TimeRange newRange = TimeRange.fromStartEnd(limitTime, TimeRange.END_OF_DAY, true);
            //if the duration of the new TimeRange is greater or equal to the duration of the meeting requested
            //then this is a TimeRange we can use for the meeting to occur
            if (newRange.duration() >= request.getDuration()) {
                finalTimes.add(newRange);
            }
        }
        //in case two events overlap but one does not contain the other, and there are no other events after those
        else if (overlapped && !iterator.hasNext()) {
            //create new TimeRange
            TimeRange newRange = TimeRange.fromStartEnd(limitTime, TimeRange.END_OF_DAY, true);
            //if the duration of the new TimeRange is greater or equal to the duration of the meeting requested
            //then this is a TimeRange we can use for the meeting to occur
            if (newRange.duration() >= request.getDuration()) {
                finalTimes.add(newRange);
            }
        }
        else {
            contained = false;
            overlapped = false;
            //create new TimeRange
            TimeRange newRange = TimeRange.fromStartEnd(limitTime, currentEvent.getWhen().start(), false);
            //if the duration of the new TimeRange is greater or equal to the duration of the meeting requested
            //then this is a TimeRange we can use for the meeting to occur
            if (newRange.duration() >= request.getDuration()) {
                finalTimes.add(newRange);
            }
            //if there is a next event
            if (iterator.hasNext()) { 
                //if current event does not overlap next event
                if (!currentEvent.getWhen().overlaps(nextEvent.getWhen())) { 
                    //limitTime becomes the currentEvent's end time
                    limitTime = currentEvent.getWhen().end(); 
                }
                //if events overlap
                else { 
                    //if currentEvent contains nextEvent 
                    if (currentEvent.getWhen().contains(nextEvent.getWhen())) {
                        //limitTime becomes the currentEvent's end time
                        limitTime = currentEvent.getWhen().end(); 
                        contained = true;
                    }
                    //if currentEvent doesn't contain the next event
                    else { 
                        //limitTime becomes the nextEvent's end time
                        limitTime = nextEvent.getWhen().end(); 
                        overlapped = true;    
                    }
                }
            }
            //if there is not a next event
            else {  
                //create new TimeRange from the current event's end time to the end of the day
                TimeRange newTimeRange = TimeRange.fromStartEnd(currentEvent.getWhen().end(), TimeRange.END_OF_DAY, true);
                // if duration of new TimeRange is greater or equal to duration of meeting requested
                //then this is a TimeRange we can use for the meeting to occur
                if (newTimeRange.duration() >= request.getDuration()) {
                    finalTimes.add(newTimeRange);
                }
            }
        }
    }
    return finalTimes;
  }
}
