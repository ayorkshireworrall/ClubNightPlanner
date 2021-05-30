package alex.worrall.clubnightplanner.utils;

public class TimeUtil {
    public static String convertTimeToString(int hr, int min) {
        String am_pm, minutes;
        int hours;
        if (hr > 12) {
            am_pm = " PM";
            hours = hr - 12;
        } else if (hr == 0) {
            am_pm = " AM";
            hours = 12;
        } else {
            am_pm = " AM";
            hours = hr;
        }
        if (min < 10) {
            minutes = "0" + min;
        } else {
            minutes = "" + min;
        }
        return hours + ":" + minutes + am_pm;
    }

    public static String convertTimeToString(int time) {
        int min = time % 60;
        int hour = (time - min) / 60;
        return convertTimeToString(hour, min);
    }

    public static int convertTimeToInt(int hr, int min) {
        return hr * 60 + min;
    }
}
