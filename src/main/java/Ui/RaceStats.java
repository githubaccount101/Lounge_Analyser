package Ui;

import java.util.List;
import java.util.ArrayList;
import mkw.Event;
import mk8dx.EventD;
import java.sql.*;
import java.util.Random;
@Deprecated
class RaceStats {

    private ArrayList<Event> events;
    private ArrayList<Event> recentEvents;
    private ArrayList<EventD> eventsD;
    private ArrayList<EventD> recentEventsD;

    public RaceStats(){
        this.events = new ArrayList<>();
        this.recentEvents = new ArrayList<>();
        this.eventsD = new ArrayList<>();
        this.recentEventsD = new ArrayList<>();
    }

    public void add(Event event){
        recentEvents.add(event);
    }

    public void refresh(){
        for(Event e: recentEvents){
            events.add(e);
        }
        recentEvents.clear();
    }

    public void addD(EventD event) {
        recentEventsD.add(event);
    }

    public void refreshD() {
        for (EventD e : recentEventsD) {
            eventsD.add(e);
        }
        recentEvents.clear();
    }

    public void printAll(){
        for(Event e: events){
            System.out.println(e);
        }
        for(EventD e: eventsD){
            System.out.println(e);
        }
    }

    public void printRecent(){
        for(Event e: recentEvents){
            System.out.println(e);
            e.printEvent();
        }
        for(EventD e: recentEventsD){
            System.out.println(e);
            e.printEvent();
        }
    }


}

