// ----------------------------------------------------------------------------
// [b12] Java Source File: TrackComparator.java
//                created: 10.01.2004
//              $Revision: 1.4 $
// ----------------------------------------------------------------------------
package b12.panik.io;

import java.io.File;
import java.net.URL;

import javax.media.Buffer;
import javax.media.Format;
import javax.media.Time;
import javax.media.Track;
import javax.media.TrackListener;

/**
 * @author Teo
 */
public class UrlTrack implements Track {
    private static final double NANOS_TO_MILLIS = 1.0E-6;
    private static final double MILLIS_TO_NANOS = 1.0E6;
    
    private URL url; //url des tracks
    private Time duration;
    //private Time startTime;
    private long startTime;
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
        //!!! the constructor Time need millisecond!!! 
        //startTime = new Time(wantedBegin );
        startTime=wantedBegin;
        //System.out.println("wanted begin: "+wantedBegin+" time applique en ns: "+startTime.getNanoseconds()+" idem en ms: "+startTime.getNanoseconds() * NANOS_TO_MILLIS);
        
    }

	public UrlTrack(File file, long wantedBegin) {
		try{
			//UrlTrack urlTrackTemp=
			this=new UrlTrack(file.toURL(),wantedBegin);
		} catch (Exception e) {
			System.out.println("unable to become url from file"); 
		}
        
	}

    /**
     * Returns the begin in milliseconds.
     * @return the time for the begin of this track.
     */
    public long getBegin() {
        //return (long) (startTime.getNanoseconds() * NANOS_TO_MILLIS);
        return startTime;
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
    	//!!! false: have to use getBegin()
        return new Time(startTime);
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