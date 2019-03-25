import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.swing.JLabel;

/**
 * YearView is a concrete strategy that implements the ViewStrategy interface. Defines the look and 
 * feel of the header and year grid to be plugged into a container's paintComponent method . 
 * @author Christina Andrade
 */
public class YearView implements ViewStrategy{

	private static final String[] arrayOfMonths = {"January", "February","March","April","May","June",
			"July", "August", "September", "October", "November", "December" };
	private static final String[] arrayOfDays = {"S", "M", "T", "W", "T", "F", "S" }; 

	static final int DAYS_IN_WEEK = 7; 
	static final int X_BUFFER = 36; 
	final static int PADDING = 50; 

	@Override
	/**
	 * Defines the look of the header for year view
	 * @param c GregorianCalendar holding the year to be drawn
	 * @param g2 the graphics package
	 * @param container the container that will rely on this method to define it's look and feel 
	 */
	public void drawHeader(GregorianCalendar c, Graphics2D g2, Component container) {

		Font defaultFont = new JLabel().getFont(); 
		Font yearFont = new Font(defaultFont.toString(), Font.BOLD, 36); 

		String year = ("" + c.get(Calendar.YEAR)); 

		g2.setFont(yearFont);

		Font f = g2.getFont(); 
		FontRenderContext context = g2.getFontRenderContext(); 
		Rectangle2D bounds = f.getStringBounds(year, context);

		double x = (container.getWidth() - bounds.getWidth()) / 2;
		//double y = (this.getHeight() - bounds.getHeight()) / 2;

		g2.drawString(year, (int) x, (int) ((container.getHeight()/2) + (bounds.getHeight()/2))); 

	}

	@Override
	/**
	 * Defines the look of the year view grid (the months and dates shown) 
	 * @param c GregorianCalendar holding the year to be drawn
	 * @param d the data model holding event information 
	 * @param g2 the graphics package
	 * @param container the container that will rely on this method to define it's look and feel 
	 */
	public void drawEventGrid(GregorianCalendar c, DataModel d, Graphics2D g2, Component container) {

		GregorianCalendar cal = new GregorianCalendar(c.get(Calendar.YEAR), 0, 1);

		int containerWidth = container.getWidth(); 
		//int containerWidth = 1000;  
		int containerHeight = 840;  



		for (int i = 0; i < 4; i++)
		{

			for (int j = 0; j < 3; j++)
			{
				drawMonth(cal, g2, containerWidth/3 * j, containerHeight/4 * i);
				cal.add(Calendar.MONTH, 1);
			}
		}

	}

	/**
	 * Helper method that draws a month based on a given calendar
	 * @param c the calendar whose month should be drawn
	 * @param g2 the graphics package
	 * @param x the x coordinate the month should be drawn at
	 * @param y the y coordinate the month should be drawn at
	 */
	public static void drawMonth(GregorianCalendar c, Graphics2D g2, int x, int y)
	{
		//Set the color of the graphics package
		g2.setColor(Color.BLACK);

		//Get the date at runtime 
		GregorianCalendar dateOfRun = new GregorianCalendar(); 
		
		//Set x and y coordinates to include padding for proper formatting
		x = x + PADDING; 
		y = y + PADDING; 
		
		//Keep a record of the starting x coordinate (including padding) 
		int initialX = x; 

		//Set up a GregorianCalendar that can be incremented 
		GregorianCalendar temp = new GregorianCalendar(c.get(Calendar.YEAR), c.get(Calendar.MONTH), 1); 
		
		//GregorianCalendar calendar to keep track of the month being drawn  
		GregorianCalendar monthTracker = new GregorianCalendar(c.get(Calendar.YEAR), c.get(Calendar.MONTH), 1); 

		//get the day of the week of the temporary calendar 
		int weekDayIndex = temp.get(Calendar.DAY_OF_WEEK);

		//calculation to help move the calendar back to the first day of the week so calendar starts on the
		//correct date
		int backTrack = 1 - weekDayIndex;

		//move the calendar to the start of the week 
		temp.add(Calendar.DAY_OF_MONTH, backTrack); 

		//Set fonts, and set up to get font sizing 
		Font defaultfont = new JLabel().getFont(); 
		Font monthFont = new Font(defaultfont.toString(), Font.PLAIN, 18);
		g2.setFont(monthFont);
		Font f = g2.getFont(); 
		FontRenderContext context = g2.getFontRenderContext(); 

		//Draw the name of the month 
		g2.drawString(arrayOfMonths[c.get(Calendar.MONTH)], x + 3, y);
		Rectangle2D bounds = f.getStringBounds(arrayOfMonths[c.get(Calendar.MONTH)], context);

		//Draw the days of the week 
		for( String s : arrayOfDays)
		{
			g2.drawString(s, x + 6 , (int) (y + bounds.getHeight()));
			x += X_BUFFER;
		}

		//Calendar shows 42 days (6 weeks) 
		int yBuffer = 5; 
		int heightIncrement = 1; 

		//Draws 6 rows of dates both in the month to be drawn and 
		// directly before or after the month (if necessary) 
		for (int i = 0; i < 42; i++)
		{

			//Move to the next row, reset X
			if( i % DAYS_IN_WEEK== 0)
			{
				heightIncrement ++; 
				x = initialX; 
			}

			//Get the day number 
			String dayNumber = Integer.toString(temp.get(Calendar.DAY_OF_MONTH)); 
			int digitPadding;
			
			//Adjust padding if the day number is less than 2 digits 
			if (dayNumber.length() < 2)
			{
				digitPadding = 6; 
			}
			else
			{
				digitPadding = 0; 
			}

			//If the day being drawn is not part of the current month being drawn 
			if (temp.get(Calendar.MONTH) != monthTracker.get(Calendar.MONTH))
			{
				g2.setColor(Color.GRAY);
			}
			
			//The date of run is marked in red 
			else if (temp.get(Calendar.DAY_OF_MONTH) == dateOfRun.get(Calendar.DAY_OF_MONTH) && (temp.get(Calendar.MONTH) == dateOfRun.get(Calendar.MONTH) && 
					temp.get(Calendar.YEAR) == dateOfRun.get(Calendar.YEAR)))
			{
				g2.setColor(Color.RED);
			}
			
			//Days in the current month being drawn are drawn in black
			else
			{
				g2.setColor(Color.BLACK);
			}

	
			if (i % DAYS_IN_WEEK == 0)
			{	
				g2.drawString(dayNumber, digitPadding + x , (int) (yBuffer + y + bounds.getHeight() * heightIncrement));
			}
			else
			{
				g2.drawString(dayNumber, digitPadding + x , (int) (yBuffer + y + bounds.getHeight() * heightIncrement));
			}

			//Increment the X coordinate for the next day to be drawn
			x += X_BUFFER; 

			//increment the temporary calendar by a day 
			temp.add(Calendar.DAY_OF_MONTH, 1);

		}


	}

	@Override
	/**
	 * Returns the dimension of the grid that the component containing the grid should be set to 
	 * @return new Dimension  
	 */
	public Dimension getGridDimension() {
		
		return new Dimension(1000,840);
	}

}
