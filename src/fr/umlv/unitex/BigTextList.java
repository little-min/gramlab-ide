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
 * large UTF16-LE files as lists of lines. This is useful to display
 * dictionaries.
 * 
 * @author S�bastien Paumier
 */
public class BigTextList extends JList {

	/**
	 * Builds a JList designed for large line lists.
	 * @param m the model containing information about the underlying text file 
	 * @param isDelaf if true, the list will use a special renderer dedicated
	 *        to DELAF lines 
	 */
	public BigTextList(TextAsListModel m,boolean isDelaf) {
		super(m);
		/* Set maximal length of a line:
		 * This value must big enough holding even the longest dlc entries,
		 * or strange effects (lines cut up somewhere in the middle) will occure! */
		setPrototypeCellValue("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx,xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx.xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
		if (isDelaf) {
			setCellRenderer(new DefaultListCellRenderer() {
				@Override
				public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
					super.getListCellRendererComponent(list,value,index,isSelected,cellHasFocus);
					setText(getDecoratedDelafLine((String)value));
					return this;
				}

				StringBuilder builder=new StringBuilder();
				
				private String getDecoratedDelafLine(String string) {
					if (string==null) return null;
					int length=string.length();
					builder.delete(0,builder.length());
					builder.append("<html><body>");
					int comma=-1;
					int pos=0;
					do {
						comma=string.indexOf(',',pos);
						if (comma==-1) return string;
						pos=comma+1;
					} while (string.charAt(comma-1)=='\\');
					builder.append("<font color=\"blue\">");
					builder.append(string.substring(0,comma));
					builder.append("</font>");
					builder.append(",");
					int startPos=pos;
					do {
						comma=string.indexOf('.',pos);
						if (comma==-1) return string;
						pos=comma+1;
					} while (string.charAt(comma-1)=='\\');
					if (pos==length) return string;
					if (startPos!=comma) {
						builder.append("<font color=\"red\">");
						builder.append(string.substring(startPos,comma));
						builder.append("</font>");
					}
					char c='.';
					char last;
					startPos=pos;
					do {
						builder.append(c);
						builder.append("<font color=\"#00B900\">");
						do {
							last=c;
							c=string.charAt(pos);
							pos++;
						} while (pos!=length && !(last!='\\' && (c=='+' || c==':')));
						if (pos==length) {
							c='\0';
							builder.append(string.substring(startPos));
						} else {
							builder.append(string.substring(startPos,pos-1));
							startPos=pos;
						}
						builder.append("</font>");
					} while (c=='+');
					if (c!='\0') {
						c=':';
						startPos=pos;
						do {
							builder.append(c);
							builder.append("<font color=\"#CE6700\">");
							do {
								last=c;
								c=string.charAt(pos);
								pos++;
							} while (pos!=length && !(last!='\\' && (c==':')));
							if (pos==length) {
								c='\0';
								builder.append(string.substring(startPos));
							} else {
								builder.append(string.substring(startPos,pos-1));
								startPos=pos;
							}
							builder.append("</font>");
						} while (c==':');
					}
					builder.append("</body></html>");
					return builder.toString();
				}
			});
		}
	}
	
	public BigTextList(TextAsListModel m) {
		this(m,false);
	}

	public BigTextList(boolean b) {
		this(new TextAsListModel(),b);
	}

	public BigTextList() {
		this(new TextAsListModel(),false);
	}
	
	public void load(File f) {
		TextAsListModel model=(TextAsListModel)getModel();
		model.load(f);
	}
	
	public void reset() {
		TextAsListModel model=(TextAsListModel)getModel();
		model.reset();
	}
	
	public void setText(String string) {
		TextAsListModel model=(TextAsListModel)getModel();
		model.setText(string);
	}
}