
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.util.GregorianCalendar;

/**
 * Created so that implementing calendar programs can easily plug in multiple views (preferably JPanels) 
 * presenting events in various formats(Day, week, month, year, agenda, etc). 
 *  For strategy pattern designs.  Any implementing class should hold a GregorianCalendar and reference to a data model.
 *  It should be able to update and provide access to the GregorianCalendar held by the view, and respond to changes from an informing data model. 
 * @author Christina Andrade
 *
 */
public interface ViewStrategy{

	void drawHeader(GregorianCalendar c, Graphics2D g2, Component container); 
	void drawEventGrid(GregorianCalendar c, DataModel d, Graphics2D g2, Component container); 
	Dimension getGridDimension(); 
}
