package app.com.ttins.gettogether.guestedit;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.io.IOException;

import app.com.ttins.gettogether.R;
import app.com.ttins.gettogether.common.ui.ThreeTwoImageView;
import app.com.ttins.gettogether.common.utils.FilePath;
import app.com.ttins.gettogether.common.utils.Permissions;
import app.com.ttins.gettogether.guestdetail.GuestDetailView;

public class GuestEditView extends Fragment implements GuestEditMVP.RequestedViewOps {

    private static final String LOG_TAG = GuestEditView.class.getSimpleName();
    private static final int SELECT_IMAGE_REQ_CODE = 1;

    GuestEditMVP.PresenterOps presenter;
    TextView guestName;
    TextView phoneNumber;
    TextView address;
    String photoSrc;
    Callback callback;
    long id;
    boolean isNewGuest;
    CircularImageView guestImage;


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

    private void loadArguments(Bundle args) {
        if (args != null && !args.isEmpty()) {
            Log.d(LOG_TAG, "Arguments received");
            guestName.setText(args.getString(GuestDetailView.FRAG_GUEST_NAME_ARG));
            phoneNumber.setText(args.getString(GuestDetailView.FRAG_PHONE_ARG));
            address.setText(args.getString(GuestDetailView.FRAG_ADDRESS_ARG));

            if (args.getString(GuestDetailView.FRAG_PHOTO_PATH_ARG) != null &&
                    !args.getString(GuestDetailView.FRAG_PHOTO_PATH_ARG).isEmpty()) {

                Glide.with(this)
                        .load(args.getString(GuestDetailView.FRAG_PHOTO_PATH_ARG))
                        .into(guestImage);
                Log.d(LOG_TAG, "Arguments photoSrc = " + photoSrc);
                if (photoSrc == null) {
                    photoSrc = args.getString(GuestDetailView.FRAG_PHOTO_PATH_ARG);
                }

            } else {
                Log.d(LOG_TAG, "Arguments photo argument is null");
            }

            id = Long.parseLong(args.getString(GuestDetailView.FRAG_GUEST_ID_ARG));
            isNewGuest = false;
        } else {
            isNewGuest = true;
        }
    }

    @Override
    public void onResume() {
        Log.d(LOG_TAG, "onResume");
        super.onResume();
        presenter.onAttachView(getContext());
        callback.onGuestEditViewResumed();
        if (this.photoSrc != null) {
            Log.d(LOG_TAG, "onResume onShowPictureEditViewToolbar: " + this.photoSrc);
            Glide.with(this)
                    .load(photoSrc)
                    .into(guestImage);
            callback.onShowPictureEditViewToolbar(this.photoSrc);
        } else {
            Log.d(LOG_TAG, "onResume photoSrc is null...");
        }
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
        guestImage = (CircularImageView) root.findViewById(R.id.image_view_guest_edit_view);

        loadArguments(getArguments());

        guestImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onPhotoClick();
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

    public void addGuest() {
        String guestName;
        String phoneNumber;
        String address;
        String photo;

        guestName = this.guestName.getText().toString();
        phoneNumber = this.phoneNumber.getText().toString();
        address = this.address.getText().toString();
        photo = this.photoSrc;

        if (isNewGuest) {
            presenter.saveGuest(guestName, phoneNumber, address, photo);
        } else {
            presenter.saveGuest(id, guestName, phoneNumber, address, photo);
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

    @Override
    public void onShowGalleryForPicture() {
        //Intent intent = new Intent();
        //intent.setType("image/*");
        //intent.setAction(Intent.ACTION_GET_CONTENT);
        //startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_IMAGE_REQ_CODE);
        Log.d(LOG_TAG, "onShowGalleryForPicture");
        if (Permissions.checkPermission(getContext())) {
            Log.d(LOG_TAG, "Permission granted!");
            Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(i, SELECT_IMAGE_REQ_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d(LOG_TAG, "onRequestPermissionsResult...");
        switch(requestCode) {
            case Permissions.MY_PERMISSIONS_REQUEST_MANAGE_DOCUMENTS:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    Intent photoPickerIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                    photoPickerIntent.addCategory(Intent.CATEGORY_OPENABLE);
                    photoPickerIntent.setType("image/*");
                    startActivityForResult(photoPickerIntent, SELECT_IMAGE_REQ_CODE);

                } else {
                    Log.d(LOG_TAG, "onRequestPermissionsResult: Permission denied");
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                break;
            default:
                break;
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(LOG_TAG, "onActivityResult...");
        if (requestCode == SELECT_IMAGE_REQ_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    try {
                        Bitmap bitmap = MediaStore.Images.Media
                                .getBitmap(getActivity().getContentResolver(), data.getData());
                        Log.d(LOG_TAG, "Bitmap received: " + data.getDataString());
                        //photoSrc = data.getData().toString();
                        this.photoSrc = FilePath.getPath(getContext(), data.getData());
                        Log.d(LOG_TAG, "onShowPictureEditViewToolbar onActivityResult.photoSrc = " + photoSrc);
                        //toolbarPhotoPending = null;
                        //callback.onShowPictureEditViewToolbar(photoSrc);
                        Glide.with(this)
                                .load(photoSrc)
                                .into(guestImage);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.d(LOG_TAG, "Bitmap is null!");
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(getActivity(), "Cancelled", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public interface Callback {
        void onGuestSaved();
        void onGuestEditViewResumed();
        void onShowPictureEditViewToolbar(String photoSrc);
    }
}
