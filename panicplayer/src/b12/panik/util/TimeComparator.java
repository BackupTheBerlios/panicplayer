// ----------------------------------------------------------------------------
// [b12] Java Source File: TimeComparator.java
//                created: 29.10.2003
//              $Revision: 1.4 $
// ----------------------------------------------------------------------------
package b12.panik.util;

import java.util.Comparator;

import javax.media.Time;

/**
 * Compares two {@linkplain Time} objects with each other.
 */
public class TimeComparator implements Comparator {

    /** @see Comparator#compare(Object, Object) */
    public int compare(Object o1, Object o2) {
        return compare((Time) o1, (Time) o2);
    }

    /**
     * Compares two <code>Time</code> objects.
     * @param time1 the first time.
     * @param time2 the second time.
     * @return their differents in nanoseconds as <code>int</code>.
     */
    public int compare(Time time1, Time time2) {
        return Utils.convertToInt(time1.getNanoseconds() - time2.getNanoseconds());
    }
}