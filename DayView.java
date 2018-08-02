
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TreeSet;

import javax.swing.JLabel;

/**
 * Custom JPanel Class. Holds a calendar, instance of a data model, and Grid depicting times of day. 
 * According to the data passed from the dataModel and calendar, DayView depicts a day and any events scheduled.
 * throughout the day. 
 * 
 * @author Christina Andrade
 *
 */
public class DayView implements ViewStrategy {

	private static String[] fullNameDays = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday" };

	GregorianCalendar c; 
	DataModel d; 

	public void drawEventGrid(GregorianCalendar c, DataModel d, Graphics2D g2, Component container)
	{
				//Draw line dividing hours and events
				Line2D.Double vertLine = new Line2D.Double(60, 0, 60, container.getHeight()); 
				g2.setColor(Color.LIGHT_GRAY);
				g2.draw(vertLine);

				//Draw the time text
				int hour = 1;  
				String halfOfDay = "am"; 

				for (int i = 0; i<24; i ++)
				{	
					if (i < 23)
					{
						g2.setColor(Color.BLACK);
						if (i == 11)
						{
							halfOfDay = "pm";
						}
						if (i == 12)
						{
							hour = 1; 
						}

						String time = Integer.toString(hour) + halfOfDay; 
						hour++; 
						g2.drawString(time, 20, ( i + 1 )*60);
					}


					//Draw the Lines indicating where hours begin 
					g2.setColor(Color.LIGHT_GRAY);
					Line2D.Double hourLine = new Line2D.Double(60, i*60, container.getWidth(), i*60); 
					g2.draw(hourLine); 

				}

				//Get events from the datModel 
				TreeSet<Event> events = d.getEvents(c.get(Calendar.MONTH) + 1 , c.get(Calendar.DAY_OF_MONTH), c.get(Calendar.YEAR));
				if (events.size() != 0)
				{
					for (Event e : events)
					{
						g2.setColor(Color.BLUE);
						String eventStart = e.getStartTime(); 
						String eventEnd = e.getEndTime(); 
						int startHour = Integer.parseInt(eventStart.substring(0,2)); 
						int startMin = Integer.parseInt(eventStart.substring(3)); 
						int endHour = Integer.parseInt(eventEnd.substring(0,2)); 
						int endMin = Integer.parseInt(eventEnd.substring(3)); 

						//Draw the Rectangle representing the scheduled event 
						Rectangle2D.Double eventBox = new Rectangle2D.Double(60, ((startHour * 60) + startMin), 
								container.getWidth(), ((endHour * 60) - (startHour * 60) + (endMin - startMin)) ); 
						g2.fill(eventBox);

						//Draw the event details onto the rectangle
						FontMetrics fm = g2.getFontMetrics(container.getFont()); 
						g2.setColor(Color.WHITE);
						g2.drawString( " " + e.getName() + " " + e.getStartTime() + " - " + e.getEndTime(), 60, ((startHour * 60) + startMin + fm.getHeight())); 
					}
				}


	}


	@Override
	public void drawHeader(GregorianCalendar c, Graphics2D g2, Component container) {


				String day = fullNameDays[c.get(Calendar.DAY_OF_WEEK) - 1 ] + " " +
						(c.get(Calendar.MONTH) + 1) + "/"+ c.get(Calendar.DAY_OF_MONTH); 
				
				Font defaultFont = new JLabel().getFont(); 
				Font bigFont = new Font(defaultFont.toString(), Font.BOLD, 24); 
				
				g2.setFont(bigFont);
				Font f = g2.getFont(); 
				FontRenderContext context = g2.getFontRenderContext(); 
				Rectangle2D bounds = f.getStringBounds(day, context);
				
				double x = (container.getWidth() - bounds.getWidth()) / 2;
				//double y = (this.getHeight() - bounds.getHeight()) / 2;

				g2.drawString(day, (int) x, (int) ((container.getHeight()/2) + (bounds.getHeight()/2))); 
		

	}


//	@Override
//	public Dimension getGridDimension() {
//	
//		return new Dimension(1000,1440);
//	}
	
//	public boolean needsScroll()
//	{
//		return true; 
//	}
}
