// ----------------------------------------------------------------------------
// [b12] Java Source File: MixEffect.java
//                created: 29.10.2003
//              $Revision: 1.5 $
// ----------------------------------------------------------------------------
package b12.panik.player;

import java.util.Collection;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.media.*;
import javax.media.format.AudioFormat;

import b12.panik.util.TrackComparator;

import com.sun.media.ExtBuffer;

/**
 * This is the mixing effect. This class is used to mix different tracks. The
 * background track is the input parameter for the method.
 * {@linkplain #process(Buffer, Buffer)}. The other tracks have to be set from
 * the outside, so that the <code>MixEffect</code> is capable of modifying
 * the input buffer correctly.
 */
public class MixEffect implements Codec {

    // one main output format ... at a later stage there may be more
    
     private static final AudioFormat outputFormat = 
             new AudioFormat(AudioFormat.ULAW, 8000, 8, Format.NOT_SPECIFIED,
                     AudioFormat.NOT_SPECIFIED, AudioFormat.UNSIGNED, 8, 8000, Format.byteArray
             //teo
    //private static AudioFormat outputFormat ; /*= new AudioFormat(  
     //fin teo      
    /* AudioFormat.LINEAR,
     Format.NOT_SPECIFIED,
     16,
     Format.NOT_SPECIFIED,
     AudioFormat.BIG_ENDIAN,
     AudioFormat.SIGNED,
     16,
     Format.NOT_SPECIFIED,
     Format.byteArray  */
     
   
    );

    // one main input format ... later more
    //private static final AudioFormat inputFormat = new AudioFormat(
    //teo
    private static final AudioFormat inputFormat =
        new AudioFormat(AudioFormat.ULAW, 8000, 8, Format.NOT_SPECIFIED,
                AudioFormat.NOT_SPECIFIED, AudioFormat.UNSIGNED, 8, 8000, Format.byteArray);
                
     /*= new AudioFormat(
     //fin teo
     AudioFormat.LINEAR,
     Format.NOT_SPECIFIED,
     16,
     Format.NOT_SPECIFIED,
     AudioFormat.BIG_ENDIAN,
     AudioFormat.SIGNED,
     16,
     Format.NOT_SPECIFIED,
     Format.byteArray  
     
     
     
     /* AudioFormat.LINEAR,
     Format.NOT_SPECIFIED,
     8,
     Format.NOT_SPECIFIED,
     AudioFormat.NOT_SPECIFIED,
     AudioFormat.UNSIGNED,
     8,
     Format.NOT_SPECIFIED,
     Format.byteArray */
    
    //);

    // the arrays for input and output formats
    /** input formats */
    public static AudioFormat[] FORMATS_INPUT = new AudioFormat[]{inputFormat};
    /** output formats */
    public static AudioFormat[] FORMATS_OUTPUT = new AudioFormat[]{outputFormat};
    
    private String effectName="PanicPlayerEffect";

    /**
     * Contains a sorted set of tracks that have to be mixed into the input.
     * The list is sorted by the starting time of the tracks.
     */
    SortedSet tracks;
    
    /**
     * Creates a new instance of <code>MixEffect</code>.
     */
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
        // return true;
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
        // TODO Implement method
        /* inputFormat = (AudioFormat)format; */
        
        //teo
        //inputFormat=(AudioFormat)format;
        //AudioFormat[] FORMATS_INPUT = new AudioFormat[]{inputFormat};
        //fin teo
        
        return inputFormat;
    }
    
    //teo
    public Format getInputFormat() {
        return inputFormat;
    }
    //fin teo

    /** @see Codec#setOutputFormat(Format) */
    public Format setOutputFormat(Format format) {
        // TODO Implement method
        // outputFormat = (AudioFormat)format;

        //teo
        //outputFormat=(AudioFormat)format;
        //AudioFormat[] FORMATS_OUTPUT = new AudioFormat[]{outputFormat};
        //fin teo

        return outputFormat;
    }

    /** @see Codec#process(Buffer, Buffer) */
    public int process(Buffer inputBuffer, Buffer outputBuffer) {
        // TODO Implement method
        // use input.getTimeStamp() to obtain the buffers position
        // add information from the tracks at this position up to the duration
        // input.getLength().
        System.out.println("performing process");
       
        
        // ---- begin default implementation
        //output = (Buffer) input.clone();
        //output = input;
        //output=new Buffer();
        
        // == prolog
        byte[] inData = (byte[])inputBuffer.getData();
        int inLength = inputBuffer.getLength();
        if(inLength!=0)  {
            printBuffer(inputBuffer);
        int inOffset = inputBuffer.getOffset();

        
        byte[] outData = validateByteArraySize(outputBuffer, inLength);
        int outOffset = outputBuffer.getOffset();
        
        int samplesNumber = inLength / 2 ;
        
        // == main
        
        for (int i=0; i< samplesNumber;i++) {
            int tempL = inData[inOffset ++];
            int tempH = inData[inOffset ++];
            int sample = tempH | (tempL & 255);
            
            sample = (int)(sample * 2.0F);
            
            if (sample>32767) // saturate
                sample = 32767;
            else if (sample < -32768)
                sample = -32768;
            
            outData[outOffset ++]=(byte) (sample &  255);
            outData[outOffset ++]=(byte) (sample >> 8);
            
        }
        updateOutput(outputBuffer,outputFormat, samplesNumber, 0);
        System.out.println("buffer out:");
        printBuffer(outputBuffer);
        }
        else
        {
            System.out.println("buffer in vide");
        }
        
        System.out.println("end process");
        // ---- end default implementation
        return BUFFER_PROCESSED_OK;
    }
    
    
    protected byte[] validateByteArraySize(Buffer buffer,int newSize) {
        Object objectArray=buffer.getData();
        byte[] typedArray;
        if (objectArray instanceof byte[]) { // is correct type AND not null
            typedArray=(byte[])objectArray;
            if (typedArray.length >= newSize ) { // is sufficient capacity
                return typedArray;
            }
        }
        System.out.println(getClass().getName()+
                " : allocating byte["+newSize+"] ");
        typedArray = new byte[newSize];
        buffer.setData(typedArray);
        return typedArray;
    }
    
    
    protected void updateOutput(Buffer outputBuffer,
            Format format,int length, int offset) {

        
        outputBuffer.setFormat(format);
        outputBuffer.setLength(length);
        outputBuffer.setOffset(offset);
    }

    
    
    
    /**
     * @param input
     */
    private void printBuffer(Buffer input) {
        Object inputData = input.getData();
        if (inputData instanceof byte[]) {
            byte[] data = (byte[]) inputData;
            for (int i = 0; i < data.length; i++) {
                System.out.print(data[i]);
            }
            System.out.println();
        } else {
            System.out.println(input);
            ExtBuffer buffer = (ExtBuffer) input;
            System.out.println(buffer.getData().getClass());
            System.out.println(buffer.getLength());
        }
    }



    /** @see PlugIn#getName() */
    public String getName() {
        // TODO Implement method
        return effectName;
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

}