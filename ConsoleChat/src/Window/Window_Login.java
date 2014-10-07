package Window;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import User.LoginSession;
import Utility.Logger;

public class Window_Login extends Window{
	
	private LoginSession ls;
	private boolean hasResult;
	
	public Window_Login(String address){
		super();
		hasResult = false;
		
		//Declarations
		final JLabel labelUsername = new JLabel("Username");
		final JLabel labelPassword = new JLabel("Password");
		final JTextField fieldUsername = new JTextField(TEXT_FIELD_LENGTH);
		final JPasswordField fieldPassword = new JPasswordField(TEXT_FIELD_LENGTH);
		final JButton buttonLogin = new JButton("Login");
		
		JPanel panelData = new JPanel();{
			panelData.setLayout(new FlowLayout());
			JPanel panelLabel = new JPanel();{
				panelLabel.setLayout(new BoxLayout(panelLabel, BoxLayout.Y_AXIS));
				panelLabel.add(labelUsername);
				panelLabel.add(labelPassword);
			}
			panelData.add(panelLabel);
			JPanel panelField = new JPanel();{
				panelField.setLayout(new BoxLayout(panelField, BoxLayout.Y_AXIS));
				panelField.add(fieldUsername);
				panelField.add(fieldPassword);
			}
			panelData.add(panelField);
		}
		
		//Action listeners
		buttonLogin.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				String username = fieldUsername.getText();
				if(username.length()<6){
					Logger.showWarning("Username must not be shorter than 6 characters");
					return;
				}
				String password = new String(fieldPassword.getPassword());
				if(password.length()<4){
					Logger.showWarning("Password must not be shorter than 4 characters");
					return;
				}
				ls = new LoginSession();
				ls.setUsername(username);
				ls.setPassword(password);
				frame.setVisible(false);
				frame.dispose();
				hasResult = true;
			}
		});
		KeyListener tmp = new KeyListener(){
			@Override public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER){
					buttonLogin.doClick();
				}
			}
			@Override public void keyReleased(KeyEvent e) {}
			@Override public void keyTyped(KeyEvent e) {}
		};
		fieldPassword.addKeyListener(tmp);
		fieldUsername.addKeyListener(tmp);
		
		frame.add(panelData);
		frame.add(buttonLogin);
		frame.setTitle("Logging into " + address);
		frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
		frame.pack();
		finalize();
	}

	public boolean hasResult() {
		return hasResult;
	}
	
	public LoginSession getSession(){
		return ls;
	}
}