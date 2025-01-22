package com.coffeecode.presentation.viewmodel.listener;

import com.coffeecode.presentation.viewmodel.state.MapState;

public interface MapStateListener {

    void onMapStateChanged(MapState newState);

    void onError(String message);

}
