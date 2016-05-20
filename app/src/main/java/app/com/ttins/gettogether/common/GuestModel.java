package app.com.ttins.gettogether.common;

public class GuestModel implements GuestMVP.ModelOps {

    GuestMVP.RequestedPresenterOps presenter;


    public GuestModel(GuestMVP.RequestedPresenterOps presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onDestroy() {
        // destroying actions
    }
}
