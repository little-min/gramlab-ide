 /*
  * Unitex
  *
  * Copyright (C) 2001-2007 Universit� Paris-Est Marne-la-Vall�e <unitex@univ-mlv.fr>
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

package fr.umlv.unitex.process;

import java.io.*;

/**
 * @author S�bastien Paumier
 *
 */
public class MkdirCommand extends AbstractMethodCommand {
    
	File dir;
	
	public MkdirCommand() {
		super("mkdir");
	}
	
    public MkdirCommand name(File s) {
      dir=s;
      protectElement(s.getAbsolutePath());
      return this;
    }

	@Override
	public boolean execute() {
		if (dir.exists()) {
			return true;
		}
		return dir.mkdir();
	}

}