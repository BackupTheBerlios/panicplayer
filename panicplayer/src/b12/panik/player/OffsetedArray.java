// ----------------------------------------------------------------------------
// [b12] Java Source File: OffsetedArray.java
//                created: 09.01.2004
//              $Revision: 1.4 $
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
		//content = (byte[])buffer.getData();
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
    	
    	if( (beginIndex>(source.getStartIndex()+source.getDurationIndex()))
    		|| (endIndex<source.getStartIndex())    ) {
    			content=new byte[0];
    			return;
    		}
    	
    	//System.out.println("sur une longueur de: "+length);
    	for (int i = 0; i < length; i++) {
    		//System.out.println("on tente d ajouter le "+i);
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
    
    public OffsetedArray(UrlTrack urlTrack, double timePer1000Bytes, int invGain) {
    	startIndex=(long) Math.floor(((double) (1000*urlTrack.getBegin()))/timePer1000Bytes);
    	System.out.print("le begin est: "+urlTrack.getBegin()+" le time est de "+timePer1000Bytes+" le startIndex est de "+startIndex);
    	try{
    		System.out.println("l url est "+urlTrack.getUrl());
    		AudioInputStream inputStream = AudioSystem.getAudioInputStream(urlTrack.getUrl());
    		
    		System.out.println("format de l url track: "+inputStream.getFormat());
    		
			int numberOfBytes=inputStream.available();
			byte[] contentResult=new byte[numberOfBytes];
			inputStream.read(contentResult,0,numberOfBytes);
			if(invGain!=1) {
				for(int i=0;i<contentResult.length;i++) {
					contentResult[i]=intToUnsignedByte(unsignedByteToInt(contentResult[i])/invGain);
				}
			}
			content=contentResult;
			System.out.println(" le nb de byte: "+numberOfBytes);
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
					System.out.print("+");
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
		
		System.out.println("");
	
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
    	//System.out.println("on ajoute "+((int) byte1)+" a "+((int) byte2)+" le res est "+(((int) byte1)+((int) byte2)));
    	//int intResult=(((int) byte1)+((int) byte2)) /2;
    	//byte result=byte1+byte2;
    	
    	int int1=unsignedByteToInt(byte1);
    	int int2=unsignedByteToInt(byte2);
    	
 		
		int intResultTemp=int1+int2;
		
		return intToUnsignedByte(intResultTemp);
    	
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
    
    public void printShorted() {
    	for(int i=0;i<content.length;i+=800) {
    		System.out.print(content[i]+" ");   
    	}
    	System.out.println("");
    }
    
    public void putData(Buffer buffer) {
    	byte[] resultContent=new byte[content.length+buffer.getOffset()];
    	for(int i=0;i<content.length;i++) {
    		resultContent[i+buffer.getOffset()]=content[i];    		
    	}
    	buffer.setData(resultContent);    	
    }
    
    private static int unsignedByteToInt(byte b) {
		int intResult=((new Byte(b)).intValue());
		if(intResult<0) 	{
			intResult+=256;
		}
		return intResult;
    } 
    
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
