package baksha97.com.euleritychallenge.imaging;

import java.util.ArrayList;

import ja.burhanrashid52.photoeditor.PhotoFilter;

/**
 * This custom class has been built to compartmentalize the cycling of all available filters.
 */
public class FilterCycler {

    private static FilterCycler sInstance;


    private ArrayList<PhotoFilter> filters;
    private int currentIndex;

    /**
     * This constructor initializes a new cycler with every type of photo filter available.
     */
    private FilterCycler() {
        currentIndex = 0;
        filters = new ArrayList<>();
        for (PhotoFilter f : PhotoFilter.values()) {
            filters.add(f);
        }
    }

    //Ensures only one instance is created
    public static synchronized FilterCycler getsInstance() {
        if (sInstance == null) sInstance = new FilterCycler();
        return sInstance;
    }

    //Wrapping around the array to ensure no IndexOutOfBounds exceptions
    public PhotoFilter nextFilter() {
        currentIndex++;
        int index = currentIndex % (filters.size());
        return filters.get(index);
    }
}