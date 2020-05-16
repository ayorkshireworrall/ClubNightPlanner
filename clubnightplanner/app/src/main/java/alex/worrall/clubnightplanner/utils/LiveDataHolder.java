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

    private T getData() {
        return data;
    }

    private void setData(T data) {
        this.data = data;
    }

    /**
     * Extracts current data from LiveData object
     * @param lifecycleOwner usually the activity
     * @param liveData live data object
     * @return data
     */
    public T getObservedData(LifecycleOwner lifecycleOwner, LiveData<T> liveData) {
        final LiveDataHolder<T> dataHolder = new LiveDataHolder<>();
        liveData.observe(lifecycleOwner, new Observer<T>() {
            @Override
            public void onChanged(T data) {
                dataHolder.setData(data);
            }
        });
        return dataHolder.getData();
    }
}
