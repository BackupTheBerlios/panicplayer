package b12.panik.ui;

import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;


// ----------------------------------------------------------------------------
// [b12] Java Source File: StringTranferable.java
//                created: 30.12.2003
//              $Revision: 1.1 $
// ----------------------------------------------------------------------------

/**
 * @author Jorge De Mar
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class StringTransferable implements Transferable, ClipboardOwner {

  public static final DataFlavor plainTextFlavor = DataFlavor.plainTextFlavor;
  public static final DataFlavor localStringFlavor = DataFlavor.stringFlavor;
  
  public static final DataFlavor[] flavors = {
	StringTransferable.plainTextFlavor,
	StringTransferable.localStringFlavor
  };
  
  private static final List flavorList = Arrays.asList( flavors );
  private String string;

  /**
   * Constructor.
   * simply initializes instance variable
   */
  public StringTransferable(String string) {
	this.string = string;
  }
  
  private void dumpFlavor(DataFlavor flavor) {
	System.out.println( "getMimeType " +
	  flavor.getMimeType() );
	System.out.println( "getHumanPresentableName " +
	  flavor.getHumanPresentableName() );
	System.out.println( "getRepresentationClass " +
	  flavor.getRepresentationClass().getName() );
	System.out.println( "isMimeTypeSerializedObject " +
	  flavor.isMimeTypeSerializedObject() );
	System.out.println( "isRepresentationClassInputStream " +
	  flavor.isRepresentationClassInputStream() );
	System.out.println( "isRepresentationClassSerializable " +
	  flavor.isRepresentationClassSerializable() );
	System.out.println( "isRepresentationClassRemote " +
	  flavor.isRepresentationClassRemote() );
	System.out.println( "isFlavorSerializedObjectType " +
	  flavor.isFlavorSerializedObjectType() );
	System.out.println( "isFlavorRemoteObjectType " +
	  flavor.isFlavorRemoteObjectType() );
	System.out.println( "isFlavorJavaFileListType " +
	  flavor.isFlavorJavaFileListType() );
  }
     
  public synchronized DataFlavor[] getTransferDataFlavors() {
//	  return (DataFlavor[]) flavorList.toArray();
	return flavors;
  }
     
  public boolean isDataFlavorSupported( DataFlavor flavor ) {
	return ( flavorList.contains( flavor ) );
  }
  
  public synchronized Object getTransferData(DataFlavor flavor)
	throws UnsupportedFlavorException, IOException {
	System.err.println( "getTransferData(): ");
	dumpFlavor(flavor);    
    
	if (flavor.equals(StringTransferable.plainTextFlavor)) {
	  return new ByteArrayInputStream(this.string.getBytes("Unicode"));
	} else if (StringTransferable.localStringFlavor.equals(flavor)) {
	  return this.string;
	} else {
	  throw new UnsupportedFlavorException (flavor);
	}
  }
  public String toString() {
	return "StringTransferable";
  }
  
  public void lostOwnership(Clipboard clipboard, Transferable contents) {
	System.out.println ("StringTransferable lost ownership of "  +
	  clipboard.getName());
	System.out.println ("data: " + contents);    
  }
}
