package com.coffeecode.presentation.viewmodel.base;

public interface Observable {

    void addObserver(Observer listener);

    void removeObserver(Observer listener);

    void notifyObservers();

}
