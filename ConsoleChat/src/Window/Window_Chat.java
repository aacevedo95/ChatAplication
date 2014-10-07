package Window;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import Network.ServerConnection;

public class Window_Chat extends Window{

	private ServerConnection connection;
	private JTextArea area;
	private JList<String> userList;

	public Window_Chat(ServerConnection sc) {
		super();
		connection = sc;

		//Declarations
		area = new JTextArea(20,40);
		userList = new JList<String>();
		final JScrollPane scrollPane = new JScrollPane(area);
		final JScrollPane userScroll = new JScrollPane(userList);
		final JTextField input = new JTextField(35);
		final JButton send = new JButton("Send");
		final JLabel listLabel = new JLabel("User list");
		final JButton sendUsrMsg = new JButton("Message");
		final JButton disconnect = new JButton("Disconnect");

		JPanel chatPanel = new JPanel();{
			chatPanel.setLayout(new FlowLayout());
			area.setEditable(false);
			chatPanel.add(scrollPane);
			JPanel userPanel = new JPanel();{
				userPanel.setLayout(new BoxLayout(userPanel, BoxLayout.Y_AXIS));
				JPanel labelPanel = new JPanel();{
					labelPanel.add(listLabel);
					userPanel.add(labelPanel);
				}
				userScroll.setPreferredSize(new Dimension(175,280));
				userPanel.add(userScroll);
				userPanel.add(sendUsrMsg);
			}
			chatPanel.add(userPanel);
		}
		frame.add(chatPanel);
		JPanel inputPanel = new JPanel();{
			inputPanel.setLayout(new FlowLayout());
			inputPanel.add(input);
			send.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent arg0) {
					//DO CODE
				}
			});
			inputPanel.add(send);
			JPanel disconnectPanel = new JPanel();{
				disconnectPanel.add(disconnect);
			}
			inputPanel.add(disconnectPanel);
		}
		frame.add(inputPanel);

		/*
		 * Action listeners
		 */
		disconnect.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				//DO CODE
			}
		});
		sendUsrMsg.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String user = userList.getSelectedValue();
				if(user!=null){
					input.setText("/msg " + user + " ");
				}
			}
		});
		input.addKeyListener(new KeyListener(){
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER){
					String msg = input.getText();
					if(msg!=null)connection.sendMessage(msg);;
				}
			}
			@Override public void keyReleased(KeyEvent e) {}
			@Override public void keyTyped(KeyEvent e) {}
		});

		/*
		 * Frame properties
		 */
		frame.setTitle("Chat window");
		frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
		frame.pack();
		finalize();
	}

	public void refreshUsers(String[] u){
		userList.setListData(u);
	}

	public void write(String msg){
		area.append(msg + '\n');
	}
}