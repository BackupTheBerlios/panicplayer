// ----------------------------------------------------------------------------
// [b12] Java Source File: OffsetedArray.java
//                created: 09.01.2004
//              $Revision: 1.9 $
// ----------------------------------------------------------------------------
package b12.panik.player;

import java.io.File;

import javax.media.Buffer;
import javax.media.format.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

import b12.panik.io.IOUtils;
import b12.panik.io.UrlTrack;

/**
 * A array of byte with a start index.
 * @author olivier
 */
public class OffsetedArray {
    private long startIndex;
    private byte[] content;
    String name;

    /**
     * Creates a new instance of <code>OffsetedArray</code> from a buffer.
     * @param buffer the source buffer.
     * @param beginIndex the startIndex in byte.
     * @param signedToUnsigned whether to convert signet to unsigned.
     */
    public OffsetedArray(Buffer buffer, long beginIndex, boolean signedToUnsigned) {
        startIndex = beginIndex;
        final byte[] inputData = (byte[]) buffer.getData();
        final int bufferLength = buffer.getLength();
        final int offset = buffer.getOffset();

        content = new byte[bufferLength];

        // quick array copy
        System.arraycopy(inputData, offset, content, 0, bufferLength);

        if (signedToUnsigned) {
            for (int i = 0; i < content.length; i++) {
                // conversion
                content[i] = (byte) (content[i] + 128);
            }
        }
    }

    /** Creates a new null instance of <code>OffsetedArray</code>. */
    private OffsetedArray() {
        // empty constructor to be called from within class.
    }

    /**
     * Creates a new instance of <code>OffsetedArray</code> as a copy of a given OffsetedArray.
     * @param original the OffsetedArray to be copied.
     */
    public OffsetedArray(OffsetedArray original) {
        startIndex = original.getStartIndex();
        content = new byte[((int) original.getDurationIndex())];
        for (int i = 0; i < original.getDurationIndex(); i++) {
            content[i] = original.getByteI(i);
        }
    }

    /**
     * Creates a new instance of <code>OffsetedArray</code> from an other OffsetedArray.
     * @param source the source OffsetedArray.
     * @param beginIndex the startIndex in byte.
     * @param endIndex the endIndex in byte.
     */
    public OffsetedArray(OffsetedArray source, long beginIndex, long endIndex) {
        startIndex = beginIndex;
        int length = ((int) (endIndex - beginIndex));
        content = new byte[length];
        int j;

        if ((beginIndex > (source.getStartIndex() + source.getDurationIndex()))
                || (endIndex < source.getStartIndex())) {
            content = new byte[0];
            return;
        }

        for (int i = 0; i < length; i++) {
            j = i + ((int) beginIndex) - ((int) source.getStartIndex());
            if (j < 0) {
                content[i] = byteZero();
            } else {
                if (j >= source.getDurationIndex()) {
                    content[i] = byteZero();
                } else {
                    content[i] = source.getByteI(j);
                }
            }
        }
    }

    /**
     * Creates a new instance of <code>OffsetedArray</code> from a urlTracks.
     * @param urlTrack the source UrlTrack.
     * @param timePer1000Bytes the time in ms for 1000 byte.
     */
    public OffsetedArray(UrlTrack urlTrack, double timePer1000Bytes) {
        AudioFormat trackFormat = (AudioFormat) urlTrack.getFormat();
        System.out.println("time per 1000 bytes: " + timePer1000Bytes);
        if (trackFormat != null) {
            int frameSize = trackFormat.getFrameSizeInBits();
            double frameRate = trackFormat.getFrameRate();
            if (frameSize > 0 && frameRate > 0) {
                timePer1000Bytes = IOUtils.timePer1000Byte(frameSize, frameRate);
            }
            System.out.println("         changed to: " + timePer1000Bytes);
        }
        name = urlTrack.getUri().toString();
        startIndex = (long) (1000 * urlTrack.getBegin() / timePer1000Bytes);
        try {
            AudioInputStream inputStream = AudioSystem.getAudioInputStream(new File(urlTrack
                    .getUri()));
            int numberOfBytes = inputStream.available();
            byte[] contentResult = new byte[numberOfBytes];
            inputStream.read(contentResult, 0, numberOfBytes);
            /*            if (invGain != 1) {
             for (int i = 0; i < contentResult.length; i++) {
             contentResult[i] = intToUnsignedByte(unsignedByteToInt(contentResult[i])
             / invGain);
             }
             }
             */
            content = contentResult;
        } catch (Exception e) {
            System.out.println("inputStream ungettable");
            content = new byte[0];
            return;
        }
    }

    /**
     * Return the i° byte of an OffsetedArray.
     * @param index the index of the wanted byte.
     * @return the index° byte
     */
    public byte getByteI(int index) {
        return content[index];
    }

    /**
     * Return the start index of an array.
     * @return the start index
     */
    public long getStartIndex() {
        return startIndex;
    }

    /**
     * Set the startIndex.
     * @param wantedStart the wanted startIndex
     */
    public void setStartIndex(long wantedStart) {
        startIndex = wantedStart;
    }

