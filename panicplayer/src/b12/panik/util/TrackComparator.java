// ----------------------------------------------------------------------------
// [b12] Java Source File: TrackComparator.java
//                created: 29.10.2003
//              $Revision: 1.3 $
// ----------------------------------------------------------------------------
package b12.panik.util;

import java.util.Comparator;

import javax.media.Track;

/**
 * A <code>Comparator</code> that compares <code>Track</code>s.
 * @author kariem
 */
public class TrackComparator implements Comparator {

    private static final TimeComparator tComp = new TimeComparator();

    /** @see Comparator#compare(Object, Object) */
    public int compare(Object o1, Object o2) {
        return compare((Track) o1, (Track) o2);
    }

    /**
     * Compares two <code>Track</code> objects.
     *
     * @param track1 the first track.
     * @param track2 the second track-
     * @return the difference in start time and duration. If these are not
     *          different, their difference in hash code is returned.
     */
    public int compare(Track track1, Track track2) {
        // compare start times
        int dif = tComp.compare(track1.getStartTime(), track2.getStartTime());
        if (dif != 0) {
            return dif;
        }
        // compare durations ... shorter duration first
        dif = tComp.compare(track1.getDuration(), track2.getDuration());
        if (dif != 0) {
            return dif;
        }
        // if start time and duration are equal use sorting alternative
        return track1.hashCode() - track2.hashCode();
    }
}
