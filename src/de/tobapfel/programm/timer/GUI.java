package de.tobapfel.programm.timer;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class GUI implements Runnable {

	private JFrame frmShutdowntimer;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private JLabel label;
	private JLabel lblNewLabel;
	private JLabel label_1;
	private JLabel lblStunden;
	private JLabel lblMinuten;
	private JLabel lblSekunden;
	private JButton start;
	
	private boolean running = false;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					GUI window = new GUI();
					window.frmShutdowntimer.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public GUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmShutdowntimer = new JFrame();
		frmShutdowntimer.setTitle("Shutdowntimer");
		frmShutdowntimer.setFont(new Font("Dialog", Font.PLAIN, 12));
		frmShutdowntimer.setResizable(false);
		frmShutdowntimer.setBounds(100, 100, 650, 177);
		frmShutdowntimer.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		start = new JButton("Start");
		start.setBounds(453, 94, 160, 33);
		start.setFont(new Font("Tahoma", Font.PLAIN, 20));
		start.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Thread timerThread = new Thread(GUI.this);
				if (running) {
					running = false;
					GUI.this.start.setText("Start");
					textField.setText("00");
					textField_1.setText("00");
					textField_2.setText("00");
					label.setText("");

				} else {
					running = true;
					GUI.this.start.setText("Stop");
					timerThread.start();
				}

			}
		});

		textField = new JTextField();
		textField.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				textField.setText("");
			}
		});
		textField.setHorizontalAlignment(SwingConstants.CENTER);
		textField.setText("00");
		textField.setBounds(27, 51, 176, 31);
		textField.setFont(new Font("Tahoma", Font.PLAIN, 20));
		textField.setColumns(10);

		textField_1 = new JTextField();
		textField_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				textField_1.setText("");
			}
		});
		textField_1.setHorizontalAlignment(SwingConstants.CENTER);
		textField_1.setText("00");
		textField_1.setBounds(240, 51, 176, 31);
		textField_1.setFont(new Font("Tahoma", Font.PLAIN, 20));
		textField_1.setColumns(10);

		textField_2 = new JTextField();
		textField_2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				textField_2.setText("");
			}
		});
		textField_2.setText("00");
		textField_2.setHorizontalAlignment(SwingConstants.CENTER);
		textField_2.setBounds(453, 51, 160, 31);
		textField_2.setFont(new Font("Tahoma", Font.PLAIN, 20));
		textField_2.setColumns(10);

		label = new JLabel("                 ");
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setBounds(240, 98, 176, 25);
		label.setFont(new Font("Tahoma", Font.PLAIN, 20));

		lblNewLabel = new JLabel(":");
		lblNewLabel.setBounds(218, 54, 7, 25);
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 20));

		label_1 = new JLabel(":");
		label_1.setBounds(431, 54, 7, 25);
		label_1.setFont(new Font("Tahoma", Font.BOLD, 20));
		frmShutdowntimer.getContentPane().setLayout(null);

		lblStunden = new JLabel("Stunden");
		lblStunden.setBounds(27, 16, 176, 25);
		lblStunden.setFont(new Font("Tahoma", Font.PLAIN, 20));
		frmShutdowntimer.getContentPane().add(lblStunden);
		frmShutdowntimer.getContentPane().add(textField);
		frmShutdowntimer.getContentPane().add(lblNewLabel);
		frmShutdowntimer.getContentPane().add(textField_1);
		frmShutdowntimer.getContentPane().add(label_1);
		frmShutdowntimer.getContentPane().add(label);
		frmShutdowntimer.getContentPane().add(textField_2);
		frmShutdowntimer.getContentPane().add(start);

		lblMinuten = new JLabel("Minuten");
		lblMinuten.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblMinuten.setBounds(240, 18, 176, 20);
		frmShutdowntimer.getContentPane().add(lblMinuten);

		lblSekunden = new JLabel("Sekunden");
		lblSekunden.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblSekunden.setBounds(453, 20, 160, 20);
		frmShutdowntimer.getContentPane().add(lblSekunden);
	}

	@Override
	public void run() {
		int hour = Integer.valueOf(textField.getText());
		int min = Integer.valueOf(textField_1.getText());
		int sec = Integer.valueOf(textField_2.getText());
		time zeit = new time(hour, min, sec);
		zeit.normalizeTime();
		int second = zeit.toSecond();
		for (int i = second; i >= 0; i--) {
			if (!running)
				return;
			zeit = time.normalizeSec(i);
			label.setText(zeit.toString());
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		try {
			shutdown();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
	}

	public static void shutdown() throws RuntimeException, IOException {
		String shutdownCommand;
		String operatingSystem = System.getProperty("os.name");

		if (operatingSystem.contains("Linux")) {
			shutdownCommand = "shutdown -h now";
		} else if (operatingSystem.contains("Windows")) {
			shutdownCommand = "shutdown.exe -s -t 0";
		} else

		{
			throw new RuntimeException("Unsupported operating system.");
		}

		Runtime.getRuntime().exec(shutdownCommand);
		System.exit(0);
	}
}
