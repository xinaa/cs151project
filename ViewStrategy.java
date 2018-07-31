
import java.util.GregorianCalendar;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Created so that implementing calendar programs can easily plug in multiple views (preferably JPanels) 
 * presenting events in various formats(Day, week, month, year, agenda, etc). 
 *  For strategy pattern designs.  Any implementing class should hold a GregorianCalendar and reference to a data model.
 *  It should be able to update and provide access to the GregorianCalendar held by the view, and respond to changes from an informing data model. 
 * @author Christina Andrade
 *
 */
public interface ViewStrategy extends ChangeListener{

	void updateCalendar(GregorianCalendar c); 
	void stateChanged(ChangeEvent e);
	GregorianCalendar getCalendar(); 	
}
