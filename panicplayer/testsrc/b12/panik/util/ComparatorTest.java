// ----------------------------------------------------------------------------
// [b12] Java Source File: ComparatorTest.java
//                created: 29.10.2003
//              $Revision: 1.2 $
// ----------------------------------------------------------------------------
package b12.panik.util;

import java.util.*;

import javax.media.*;

import junit.framework.TestCase;
import b12.panik.util.TimeComparator;
import b12.panik.util.TrackComparator;

/**
 * Tests the <code>Comparator</code> classes contained in the package <i>
 * b12.panik.util</i>.
 */
public class ComparatorTest extends TestCase {

    private static final int NB_TEST_UNITS = 100;

    private Random random;
    
    /** @see junit.framework.TestCase#setUp() */
    protected void setUp() throws Exception {
        random = new Random();
    }
    
    /** Test for time comparator. */
    public final void testTimeComparator() {
        Time[] times = new Time[NB_TEST_UNITS];
        for (int i = 0; i < times.length; i++) {
            times[i] = new Time(i * 3);
        }

        // see if array is ordered
        Time tmpTime = times[0];
        for (int i = 1; i < times.length; i++) {
            assertTrue(tmpTime.getNanoseconds() < times[i].getNanoseconds());
            tmpTime = times[i];
        }

        // shuffle
        List timesList = Arrays.asList(times);
        // shuffle collection
        Collections.shuffle(timesList);
        // sort with TimeComparator
        Collections.sort(timesList, new TimeComparator());
        // retrieve array
        Time[] shuffledTimes = (Time[]) timesList.toArray(
                new Time[NB_TEST_UNITS]);

        // see if array is ordered
        tmpTime = shuffledTimes[0];
        for (int i = 1; i < shuffledTimes.length; i++) {
            assertTrue(tmpTime.getNanoseconds() < shuffledTimes[i]
                    .getNanoseconds());
            tmpTime = shuffledTimes[i];
        }
    }

    /** Test for track comparator. */
    public final void testTrackComparator() {
        Track[] tracks = new Track[NB_TEST_UNITS];
        int maxStart = NB_TEST_UNITS / 2;
        int maxDuration = NB_TEST_UNITS;

        int start, duration, durationCandidate;
        for (int i = 0; i < tracks.length; i++) {
            start = random.nextInt(maxStart);
            durationCandidate = random.nextInt(maxDuration);
            duration = Math.min(durationCandidate, maxDuration);
            tracks[i] = createEmptyTrack(start, duration);
        }

        // shuffle tracks, to be sure that they cannot be ordered
        List tracksList = Arrays.asList(tracks);
        Collections.shuffle(tracksList);
        /* commented out output
        System.out.println("shuffled tracks:");
        for (Iterator i = tracksList.iterator(); i.hasNext();) {
            System.out.println(i.next());
        }
        */
        
        // order collection and convert to array
        Collections.sort(tracksList, new TrackComparator());
        Track[] shuffledTracks = (Track[]) tracksList.toArray(
                new Track[NB_TEST_UNITS]);

        // see if array is ordered
        Track tmpTrack = shuffledTracks[0];
        for (int i = 1; i < shuffledTracks.length; i++) {
            // start and duration for tmpTrack
            long tmpStart = tmpTrack.getStartTime().getNanoseconds();
            long tmpDuration = tmpTrack.getDuration().getNanoseconds();
            // start and duration for currentTrack
            long curStart = shuffledTracks[i].getStartTime().getNanoseconds();
            long curDuration = shuffledTracks[i].getDuration().getNanoseconds();

            // start time should be smaller or equal
            boolean startTimeSmaller = tmpStart < curStart;
            if (!startTimeSmaller) {
                assertFalse(tmpStart > curStart);
                // if start times are equal ...
                // duration should be longer or equal
                boolean durationLonger = tmpDuration < curDuration;
                if (!durationLonger) {
                    assertFalse(tmpDuration > curDuration);
                }
            }
            tmpTrack = shuffledTracks[i];
        }
        
        /* commented out output
        System.out.println("ordered tracks:");
        for (int i = 0; i < shuffledTracks.length; i++) {
            System.out.println(shuffledTracks[i]);
        }
        */
    }
    
    private Track createEmptyTrack(final long start, final long duration) {
        return new Track() {
            // only getStartTime() and getDuration() are implemented
            
            public Time getStartTime() {
                return new Time(start);
            }

            public Time getDuration() {
                return new Time(duration);
            }
            
            /** @see java.lang.Object#toString() */
            public String toString() {
                // returns a visual representation of start and duration (ascii)
                StringBuffer buf = new StringBuffer("Track: ");
                for (int i = 0; i < start; i++) {
                    buf.append(' ');
                }
                for (int i = 0; i < duration; i++) {
                    buf.append('~');
                }
                return buf.toString();
            }
            
            //
            // for interface implementation purposes
            //
            
            public Format getFormat() {
                return null;
            }
            public void setEnabled(boolean t) {
                // empty
            }
            public boolean isEnabled() {
                return false;
            }
            public void readFrame(Buffer buffer) {
                // empty
            }
            public int mapTimeToFrame(Time t) {
                return 0;
            }
            public Time mapFrameToTime(int frameNumber) {
                return null;
            }
            public void setTrackListener(TrackListener listener) {
                // empty
            }
        };
    }
}