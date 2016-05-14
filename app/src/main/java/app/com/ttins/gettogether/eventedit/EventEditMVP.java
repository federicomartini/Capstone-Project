package app.com.ttins.gettogether.eventedit;


import android.content.Context;

public interface EventEditMVP {

    interface PresenterOps {
        void saveEvent(String title, String location, String meetingLocation, String phone);
        void onAttachView(Context context);
        void onDetachView();

    }

    interface ModelOps {
        void onAttachView(Context context);
        void onDetachView();
        void saveEventData(String title, String location, String meetingLocation, String phone);
    }

    interface RequiredPresenterOps {

    }

    interface RequiredViewOps {

    }

}
