 /*
  * Unitex
  *
  * Copyright (C) 2001-2007 Universit� de Marne-la-Vall�e <unitex@univ-mlv.fr>
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

package fr.umlv.unitex.conversion;

import java.awt.*;
import java.awt.event.*;
import java.io.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;

import fr.umlv.unitex.*;
import fr.umlv.unitex.exceptions.*;
import fr.umlv.unitex.process.*;

/**
 * This class provides a file transcoding internal frame.
 * 
 * @author S�bastien Paumier
 */
public class ConversionFrame extends JInternalFrame {

	public final static String[] srcEncodings = {"CZECH", "ENGLISH", "FRENCH",
			"GERMAN", "GREEK", "ITALIAN", "KOREAN", "NORWEGIAN", "PORTUGUESE", "SPANISH",
			"THAI", "LATIN", "windows-1252", "windows-1250", "windows-1257",
			"windows-1251", "windows-1254", "windows-1258", 
			"windows-949", "iso-8859-1",
			"iso-8859-15", "iso-8859-2", "iso-8859-3", "iso-8859-4",
			"iso-8859-5", "iso-8859-7", "iso-8859-9", "iso-8859-10",
			"next-step", "LITTLE-ENDIAN", "BIG-ENDIAN"};

	public final static String[] destEncodings = {"LITTLE-ENDIAN", "CZECH",
			"ENGLISH", "FRENCH", "GERMAN", "GREEK", "ITALIAN", "KOREAN", "NORWEGIAN",
			"PORTUGUESE", "SPANISH", "THAI", "LATIN", "windows-1252",
			"windows-1250", "windows-1257", "windows-1251", "windows-1254",
			"windows-1258", 
			"windows-949", "iso-8859-1", "iso-8859-15", "iso-8859-2",
			"iso-8859-3", "iso-8859-4", "iso-8859-5", "iso-8859-7",
			"iso-8859-9", "iso-8859-10", "next-step", "BIG-ENDIAN", "UTF-8"};

	static ConversionFrame frame = null;
	JList srcEncodingList = new JList(srcEncodings);
	JList destEncodingList = new JList(destEncodings);
	JRadioButton replace = new JRadioButton("Replace");
	JRadioButton renameSourceWithPrefix = new JRadioButton(
			"Rename source with prefix");
	JRadioButton renameSourceWithSuffix = new JRadioButton(
			"Rename source with suffix");
	JRadioButton nameDestWithPrefix = new JRadioButton(
			"Name destination with prefix");
	JRadioButton nameDestWithSuffix = new JRadioButton(
			"Name destination with suffix");
	JTextField prefixSuffix = new JTextField("");
	public DefaultListModel listModel = new DefaultListModel();
	JList fileList = new JList(listModel);
	private JButton addFiles = new JButton("Add Files");
	private JButton removeFiles = new JButton("Remove Files");
	private JButton transcode = new JButton("Transcode");
	private JButton cancel = new JButton("Cancel");

	public ConversionFrame() {
		super("Transcode Files", false, true);
		setContentPane(constructPanel());
		setBounds(100, 100, 500, 500);
		setResizable(true);
		setVisible(false);
		addInternalFrameListener(new InternalFrameAdapter() {
			public void internalFrameClosing(InternalFrameEvent e) {
				setVisible(false);
			}
		});
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
	}

	/**
	 * Initializes the frame.
	 *  
	 */
	private static void init() {
		frame = new ConversionFrame();
		UnitexFrame.addInternalFrame(frame);
	}


