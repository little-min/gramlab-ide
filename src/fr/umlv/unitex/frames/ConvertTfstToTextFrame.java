/*
 * Unitex
 *
 * Copyright (C) 2001-2010 Université Paris-Est Marne-la-Vallée <unitex@univ-mlv.fr>
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA.
 *
 */

package fr.umlv.unitex.frames;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

import fr.umlv.unitex.Config;
import fr.umlv.unitex.process.Launcher;
import fr.umlv.unitex.process.commands.MultiCommands;
import fr.umlv.unitex.process.commands.Tfst2UnambigCommand;

/**
 * This class describes the FST-Text to Text conversion frame.
 * 
 * @author Sébastien Paumier
 *  
 */
public class ConvertTfstToTextFrame extends JInternalFrame {

	JTextField textName = new JTextField();

	ConvertTfstToTextFrame() {
		super("Convert Text Automaton to Text", false, true);
		setContentPane(constructPanel());
		pack();
		setResizable(false);
		setVisible(false);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addInternalFrameListener(new InternalFrameAdapter() {
			public void internalFrameClosing(InternalFrameEvent e) {
				setVisible(false);
			}
		});
	}


	private JPanel constructPanel() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBorder(new TitledBorder(
		"Output text file:"));
		Action setTextAction = new AbstractAction("Set") {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fc = Config.getFst2UnambigDialogBox();
				int returnVal = fc.showOpenDialog(null);
				if (returnVal != JFileChooser.APPROVE_OPTION) {
					// we return if the user has clicked on CANCEL
					return;
				}
				textName.setText(fc.getSelectedFile()
						.getAbsolutePath());
			}
		};
		JButton setTextButton = new JButton(setTextAction);
		textName.setPreferredSize(new Dimension(300,20));
		panel.add(textName, BorderLayout.CENTER);
		panel.add(setTextButton, BorderLayout.EAST);
		
		JPanel buttonPanel=new JPanel(new GridLayout(1,2));
		JButton OK=new JButton("OK");
		OK.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				launchFst2Unambig();
			}
		});
		JButton CANCEL=new JButton("Cancel");
		CANCEL.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		});
		buttonPanel.add(CANCEL);
		buttonPanel.add(OK);
		panel.add(buttonPanel,BorderLayout.SOUTH);
		panel.setOpaque(true);
		return panel;
	}





	void launchFst2Unambig() {
		MultiCommands commands = new MultiCommands();
		if (textName.getText().equals("")) {
			JOptionPane.showMessageDialog(null,
					"You must specify a file name", "Error",
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		Tfst2UnambigCommand cmd = new Tfst2UnambigCommand();
		File fst2=new File(Config.getCurrentSntDir(),"text.tfst");
		if (!fst2.exists()) {
			JOptionPane.showMessageDialog(null,
					"Cannot find text automaton", "Error",
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		cmd=cmd.fst2(fst2);
		File txt=new File(textName.getText());
		cmd.output(txt);		
		commands.addCommand(cmd);
		setVisible(false);
		Launcher.exec(commands, true, null,true);
	}

}