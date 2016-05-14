package app.com.ttins.gettogether.eventedit;


import android.content.Context;

import java.lang.ref.WeakReference;

public class EventEditPresenter implements EventEditMVP.PresenterOps, EventEditMVP.RequiredPresenterOps {


    private WeakReference<EventEditMVP.RequiredViewOps> view;
    private EventEditMVP.ModelOps model;
    private Context viewContext;

    public EventEditPresenter(EventEditMVP.RequiredViewOps view) {
        this.view = new WeakReference<>(view);
        this.model = new EventEditModel(this);

    }

    @Override
    public void saveEvent(String title, String location, String meetingLocation, String phone) {
        model.saveEventData(title, location, meetingLocation, phone);
    }

    @Override
    public void onAttachView(Context context) {
        this.viewContext = context;
        model.onAttachView(context);
    }

    @Override
    public void onDetachView() {
        this.viewContext = null;
        model.onDetachView();
    }
}
