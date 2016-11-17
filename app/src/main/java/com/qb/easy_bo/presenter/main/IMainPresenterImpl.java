package com.qb.easy_bo.presenter.main;


import com.qb.easy_bo.R;
import com.qb.easy_bo.view.main.IMainView;

public class IMainPresenterImpl implements IMainPresenter {
    private IMainView mView;

    public IMainPresenterImpl(IMainView mView) {
        this.mView = mView;
    }

    @Override
    public void switchNavigation(int id) {
        switch (id) {
            case R.id.navigation_item_medias:
                mView.switch2Medias();
                break;
            case R.id.navigation_item_downloads:
                mView.switch2Downloads();
                break;
            case R.id.navigation_item_settings:
                mView.switch2Settings();
                break;
            case R.id.navigation_item_about:
                mView.switch2About();
                break;
            default:
                mView.switch2Medias();
                break;
        }
    }
}
