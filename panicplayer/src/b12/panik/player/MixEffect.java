// ----------------------------------------------------------------------------
// [b12] Java Source File: MixEffect.java
//                created: 29.10.2003
//              $Revision: 1.14 $
// ----------------------------------------------------------------------------
package b12.panik.player;

import java.util.*;

import javax.media.*;
import javax.media.format.AudioFormat;

import b12.panik.io.UrlTrack;
import b12.panik.util.*;

/**
 * This is the mixing effect. This class is used to mix different tracks. The
 * background track is the input parameter for the method.
 * {@linkplain #process(Buffer, Buffer)}. The other tracks have to be set from
 * the outside, so that the <code>MixEffect</code> is capable of modifying
 * the input buffer correctly.
 */
public class MixEffect implements Codec {
    // two audio formats
    private static final AudioFormat[] FORMATS = new AudioFormat[]{
            new AudioFormat(AudioFormat.LINEAR, 11025, 8, 1, AudioFormat.BIG_ENDIAN,
                    AudioFormat.UNSIGNED),
            new AudioFormat(AudioFormat.LINEAR, 8000, 8, 1, AudioFormat.BIG_ENDIAN,
                    AudioFormat.UNSIGNED)};
    // the arrays for input and output formats
    /** input formats */
    public static final AudioFormat[] FORMATS_INPUT = FORMATS;
    /** output formats */
    public static final AudioFormat[] FORMATS_OUTPUT = FORMATS;

    private Format currentFormat;
    private String effectName = "PanicPlayerEffect";

    //the number of tracks that can be played simultaneous
    private int maxConcurrentTracks = 4;

    private SortedSet offsetedArrays;
    private Set playedOffsetedArrays;
    private SortedSet nonPlayedOffsetedArrays;
    private OffsetedArray currentOffsetedArray;
    private OffsetedArray currentOffsetedArrayToPlay;
    private Set offsetedArraysToAdd;
    private SortedSet tempNonPlayedOffsetedArrays;
    private SortedSet tempPlayedOffsetedArrays;

    //	time in ms pro 1000 byte
    private double timePer1000Byte;

    private boolean initialisationRealized = false;

    private long currentBeginIndex;
    private long currentEndIndex;
    private Iterator iterator;

    private Set urlTracks;

    private byte[] currentData;

    /**
     * Creates a new instance of <code>MixEffect</code>.
     */
    public MixEffect() {
        urlTracks = new TreeSet(new TrackComparator());
    }

    /**
     * Set the number of maximal simultaneous tracks.
     * @param nb the desired number. 
     */
    public void setMaxConcurrentTracks(int nb) {
        maxConcurrentTracks = nb;
    }

    /**
     * Return the number of maximal simultaneous tracks.
     * @return the number of simultaneous tracks.
     */
    public int getMaxConcurrentTracks() {
        return maxConcurrentTracks;
    }

    /**
     * Adds a single input <code>UrlTrack</code> to the effect.
     * @param urlTrack the UrlTrack to be added.
     */
    void addInputurlTrack(UrlTrack urlTrack) throws ConstraintsException {
        urlTracks.add(urlTrack);
    }

    //
    // Codec implementation methods
    //
    /** @see Codec#getSupportedInputFormats() */
    public Format[] getSupportedInputFormats() {
        return FORMATS_INPUT;
    }

    /** @see Codec#getSupportedOutputFormats(Format) */
    public Format[] getSupportedOutputFormats(Format input) {
        return FORMATS_OUTPUT;
    }

    /** @see Codec#setInputFormat(Format) */
    public Format setInputFormat(Format format) {
        for (int i = 0; i < FORMATS.length; i++) {
            if (FORMATS[i].matches(format)) {
                currentFormat = format;
                break;
            }
        }
        Logging.fine("MixEffect: current input format set to " + currentFormat);
        return currentFormat;
    }

    /** @see Codec#setOutputFormat(Format) */
    public Format setOutputFormat(Format format) {
        for (int i = 0; i < FORMATS.length; i++) {
            if (FORMATS[i].matches(format)) {
                currentFormat = format;
                break;
            }
        }
        Logging.fine("MixEffect: current output format set to " + currentFormat);
        return currentFormat;
    }

