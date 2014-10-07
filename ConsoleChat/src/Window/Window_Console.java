package Window;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import Utility.Logger;

public class Window_Console extends Window{
	
	private JTextArea area;

	public Window_Console() {
		super();
		frame.setTitle("Console log");
		//
		frame.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				Logger.disable();
			}
		});
		//
		area = new JTextArea();
		area.setEditable(false);
		area.setBackground(Color.black);
		area.setForeground(Color.white);
		JScrollPane pane = new JScrollPane(area);
		pane.setPreferredSize(new Dimension(700,450));
		frame.add(pane);
		frame.pack();
		PrintStream out = new PrintStream(new SystemTextAreaOutputStream(area));
		System.setOut(out);
		System.setErr(out);
		setVisible(true);
		center();
	}
	
	public void setVisible(boolean b){
		frame.setVisible(b);
	}
	
	public JTextArea getArea(){
		return area;
	}
}

class SystemTextAreaOutputStream extends OutputStream {
	
    private JTextArea area;
    
    public SystemTextAreaOutputStream( JTextArea box ) {
        area = box;
    }
    
    public void write(int b) throws IOException{
        area.append(Character.toString((char)b));
    }  
}