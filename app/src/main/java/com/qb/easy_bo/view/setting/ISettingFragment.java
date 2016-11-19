package com.qb.easy_bo.view.setting;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qb.easy_bo.R;
import com.qb.easy_bo.utils.common.CacheUtil;
import com.qb.easy_bo.utils.common.SharedPreUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ISettingFragment extends Fragment {

    @Bind(R.id.rlWifi)
    RelativeLayout rlWifi;

    @Bind(R.id.rlCache)
    RelativeLayout rlCache;

    @Bind(R.id.checkbox)
    CheckBox mCheckBox;

    @Bind(R.id.tvCache)
    TextView tvCache;

    @Bind(R.id.npMax)
    NumberPicker mPicker;

    private boolean checkStatus;

    public ISettingFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.setting_fragment, null);
        ButterKnife.bind(this, view);
        mPicker.setMaxValue(5);
        mPicker.setMinValue(0);
        mPicker.setValue((Integer) SharedPreUtils.getData(getActivity(), "max_task", 1));
        mCheckBox.setChecked((boolean) SharedPreUtils.getData(getActivity(), "wifi", true));
        checkStatus = mCheckBox.isChecked();
        try {
            tvCache.setText(CacheUtil.getTotalCacheSize(getActivity()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        mPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                SharedPreUtils.saveData(getActivity(), "max_task", i1);
            }
        });

        return view;
    }

    @OnClick(R.id.rlWifi)
    public void checkStatusChange() {
        if (checkStatus) {
            mCheckBox.setChecked(false);
            checkStatus = false;
            SharedPreUtils.saveData(getActivity(), "wifi", checkStatus);
        } else {
            mCheckBox.setChecked(true);
            checkStatus = true;
            SharedPreUtils.saveData(getActivity(), "wifi", checkStatus);
        }
    }

    @OnClick(R.id.rlCache)
    public void clearCache() {
        if ("0K".equals(tvCache.getText().toString())) {
            Snackbar.make(rlCache, "没有缓存无需清理", Snackbar.LENGTH_SHORT).show();
        } else {
            CacheUtil.clearAllCache(getActivity());
            try {
                tvCache.setText(CacheUtil.getTotalCacheSize(getActivity()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            Snackbar.make(rlCache, "清理缓存成功", Snackbar.LENGTH_SHORT).show();
        }
    }

}
