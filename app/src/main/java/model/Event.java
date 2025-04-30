package model;

public class Event {
    private String title;
    private String location;
    private String date;

    public Event(String title, String location, String date) {
        this.title = title;
        this.location = location;
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public String getLocation() {
        return location;
    }

    public String getDate() {
        return date;
    }
}

