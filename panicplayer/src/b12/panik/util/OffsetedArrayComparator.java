// ----------------------------------------------------------------------------

// [b12] Java Source File: OffsetedArrayComparator.java

//                created: 29.10.2003

//              $Revision: 1.1 $

// ----------------------------------------------------------------------------

package b12.panik.util;



import java.util.Comparator;

import b12.panik.player.OffsetedArray;







/**

 * A <code>Comparator</code> that compares <code>Track</code>s.

 * @author olivier

 */

public class OffsetedArrayComparator implements Comparator {



   // private static final TimeComparator tComp = new TimeComparator();



    /** @see Comparator#compare(Object, Object) */

    public int compare(Object o1, Object o2) {

        return compare((OffsetedArray) o1, (OffsetedArray) o2);

    }



    /**

     * Compares two <code>Track</code> objects.

     *

     * @param track1 the first track.

     * @param track2 the second track-

     * @return the difference in start time and duration. If these are not

     *          different, their difference in hash code is returned.

     */

    public int compare(OffsetedArray array1, OffsetedArray array2) {

        // compare start times

        long dif=array1.getStartIndex()-array2.getStartIndex();
        
        if(dif!=0) {
        	return ((int) dif);
        } else {
        	return array1.hashCode()-array2.hashCode();
        }
    }
}

