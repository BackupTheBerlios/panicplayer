// ----------------------------------------------------------------------------
// [b12] Java Source File: MixEffect.java
//                created: 29.10.2003
//              $Revision: 1.1 $
// ----------------------------------------------------------------------------
package b12.panik.player;

import java.util.Collection;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.media.*;
import javax.media.format.AudioFormat;

import b12.panik.util.TrackComparator;

/**
 * This is the mixing effect. This class is used to mix different tracks. The
 * background track is the input parameter for the method.
 * {@linkplain #process(Buffer, Buffer)}. The other tracks have to be set from
 * the outside, so that the <code>MixEffect</code> is capable of modifying
 * the input buffer correctly.
 */
public class MixEffect implements Codec {

    // one main output format ... at a later stage there may be more
    private static final AudioFormat outputFormat = new AudioFormat("???");

    // one main input format ... later more
    private static final AudioFormat inputFormat = new AudioFormat("???");

    // the arrays for input and output formats
    private AudioFormat[] inputFormats = new AudioFormat[]{inputFormat};
    private AudioFormat[] outputFormats = new AudioFormat[]{outputFormat};

    /**
	 * Contains a sorted set of tracks that have to be mixed into the input.
	 * The list is sorted by the starting time of the tracks.
	 */
    SortedSet tracks;
    
    public MixEffect() {
        // should be sorted automatically
        tracks = new TreeSet(new TrackComparator());
    }
    

    /**
     * Adds a single input <code>Track</code> to the effect.
     * @param track the track to be added. 
     */
    void addInputTrack(Track track) {
        tracks.add(track);
    }
    
    /**
     * Adds a <code>Collection</code> of <code>Track</code> objects to the
     * effect.
     * @param newTracks the new <code>Track</code>s to be added.
     * @return <code>true</code> if this list changed as a result of the call.
     */
    boolean addInputTracks(Collection newTracks) {
        return tracks.addAll(newTracks);
    }
    
    //
    // Codec implementation methods
    //
    /** @see Codec#getSupportedInputFormats() */
    public Format[] getSupportedInputFormats() {
        return inputFormats;
    }

    /** @see Codec#getSupportedOutputFormats(Format) */
    public Format[] getSupportedOutputFormats(Format input) {
        return outputFormats;
    }

    /** @see Codec#setInputFormat(Format) */
    public Format setInputFormat(Format format) {
        // TODO Implement method
        return null;
    }

    /** @see Codec#setOutputFormat(Format) */
    public Format setOutputFormat(Format format) {
        // TODO Implement method
        return null;
    }

    /** @see Codec#process(Buffer, Buffer) */
    public int process(Buffer input, Buffer output) {
        // TODO Implement method
        // use input.getTimeStamp() to obtain the buffers position
        // add information from the tracks at this position up to the duration
        // input.getLength().
        return 0;
    }

    /** @see PlugIn#getName() */
    public String getName() {
        // TODO Implement method
        return null;
    }

    /** @see PlugIn#open() */
    public void open() throws ResourceUnavailableException {
        // TODO Implement method
        // see if sound is available, and if output on sound is possible
    }

    /** @see PlugIn#close() */
    public void close() {
        // TODO Implement method
    }

    /** @see PlugIn#reset() */
    public void reset() {
        // TODO Implement method
    }

    /** @see Controls#getControls() */
    public Object[] getControls() {
        // TODO Implement method
        return null;
    }

    /** @see Controls#getControl(String) */
    public Object getControl(String controlType) {
        // TODO Implement method
        return null;
    }

}