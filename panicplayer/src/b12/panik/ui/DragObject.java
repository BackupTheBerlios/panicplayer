// ----------------------------------------------------------------------------
// [b12] Java Source File: DragObject.java
//                created: 30.12.2003
//              $Revision: 1.2 $
// ----------------------------------------------------------------------------
package b12.panik.ui;

import java.awt.Color;
import java.awt.Container;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceContext;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;
import java.awt.dnd.InvalidDnDOperationException;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 * @author Jorge De Mar
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class DragObject extends JLabel {

  public DragObject(String s) {
	this.setText(s);
	this.setOpaque(true);
	this.dragSource = DragSource.getDefaultDragSource();
	this.dgListener = new DGListener();
	this.dsListener = new DSListener();

	// component, action, listener
	this.dragSource.createDefaultDragGestureRecognizer(
	  this,
	  this.dragAction,
	  this.dgListener);
  }

  public DragObject(String s, int a) {
	if (a != DnDConstants.ACTION_NONE &&
	  a != DnDConstants.ACTION_COPY &&
	  a != DnDConstants.ACTION_MOVE &&
	  a != DnDConstants.ACTION_COPY_OR_MOVE &&
	  a != DnDConstants.ACTION_LINK
	) throw new IllegalArgumentException("action" + a);

	this.dragAction = a;
	this.setText(s);
	this.setOpaque(true);
	this.dragSource = DragSource.getDefaultDragSource();
	this.dgListener = new DGListener();
	this.dsListener = new DSListener();

	// component, action, listener
	this.dragSource.createDefaultDragGestureRecognizer(
	  this,
	  this.dragAction,
	  this.dgListener);
  }


  /**
   * DGListener
   * a listener that will start the drag.
   * has access to top level's dsListener and dragSource
   * @see java.awt.dnd.DragGestureListener
   * @see java.awt.dnd.DragSource
   * @see java.awt.datatransfer.StringSelection
   */
  class DGListener implements DragGestureListener {
	/**
	 * Start the drag if the operation is ok.
	 * uses java.awt.datatransfer.StringSelection to transfer
	 * the label's data
	 * @param e the event object
	 */
	public void dragGestureRecognized(DragGestureEvent e) {

	  // if the action is ok we go ahead
	  // otherwise we punt
	  System.out.println(e.getDragAction());
	  if((e.getDragAction() & DragObject.this.dragAction) == 0)
	return;
	  System.out.println( "kicking off drag");

	  Transferable transferable = new StringTransferable( DragObject.this.getText() );

	  try {
	e.startDrag(DragSource.DefaultCopyNoDrop,
	  transferable,
	  DragObject.this.dsListener);


	  }catch( InvalidDnDOperationException idoe ) {
	System.err.println( idoe );
	  }
	}
  }

  /**
   * DSListener
   * a listener that will track the state of the DnD operation
   *
   * @see java.awt.dnd.DragSourceListener
   * @see java.awt.dnd.DragSource
   * @see java.awt.datatransfer.StringSelection
   */
  class DSListener implements DragSourceListener {

	/**
	 * @param e the event
	 */
	public void dragDropEnd(DragSourceDropEvent e) {
	  if( e.getDropSuccess() == false ) {
	System.out.println( "not successful");
	return;
	  }

	  	  System.out.println( "dragdropend action " + e.getDropAction() );


	  if(e.getDropAction() == DnDConstants.ACTION_MOVE)
	DragObject.this.setText("");
	}

	/**
	 * @param e the event
	 */
	public void dragEnter(DragSourceDragEvent e) {
	  System.out.println( "DragObject enter " + e);
	  DragSourceContext context = e.getDragSourceContext();
	  int myaction = e.getDropAction();
	  if( (myaction & DragObject.this.dragAction) != 0) {
	context.setCursor(DragSource.DefaultCopyDrop);
	  } else {
	context.setCursor(DragSource.DefaultCopyNoDrop);
	  }
	}
	/**
	 * @param e the event
	 */
	public void dragOver(DragSourceDragEvent e) {
	  DragSourceContext context = e.getDragSourceContext();
	  int sa = context.getSourceActions();
	  int ua = e.getUserAction();
	  int da = e.getDropAction();
	  int ta = e.getTargetActions();
	  System.out.println("dl dragOver source actions" + sa);
	  System.out.println("user action" + ua);
	  System.out.println("drop actions" + da);
	  System.out.println("target actions" + ta);
	}
	/**
	 * @param e the event
	 */
	public void dragExit(DragSourceEvent e) {
	  System.out.println( "DragObject exit " + e);
	  DragSourceContext context = e.getDragSourceContext();
	}

	/**
	 * for example, press shift during drag to change to
	 * a link action
	 * @param e the event
	 */
	public void dropActionChanged (DragSourceDragEvent e) {
	  DragSourceContext context = e.getDragSourceContext();
	  context.setCursor(DragSource.DefaultCopyNoDrop);
	}
  }


  public static void main(String[] args) {
	JFrame frame = new JFrame();
	frame.setTitle("DragObject test");
	Container cont = frame.getContentPane();
	DragObject l = new DragObject("Here is some text");
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

  private DragSource dragSource;
  private DragGestureListener dgListener;
  private DragSourceListener dsListener;
  private int dragAction = DnDConstants.ACTION_COPY;
}

