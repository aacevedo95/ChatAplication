package Window;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import User.RegistrationSession;
import Utility.Logger;

public class Window_Register extends Window{
	
	private RegistrationSession rs;
	private boolean hasResult;
	
	public Window_Register(){
		super();
		hasResult = true;
		
		JButton register = new JButton("Register");
		JLabel labelUsername = new JLabel("Username");
		JLabel labelPassword = new JLabel("Password");
		JLabel labelEmail = new JLabel("Email");
		JLabel labelGroup = new JLabel("Group");
		final JTextField fieldUsername = new JTextField(TEXT_FIELD_LENGTH);
		final JTextField fieldPassword = new JTextField(TEXT_FIELD_LENGTH);
		final JTextField fieldEmail = new JTextField(TEXT_FIELD_LENGTH);
		final JTextField fieldGroup = new JTextField(TEXT_FIELD_LENGTH);
		
		register.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				String username = fieldUsername.getText();
				if(username.equals("")){
					Logger.showWarning("Empty username field");
					return;
				}
				String password = fieldPassword.getText();
				if(password.length()<6){
					Logger.showWarning("Password cannot be less than 6 characters long");
					return;
				}
				String email = fieldEmail.getText();
				String group = fieldGroup.getText();
				rs = new RegistrationSession();
				rs.setUsername(username);
				rs.setPassword(password);
				rs.setEmail(email);
				rs.setGroup(group);
				hasResult = true;
			}
		});
		
		JPanel dataPanel = new JPanel();{
			dataPanel.setLayout(new FlowLayout());
			JPanel labelPanel = new JPanel();{
				labelPanel.setLayout(new BoxLayout(labelPanel, BoxLayout.Y_AXIS));
				labelPanel.add(labelUsername);
				labelPanel.add(labelPassword);
				labelPanel.add(labelEmail);
				labelPanel.add(labelGroup);
			}
			dataPanel.add(labelPanel);
			JPanel fieldPanel = new JPanel();{
				fieldPanel.setLayout(new BoxLayout(fieldPanel, BoxLayout.Y_AXIS));
				fieldPanel.add(fieldUsername);
				fieldPanel.add(fieldPassword);
				fieldPanel.add(fieldEmail);
				fieldPanel.add(fieldGroup);
			}
			dataPanel.add(fieldPanel);
		}
		frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
		frame.add(dataPanel);
		frame.add(register);
		frame.pack();
		frame.setTitle("Registration");
		finalize();
	}

	public boolean hasResult() {
		return hasResult;
	}
	
	public RegistrationSession getSession(){
		return rs;
	}
	
	public static RegistrationSession showRegistrationWindow(){
		Window_Register wr = new Window_Register();
		while(!wr.hasResult())
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				Logger.logError("Registration session interrupted");
			}
		RegistrationSession session = wr.getSession();
		wr.getFrame().dispose();
		wr = null;
		return session;
	}
}
