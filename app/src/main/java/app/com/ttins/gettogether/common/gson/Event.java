package app.com.ttins.gettogether.common.gson;


public class Event {
    private long eventId;
    private String eventTitle;
    private String eventDate;
    private String eventTime;
    private String eventPhotoPath;


    public Event() {}


    public void setEventId(long eventId) {
        this.eventId = eventId;
    }

    public long getEventId() {
        return this.eventId;
    }

    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }

    public String getEventTitle(){
        return this.eventTitle;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public String getEventDate() {
        return this.eventDate;
    }

    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }

    public String getEventTime() {
        return this.eventTime;
    }

    public String getEventPhotoPath() {
        return this.eventPhotoPath;
    }

    public void setEventPhotoPath(String photoPath) {
        this.eventPhotoPath = photoPath;
    }
}