	/**
	 * Gives an encoding value that can be used as a parameter for the
	 * <code>Convert</code> program.
	 * 
	 * Example: "Russian" => "windows-1251"
	 * 
	 * @param language
	 * @return a <code>String</code> that represents the encoding value for
	 *         the given language
	 */
	public static String getEncodingForLanguage(String language) {
		if (language.equals("Portuguese (Brazil)")
				|| language.equals("Portuguese (Portugal)"))
			return "PORTUGUESE";

		else if (language.equals("English"))
			return "ENGLISH";

		else if (language.equals("Finnish"))
			return "iso-8859-1";

		else if (language.equals("French"))
			return "FRENCH";

		else if (language.equals("Greek (Modern)"))
			return "GREEK";

		else if (language.equals("Italian"))
			return "ITALIAN";

		else if (language.equals("Norwegian"))
			return "NORWEGIAN";

		else if (language.equals("Russian"))
			return "windows-1251";

		else if (language.equals("Spanish"))
			return "SPANISH";

		else if (language.equals("Thai"))
			return "THAI";

		else {
			// by default, we chose the latin codepage
			return "LATIN";
		}
	}

	/**
	 * Shows the frame
	 *  
	 */
	public static void showFrame() {
		if (frame == null) {
			init();
		}
		frame.srcEncodingList.setSelectedValue(getEncodingForLanguage(Config
				.getCurrentLanguage()), true);
		frame.destEncodingList.setSelectedValue("LITTLE-ENDIAN", true);
		frame.listModel.removeAllElements();
		frame.setVisible(true);
		try {
			frame.setSelected(true);
			frame.setIcon(false);
		} catch (java.beans.PropertyVetoException e2) {
			e2.printStackTrace();
		}
	}

	private JPanel constructPanel() {
		JPanel panel = new JPanel(new BorderLayout());
		JPanel tmp = new JPanel(new GridLayout(1, 2));
		tmp.add(constructSrcEncodingPanel());
		tmp.add(constructDestEncodingPanel());
		JPanel up = new JPanel(new BorderLayout());
		up.add(tmp, BorderLayout.CENTER);
		up.add(constructFileNamePanel(), BorderLayout.EAST);
		panel.add(up, BorderLayout.NORTH);
		JPanel down = new JPanel(new BorderLayout());
		down.add(constructFileListPanel(), BorderLayout.CENTER);
		down.add(constructButtonPanel(), BorderLayout.EAST);
		panel.add(down, BorderLayout.CENTER);
		return panel;
	}

	private JPanel constructSrcEncodingPanel() {
		JPanel srcEncodingPanel = new JPanel(new BorderLayout());
		srcEncodingPanel
				.add(new JLabel("Source encoding:"), BorderLayout.NORTH);
		srcEncodingList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		srcEncodingPanel.add(new JScrollPane(srcEncodingList),
				BorderLayout.CENTER);
		return srcEncodingPanel;
	}

	private JPanel constructDestEncodingPanel() {
		JPanel destEncodingPanel = new JPanel(new BorderLayout());
		destEncodingPanel.add(new JLabel("Destination encoding:"),
				BorderLayout.NORTH);
		destEncodingList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		destEncodingPanel.add(new JScrollPane(destEncodingList),
				BorderLayout.CENTER);
		return destEncodingPanel;
	}

	private JPanel constructFileNamePanel() {
		JPanel fileNamePanel = new JPanel(new GridLayout(7, 1));
		fileNamePanel.setBorder(new EmptyBorder(2, 2, 2, 2));
		fileNamePanel.add(replace);
		fileNamePanel.add(renameSourceWithPrefix);
		fileNamePanel.add(renameSourceWithSuffix);
		fileNamePanel.add(nameDestWithPrefix);
		fileNamePanel.add(nameDestWithSuffix);
		fileNamePanel.add(new JLabel("Prefix/suffix:"));
		fileNamePanel.add(prefixSuffix);
		ButtonGroup bg = new ButtonGroup();
		bg.add(replace);
		bg.add(renameSourceWithPrefix);
		bg.add(renameSourceWithSuffix);
		bg.add(nameDestWithPrefix);
		bg.add(nameDestWithSuffix);
		renameSourceWithSuffix.setSelected(true);
		prefixSuffix.setText("-old");
		return fileNamePanel;
	}

