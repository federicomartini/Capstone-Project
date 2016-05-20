package app.com.ttins.gettogether.guestedit;


import android.content.Context;

import java.lang.ref.WeakReference;

public class GuestEditPresenter implements GuestEditMVP.PresenterOps, GuestEditMVP.RequestedPresenterOps {

    private static final String LOG_TAG = GuestEditPresenter.class.getSimpleName();

    private static final int INSERT_DATA_NO_ERROR = 0;
    private static final int INSERT_DATA_NAME_EMPTY_ERROR = 1;

    WeakReference<GuestEditMVP.RequestedViewOps> view;
    GuestEditMVP.ModelOps model;
    Context viewContext;

    public GuestEditPresenter(GuestEditMVP.RequestedViewOps view) {
        this.view = new WeakReference<>(view);
        this.model = new GuestEditModel(this);
    }

    @Override
    public void saveGuest(String guestName, String phoneNumber, String address) {
        int retCheck;

        retCheck = areAllGuestDataOk(guestName, phoneNumber, address);

        switch(retCheck) {
            case INSERT_DATA_NAME_EMPTY_ERROR:
                view.get().onShowToast("Guest Name field can't be empty");
                break;
            default:
                break;
        }

        if (retCheck == INSERT_DATA_NO_ERROR) {
            model.saveGuestData(guestName, phoneNumber, address);
        }
    }

    private int areAllGuestDataOk(String guestName, String phoneNumber, String address) {

        if (guestName.isEmpty()) {
            return INSERT_DATA_NAME_EMPTY_ERROR;
        }

        return 0;
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

    @Override
    public void onGuestSaved() {
        view.get().onGuestSaved();
    }
}
