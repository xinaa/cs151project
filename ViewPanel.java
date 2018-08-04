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

/**
 * Context implementing view strategy. ViewPanel takes a calendar, datamodel, and view strategy and builds a JPanel by calling methods
 * of the viewStrategy. ViewStrategy can have it's calendar and viewStrategy updated. ViewPanel also listens for data changes from the model. 
 * @author main: Christina Andrade collaborators: Kevin Le, Samantha Jaime, Amala Chirayil
 **/ 

public class ViewPanel extends JPanel implements ChangeListener{

	private ViewStrategy viewStrat; 
	private GregorianCalendar calendar; 
	private DataModel events; 
	
	private JPanel eventGrid; 
	private JPanel header; 
	private JScrollPane scroll; 
	
	static final Dimension SCROLL_DIM = new Dimension(1000, 840); 
	
	/**
	 * Constructs the view Panel 
	 * @param v the viewStrategy the panel should refer to for its look and feel
	 * @param c the Calendar of the date the viewStrategy should present in some form
	 * @param d the data model containing the events that should be presented by the viewPanel
	 */
	public ViewPanel(ViewStrategy v, GregorianCalendar c, DataModel d)
	{
		viewStrat = v; 
		calendar = c; 
		events = d; 
		
		
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
	
		//Create GridPanel showing events by referring to the implemented concrete strategy 
		eventGrid = new JPanel()
				{
					public void paintComponent(Graphics g)
					{
						super.paintComponent(g);
						Graphics2D g2 = (Graphics2D) g; 
						
						viewStrat.drawEventGrid(calendar, events, g2, this);
					}
				};
				
		//Set formatting of eventGrid
		eventGrid.setPreferredSize(new Dimension(viewStrat.getGridDimension()));
		eventGrid.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
		eventGrid.setBackground(Color.WHITE);
		
		//Set up a scroll panel and add eventGrid
		int horizontalPolicy = JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED;
		int verticalPolicy = JScrollPane.VERTICAL_SCROLLBAR_ALWAYS;
		scroll = new JScrollPane(eventGrid, verticalPolicy, horizontalPolicy); 

		
		//Create the header JPanel by referring to implemented concrete strategy 
		header = new JPanel()
		{
			public void paintComponent(Graphics g)
			{
				super.paintComponent(g);
				Graphics2D g2 = (Graphics2D) g; 
				
				viewStrat.drawHeader(calendar, g2, this);
			}
		};
		
		//Format the header panel
		header.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
		header.setBackground(Color.WHITE);
		header.setPreferredSize(new Dimension(this.getWidth(), 50));
		header.setMinimumSize(new Dimension(this.getWidth(), 50));
		
		//Add components to this
		this.add(header); 
		this.add(scroll);
	}

	/**
	 * Updates the viewStrategy to a concrete strategy 
	 * @param v the concrete ViewStrategy to update to 
	 */
	public void changeView(ViewStrategy v)
	{
		viewStrat = v; 
		this.repaint(); 
	}
	
	/**
	 * Override = get the dimension from the current concrete ViewStrategy and updates the eventGrid to that dimension,sets scroll panes viewPort
	 * to ensure the scroll pane will have the correct dimensions for the concrete ViewStrategy being used 
	 * @param g the Graphics package
	 */
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		eventGrid.setPreferredSize(viewStrat.getGridDimension());

		scroll.setViewportView(eventGrid);
		
	}

	@Override
	/**
	 * Listens for changes from the data model and repaints this to update its view and presentation of events 
	 */
	public void stateChanged(ChangeEvent e) {
		// change to data model prompts repaint of the view
		this.repaint(); 
	}
	
	/**
	 * Updates the calendar to be shown by the ViewPanel 
	 * @param c the new calendar that should be shown in the view
	 */
	public void updateCalendar(GregorianCalendar c)
	{
		calendar = c; 
		this.repaint(); 
	}
	
}
