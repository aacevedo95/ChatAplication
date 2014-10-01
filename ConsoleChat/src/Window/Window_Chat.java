package Window;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Window_Chat extends Window{

	private JTextArea area;
	private JList<String> userList;

	public Window_Chat() {
		super();
		frame.setTitle("Chat window");

		//Declaration of components
		JPanel chatPanel;
		JPanel inputPanel;
		JScrollPane scrollPane;
		JScrollPane userScroll;
		JTextField input;
		JButton send;

		chatPanel = new JPanel();{
			chatPanel.setLayout(new FlowLayout());
			area = new JTextArea(20,40);
			area.setEditable(false);
			scrollPane = new JScrollPane(area);
			chatPanel.add(scrollPane);
			userList = new JList<String>();
			JPanel userPanel = new JPanel();{
				JLabel listLabel = new JLabel("User list");
				JPanel labelPanel = new JPanel();
				labelPanel.add(listLabel);
				userPanel.add(labelPanel);
				userScroll = new JScrollPane(userList);
				userScroll.setPreferredSize(new Dimension(175,280));
				userPanel.add(userScroll);
				userPanel.setLayout(new BoxLayout(userPanel, BoxLayout.Y_AXIS));
			}
			chatPanel.add(userPanel);
		}
		inputPanel = new JPanel();{
			inputPanel.setLayout(new FlowLayout());
			input = new JTextField(35);
			inputPanel.add(input);
			send = new JButton("Send");
			send.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent arg0) {
					//DO CODE
				}
			});
			inputPanel.add(send);
			JPanel disconnectPanel = new JPanel();{
				JButton disconnect = new JButton("Disconnect");
				disconnect.addActionListener(new ActionListener(){
					@Override
					public void actionPerformed(ActionEvent e) {
						//DO CODE
					}
				});
				disconnectPanel.add(disconnect);
			}
			inputPanel.add(disconnectPanel);
		}
		frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
		frame.add(chatPanel);
		frame.add(inputPanel);
		frame.pack();
		finalize();
	}

	public void write(String msg){
		area.append(msg);
	}
}