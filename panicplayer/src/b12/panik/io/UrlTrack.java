// ----------------------------------------------------------------------------
// [b12] Java Source File: UrlTrack.java
//                created: 10.01.2004
//              $Revision: 1.9 $
// ----------------------------------------------------------------------------
package b12.panik.io;

import java.io.File;
import java.io.Serializable;
import java.net.URI;

import javax.media.*;
import javax.media.format.AudioFormat;

/**
 * @author Teo
 */
public class UrlTrack implements Track, Serializable {
    private static final double MILLIS_TO_SECONDS = 1.0E-3;

    //tracks url
    private URI uri;
    private Time duration;
    private Time start;
    private AudioFormat format;

    //start time in nanosecond
    private long startTime;
    private boolean enabled;
    private TrackPropertyListener listener;

    /**
     * Creates a new instance of <code>UrlTrack</code>.
     * @param wantedUrl the url for this track.
     * @param wantedBegin the begin in milli seconds.
     * @param enabled whether this track is enabled.
     */
    public UrlTrack(URI wantedUrl, long wantedBegin, boolean enabled) {
        this.enabled = enabled;
        uri = wantedUrl;
        // convert from millis to nanos
        startTime = wantedBegin;
        start = new Time(wantedBegin * MILLIS_TO_SECONDS);
        duration = new Time(0);
        // by default enabled
        enabled = true;
        format = new AudioFormat("no format");
    }

    /**
     * Creates a new instance of <code>UrlTrack</code>.
     * @param wantedUrl the url for this track.
     * @param wantedBegin the begin in milli seconds.
     */
    public UrlTrack(URI wantedUrl, long wantedBegin) {
        this(wantedUrl, wantedBegin, true);
    }

    /**
     * Creates a new instance of <code>UrlTrack</code>.
     * @param file the File for this track.
     * @param wantedBegin the begin in milli seconds.
     */
    public UrlTrack(File file, long wantedBegin) {
        this(file.toURI(), wantedBegin);
    }

    /**
     * Creates a new instance of <code>UrlTrack</code>.
     * @param uri the url.
     * @param start the start in milliseconds.
     * @param seconds the duration in seconds.
     * @param format the format.
     */
    public UrlTrack(URI uri, int start, double seconds, javax.sound.sampled.AudioFormat format) {
        this(uri, start);
        this.format = new AudioFormat(format.getEncoding().toString(), format.getSampleRate(),
                format.getSampleSizeInBits(), format.getChannels());
        duration = new Time(seconds);
    }

    /**
     * Returns the begin in milliseconds.
     * @return the time for the begin of this track.
     */
    public long getBegin() {
        return startTime;
    }

    /**
     * Returns the URL associated with this track.
     * @return the url for this track.
     *
    public URL getUrl() {
        return url;
    }*/

    public URI getUri() {
        return uri;
    }

    /**
     * Returns the exact duration.
     * @return the duration.
     */
    public Time getDuration() {
        return duration;
    }

    /** @see javax.media.Track#setEnabled(boolean) */
    public void setEnabled(boolean enabled) {
        if (enabled != this.enabled) {
            this.enabled = enabled;
            if (listener != null) {
                listener.propertyChanged(this, TrackPropertyListener.PROP_ENABLED, Boolean.valueOf(!enabled), Boolean.valueOf(enabled));
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
            this.listener = new TrackPropertyAdapter() {
                /** @see TrackPropertyAdapter#readHasBlocked(Track) */
                public void readHasBlocked(Track t) {
                    listener.readHasBlocked(t);
                }
            };
        }
    }

    /**
     * Sets the start time for this track.
     * @param seconds the new start time.
     */
    public void setStartTime(double seconds) {
        if (seconds >= 0) {
            startTime = (long) seconds * 1000;
            start = new Time(seconds);
        }
    }

    /** @see javax.media.Track#getFormat() */
    public Format getFormat() {
        return format;
    }

    /*
     * 
     *  unimplemented methods, needed for interface
     *
     */

    /**
     * Returns nothing.
     * @return nothing.
     */
    public Time getStartTime() {
        return start;
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