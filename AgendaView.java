import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeSet;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.LineBorder;

public class AgendaView implements ViewStrategy
{

	//private static String[] fullNameDays = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday" };

	GregorianCalendar c; 
	DataModel d; 
	static JTextArea area;
	private String startDate; 
	private String endDate;
	
	public AgendaView(String start, String end)
	{
		startDate = start;
		endDate = end; 
	}
	
	public void drawEventGrid(GregorianCalendar c, DataModel d, Graphics2D g2, Component container)
	{
		ArrayList<Event> eventsForThisAgenda = new ArrayList<Event>();
		
		int startMonth = Integer.parseInt(startDate.substring(0,2)); 
		int startDay = Integer.parseInt(startDate.substring(3,5));
		int startYear = Integer.parseInt(startDate.substring(6));
		
		int endMonth = Integer.parseInt(endDate.substring(0,2)); 
		int endDay = Integer.parseInt(endDate.substring(3,5));
		int endYear = Integer.parseInt(startDate.substring(6));
		
		GregorianCalendar temp = new GregorianCalendar(startYear, startMonth - 1, startDay); 
		
		while (/*(temp.get(Calendar.MONTH) != (startMonth - 1) && temp.get(Calendar.DAY_OF_MONTH) != (startDay) && temp.get(Calendar.YEAR) != startYear) &&*/ 
				(temp.get(Calendar.MONTH) != (endMonth - 1) && temp.get(Calendar.DAY_OF_MONTH) != (endDay) && temp.get(Calendar.YEAR) != endYear))
		{
			eventsForThisAgenda.addAll(d.getEvents(temp.get(Calendar.MONTH), temp.get(Calendar.DAY_OF_MONTH), 
					temp.get(Calendar.YEAR))); 
			System.out.println(eventsForThisAgenda);
			temp.add(Calendar.DAY_OF_MONTH, 1);
		}
		
		int i = 0; 
		for (Event e : eventsForThisAgenda)
		{

			g2.setColor(Color.LIGHT_GRAY);
			Line2D.Double hourLine = new Line2D.Double(0, i * 60, container.getWidth(), i * 60); 
			g2.draw(hourLine); 

			String eventDetails; 
			eventDetails = e.getDate() + " " + e.getName() + " " + e.getStartTime() + " " + e.getEndTime(); 
			g2.setColor(Color.BLACK);
			g2.drawString(eventDetails, 60, (i * 60) - 10);

		}
	}
		
			/*System.out.println(eventsForThisAgenda);
			int i = 0; 
			for (Event e : eventsForThisAgenda)
			{
				
				g2.setColor(Color.LIGHT_GRAY);
				Line2D.Double hourLine = new Line2D.Double(0, i * 60, container.getWidth(), i * 60); 
				g2.draw(hourLine); 
				
				String eventDetails; 
				eventDetails = e.getDate() + " " + e.getName() + " " + e.getStartTime() + " " + e.getEndTime(); 
				g2.setColor(Color.BLACK);
				g2.drawString(eventDetails, 60, (i * 60) - 10);
				
			}*/
		//Get all events for dates specified in range
		/*
		int i = 0; 
		for (Event e : eventsForThisAgenda)
		{
			
			g2.setColor(Color.LIGHT_GRAY);
			Line2D.Double hourLine = new Line2D.Double(0, i * 60, container.getWidth(), i * 60); 
			g2.draw(hourLine); 
			
			String eventDetails; 
			eventDetails = e.getDate() + " " + e.getName() + " " + e.getStartTime() + " " + e.getEndTime(); 
			g2.setColor(Color.BLACK);
			g2.drawString(eventDetails, 60, (i * 60) - 10);
			
		}*/
		
//				for (int i = 0; i < 100; i ++)
//				{	
//
//					//Draw the Lines between events
//					g2.setColor(Color.LIGHT_GRAY);
//					Line2D.Double hourLine = new Line2D.Double(60, i * 60, container.getWidth(), i * 60); 
//					g2.draw(hourLine); 
//				}
	

	
	public void drawHeader(GregorianCalendar c, Graphics2D g2, Component container) 
	{
		Font f = g2.getFont(); 
		FontRenderContext context = g2.getFontRenderContext(); 
		Rectangle2D bounds = f.getStringBounds("Agenda", context);
		
		double x = (container.getWidth() - bounds.getWidth()) / 2;

		g2.drawString("Agenda: " + startDate + " - " + endDate, (int) x, container.getHeight()/2); 
	}
	public static void setText(String newText)
    {
        area.setText(newText);
    }
}
