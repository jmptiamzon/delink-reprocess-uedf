package com.sprint.delink.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import com.sprint.delink.controller.Controller;

public class View extends JFrame implements ActionListener {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel panel;
	private JTextArea textAreaField;
	private JButton submitBtn;
	private JLabel textAreaLbl;
	private JScrollPane scrollPane;
	private Controller controller;
	private GridBagConstraints gbc;
	private Insets inset;
	
	
	public View() {
		try {
			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		controller = new Controller();
		inset = new Insets(5, 5, 5, 5);
		gbc = new GridBagConstraints();
		panel = new JPanel(new GridBagLayout());
		
		textAreaField = new JTextArea();
		textAreaLbl = new JLabel("Enter filename/s: ");
		
		submitBtn = new JButton("Submit");
		submitBtn.addActionListener(this);
		
		scrollPane = new JScrollPane(textAreaField);
		
		setLayout(new BorderLayout());
		setResizable(false);
		setTitle("Delink Reprocess UEDF");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		add(panel, BorderLayout.CENTER);
		setComponent(textAreaLbl, panel, 1, 0, 0, 0, 0);
		setComponent(scrollPane, panel, 1, 0, 1, 100, 400);	
		setComponent(submitBtn, panel, 1, 0, 2, 0, 0);
		
		pack();
		setLocationRelativeTo(null);
		
	}
	
	public void setComponent (Component component, JPanel panel, double weightx, int gridx, int gridy, int ipady, int ipadx) {
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = inset;
		gbc.weightx = weightx;
		gbc.gridx = gridx;
		gbc.gridy = gridy;
		gbc.ipadx = ipadx;
		gbc.ipady = ipady;
		panel.add(component, gbc);
	}
	
	@Override
	public void actionPerformed(ActionEvent event) {
		
		if (event.getSource() == submitBtn) {
			controller.runApp(textAreaField.getText());
		}
		// TODO Auto-generated method stub
		
	}

}
