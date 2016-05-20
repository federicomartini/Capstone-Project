package app.com.ttins.gettogether.guestdetail;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.app.LoaderManager;
import android.util.Log;

import java.lang.ref.WeakReference;
import java.util.HashMap;

import app.com.ttins.gettogether.guestdetail.loader.GuestDetailLoader;

public class GuestDetailPresenter implements GuestDetailMVP.PresenterOps, GuestDetailMVP.RequestedPresenterOps {

    private static final String LOG_TAG = GuestDetailPresenter.class.getSimpleName();

    private WeakReference<GuestDetailMVP.RequestedViewOps> view;
    private GuestDetailMVP.ModelOps model;
    private HashMap<Integer, String> guestDataMap;

    public GuestDetailPresenter(GuestDetailMVP.RequestedViewOps view) {
        this.model = new GuestDetailModel(this);
    }

    @Override
    public void onPopulateView(long id) {
        Log.d(LOG_TAG, "onPopulateDetailView");
        model.getGuestData(id);
    }

    @Override
    public void initLoader() {
        model.initLoader();
    }

    @Override
    public void onAttachView(GuestDetailMVP.RequestedViewOps view) {
        this.view = new WeakReference<>(view);
    }

    @Override
    public void onDetachView() {
        this.view = null;
    }

    @Override
    public void onLoaderInitCompleted(LoaderManager.LoaderCallbacks<Cursor> loaderClass) {
        view.get().onLoaderInitCompleted(loaderClass);
    }

    @Override
    public Context onContextViewRequired() {
        return view.get().onContextViewRequired();
    }

    @Override
    public void onGuestLoadFinished(HashMap<Integer, String> dataMap) {
        Log.d(LOG_TAG, "onEventLoadFinished");

        guestDataMap = dataMap;

        if (view != null) {
            view.get().onChangeGuestName(this.guestDataMap.get(GuestDetailLoader.Query.NAME));
            view.get().onChangePhoneNumber(this.guestDataMap.get(GuestDetailLoader.Query.PHONE_NUMBER));
            view.get().onChangeAddress(this.guestDataMap.get(GuestDetailLoader.Query.ADDRESS));


            /*if (this.eventDataMap.get(EventDetailLoader.Query.CONFIRMATION_STATUS).compareTo(EVENT_STATUS_CONFIRMED) == 0) {
                confirmButtonStatus = true;
            } else {
                confirmButtonStatus = false;
            }*/
        }
    }
}
