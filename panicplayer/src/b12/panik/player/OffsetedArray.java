// ----------------------------------------------------------------------------
// [b12] Java Source File: OffsetedArray.java
//                created: 09.01.2004
//              $Revision: 1.5 $
// ----------------------------------------------------------------------------
package b12.panik.player;

import javax.media.Buffer;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

import b12.panik.io.UrlTrack;

/**

 * A array of byte with a start index.

 * @author olivier

 */

public class OffsetedArray {
    private long startIndex;
    private byte[] content;
    
	/**
	 * Creates a new instance of <code>OffsetedArray</code> from a buffer.
	 * @param buffer the source buffer.
	 * @param beginIndex the startIndex in byte.
	 * @param invGain the inverse of the gain.
	 */
    public OffsetedArray(Buffer buffer,long beginIndex, int invGain) {
        startIndex=beginIndex;
        byte[] inputData=(byte[])buffer.getData();
		content=new byte[buffer.getLength()];
		int offset=buffer.getOffset();
		for(int i=0;i<buffer.getLength();i++) {
			if(invGain==1) {
				content[i]=inputData[i+offset];
			} else {
				content[i]=intToUnsignedByte(unsignedByteToInt(inputData[i+offset])/invGain);
			}
		}
    }
    
	/**
	 * Creates a new null instance of <code>OffsetedArray</code>.
	 */    
    public OffsetedArray() {
    }
 
	/**
	 * Creates a new instance of <code>OffsetedArray</code> as a copy of a given OffsetedArray.
	 * @param original the OffsetedArray to be copied.
	 */
	public OffsetedArray(OffsetedArray original) {
		startIndex=original.getStartIndex();
		content=new byte[((int) original.getDurationIndex())];
		for(int i=0;i<original.getDurationIndex();i++) {
			content[i]=original.getByteI(i);
		}    	
	 }

	/**
	 * Creates a new instance of <code>OffsetedArray</code> from an other OffsetedArray.
	 * @param source the source OffsetedArray.
	 * @param beginIndex the startIndex in byte.
	 * @param endIndex the endIndex in byte.
	 */    
    public OffsetedArray(OffsetedArray source,long beginIndex,long endIndex) {
    	startIndex=beginIndex;
    	int length=((int) (endIndex-beginIndex));
    	content=new byte[length];
    	int j;
    	
    	if( (beginIndex>(source.getStartIndex()+source.getDurationIndex()))
    		|| (endIndex<source.getStartIndex())    ) {
    			content=new byte[0];
    			return;
    		}

    	for (int i = 0; i < length; i++) {
    		j=i+((int) beginIndex)-((int) source.getStartIndex());
    		if(j<0) {
    			content[i]=byteZero();
    		} else {
    			if(j>=source.getDurationIndex()) {
    				content[i]=byteZero();
    			} else {
    				content[i]=source.getByteI(j);
    			}
    		}    
		}       
    }

