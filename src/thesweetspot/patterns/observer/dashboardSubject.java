
package thesweetspot.patterns.observer;

import thesweetspot.*;
import java.util.ArrayList;
import java.util.List;

public class dashboardSubject {

    private static final List<dashboardObserver> observers = new ArrayList<>();

    public static void addObserver(dashboardObserver observer) {
        observers.add(observer);
    }

    public static void removeObserver(dashboardObserver observer) {
        observers.remove(observer);
    }

    public static void notifyObservers() {
        for (dashboardObserver observer : observers) {
            observer.updateDashboard();
        }
    }
}
