/*
 * ChessMainWindow.java
 *
 * Created on October 4, 2005, 9:35 PM
 */
 
/**
 *
 * @author David Souther
 */

import java.awt.*;
import javax.swing.*;
import java.util.*;

public class ChessMainFrame extends JFrame{
    private static final int FRAME_WIDTH     = 900;
    private static final int FRAME_HEIGHT    = 750;
    private static final int FRAME_X_ORIGIN  = 200;
    private static final int FRAME_Y_ORIGIN  = 100;
    
    private static ResourceBundle   bundle = null;
    private static Locale			locale;
    private static String			imagePath;
	
    /** Creates a new instance of ChessMainWindow */
    public ChessMainFrame() {
        super(ChessMainFrame.getString("title"));
		setSize(FRAME_WIDTH, FRAME_HEIGHT);
		setLocation(FRAME_X_ORIGIN, FRAME_Y_ORIGIN);
		//setTitle("Chess");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setDefaultLookAndFeelDecorated(false);
		ChessController control = new ChessController(this);
//		pack();
		moveToCenter();
//		setVisible(true);
    }
	
    public static String getString(String key) {
		String value = null;
		try {
                    value = getResourceBundle().getString(key);
		} catch (MissingResourceException e) {
                    System.out.println("java.util.MissingResourceException: Couldn't find value for: " + key);
		}
		if(value == null) {
                    value = "Could not find resource: " + key + "  ";
		}
		return value;
    }

    public static ResourceBundle getResourceBundle() {
		if(bundle == null) {
                    bundle = ResourceBundle.getBundle("chessProperties", locale);
		}
		return bundle;
    }	

    private void moveToCenter( )
    {
         Toolkit toolkit = Toolkit.getDefaultToolkit();
         
         Dimension screenSize  = toolkit.getScreenSize();
         Dimension selfBounds  = getSize();
         
         setLocation((screenSize.width - selfBounds.width) / 2,
                     (screenSize.height - selfBounds.height) / 2);
    }	
    
    static public void main(String [] args) {
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
            //UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
            //UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
            //UIManager.setLookAndFeel("javax.swing.plaf.basic.BasicLookAndFeel");
            //UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
            //UIManager.setLookAndFeel("javax.swing.plaf.multi.MultiLookAndFeel");
        } catch (Exception e) {e.printStackTrace();}	

        String language;
        String country;

        if (args.length > 0){
            imagePath = new String(args[0]);
        }
        if (args.length != 3) {
            language = new String("en");
            country = new String("US");
        } else {
            language = new String(args[1]);
            country = new String(args[2]);
        }
        locale = new Locale(language, country);
        System.out.println(locale);

        ChessMainFrame CMF = new ChessMainFrame();
        CMF.setVisible(true);
    }    
}
