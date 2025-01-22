package com.coffeecode.presentation.viewmodel.base;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public abstract class AbstractViewModel implements Observable {

    protected final List<Observer> observers = new CopyOnWriteArrayList<>();

    @Override
    public void addObserver(Observer observer) {
        if (observer != null) {
            observers.add(observer);
        }
    }

    @Override
    public void removeObserver(Observer observer) {
        if (observer != null) {
            observers.remove(observer);
        }
    }

    protected void notifyObservers(Runnable notification) {
        observers.forEach(observer -> {
            try {
                notification.run();
            } catch (Exception e) {
                handleError(e);
            }
        });
    }

    protected abstract void handleError(Exception e);
}
