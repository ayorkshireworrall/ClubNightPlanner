package alex.worrall.clubnightplanner.utils;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import java.util.Collection;
import java.util.List;

/**
 * Object to store information from live data observe method
 * @param <T> List of live data objects
 */
public class LiveDataHolder<T extends Collection> {
    T data;

    public LiveDataHolder() {
    }

    /**
     * Extracts current data from LiveData object
     * @param lifecycleOwner usually the activity
     * @param liveData live data object
     * @return data
     */
    public T getObservedData(LifecycleOwner lifecycleOwner, LiveData<T> liveData) {
        liveData.observe(lifecycleOwner, new Observer<T>() {
            @Override
            public void onChanged(T data) {
                LiveDataHolder.this.data = data;
            }
        });
        return data;
    }
}
