// ----------------------------------------------------------------------------// [b12] Java Source File: MixEffect.java//                created: 29.10.2003//              $Revision: 1.10 $// ----------------------------------------------------------------------------package b12.panik.player;import java.net.URL;import java.util.Collection;import java.util.Iterator;import java.util.Set;import java.util.SortedSet;import java.util.TreeSet;import javax.media.Buffer;import javax.media.Codec;import javax.media.Control;import javax.media.Format;import javax.media.ResourceUnavailableException;import javax.media.Track;import javax.media.format.AudioFormat;import b12.panik.io.UrlTrack;import b12.panik.util.Logging;import b12.panik.util.OffsetedArrayComparator;import b12.panik.util.TrackComparator;/** *  * This is the mixing effect. This class is used to mix different tracks. The *  * background track is the input parameter for the method. *  * {@linkplain #process(Buffer, Buffer)}. The other tracks have to be set from *  * the outside, so that the <code>MixEffect</code> is capable of modifying *  * the input buffer correctly. *   */public class MixEffect implements Codec {    // two audio formats    private static final AudioFormat[] FORMATS = new AudioFormat[]{            new AudioFormat(AudioFormat.LINEAR, 11025, 8, 1, AudioFormat.BIG_ENDIAN,                    AudioFormat.UNSIGNED),            new AudioFormat(AudioFormat.LINEAR, 8000, 8, 1, AudioFormat.BIG_ENDIAN,                    AudioFormat.UNSIGNED)};    // the arrays for input and output formats    /** input formats */    public static final AudioFormat[] FORMATS_INPUT = FORMATS;    /** output formats */    public static final AudioFormat[] FORMATS_OUTPUT = FORMATS;    private Format currentFormat;    private String effectName = "PanicPlayerEffect";            //teo    private SortedSet offsetedArrays;    private Set playedOffsetedArrays;    private SortedSet nonPlayedOffsetedArrays;    private OffsetedArray currentOffsetedArray;	private OffsetedArray currentOffsetedArrayToPlay;    private Set offsetedArraysToAdd;        private double timePer1000Byte; // time in ms pro 1000 byte        //private OffsetedArray currentOffsetedArrayToPlay;            private boolean initialisationRealized=false;    //private boolean currentTrack[]; /*   private SortedSet nonPlayedTracks;    private Set currentTracks;    private Set bufferToPlay; */    private long currentBeginIndex;    private long currentEndIndex; //   private Track actualTrack ;    Iterator iterator;        private Set urlTracks;        //fin teo                                /**     *      * Contains a sorted set of tracks that have to be mixed into the input.     *      * The list is sorted by the starting time of the tracks.     *       */    SortedSet tracks;    private byte[] currentData;    /**     *      * Creates a new instance of <code>MixEffect</code>.     *       */    public MixEffect() {        // should be sorted automatically        tracks = new TreeSet(new TrackComparator());        urlTracks=new TreeSet();    }    /**     *      * Adds a single input <code>Track</code> to the effect.     *      * @param track     *            the track to be added.     *       */    void addInputTrack(Track track) {        tracks.add(track);    }    	void addInputurlTrack(UrlTrack urlTrack) {			urlTracks.add(urlTrack);		}    /**     *      * Adds a <code>Collection</code> of <code>Track</code> objects to the     *      * effect.     *      * @param newTracks     *            the new <code>Track</code> s to be added.     *      * @return <code>true</code> if this list changed as a result of the     *         call.     *       */    boolean addInputTracks(Collection newTracks) {        return tracks.addAll(newTracks);        // return true;    }    //    // Codec implementation methods    //    /** @see Codec#getSupportedInputFormats() */    public Format[] getSupportedInputFormats() {        return FORMATS_INPUT;    }    /** @see Codec#getSupportedOutputFormats(Format) */    public Format[] getSupportedOutputFormats(Format input) {        return FORMATS_OUTPUT;    }    /** @see Codec#setInputFormat(Format) */    public Format setInputFormat(Format format) {        for (int i = 0; i < FORMATS.length; i++) {            if (FORMATS[i].matches(format)) {                currentFormat = format;                break;            }        }        Logging.fine("MixEffect: current input format set to " + currentFormat);        return currentFormat;    }    /** @see Codec#setOutputFormat(Format) */    public Format setOutputFormat(Format format) {        for (int i = 0; i < FORMATS.length; i++) {            if (FORMATS[i].matches(format)) {                currentFormat = format;                break;            }        }        Logging.fine("MixEffect: current output format set to " + currentFormat);        return currentFormat;    }    /** @see Codec#process(Buffer, Buffer) */    public int process(Buffer inputBuffer, Buffer outputBuffer) {        // TODO Implement method        // use input.getTimeStamp() to obtain the buffers position        // add information from the tracks at this position up to the duration        // input.getLength().                                System.out.println("processing(ing)");     /*   this.currentData = (byte[]) inputBuffer.getData();        outputBuffer.copy(inputBuffer);  */          		if(!initialisationRealized) {			//currentTrack=new boolean[tracks.size()];                        			setTimePer1000Byte(inputBuffer);            			//TODO: creer le offsetedArrays						//test			try{							URL urlAah=new URL("http://stud4.tuwien.ac.at/~e0326999/aaaa2.25.wav");				UrlTrack trackAah=new UrlTrack(urlAah,1000);				urlTracks.add(trackAah);			} catch (Exception e) {				System.out.println("pb d url");			}			//fin test				initialiseOffsetedArrays();            			currentBeginIndex=0;			playedOffsetedArrays= new TreeSet();			//nonPlayedOffsetedArrays=offsetedArrays			if(!(offsetedArrays.isEmpty())) {				//nonPlayedOffsetedArrays=offsetedArrays.subSet(offsetedArrays.first(), offsetedArrays.last());				//nonPlayedOffsetedArrays.add(offsetedArrays.last());				nonPlayedOffsetedArrays=offsetedArrays;			}			initialisationRealized=true;		}				        if( 	   ((((byte[]) inputBuffer.getData()) .length)==0)        		|| (offsetedArrays.isEmpty())  )  {        	outputBuffer.copy(inputBuffer);        	return BUFFER_PROCESSED_OK;        }                        currentEndIndex=currentBeginIndex+((byte[]) inputBuffer.getData()).length;                iterator=nonPlayedOffsetedArrays.iterator();        currentOffsetedArray=(OffsetedArray) iterator.next();        //TODO: les arrays devraient etre tries        while((currentOffsetedArray!=null)&& (currentOffsetedArray.getStartIndex()<=currentEndIndex)) {            playedOffsetedArrays.add(currentOffsetedArray);            nonPlayedOffsetedArrays.remove(currentOffsetedArray);            if(iterator.hasNext()) {               currentOffsetedArray=(OffsetedArray) iterator.next();            } else {				currentOffsetedArray=null;            }        }        offsetedArraysToAdd=new TreeSet(new OffsetedArrayComparator());        offsetedArraysToAdd.add(new OffsetedArray(inputBuffer,currentBeginIndex));              currentOffsetedArray=null;        for (Iterator i = playedOffsetedArrays.iterator(); i.hasNext(); ) {            currentOffsetedArray= (OffsetedArray) i.next();            currentOffsetedArrayToPlay=new OffsetedArray(currentOffsetedArray,currentBeginIndex,currentEndIndex);                                    //actualTrack.readFrame(actualBuffer);            if(currentOffsetedArrayToPlay.getDurationIndex()==0) {                   playedOffsetedArrays.remove(currentOffsetedArray);            } else {                                offsetedArraysToAdd.add(currentOffsetedArrayToPlay);            }        }                outputBuffer=addOffsetedArrays(offsetedArraysToAdd,(((byte[]) inputBuffer.getData()) .length));               //TODO: assure length of output                        return BUFFER_PROCESSED_OK;    }        public Buffer addOffsetedArrays(Set offsetedArraysSet,long length) {        OffsetedArray offsetedArrayResult=null;        for (Iterator iter = offsetedArraysSet.iterator(); iter.hasNext(); ) {			currentOffsetedArray = (OffsetedArray) iter.next();			offsetedArrayResult=OffsetedArray.addOffsetedArray(offsetedArrayResult,currentOffsetedArray);                    }        		offsetedArrayResult.setSizeTo(length);                           return offsetedArrayResult.toBuffer();    }        public void setTimePer1000Byte(Buffer buffer) {    	//double frameRate=((AudioFormat) (buffer.getFormat())).getFrameRate();    	//timePer1000Byte=((double) (1000*1000))/(frameRate);    	//sinon: duree du buffer input/longueur		//int nbByte=((byte[])buffer.getData()).length;		//long duration=buffer.getDuration();		//timePer1000Byte=((double) (1000*duration))/((double) nbByte);		//System.out.println("calcul: nbByte "+nbByte+" duration "+duration+" time "+timePer1000Byte);		//System.out.println("calcul: framerate "+frameRate+" time "+timePer1000Byte);		timePer1000Byte=45.35;    }    /** @see PlugIn#getName() */    public String getName() {        // TODO Implement method        return effectName;    }    /** @see PlugIn#open() */    public void open() throws ResourceUnavailableException {        // TODO Implement method        // see if sound is available, and if output on sound is possible    }    /** @see PlugIn#close() */    public void close() {        // TODO Implement method    }    /** @see PlugIn#reset() */    public void reset() {        // TODO Implement method    }    /** @see Controls#getControls() */    public Object[] getControls() {        // TODO Implement method        return new Control[0];    }    /** @see Controls#getControl(String) */    public Object getControl(String controlType) {        // TODO Implement method        try {            Class cls = Class.forName(controlType);            Object cs[] = getControls();            for (int i = 0; i < cs.length; i++) {                if (cls.isInstance(cs[i]))                    return cs[i];            }            return null;        } catch (Exception e) { // no such controlType or such control            return null;        }    }    public byte[] currentData() {        return currentData;    }        public void initialiseOffsetedArrays() {    	//TODO: implement		offsetedArrays = new TreeSet(new OffsetedArrayComparator());    	    	for (Iterator iter = urlTracks.iterator(); iter.hasNext();) {			UrlTrack currentUrlTrack = (UrlTrack) iter.next();			offsetedArrays.add(new OffsetedArray(currentUrlTrack,timePer1000Byte));		}    	    	    	    }   }
