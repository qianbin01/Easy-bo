package com.qb.easy_bo.model.media;

import java.util.List;



public interface OnLoadCallback<T> {
    void OnSuccess(List<T> t);

    void OnFailure(String msg);
}
