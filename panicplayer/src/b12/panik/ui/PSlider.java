// ----------------------------------------------------------------------------
// [b12] Java Source File: PSlider.java
//                created: 06.12.2003
//              $Revision: 1.1 $
// ----------------------------------------------------------------------------
package b12.panik.ui;

import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * @author Schurli Creates a Slider with values
 */
public class PSlider extends JPanel {

    private JSlider slider;
    private JLabel value;
    private JLabel txt;
    private Vector changeListeners = null;
    private int min, max, actu;

    /**
	 * Creates a Slider with parameters
	 * 
	 * @param min
	 *            the lowest value
	 * @param max
	 *            the highest value
	 * @param actual
	 *            the initial value
	 * @param type
	 *            the type the slider controls
	 */
    public PSlider(int minimum, int maximum, int actual, String type) {

        min = minimum;
        max = maximum;
        actu = actual;

        setLayout(null);
        slider = new JSlider(JSlider.VERTICAL, min, max, actual);
        slider.setBounds(0, 0, 16, 140);
        add(slider);

        value = new JLabel("   ");
        changeValue();
        value.setBounds(18, 60, 30, 20);
        add(value);

        txt = new JLabel(type);
        txt.setBounds(0, 135, 30, 20);
        add(txt);

    }

    /**
	 * @param event
	 */
    private void fireChangeEvent(ChangeEvent event) {
        for (int i = 0; i < getChangeListeners().size(); i++)
             ((ChangeListener) getChangeListeners().elementAt(i)).stateChanged(event);
    }

    /**
	 * @return
	 */
    public Vector getChangeListeners() {
        if (changeListeners == null)
            changeListeners = new Vector();
        return changeListeners;
    }

    public void addChangeListener(ChangeListener l) {
        if (!getChangeListeners().contains(l))
            getChangeListeners().addElement(l);
    }

    public void removeChangeListener(ChangeListener l) {
        getChangeListeners().removeElement(l);
    }

    /**
	 * @return
	 */
    private int getValue() {
        return slider.getValue();
    }

    public void setValeur(int val) {
        slider.setValue(val);
        changeValue();
    }

    /**
	 *  
	 */
    private void changeValue() {
        fireChangeEvent(new ChangeEvent(this));
        int val = getValue();
        if (val > 0)
            value.setText("<html><small>+" + val + "</small></html>");
        else
            value.setText("<html><small>" + val + "</small></html>");
        value.revalidate();
    }

}
