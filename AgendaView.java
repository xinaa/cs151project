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
import javax.swing.JTextArea;

public class AgendaView implements ViewStrategy
{

	//private static String[] fullNameDays = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday" };

	GregorianCalendar c; 
	DataModel d; 
	static JTextArea area;
	private String startDate; 
	private String endDate;
	
	private int height; 
	private int width; 

	public AgendaView(String start, String end)
	{
		startDate = start;
		endDate = end; 
	}

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


		while (temp.get(Calendar.MONTH) != (endMonth - 1) || temp.get(Calendar.DAY_OF_MONTH) != (endDay) || temp.get(Calendar.YEAR) != endYear)
		{
			eventsForThisAgenda.addAll(d.getEvents(temp.get(Calendar.MONTH) + 1, temp.get(Calendar.DAY_OF_MONTH), 
					temp.get(Calendar.YEAR))); 

			temp.add(Calendar.DAY_OF_MONTH, 1);
			
			
		}


		if (eventsForThisAgenda.size() == 0)
		{
			g2.drawString("No Events scheduled in this time frame", 60, 50); 
		}
		else
		{
			
			for (int j = 0; j < eventsForThisAgenda.size(); j++)
			{

				g2.setColor(Color.LIGHT_GRAY);
				Line2D.Double hourLine = new Line2D.Double(0, (j + 1) * 60, container.getWidth(), (j + 1) * 60); 
				g2.draw(hourLine); 

				String eventDetails; 
				Event e = eventsForThisAgenda.get(j); 
				eventDetails = e.getDate() + "     "  +e.getStartTime() + "  -  " + e.getEndTime() + "  " + e.getName() + " "; 
				g2.setColor(Color.BLACK);
				g2.drawString(eventDetails, 60, ((j+1) * 60) - 10);

				height = j*60; 
			}
			

		}
	}


	public void drawHeader(GregorianCalendar c, Graphics2D g2, Component container) 
	{
		Font f = g2.getFont(); 
		FontRenderContext context = g2.getFontRenderContext(); 
		Rectangle2D bounds = f.getStringBounds("Agenda", context);

		double x = (container.getWidth() - bounds.getWidth()) / 2;

		g2.drawString("Agenda: " + startDate + " - " + endDate, (int) x, container.getHeight()/2); 
	}

	@Override
	public Dimension getGridDimension() {
	
		return new Dimension(width, height);
	}


}