	/**
	 * Creates a new instance of <code>OffsetedArray</code> from a urlTracks.
	 * @param urlTrack the source UrlTrack.
	 * @param timePer1000Bytes the time in ms for 1000 byte.
	 * @param invGain the inverse of the gain.
	 */    
    public OffsetedArray(UrlTrack urlTrack, double timePer1000Bytes, int invGain) {
    	startIndex=(long) Math.floor(((double) (1000*urlTrack.getBegin()))/timePer1000Bytes);
    	try{
    		AudioInputStream inputStream = AudioSystem.getAudioInputStream(urlTrack.getUrl());
			int numberOfBytes=inputStream.available();
			byte[] contentResult=new byte[numberOfBytes];
			inputStream.read(contentResult,0,numberOfBytes);
			if(invGain!=1) {
				for(int i=0;i<contentResult.length;i++) {
					contentResult[i]=intToUnsignedByte(unsignedByteToInt(contentResult[i])/invGain);
				}
			}
			content=contentResult;
    	} catch (Exception e) {
    		System.out.println("inputStream ungettable");
    		content=new byte[0];
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
		   startIndex=wantedStart;
	}
	
	/**
	 * Set the content of an OffsetedArray.
	 * @param wantedContent the wanted content.
	 */
	public void setContent(byte[] wantedContent) {
		content=wantedContent; 
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
	public static OffsetedArray addOffsetedArray(OffsetedArray array1,OffsetedArray array2) {
		OffsetedArray result=new OffsetedArray();
		OffsetedArray longerArray, shorterArray;
		long begin, commonBegin,commonEnd,end, length1, length2,length;
		byte[] resultContent;
		byte resultByte;
		boolean isInLonger;
		boolean isInShorter;
		
		if(array1==null) {
			return array2;
		}
		
		if(array2==null) {
			return array1;
		}
				
		
		if(array1.getDurationIndex()>array2.getDurationIndex()) {
			longerArray=new OffsetedArray(array1);
			shorterArray=new OffsetedArray(array2);
		} else {
			longerArray=new OffsetedArray(array2);
			shorterArray=new OffsetedArray(array1);			
		}
		
		begin=Math.min(longerArray.getStartIndex(),shorterArray.getStartIndex());
		end=Math.max(longerArray.getStartIndex()+longerArray.getDurationIndex(),shorterArray.getStartIndex()+shorterArray.getDurationIndex());
		length=end-begin;
		resultContent=new byte[(int) length];
		
		for(int i=((int) begin);i<length+begin;i++) {
			isInLonger=((i>=longerArray.getStartIndex())&& (i<longerArray.getStartIndex()+longerArray.getDurationIndex()));
			isInShorter=((i>=shorterArray.getStartIndex())&& (i<shorterArray.getStartIndex()+shorterArray.getDurationIndex()));
			if(isInLonger) {
				if(isInShorter) {
					resultByte=addByte(longerArray.getByteI(i-((int) longerArray.getStartIndex())),shorterArray.getByteI(i-((int) shorterArray.getStartIndex())));
				} else {
					resultByte=longerArray.getByteI(i-((int) longerArray.getStartIndex()));
				}
			} else {
				if(isInShorter) {
					resultByte=shorterArray.getByteI(i-((int) shorterArray.getStartIndex()));
				} else {
					resultByte=byteZero();
				}
			}
			resultContent[i-((int) begin)]=resultByte;
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
    	Buffer bufferResult=new Buffer();
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
    public static byte addByte(byte byte1,byte byte2) {
    	int int1=unsignedByteToInt(byte1);
    	int int2=unsignedByteToInt(byte2);
 		int intResultTemp=int1+int2;
		return intToUnsignedByte(intResultTemp);
 
    }
    
    /**
     * Adjust the content in order to have an array with the wanted length. 
     * @param length the wanted length
     */
    public void setSizeTo(long length) {
    	if(content.length<length) 	{
    		byte[] newContent=new byte[((int) length)];
    		for(int i=0;i<content.length;i++) {
    			newContent[i]=content[i];
    		}
    		for(int i=content.length;i<length;i++) {
    			newContent[i]=byteZero();
    		}
    		content=newContent;    			
    	}
    	if(content.length>length)
    	{
			byte[] newContent=new byte[((int) length)];
			for(int i=0;i<length;i++) {
				newContent[i]=content[i];
			}
			content=newContent;      		
    	}
    } 
    
    /**
     * Display one byte on 800 of the content.
     */
    public void printShorted() {
    	for(int i=0;i<content.length;i+=800) {
    		System.out.print(content[i]+" ");   
    	}
    	System.out.println("");
    }
    
    /**
     * Put the content in a buffer
     * @param buffer the buffer where to put the content
     */
    public void putData(Buffer buffer) {
    	byte[] resultContent=new byte[content.length+buffer.getOffset()];
    	for(int i=0;i<content.length;i++) {
    		resultContent[i+buffer.getOffset()]=content[i];    		
    	}
    	buffer.setData(resultContent);    	
    }
    
    /**
     * Convert an unsigned byte in an integer
     * @param b the unsigned byte
     * @return the integer value of the unsigned byte
     */
    private static int unsignedByteToInt(byte b) {
		int intResult=((new Byte(b)).intValue());
		if(intResult<0) 	{
			intResult+=256;
		}
		return intResult;
    } 
    
    /**
     * Convert an int in an unsigned byte
     * @param i the int to convert
     * @return the byte value of the int
     */
    private static byte intToUnsignedByte(int i) {
		if(i>255) {
			i=255;
		}
		if(i>=128) {
			i-=256;
		}
		return ((new Integer(i)).byteValue());
    }   
}
