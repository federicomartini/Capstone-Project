package app.com.ttins.gettogether.guestedit;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import app.com.ttins.gettogether.R;
import app.com.ttins.gettogether.common.GuestActivity;
import app.com.ttins.gettogether.guestdetail.GuestDetailView;

public class GuestEditView extends Fragment implements GuestEditMVP.RequestedViewOps {

    private static final String LOG_TAG = GuestEditView.class.getSimpleName();
    GuestEditMVP.PresenterOps presenter;
    TextView guestName;
    TextView phoneNumber;
    TextView address;
    ImageView photo;
    Callback callback;
    long id;
    boolean isNewGuest;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if(context instanceof Activity) {
            callback = (GuestEditView.Callback) context;
        }
    }

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        presenter = new GuestEditPresenter(this);
        setHasOptionsMenu(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.onAttachView(getContext());
        callback.onGuestEditViewResumed();
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.guest_edit_view, container, false);

        guestName = (TextView) root.findViewById(R.id.guest_name_edit_text_guest_edit_view);
        phoneNumber = (TextView) root.findViewById(R.id.phone_edit_text_guest_edit_view);
        address = (TextView) root.findViewById(R.id.address_edit_text_guest_edit_view);

        Bundle args = getArguments();

        if (args != null && !args.isEmpty()) {
            Log.d(LOG_TAG, "Arguments received");
            guestName.setText(args.getString(GuestDetailView.FRAG_GUEST_NAME_ARG));
            phoneNumber.setText(args.getString(GuestDetailView.FRAG_PHONE_ARG));
            address.setText(args.getString(GuestDetailView.FRAG_ADDRESS_ARG));
            id = Long.parseLong(args.getString(GuestDetailView.FRAG_GUEST_ID_ARG));
            isNewGuest = false;
        } else {
            isNewGuest = true;
        }

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

    public void addGuest() {
        String guestName;
        String phoneNumber;
        String address;

        guestName = this.guestName.getText().toString();
        phoneNumber = this.phoneNumber.getText().toString();
        address = this.address.getText().toString();

        if (isNewGuest) {
            presenter.saveGuest(guestName, phoneNumber, address);
        } else {
            presenter.saveGuest(id, guestName, phoneNumber, address);
        }

    }

    @Override
    public void onShowToast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onGuestSaved() {
        callback.onGuestSaved();
    }

    public interface Callback {
        void onGuestSaved();
        void onGuestEditViewResumed();
    }
}
