
import java.awt.Graphics;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.swing.JButton;

/**
 * Custom JButton Class that stores a reference to a GregorianCalendar that can be retrieved and updated. 
 * Button displays the day of the month of the GregorianCalendar it is given. 
 * @author Christina
 */
public class DayButton extends JButton {

		private GregorianCalendar c; 

		

		/**
		 * Constructs the button and attaches the calendar for it to display
		 * @param cal calendar who's day of month the button should display 
		 */
		public DayButton(GregorianCalendar cal)
		{
			c = cal; 
			this.setText(Integer.toString(c.get(Calendar.DAY_OF_MONTH)));
			
		}
		
		/**
		 * Overload constructor - no calendar given. 
		 */
		public DayButton()
		{
			
		}
		
		/**
		 * Updates the calendar and changes the day displayed
		 * @param cal calendar who's day of month the button should display 
		 */
		public void updateDay(GregorianCalendar cal)
		{
			c = (GregorianCalendar) cal.clone(); 
			this.setText(Integer.toString(c.get(Calendar.DAY_OF_MONTH)));
			
		}
		
		/**
		 * Get the calendar that the button is currently displaying the day of month from 
		 * @return c the Calendar the button is holding 
		 */
		public GregorianCalendar getCalendar()
		{
			return c; 
		}
		

		
	
	
}
