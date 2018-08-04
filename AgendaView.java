import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.swing.JLabel;
import javax.swing.JTextArea;
/**
 * The view responsible for displaying all events in agenda format 
 * When user clicks on agenda, the user will be able to type in a start date and end date 
 * @Authors Christina Andrade and Samantha Jaime
 */
public class AgendaView implements ViewStrategy
{
	GregorianCalendar c; 
	DataModel d; 
	static JTextArea area;
	private String startDate; 
	private String endDate;
	
	private int height; 
	private int width; 

	/**
	 * Constructs the agenda with the end dates and start dates
	 * @param start the start date 
	 * @param end the end date 
	 */
	public AgendaView(String start, String end)
	{
		startDate = start;
		endDate = end; 
	}
	
	@Override 
	/**
	 * The method implemented from the View strategy interface that will draw the grid for the agenda view 
	 * @param c the calendar it uses to get the events on the grid
	 * @param d the data it gets for the agenda in order for the correct events to be printed on the screen 
	 * @param containter the container used for the grid to be drawn with selected events 
	 */
	public void drawEventGrid(GregorianCalendar c, DataModel d, Graphics2D g2, Component container)
	{
		width = container.getWidth(); 
		
		ArrayList<Event> eventsForThisAgenda = new ArrayList<Event>();

		int startMonth = Integer.parseInt(startDate.substring(0,2)); 
		int startDay = Integer.parseInt(startDate.substring(3,5));
		int startYear = Integer.parseInt(startDate.substring(6));
		

		int endMonth = Integer.parseInt(endDate.substring(0,2)); 
		int endDay = Integer.parseInt(endDate.substring(3,5));
		int endYear = Integer.parseInt(endDate.substring(6));

		GregorianCalendar temp = new GregorianCalendar(startYear, startMonth - 1, startDay); 
		GregorianCalendar endCal = new GregorianCalendar(endYear, endMonth - 1, endDay);
	
		//incremented endCal so that the agenda is inclusive of the end date given 
		endCal.add(Calendar.DAY_OF_YEAR, 1);
		
		
		while (temp.compareTo(endCal) < 0)
		{
			eventsForThisAgenda.addAll(d.getEvents(temp.get(Calendar.MONTH) + 1, temp.get(Calendar.DAY_OF_MONTH), 
					temp.get(Calendar.YEAR))); 
			
			temp.add(Calendar.DAY_OF_MONTH, 1);
			
			
		}

		System.out.println("Loop Detector" ); 

		if (eventsForThisAgenda.size() == 0) //If there are no  events scheduled for the date range then show the message
		{
			Font message = new JLabel().getFont();
			Font letters = new Font(message.toString(), Font.BOLD, 21);
			g2.setFont(letters);

			g2.drawString("No events scheduled for this time frame.", 50, 135); 
		}
		else
		{
			
			for (int j = 0; j < eventsForThisAgenda.size(); j++) //If there are events then get the events from those dates and print them out on each line 
			{

				//draws the lines between each event
				g2.setColor(Color.LIGHT_GRAY);
				Line2D.Double hourLine = new Line2D.Double(0, (j + 1) * 60, container.getWidth(), (j + 1) * 60); 
				g2.draw(hourLine); 

				//Gets the font and font style for each event calling the toString() method
				String eventDetails; 
				Font description = new JLabel().getFont();
				Font time = new Font(description.toString(), Font.BOLD, 16);
				g2.setFont(time);
				
				//Gets all event details and prints it on the screen according with the font style and dimensions stated above
				Event e = eventsForThisAgenda.get(j); 
				eventDetails = e.getDate() + "     "  +e.getStartTime() + "  -  " + e.getEndTime() + "  " + e.getName() + " "; 
				g2.setColor(Color.BLACK);
				g2.drawString(eventDetails, 60, ((j+1) * 60) - 10);

				height = j * 60; 
			}
			

		}
	}

	@Override
	/**
	 * Implemented method from the ViewStrategy interface
	 * Draws the header of the agenda view
	 * @param c the calendar it uses to get the header information 
	 * @param g2 the draw the strings 
	 * @param container the container it gets the dimensions for so header can fit in appropriately 
	 */
	public void drawHeader(GregorianCalendar c, Graphics2D g2, Component container) 
	{
		Font text = new JLabel().getFont();
		Font f = new Font(text.toString(), Font.BOLD, 24);
		FontRenderContext context = g2.getFontRenderContext();
		g2.setFont(f);
		
		Rectangle2D bounds = f.getStringBounds("Agenda", context);
		double x = (container.getWidth() - bounds.getWidth())/(3.5);
		g2.drawString("      Agenda: " + startDate + " - " + endDate, (int) x, container.getHeight()/2); 
	}

	@Override
	/**
	 * Gets the dimensions for the grid 
	 */
	public Dimension getGridDimension() 
	{
	
		return new Dimension(width, 20000);
	}


}
