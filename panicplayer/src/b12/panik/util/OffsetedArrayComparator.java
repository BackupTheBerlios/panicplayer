// ----------------------------------------------------------------------------
// [b12] Java Source File: OffsetedArrayComparator.java
//                created: 29.10.2003
//              $Revision: 1.3 $
// ----------------------------------------------------------------------------

package b12.panik.util;

import java.util.Comparator;

import b12.panik.player.OffsetedArray;

/**
 * A <code>Comparator</code> that compares <code>OffsetedArray</code>s.
 * @author olivier
 */
public class OffsetedArrayComparator implements Comparator {

    /** @see Comparator#compare(Object, Object) */
    public int compare(Object o1, Object o2) {
        return compare((OffsetedArray) o1, (OffsetedArray) o2);
    }

    /**
     * Compares two <code>OffsetedArray</code> objects.
     *
     * @param array1 the first OffsetedArray.
     * @param array2 the second OffsetedArray-
     * @return the difference in start index. If these are not
     *          different, their difference in hash code is returned.
     */
    public int compare(OffsetedArray array1, OffsetedArray array2) {
        // compare start index
        long dif = array1.getStartIndex() - array2.getStartIndex();

        if (dif != 0) {
            return ((int) dif);
        } else {
            return array1.hashCode() - array2.hashCode();
        }
    }
}
