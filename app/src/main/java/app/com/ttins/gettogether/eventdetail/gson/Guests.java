package app.com.ttins.gettogether.eventdetail.gson;

import java.util.List;

public class Guests {
    List<Guest> guestList;

    public void setGuests(List<Guest> guestList) {
        this.guestList = guestList;
    }

    public List<Guest> getGuests() {
        return this.guestList;
    }
}
