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

package fr.umlv.unitex;

import java.awt.*;
import java.io.*;

import javax.swing.*;


/**
 * This class provides a text component that can display in read-only
 * large HTML concordance files.
 * 
 * @author S�bastien Paumier
 */
public class BigConcordance extends JList {

	
	public BigConcordance(ConcordanceAsListModel m) {
		super(m);
		setPrototypeCellValue("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
		setFont(new Font(Preferences.getConcordanceFontName(),0,Preferences.getConcordanceFontSize()));
		setCellRenderer(new DefaultListCellRenderer() {
			@Override
			public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
				super.getListCellRendererComponent(list,value,index,isSelected,cellHasFocus);
				StringBuilder builder=new StringBuilder();
				builder.append("<html><body>");
				builder.append((String)value);
				builder.append("</body></html>");
				setText(builder.toString());
				return this;
			}
		});
	}
	

	public BigConcordance() {
		this(new ConcordanceAsListModel());
	}

	public void load(File f) {
		ConcordanceAsListModel model=(ConcordanceAsListModel)getModel();
		model.load(f);
	}
	
	public void reset() {
		ConcordanceAsListModel model=(ConcordanceAsListModel)getModel();
		model.reset();
	}
	
}
