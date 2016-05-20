package app.com.ttins.gettogether.guestdetail;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import app.com.ttins.gettogether.R;

public class GuestDetailView extends Fragment implements GuestDetailMVP.RequestedViewOps {

    GuestDetailMVP.PresenterOps presenter;
    long guestId;
    TextView phoneNumber;
    TextView address;
    Callback callback;

    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof Activity) {
            callback = (GuestDetailView.Callback) context;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        if (presenter == null) {
            presenter = new GuestDetailPresenter(this);
        }

        Bundle args = getArguments();
        guestId = args.getLong("FRAG_GUEST_DETAIL_EVENT_ID");
        setHasOptionsMenu(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.onAttachView(this);
        presenter.initLoader();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.guest_detail_view, container, false);
        presenter.onPopulateView(guestId);

        phoneNumber = (TextView) root.findViewById(R.id.phone_num_text_view_guest_detail_view);
        address = (TextView) root.findViewById(R.id.address_text_view_guest_detail_view);

        return root;
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.onDetachView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onLoaderInitCompleted(LoaderManager.LoaderCallbacks<Cursor> loaderClass) {
        getLoaderManager().initLoader(2, null, loaderClass);
    }

    @Override
    public Context onContextViewRequired() {
        return getContext();
    }

    @Override
    public void onChangeGuestName(String guestName) {
        callback.onChangeToolbarTitleToGuestName(guestName);
    }

    @Override
    public void onChangePhoneNumber(String phoneNumber) {
        this.phoneNumber.setText(phoneNumber);
    }

    @Override
    public void onChangeAddress(String address) {
        this.address.setText(address);
    }

    public interface Callback {
        void onChangeToolbarTitleToGuestName(String guestName);
    }
}
