import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

public class YearView implements ViewStrategy{

	private static final String[] arrayOfMonths = {"January", "February","March","April","May","June",
			"July", "August", "September", "October", "November", "December" };
	private static final String[] arrayOfDays = {"S", "M", "T", "W", "T", "F", "S" }; 

	static final int DAYS_IN_WEEK = 7; 
	static final int X_BUFFER = 36; 
	final static int PADDING = 50; 

	@Override
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

	public static void drawMonth(GregorianCalendar c, Graphics2D g2, int x, int y)
	{



		g2.setColor(Color.BLACK);

		GregorianCalendar dateOfRun = new GregorianCalendar(); 
		x = x + PADDING; 
		y = y + PADDING; 
		int initialX = x; 

		GregorianCalendar temp = new GregorianCalendar(c.get(Calendar.YEAR), c.get(Calendar.MONTH), 1); 
		//Not necessary to create GC object, could store as an int.... 
		GregorianCalendar monthTracker = new GregorianCalendar(c.get(Calendar.YEAR), c.get(Calendar.MONTH), 1); 

		//get the day of the week of the temporary calendar 
		int weekDayIndex = temp.get(Calendar.DAY_OF_WEEK);

		//calculation to help move the calendar back to the first day of the week so calendar starts on the
		//correct date
		int backTrack = 1 - weekDayIndex;

		//move the calendar to the start of the week 
		temp.add(Calendar.DAY_OF_MONTH, backTrack); 

		Font defaultfont = new JLabel().getFont(); 
		Font monthFont = new Font(defaultfont.toString(), Font.PLAIN, 18);
		g2.setFont(monthFont);
		Font f = g2.getFont(); 
		FontRenderContext context = g2.getFontRenderContext(); 




		g2.drawString(arrayOfMonths[c.get(Calendar.MONTH)], x + 3, y);
		Rectangle2D bounds = f.getStringBounds(arrayOfMonths[c.get(Calendar.MONTH)], context);

		for( String s : arrayOfDays)
		{
			g2.drawString(s, x + 6 , (int) (y + bounds.getHeight()));
			x += X_BUFFER;
		}


		//Calendar shows 42 days (6 weeks) 
		int yBuffer = 5; 
		int heightIncrement = 1; 

		for (int i = 0; i < 42; i++)
		{

			if( i % DAYS_IN_WEEK== 0)
			{
				heightIncrement ++; 
				x = initialX; 
			}

			String dayNumber = Integer.toString(temp.get(Calendar.DAY_OF_MONTH)); 
			int digitPadding;
			if (dayNumber.length() < 2)
			{
				digitPadding = 6; 
			}
			else
			{
				digitPadding = 0; 
			}



			if (temp.get(Calendar.MONTH) != monthTracker.get(Calendar.MONTH))
			{
				g2.setColor(Color.GRAY);
			}
			else if (temp.get(Calendar.DAY_OF_MONTH) == dateOfRun.get(Calendar.DAY_OF_MONTH) && (temp.get(Calendar.MONTH) == dateOfRun.get(Calendar.MONTH) && 
					temp.get(Calendar.YEAR) == dateOfRun.get(Calendar.YEAR)))
			{
				g2.setColor(Color.RED);
			}
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

			x += X_BUFFER; 


			temp.add(Calendar.DAY_OF_MONTH, 1);

		}


	}

	@Override
	public Dimension getGridDimension() {
		
		return new Dimension(1000,840);
	}

}
