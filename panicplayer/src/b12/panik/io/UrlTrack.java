// ----------------------------------------------------------------------------
// [b12] Java Source File: TrackComparator.java
//                created: 10.01.2004
//              $Revision: 1.3 $
// ----------------------------------------------------------------------------
package b12.panik.io;

import java.net.URL;

import javax.media.*;

/**
 * @author Teo
 */
public class UrlTrack implements Track {
    private static final double NANOS_TO_MILLIS = 1.0E-6;
    private static final double MILLIS_TO_NANOS = 1.0E6;
    
    private URL url; //url des tracks
    private Time duration;
    private Time startTime;
    private boolean enabled;
    private TrackPropertyListener listener;

    /**
     * Creates a new instance of <code>UrlTrack</code>.
     * @param wantedUrl the url for this track.
     * @param wantedBegin the begin in milli seconds.
     */
    public UrlTrack(URL wantedUrl, long wantedBegin) {
        url = wantedUrl;
        // convert from millis to nanos
        startTime = new Time(wantedBegin * MILLIS_TO_NANOS);
    }

    /**
     * Returns the begin in milliseconds.
     * @return the time for the begin of this track.
     */
    public long getBegin() {
        return (long) (startTime.getNanoseconds() * NANOS_TO_MILLIS);
    }

    /**
     * Returns the URL associated with this track.
     * @return the url for this track.
     */
    public URL getUrl() {
        return url;
    }

    /**
     * Returns the exact start time.
     * @return the start time.
     */
    public Time getStartTime() {
        return startTime;
    }

    /**
     * Returns the exact duration.
     * @return the duration.
     */
    public Time getDuration() {
        return duration;
    }

    /** @see javax.media.Track#setEnabled(boolean) */
    public void setEnabled(boolean t) {
        if (t != this.enabled) {
            this.enabled = t;
            if (listener != null) {
                listener.stateHasChanged(this);
            }
        }
    }

    /** @see javax.media.Track#isEnabled() */
    public boolean isEnabled() {
        return enabled;
    }

    /** @see javax.media.Track#setTrackListener(javax.media.TrackListener) */
    public void setTrackListener(final TrackListener listener) {
        if (listener == null) {
            return;
        }
        if (listener instanceof TrackPropertyListener) {
            this.listener = (TrackPropertyListener) listener;
        } else {
            this.listener = new TrackPropertyListener() {
                /** @see TrackPropertyListener#readHasBlocked(Track) */
                public void readHasBlocked(Track t) {
                    listener.readHasBlocked(t);
                }
            };
        }
    }

    /*
     * 
     *  unimplemented methods, needed for interface
     *
     */

    /** @see javax.media.Track#getFormat() */
    public Format getFormat() {
        // TODO implement if needed
        return null;
    }

    /** @see javax.media.Track#readFrame(javax.media.Buffer) */
    public void readFrame(Buffer buffer) {
        // TODO implement if needed.
    }

    /** @see javax.media.Track#mapTimeToFrame(javax.media.Time) */
    public int mapTimeToFrame(Time t) {
        // TODO implement if needed.
        return 0;
    }

    /** @see javax.media.Track#mapFrameToTime(int) */
    public Time mapFrameToTime(int frameNumber) {
        // TODO implement if needed.
        return null;
    }
}