    /**
     * Set the content of an OffsetedArray.
     * @param wantedContent the wanted content.
     */
    public void setContent(byte[] wantedContent) {
        content = wantedContent;
    }

    /**
     * Return the duration in byte of an OffsetedArray.
     * @return the duration in byte
     */
    public long getDurationIndex() {
        return content.length;
    }

    /**
     * Add two OffsetedArrray.
     * @param array1 the first array to add
     * @param array2 the second array to add
     * @return the sum of the two array
     */
    public static OffsetedArray addOffsetedArray(OffsetedArray array1, OffsetedArray array2) {
        OffsetedArray result = new OffsetedArray();
        OffsetedArray longerArray, shorterArray;
        int begin, end, length;
        byte[] resultContent;
        byte resultByte;
        boolean isInLonger;
        boolean isInShorter;

        if (array1 == null) {
            System.out.println("Array1 is null");
            return array2;
        }
        if (array2 == null) {
            System.out.println("Array2 is null");
            return array1;
        }

        if (array1.getDurationIndex() > array2.getDurationIndex()) {
            longerArray = new OffsetedArray(array1);
            shorterArray = new OffsetedArray(array2);
        } else {
            longerArray = new OffsetedArray(array2);
            shorterArray = new OffsetedArray(array1);
        }

        final int firstStart = (int) longerArray.getStartIndex();
        final int firstDur = (int) longerArray.getDurationIndex();
        final int firstEnd = firstStart + firstDur;

        final int secondStart = (int) shorterArray.getStartIndex();
        final int secondDur = (int) shorterArray.getDurationIndex();
        final int secondEnd = secondStart + secondDur;

        begin = Math.min(firstStart, secondStart);
        end = Math.max(firstEnd, secondEnd);
        length = end - begin;
        resultContent = new byte[length];

        for (int i = begin; i < end; i++) {
            isInLonger = (i >= firstStart && i < firstEnd);
            isInShorter = (i >= secondStart && i < secondEnd);

            resultByte = (byte) ((isInLonger ? longerArray.getByteI(i - firstStart) : 0) + (isInShorter
                    ? shorterArray.getByteI(i - secondStart)
                    : 0));

            resultContent[i - begin] = resultByte;
        }

        result.setStartIndex(begin);
        result.setContent(resultContent);
        return result;
    }

    /**
     * Return the content as a buffer
     * @return the buffer which corresponds to the content.
     */
    public Buffer toBuffer() {
        Buffer bufferResult = new Buffer();
        bufferResult.setData(content);
        return bufferResult;
    }

    /**
     * Return the byte who corresponds to 0.
     * @return the zero byte.
     */
    public static byte byteZero() {
        return ((byte) 0);
    }

    /**
     * Add two unsigned byte.
     * @param byte1 the first byte to add.
     * @param byte2 the second byte to add.
     * @return the sum.
     */
    public static byte addByte(byte byte1, byte byte2) {
        int int1 = unsignedByteToInt(byte1);
        int int2 = unsignedByteToInt(byte2);
        int intResultTemp = int1 + int2;
        return intToUnsignedByte(intResultTemp);

    }

    /**
     * Adjust the content in order to have an array with the wanted length. 
     * @param length the wanted length
     */
    public void setSizeTo(int length) {

        if (length != content.length) {
            final int max = Math.min(content.length, length);
            byte[] newContent = new byte[length];
            System.arraycopy(content, 0, newContent, 0, max);
            if (max < length) {
                for (int i = max; i < length; i++) {
                    newContent[i] = 0;
                }
            }
        }
    }

    /**
     * Display one byte on 800 of the content.
     */
    public void printShorted() {
        for (int i = 0; i < content.length; i += 800) {
            System.out.print(content[i] + " ");
        }
        System.out.println("");
    }

    /**
     * Put the content in a buffer
     * @param buffer the buffer where to put the content
     */
    public void putData(Buffer buffer) {
        byte[] resultContent = new byte[content.length + buffer.getOffset()];
        for (int i = 0; i < content.length; i++) {
            resultContent[i + buffer.getOffset()] = content[i];
        }
        buffer.setData(resultContent);
    }

    /**
     * Convert an unsigned byte in an integer
     * @param b the unsigned byte
     * @return the integer value of the unsigned byte
     */
    private static int unsignedByteToInt(byte b) {
        int intResult = b;
        if (intResult < 0) {
            intResult += 256;
        }
        return intResult;
    }

    /**
     * Convert an int in an unsigned byte
     * @param i the int to convert
     * @return the byte value of the int
     */
    private static byte intToUnsignedByte(int i) {
        if (i > 255) {
            i = 255;
        }
        if (i >= 128) {
            i -= 256;
        }
        return (byte) i;
    }

    /**
     * This is used to set the gain.
     * @param d the gain.
     */
    void multiply(double d) {
        if (d != 1) {
            for (int i = 0; i < content.length; i++) {
                //System.out.print(content[i] + " => ");
                content[i] = intToUnsignedByte((int) (unsignedByteToInt(content[i]) * d));
                //System.out.println(content[i]);
            }
        }
    }
}
