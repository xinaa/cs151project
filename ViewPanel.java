import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.GregorianCalendar;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class ViewPanel extends JPanel implements ChangeListener{

	private ViewStrategy viewStrat; 
	private GregorianCalendar calendar; 
	private DataModel events; 
	
	
	public ViewPanel(ViewStrategy v, GregorianCalendar c, DataModel d)
	{
		viewStrat = v; 
		calendar = c; 
		events = d; 
		
		
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
	
		//Create Grid showing events
		JPanel eventGrid = new JPanel()
				{
					public void paintComponent(Graphics g)
					{
						super.paintComponent(g);
						Graphics2D g2 = (Graphics2D) g; 
						
						viewStrat.drawEventGrid(calendar, events, g2, this);
					}
				};
				
		eventGrid.setPreferredSize(new Dimension(1000,1440));
		eventGrid.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
		eventGrid.setBackground(Color.WHITE);
		
		//Set up a scroll panel and add eventGrid
		int horizontalPolicy = JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED;
		int verticalPolicy = JScrollPane.VERTICAL_SCROLLBAR_ALWAYS;
		JScrollPane scroll = new JScrollPane(eventGrid, verticalPolicy, horizontalPolicy); 
		scroll.setMinimumSize(new Dimension(1000,840));
		scroll.setPreferredSize(new Dimension(1000,840));
		
		JPanel header = new JPanel()
		{
			public void paintComponent(Graphics g)
			{
				super.paintComponent(g);
				Graphics2D g2 = (Graphics2D) g; 
				
				viewStrat.drawHeader(calendar, g2, this);
			}
		};
		
		header.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
		header.setBackground(Color.WHITE);
		
		header.setPreferredSize(new Dimension(this.getWidth(), 50));
		header.setMinimumSize(new Dimension(this.getWidth(), 50));
		
		this.add(header); 
		this.add(scroll);
	}

	public void changeView(ViewStrategy v)
	{
		viewStrat = v; 
		this.repaint(); 
	}
	

	@Override
	public void stateChanged(ChangeEvent e) {
		// change to data model prompts repaint of the view
		this.repaint(); 
	}
	
	public void updateCalendar(GregorianCalendar c)
	{
		calendar = c; 
		this.repaint(); 
	}
	
}