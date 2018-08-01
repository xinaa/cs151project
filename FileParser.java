import java.io.*;
import java.util.ArrayList;
/**
 * File Parser. 
 * Text file should separate inputs with ';'
 * Each new line represents a new input for an object
 * @author Kevin Le
 *
 */
public class FileParser {
	
	String filePath;
	
	/**
	 * Constructor that takes path t
	 * @param filePath Path location of text file.
	 */
	public FileParser(String filePath) {
		this.filePath = filePath;
	}
	
	/**
	 * Parses given text file and returns an Arraylist of inputs
	 * @return ArrayList of String arrays. Each array represents inputs of an object.
	 * Order of array: eventName; year; startMonth; endMonth; sequence; startHour; endHour
	 */
	ArrayList<String[]> parse(){
		ArrayList<String[]> inputs = new ArrayList<String[]>();
		InputStream ins = null;
		Reader r = null;
		BufferedReader br = null;
		try {
		    String s;
		    ins = new FileInputStream(filePath);
		    r = new InputStreamReader(ins, "UTF-8"); // leave charset out for default
		    br = new BufferedReader(r);
		    while ((s = br.readLine()) != null) {
		    	String[] settings = s.split(";");
		    	inputs.add(settings);
		    }
		}
		catch (Exception e)
		{
		    System.err.println(e.getMessage()); // handle exception
		}
		finally {
		    if (br != null) { try { br.close(); } catch(Throwable t) { /* ensure close happens */ } }
		    if (r != null) { try { r.close(); } catch(Throwable t) { /* ensure close happens */ } }
		    if (ins != null) { try { ins.close(); } catch(Throwable t) { /* ensure close happens */ } }
		}
		
		return inputs;
	}
	
}
