package io.pivotal.pal.tracker;

import java.util.*;

public class InMemoryTimeEntryRepository implements TimeEntryRepository{

    private Map<Long,TimeEntry> timeEntryMap = new HashMap<>();

    TimeEntry timeEntryObj;

    long timeEntryId = 1L;

    List<TimeEntry> timeList = new ArrayList<>();

    public TimeEntry create (TimeEntry timeEntry){
        timeEntry.setId(timeEntryId++);
        timeEntryMap.put(timeEntry.getId(),timeEntry);
        timeList.add(timeEntryMap.get(timeEntry.getId()));
        return timeEntry;
    }


    public TimeEntry find (long timeEntry){
        timeEntryObj = timeEntryMap.get(timeEntry);
        return timeEntryObj;
    }

    public TimeEntry update (long id,TimeEntry timeEntry){
        TimeEntry testVal = find(id);

        if(timeEntryMap!=null){
            timeEntry.setId(id);
            timeEntryMap.put(id,timeEntry);
        }
        return timeEntryMap.get(id);
    }

    public void delete(long id){

        TimeEntry deleteList = find(id);
        timeEntryMap.remove(id);
        timeList.remove(deleteList);

    }
    public List<TimeEntry> list (){
        return timeList;
    }
}
