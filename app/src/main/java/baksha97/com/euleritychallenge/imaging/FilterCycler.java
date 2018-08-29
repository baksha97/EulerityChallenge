package baksha97.com.euleritychallenge.imaging;

import java.util.ArrayList;

import ja.burhanrashid52.photoeditor.PhotoFilter;

public class FilterCycler {

    private static FilterCycler sInstance;


    private ArrayList<PhotoFilter> filters;
    private int currentIndex;

    private FilterCycler() {
        currentIndex = 0;
        filters = new ArrayList<>();
        for (PhotoFilter f : PhotoFilter.values()) {
            filters.add(f);
        }
    }

    public static synchronized FilterCycler getsInstance() {
        if (sInstance == null) sInstance = new FilterCycler();
        return sInstance;
    }

    public PhotoFilter nextFilter() {
        currentIndex++;
        int index = currentIndex % (filters.size());
        return filters.get(index);
    }
}