    /** @see Codec#process(Buffer, Buffer) */
    public int process(Buffer inputBuffer, Buffer outputBuffer) {
        System.out.println("...");
        int inputLength = (inputBuffer.getLength());
        if (!initialisationRealized) {
            setTimePer1000Byte();

            //test
            /*	try {
             URL urlAah =
             new java.net.URL(
             "file:///E:/boulot/multimedia1/1°abgabe/panicplayer/src/aaaa2.25.11k.wav");
             URL urlOoh =
             new java.net.URL(
             "file:///E:/boulot/multimedia1/1°abgabe/panicplayer/src/oooo2.5.11k.wav");
             URL urlIih =
             new java.net.URL(
             "file:///E:/boulot/multimedia1/1°abgabe/panicplayer/src/iiii2.5.11k.wav");

             UrlTrack trackAah = new UrlTrack(urlAah, 500);
             urlTracks.add(trackAah);
             UrlTrack trackOoh = new UrlTrack(urlOoh, 3000);
             urlTracks.add(trackOoh);
             UrlTrack trackIih = new UrlTrack(urlIih, 6000);
             urlTracks.add(trackIih);
             UrlTrack trackOoh2=new UrlTrack(urlOoh,2000);
             urlTracks.add(trackOoh2);
             UrlTrack trackIih2 = new UrlTrack(urlIih, 1000);
             urlTracks.add(trackIih2);
             } catch (Exception e) {
             System.out.println("********pb d url*************");
             } */
            //fin test
            initialiseOffsetedArrays();
            currentBeginIndex = -inputLength;
            playedOffsetedArrays = new TreeSet(new OffsetedArrayComparator());
            if (!(offsetedArrays.isEmpty())) {
                nonPlayedOffsetedArrays = new TreeSet(offsetedArrays);
            }
            initialisationRealized = true;
        }

        if ((inputLength == 0) || (offsetedArrays.isEmpty())) {
            outputBuffer.copy(inputBuffer);
            return BUFFER_PROCESSED_OK;
        }

        currentBeginIndex += inputLength;
        currentEndIndex = currentBeginIndex + inputLength;

        if (!(nonPlayedOffsetedArrays.isEmpty())) {
            iterator = nonPlayedOffsetedArrays.iterator();
            currentOffsetedArray = (OffsetedArray) iterator.next();
        } else {
            currentOffsetedArray = null;
        }

        tempNonPlayedOffsetedArrays = new TreeSet(nonPlayedOffsetedArrays);

        while ((currentOffsetedArray != null)
                && (currentOffsetedArray.getStartIndex() <= currentEndIndex)) {
            playedOffsetedArrays.add(currentOffsetedArray);
            tempNonPlayedOffsetedArrays.remove(currentOffsetedArray);
            if (iterator.hasNext()) {
                currentOffsetedArray = (OffsetedArray) iterator.next();
            } else {
                currentOffsetedArray = null;
            }
        }

        nonPlayedOffsetedArrays = new TreeSet(tempNonPlayedOffsetedArrays);
        offsetedArraysToAdd = new TreeSet(new OffsetedArrayComparator());
        offsetedArraysToAdd.add(new OffsetedArray(inputBuffer, currentBeginIndex,
                maxConcurrentTracks));

        tempPlayedOffsetedArrays = new TreeSet(new OffsetedArrayComparator());
        tempPlayedOffsetedArrays.addAll(playedOffsetedArrays);
        currentOffsetedArray = null;
        for (Iterator i = playedOffsetedArrays.iterator(); i.hasNext(); ) {
            currentOffsetedArray = (OffsetedArray) i.next();
            currentOffsetedArrayToPlay = new OffsetedArray(currentOffsetedArray,
                    currentBeginIndex, currentEndIndex);

            if (currentOffsetedArrayToPlay.getDurationIndex() == 0) {
                tempPlayedOffsetedArrays.remove(currentOffsetedArray);
            } else {
                offsetedArraysToAdd.add(currentOffsetedArrayToPlay);
            }
        }
        playedOffsetedArrays = new TreeSet(new OffsetedArrayComparator());
        playedOffsetedArrays.addAll(tempPlayedOffsetedArrays);

        OffsetedArray outputArray = addOffsetedArrays(offsetedArraysToAdd,
                (((byte[]) inputBuffer.getData()).length));

        outputBuffer.copy(inputBuffer);

        outputArray.putData(outputBuffer);
        return BUFFER_PROCESSED_OK;
    }

    OffsetedArray addOffsetedArrays(Set offsetedArraysSet, long length) {
        OffsetedArray offsetedArrayResult = null;
        int i = 1;
        for (Iterator iter = offsetedArraysSet.iterator(); iter.hasNext(); ) {
            currentOffsetedArray = (OffsetedArray) iter.next();
            offsetedArrayResult = OffsetedArray.addOffsetedArray(offsetedArrayResult,
                    currentOffsetedArray);
            i++;
        }
        offsetedArrayResult.setSizeTo(length);
        return offsetedArrayResult;
    }

    /**
     * Set the time in nanosecond per 1000 byte.
     */
    public void setTimePer1000Byte() {
        timePer1000Byte = 90.70;
    }

    /**
     * Display a sampler (1 byte on 800) of a buffer.
     * @param buffer the buffer to be displayeded.
     */
    public void printShortedBuffer(Buffer buffer) {
        byte[] dataIn = ((byte[]) buffer.getData());
        int length = dataIn.length;
        for (int i = 0; i < length; i = i + 800) {
            System.out.print(dataIn[i] + " ");
        }
        System.out.println("");
    }

    /** @see PlugIn#getName() */
    public String getName() {
        // TODO Implement method
        return effectName;
    }

    /** @see PlugIn#open() */
    public void open() {
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
        return new Control[0];
    }

    /** @see Controls#getControl(String) */
    public Object getControl(String controlType) {
        // TODO Implement method
        try {
            Class cls = Class.forName(controlType);
            Object cs[] = getControls();
            for (int i = 0; i < cs.length; i++) {
                if (cls.isInstance(cs[i]))
                    return cs[i];
            }
            return null;
        } catch (Exception e) { // no such controlType or such control
            return null;
        }
    }

    /**
     * Returns the current data.
     * @return the currently processed data.
     */
    public byte[] currentData() {
        return currentData;
    }

    /** Initialise the OffsetedArrays by reading the urlTracks. */
    public void initialiseOffsetedArrays() {
        offsetedArrays = new TreeSet(new OffsetedArrayComparator());
        for (Iterator iter = urlTracks.iterator(); iter.hasNext(); ) {
            UrlTrack currentUrlTrack = (UrlTrack) iter.next();
            offsetedArrays.add(new OffsetedArray(currentUrlTrack, timePer1000Byte,
                    maxConcurrentTracks));
        }
    }
}