	private JPanel constructFileListPanel() {
		JPanel fileListPanel = new JPanel(new BorderLayout());
		fileListPanel.setBorder(new TitledBorder("Selected files:"));
		fileListPanel.add(new JScrollPane(fileList), BorderLayout.CENTER);
		MyDropTarget.newTranscodeDropTarget(fileList);
		return fileListPanel;
	}

	private JPanel constructButtonPanel() {
		addFiles.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int returnVal = Config.getTranscodeDialogBox().showOpenDialog(frame);
				if (returnVal != JFileChooser.APPROVE_OPTION) {
					// we return if the user has clicked on CANCEL
					return;
				}
				File[] graphs = Config.getTranscodeDialogBox().getSelectedFiles();
				for (int i = 0; i < graphs.length; i++) {
					if (!listModel.contains(graphs[i])) {
						listModel.addElement(graphs[i]);
					}
				}
			}
		});
		removeFiles.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Object[] graphs = fileList.getSelectedValues();
				for (int i = 0; i < graphs.length; i++) {
					listModel.removeElement(graphs[i]);
				}
			}
		});
		transcode.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String src = (String) srcEncodingList.getSelectedValue();
				if (src == null) {
					JOptionPane.showMessageDialog(null,
							"You must select an input encoding", "Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				String dest = (String) destEncodingList.getSelectedValue();
				if (dest == null) {
					JOptionPane.showMessageDialog(null,
							"You must select a destination encoding", "Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				String preSuf = prefixSuffix.getText();
				if (!replace.isSelected() && preSuf.equals("")) {
					JOptionPane.showMessageDialog(null,
							"You must specify a prefix/suffix", "Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				ConvertCommand command;
				try {
					command = new ConvertCommand().src(src)
							.dest(dest);
				} catch (InvalidDestinationEncodingException e) {
					e.printStackTrace();
          return;
				} catch (InvalidSourceEncodingException e) {
					e.printStackTrace();
          return;
				}
				if (replace.isSelected())
					command = command.replace();
				else if (renameSourceWithPrefix.isSelected())
					command = command.renameSourceWithPrefix(preSuf);
				else if (renameSourceWithSuffix.isSelected()) {
					command = command.renameSourceWithSuffix(preSuf);
				} else if (nameDestWithPrefix.isSelected()) {
					command = command.renameDestWithPrefix(preSuf);
				} else {
					command = command.renameDestWithSuffix(preSuf);
				}
        final ConvertCommand cmd=command;
				setVisible(false);
				// post pone code
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						int l = listModel.getSize();
						MultiCommands commands = new MultiCommands();
						for (int i = 0; i < l; i++) {
							cmd.file((File)listModel.getElementAt(i));
							commands.addCommand(cmd);
						}
						new ProcessInfoFrame(commands, false, null);
					}
				});
			}
		});
		cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				frame.setVisible(false);
			}
		});
		JPanel buttonPanel = new JPanel(new BorderLayout());
		JPanel tmp = new JPanel(new GridLayout(4, 1));
		tmp.add(addFiles);
		tmp.add(removeFiles);
		tmp.add(transcode);
		tmp.add(cancel);
		buttonPanel.add(tmp, BorderLayout.NORTH);
		buttonPanel.add(Box.createVerticalGlue(), BorderLayout.CENTER);
		return buttonPanel;
	}

	/**
	 * @return <code>true</code> if the conversion frame is visible;
	 *         <code>false</code> otherwise
	 */
	public static boolean isFrameVisible() {
		return frame.isVisible();
	}

	/**
	 * @return the list model of the conversion frame
	 */
	public static DefaultListModel getListModel() {
		return frame.listModel;
	}

	public static boolean validSrcEncoding(String s) {
		return validEncoding(srcEncodings, s);
	}

	public static boolean validDestEncoding(String s) {
		return validEncoding(destEncodings, s);
	}

	public static boolean validEncoding(String[] tab, String s) {
		for (int i = 0; i < tab.length; i++) {
			if (tab[i].equals(s))
				return true;
		}
		return false;
	}

}