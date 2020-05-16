package alex.worrall.clubnightplanner.utils;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;

import java.util.List;

import alex.worrall.clubnightplanner.model.court.CourtName;

public class CourtnameUtils {

    /**
     * Gets court name based on the currently saved list of names
     * @param lifecycleOwner most likely the activity
     * @param data court name list
     * @return
     */
    public static String getCourtNameText(LifecycleOwner lifecycleOwner,
                                       LiveData<List<CourtName>> data) {
        final LiveDataHolder<List<CourtName>> dataHolder = new LiveDataHolder<>();
        List<CourtName> courtNames = dataHolder.getObservedData(lifecycleOwner, data);
        String courtNameText;
        if (courtNames != null && !courtNames.isEmpty()) {
            CourtName lastCourtName = courtNames.get(courtNames.size() - 1);
            if (lastCourtName.getName().matches("^Court \\d*$")) {
                int count = Integer.parseInt(lastCourtName.getName().replace("Court ", ""));
                courtNameText = "Court " + (count + 1);
            } else {
                courtNameText = "Court " + (courtNames.size() + 1);
            }
        } else {
            courtNameText = "Court 1";
        }
        return courtNameText;
    }
}
