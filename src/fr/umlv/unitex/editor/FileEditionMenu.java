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

package fr.umlv.unitex.editor;

import java.awt.event.*;

import javax.swing.*;

import fr.umlv.unitex.*;
import fr.umlv.unitex.conversion.*;
import fr.umlv.unitex.editor.ui.*;

/**
 * Menu to handle file edition
 * */
public class FileEditionMenu extends JMenu {

	private static FileManager fileManager;

	/**
	 * File edition menu constructor
	 */
	public FileEditionMenu() {

		super("File Edition");
		fileManager = new FileManager();

		JMenuItem open = new JMenuItem("Open...");
		open.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				openFile();
				//save.setEnabled(true);
			}
		});

		JMenuItem newFile = new JMenuItem("New File");
		newFile.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				newFile();
				//save.setEnabled(true);
			}
		});

		// conversion menu

		JMenuItem convert = new JMenuItem("Transcode Files");
		convert.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				ConversionFrame.showFrame();
			}
		});

		JMenuItem closeAll = new JMenuItem("Close All");
		closeAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JDesktopPane desktop = UnitexFrame.desktop;
				JInternalFrame[] iFrames = desktop.getAllFrames();
				for (int i = 0; i < iFrames.length; ++i) {
					if (iFrames[i] instanceof FileEditionTextFrame) {
            iFrames[i].doDefaultCloseAction();
					}
				}
			}
		});
		add(newFile);
		add(open);
		add(convert);
		add(closeAll);
	}

	/**
	* Opens and displays the file content in an edition area. 		  
	* */
	public static void openFile() {
    JFileChooser chooser=Config.getFileEditionDialogBox();
		int returnVal = chooser.showOpenDialog(null);
		if (returnVal != JFileChooser.APPROVE_OPTION) {
			// we return if the user has clicked on CANCEL
			return;
		}
		fileManager.loadFile(chooser.getSelectedFile());
	}

	/**
	 * Create a new empty text area
	 * */
	public void newFile() {
		fileManager.newFile();
	}

}