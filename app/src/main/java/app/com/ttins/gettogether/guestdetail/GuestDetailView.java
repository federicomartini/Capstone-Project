package app.com.ttins.gettogether.guestdetail;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;

import app.com.ttins.gettogether.R;

public class GuestDetailView extends Fragment implements GuestDetailMVP.RequestedViewOps {

    private static final String LOG_TAG = GuestDetailView.class.getSimpleName();

    public static final String FRAG_GUEST_ID_ARG = "FRAG_GUEST_ID_ARG ";
    public static final String FRAG_GUEST_NAME_ARG = "FRAG_GUEST_NAME_ARG";
    public static final String FRAG_PHONE_ARG = "FRAG_PHONE_ARG";
    public static final String FRAG_ADDRESS_ARG = "FRAG_ADDRESS_ARG ";

    GuestDetailMVP.PresenterOps presenter;
    long guestId;
    String guestName;
    TextView phoneNumber;
    TextView address;
    ImageView mapImage;
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
        Log.d(LOG_TAG, "onResume");
        super.onResume();
        presenter.onAttachView(this);
        presenter.initLoader();
        callback.onGuestDetailViewResumed();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.guest_detail_view, container, false);
        presenter.onPopulateView(guestId);

        phoneNumber = (TextView) root.findViewById(R.id.phone_num_text_view_guest_detail_view);
        address = (TextView) root.findViewById(R.id.address_text_view_guest_detail_view);
        mapImage = (ImageView) root.findViewById(R.id.map_image_guest_detail_view);

        mapImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onMapClick(address.getText().toString());
            }
        });


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
        this.guestName = guestName;
    }

    @Override
    public void onChangePhoneNumber(String phoneNumber) {
        this.phoneNumber.setText(phoneNumber);
    }

    @Override
    public void onChangeAddress(String address) {
        this.address.setText(address);
    }

    public Bundle getDetails() {

        Bundle args = new Bundle();

        args.putString(FRAG_GUEST_ID_ARG, Long.toString(guestId));
        args.putString(FRAG_GUEST_NAME_ARG, guestName);
        args.putString(FRAG_PHONE_ARG, phoneNumber.getText().toString());
        args.putString(FRAG_ADDRESS_ARG, address.getText().toString());

        return args;
    }

    @Override
    public void onSendAddressToActivity(String address) {
        Log.d(LOG_TAG, "onSendAddressToActivity");
        callback.onAddressForMap(address);
    }

    public interface Callback {
        void onChangeToolbarTitleToGuestName(String guestName);
        void onGuestDetailViewResumed();
        void onAddressForMap(String address);
    }
}
