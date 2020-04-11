package alex.worrall.clubnightplanner;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Used when the fragment alters data that is used elsewhere and views require updating. To link
 * views to this, the class must implement the alex.worrall.clubnightplanner.Observer interface
 * and hence provide the update method. The view must be a single shared instance
 */
public abstract class ObservableFragment extends Fragment {
    List<Observer> observers = new ArrayList<>();

    /**
     * Regsister an observer instance with this fragment
     * @param observer view instance
     */
    public void register(Observer observer) {
        if (observer == null) {
            throw new NullPointerException();
        } else {
            observers.add(observer);
        }
    }

    /**
     * Unregister an observer instance with this fragment
     * @param observer view instance
     */
    public void unregister(Observer observer) {
        if (observer == null) {
            throw new NullPointerException();
        } else {
            observers.remove(observer);
        }
    }

    /**
     * Call the update method on all currently registered observers
     */
    public void notifyObservers() {
        for (Observer observer : observers) {
            observer.update();
        }
    }
}
