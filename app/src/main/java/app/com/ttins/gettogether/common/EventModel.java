package app.com.ttins.gettogether.common;

public class EventModel implements EventMVP.ModelOps{

    EventMVP.RequestedPresenterOps presenter;

    public EventModel (EventMVP.RequestedPresenterOps presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onDestroy() {
        // destroying actions
    }

}
