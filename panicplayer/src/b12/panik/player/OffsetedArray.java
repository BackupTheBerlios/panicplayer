// ----------------------------------------------------------------------------
// [b12] Java Source File: OffsetedArray.java
//                created: 09.01.2004
//              $Revision: 1.3 $
// ----------------------------------------------------------------------------
package b12.panik.player;

import javax.media.Buffer;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

import b12.panik.io.UrlTrack;

/**
 * 
 * @author kariem
 */
public class OffsetedArray {
    private long startIndex;
    //private long byteNumber;
    private byte[] content;
    
    public OffsetedArray(Buffer buffer,long beginIndex) {
        startIndex=beginIndex;
		content = (byte[])buffer.getData();
		//byteNumber=content.length;
    }
    
    public OffsetedArray() {
    }
 
	public OffsetedArray(OffsetedArray original) {
		startIndex=original.getStartIndex();
		content=new byte[((int) original.getDurationIndex())];
		for(int i=0;i<original.getDurationIndex();i++) {
			content[i]=original.getByteI(i);
		}    	
	 }
    
    public OffsetedArray(OffsetedArray source,long beginIndex,long endIndex) {
    	startIndex=beginIndex;
    	int length=((int) (endIndex-beginIndex));
    	content=new byte[length];
    	int j;
    	//System.out.println("sur une longueur de: "+length);
    	for (int i = 0; i < length; i++) {
    		//System.out.println("on tente d ajouter le "+i);
    		j=(i+((int) beginIndex));
    		if(j<source.getStartIndex()) {
    			content[i]=byteZero();
    		} else {
    			if(j>source.getDurationIndex()+source.getStartIndex()) {
    				content[i]=byteZero();
    			} else {
    				content[i]=source.getByteI(i+((int) beginIndex));
    			}
    		}    
		}       
    }
    
    public OffsetedArray(UrlTrack urlTrack, double timePer1000Bytes) {
    	startIndex=(long) Math.floor(((double) (1000*urlTrack.getBegin()))/timePer1000Bytes);
    	System.out.println("le time est de "+timePer1000Bytes+" le startIndex est de "+startIndex);
    	try{
    		AudioInputStream inputStream = AudioSystem.getAudioInputStream(urlTrack.getUrl());
			int numberOfBytes=inputStream.available();
			byte[] contentResult=new byte[numberOfBytes];
			inputStream.read(contentResult,0,numberOfBytes);
			content=contentResult;
    	} catch (Exception e) {
    		System.out.println("inputStream ungettable");
    		content=new byte[0];
    		return;
    	}    	
    }
    
    public byte getByteI(int index) {
    	return content[index];
    }
    
    public long getStartIndex() {
        return startIndex;
    }
	public void setStartIndex(long wantedStart) {
		   startIndex=wantedStart;
	}
	
	public void setContent(byte[] wantedContent) {
		content=wantedContent; 
	}
	
    public long getDurationIndex() {
        //return durationIndex;
        return content.length;
    }
    
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

		}
	
		result.setStartIndex(begin);
		result.setContent(resultContent);
		return result;		
	} 
    
    public Buffer toBuffer() {
    	Buffer bufferResult=new Buffer();
    	bufferResult.setData(content);
    	return bufferResult;
    }
    
    public static byte byteZero() {
    	return ((byte) 0);
    }
    
    public static byte addByte(byte byte1,byte byte2) {
    	System.out.println("on ajoute "+((int) byte1)+" a "+((int) byte2)+" le res est "+(((int) byte1)+((int) byte2)));
    	return ((byte) (((int) byte1)+((int) byte2)));
    }
    
    public void setSizeTo(long length) {
    	if(content.length<length) 	{
    		System.out.println("uncorrect outputBuffer length");
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
			System.out.println("uncorrect outputBuffer length");
			byte[] newContent=new byte[((int) length)];
			for(int i=0;i<length;i++) {
				newContent[i]=content[i];
			}
			content=newContent;      		
    	}
    }    
}
