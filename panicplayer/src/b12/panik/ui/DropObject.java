// ----------------------------------------------------------------------------
// [b12] Java Source File: DropObject.java
//                created: 30.12.2003
//              $Revision: 1.1 $
// ----------------------------------------------------------------------------
package b12.panik.ui;

import java.awt.Color;
import java.awt.Container;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

/**
 * @author Jorge De Mar
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class DropObject extends JLabel {
  
  public DropObject(String s) {    
	this.setText(s);
	this.borderColor=Color.green;
	this.dtListener = new DTListener();
    
	// component, ops, listener, accepting
	this.dropTarget = new DropTarget(this, 
	  this.acceptableActions,
	  this.dtListener,
	  true);
  }
  
  public DropObject(String s, int a) {
	if (a != DnDConstants.ACTION_NONE &&
	  a != DnDConstants.ACTION_COPY &&
	  a != DnDConstants.ACTION_MOVE &&
	  a != DnDConstants.ACTION_COPY_OR_MOVE &&      
	  a != DnDConstants.ACTION_LINK
	) throw new IllegalArgumentException("action" + a);
    
	this.acceptableActions = a;
	this.setText(s);
	this.borderColor=Color.green;
	this.dtListener = new DTListener();
    
	// component, ops, listener, accepting
	this.dropTarget = new DropTarget(this, 
	  this.acceptableActions,
	  this.dtListener,
	  true);
    
  }

  private void showBorder(boolean b) {
	if(b) {
	  this.setBorder( BorderFactory.createLineBorder(this.borderColor, 10) );      
	} else {
	  this.setBorder( BorderFactory.createEmptyBorder() );      
	}
	this.getParent().validate();    
	this.repaint();    
  }
  
  /**
   * DTListener
   * a listener that tracks the state of the operation
   * @see java.awt.dnd.DropTargetListener
   * @see java.awt.dnd.DropTarget
   */
  class DTListener implements DropTargetListener {
    
	/**
	 * Called by isDragOk
	 * Checks to see if the flavor drag flavor is acceptable
	 * @param e the DropTargetDragEvent object
	 * @return whether the flavor is acceptable
	 */
	private boolean isDragFlavorSupported(DropTargetDragEvent e) {
	  boolean ok=false;
	  if (e.isDataFlavorSupported(StringTransferable.plainTextFlavor)) {
	ok=true;
	  } else if (e.isDataFlavorSupported(
	StringTransferable.localStringFlavor)) {
	ok=true;
	  } else if (e.isDataFlavorSupported(DataFlavor.stringFlavor)) {	  
	ok=true;
	  } else if (e.isDataFlavorSupported(DataFlavor.plainTextFlavor)) {
	ok=true;
	  }
	  return ok;
	}
	/**
	 * Called by drop
	 * Checks the flavors and operations
	 * @param e the DropTargetDropEvent object
	 * @return the chosen DataFlavor or null if none match
	 */
	private DataFlavor chooseDropFlavor(DropTargetDropEvent e) {
	  if (e.isLocalTransfer() == true &&
	e.isDataFlavorSupported(StringTransferable.localStringFlavor)) {
	return StringTransferable.localStringFlavor;
	  }
	  DataFlavor chosen = null;      
	  if (e.isDataFlavorSupported(StringTransferable.plainTextFlavor)) {
	chosen = StringTransferable.plainTextFlavor;
	  } else if (e.isDataFlavorSupported(
	StringTransferable.localStringFlavor)) {
	chosen = StringTransferable.localStringFlavor;	
	  } else if (e.isDataFlavorSupported(DataFlavor.stringFlavor)) {	  
	chosen = DataFlavor.stringFlavor;
	  } else if (e.isDataFlavorSupported(DataFlavor.plainTextFlavor)) {
	chosen = DataFlavor.plainTextFlavor;	
	  }
	  return chosen;
	}

	/**
	 * Called by dragEnter and dragOver
	 * Checks the flavors and operations
	 * @param e the event object
	 * @return whether the flavor and operation is ok
	 */
	private boolean isDragOk(DropTargetDragEvent e) {
	  if(isDragFlavorSupported(e) == false) {
	System.out.println( "isDragOk:no flavors chosen" );
	return false;
	  }
      
	 
	  int da = e.getDropAction();      
	  System.out.print("dt drop action " + da);
	  System.out.println(" my acceptable actions " + acceptableActions);
      
	  if ((da & DropObject.this.acceptableActions) == 0)
	return false;
	  return true;
	}

	/**
	 * start "drag under" feedback on component
	 * invoke acceptDrag or rejectDrag based on isDragOk
	 */
	public void dragEnter(DropTargetDragEvent e) {
	  System.out.println( "dtlistener dragEnter");    
	  if(isDragOk(e) == false) {
	System.out.println( "enter not ok");
	DropObject.this.borderColor=Color.red;            
	showBorder(true);
	e.rejectDrag();      
	return;
	  }
	  DropObject.this.borderColor=Color.green;      
	  showBorder(true);
	  System.out.println( "dt enter: accepting " + e.getDropAction());
	  e.acceptDrag(e.getDropAction());      
	}

	/**
	 * continue "drag under" feedback on component
	 * invoke acceptDrag or rejectDrag based on isDragOk
	 */
	public void dragOver(DropTargetDragEvent e) {
	  if(isDragOk(e) == false) {
	System.out.println( "dtlistener dragOver not ok" );
	DropObject.this.borderColor=Color.red;            
	showBorder(true);
	e.rejectDrag();      
	return;
	  }
	  System.out.println( "dt over: accepting");
	  e.acceptDrag(e.getDropAction());            
	}
    
	public void dropActionChanged(DropTargetDragEvent e) {
	  if(isDragOk(e) == false) {
	System.out.println( "dtlistener changed not ok" );
	e.rejectDrag();      
	return;
	  }
	  System.out.println( "dt changed: accepting"+e.getDropAction());
	  e.acceptDrag(e.getDropAction());            
	}
    
	public void dragExit(DropTargetEvent e) {
	  System.out.println( "dtlistener dragExit");
	  DropObject.this.borderColor=Color.green;            
	  showBorder(false);
	}

	/**
	 * perform action from getSourceActions on
	 * the transferrable
	 * invoke acceptDrop or rejectDrop
	 * invoke dropComplete
	 * if its a local (same JVM) transfer, use StringTransferable.localStringFlavor
	 * find a match for the flavor
	 * check the operation
	 * get the transferable according to the chosen flavor
	 * do the transfer
	 */
	public void drop(DropTargetDropEvent e) {
	  System.out.println( "dtlistener drop");
      
	  DataFlavor chosen = chooseDropFlavor(e);
	  if (chosen == null) {
	System.err.println( "No flavor match found" );
	e.rejectDrop();      	
	return;
	  }
	  System.err.println( "Chosen data flavor is " + chosen.getMimeType());

	  // the actual operation
	  int da = e.getDropAction();
	  // the actions that the source has specified with DragGestureRecognizer
	  int sa = e.getSourceActions();      
	  System.out.println( "drop: sourceActions: " + sa);
	  System.out.println( "drop: dropAction: " + da);
      
	  if ( ( sa & DropObject.this.acceptableActions ) == 0 ) {
	System.err.println( "No action match found" );
	e.rejectDrop();      		
	showBorder(false);      		
	return;
	  }
    
	  Object data=null;
	  try {
	
	e.acceptDrop(DropObject.this.acceptableActions);
	// e.acceptDrop(DnDConstants.ACTION_MOVE);
	//e.acceptDrop(DnDConstants.ACTION_COPY);
	//e.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE); 
  
	data = e.getTransferable().getTransferData(chosen);
	if (data == null)
	  throw new NullPointerException();
	  } catch ( Throwable t ) {
	System.err.println( "Couldn't get transfer data: " + t.getMessage());
	t.printStackTrace();
	e.dropComplete(false);
	showBorder(false);      		
	return;
	  }
	  System.out.println( "Got data: " + data.getClass().getName() );
      
	  if (data instanceof String ) {
	String s = (String) data;
	DropObject.this.setText(s);	
	  } else if (data instanceof InputStream) {
	InputStream input = (InputStream)data;
	InputStreamReader isr = null;
	//	BufferedReader br = null;
	try {
	  //	  br = new BufferedReader(isr=new InputStreamReader(input,"Unicode"));
	  isr=new InputStreamReader(input,"Unicode");	  
	} catch(UnsupportedEncodingException uee) {
	  isr=new InputStreamReader(input);	  	  
	}

	StringBuffer str = new StringBuffer();
	int in=-1;
	try {
	  while((in = isr.read()) >= 0 ) {
		//System.out.println("read: " + in);
		if (in != 0)
		  str.append((char)in);
	  }

	 DropObject.this.setText(str.toString());
	  
	} catch(IOException ioe) {
	  
	  
	  System.err.println( "cannot read" + ioe);
	  e.dropComplete(false);      	  
	  showBorder(false);
	  String message = "Bad drop\n" + ioe.getMessage();
	  JOptionPane.showMessageDialog(DropObject.this,
		message,
		"Error",
		JOptionPane.ERROR_MESSAGE);
	  return;
	}

	  } else {
	System.out.println( "drop: rejecting");
	e.dropComplete(false);      	  	
	showBorder(false);      	
	return;
	  }
      
	  e.dropComplete(true);      
	  showBorder(false);      	      
	}

  }

  public static void main(String[] args) {
	JFrame frame = new JFrame();
	frame.setTitle("DropObject test");
	Container cont = frame.getContentPane();
	DropObject l = new DropObject("Drop here");
	l.setBackground(Color.black);
	l.setForeground(Color.yellow);    
	cont.add( l );
	frame.addWindowListener( new WindowAdapter() {
	  public void windowClosing(WindowEvent e) {
	System.exit(0);
	  }
	});
	frame.setSize(300,300); 
	frame.setVisible(true);
  }
  
  
  private DropTarget dropTarget;
  private DropTargetListener dtListener;

  /**
   * the actions supported by this drop target
   */
  
  private int acceptableActions = DnDConstants.ACTION_COPY;  
//	private int acceptableActions = DnDConstants.ACTION_MOVE;
//	private int acceptableActions = DnDConstants.ACTION_COPY_OR_MOVE;
  
  private Color borderColor;
}

