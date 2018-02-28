/**
 * Copyright (c) 2010-2013, theo@m1theo.org.
 * 
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.m1theo.apt.repo.utils;

import java.util.HashMap;
import java.util.Map;
import org.apache.maven.plugin.MojoExecutionException;
import org.m1theo.apt.repo.packages.PackageEntry;

/**
 * Parses the control file.
 * 
 * @author Theo Weiss
 * @since 0.1.0
 * 
 */
public class ControlHandler {
  private String controlContent;

  public void setControlContent(String controlContent) {
    this.controlContent = controlContent.trim();
  }

  /**
   * Returns true if the controlContent is initialized.
   * 
   * @return boolean
   */
  public boolean hasControlContent() {
    return controlContent != null ? true : false;
  }

  private void parseControl(PackageEntry packageEntry) throws MojoExecutionException {
    if (controlContent == null) {
      throw new MojoExecutionException("no controlContent to parse");
    }
    
    final Map<String, StringBuilder> controlEntries = new HashMap<String, StringBuilder>();
    String key = null;
    
    String[] lines = controlContent.split("\\r?\\n");
    for (int i = 0; i < lines.length; i++) {
      String line = lines[i];
      System.err.println(line);
      if (line.startsWith(" ") && key != null) {
        controlEntries.get(key).append("\n").append(line);
        continue;
      }
      
      String[] stmt = line.split(":", 2);
      if (stmt.length != 2) {
        continue;
      }
      key = stmt[0].trim();
      controlEntries.put(key, new StringBuilder(stmt[1].trim()));
    }
    
    if (controlEntries.containsKey("Package")) {
      packageEntry.setPackageName(controlEntries.get("Package").toString());        
    }
    
    if (controlEntries.containsKey("Version")) {
      packageEntry.setVersion(controlEntries.get("Version").toString());
    }
    
    if (controlEntries.containsKey("Architecture")) {
      packageEntry.setArchitecture(controlEntries.get("Architecture").toString());
    }
    
    if (controlEntries.containsKey("Maintainer")) {
      packageEntry.setMaintainer(controlEntries.get("Maintainer").toString());
    }
    
    if (controlEntries.containsKey("Installed-Size")) {
      packageEntry.setInstalled_size(controlEntries.get("Installed-Size").toString());
    }
    
    if (controlEntries.containsKey("Depends")) {
      packageEntry.setDepends(controlEntries.get("Depends").toString());
    }
    
    if (controlEntries.containsKey("Recommends")) {
      packageEntry.setRecommends(controlEntries.get("Recommends").toString());
    }
    
    if (controlEntries.containsKey("Conflicts")) {
      packageEntry.setConflicts(controlEntries.get("Conflicts").toString());
    }
    
    if (controlEntries.containsKey("Replaces")) {
      packageEntry.setReplaces(controlEntries.get("Replaces").toString());
    }
    
    if (controlEntries.containsKey("Section")) {
      packageEntry.setSection(controlEntries.get("Section").toString());
    }
    
    if (controlEntries.containsKey("Priority")) {
      packageEntry.setPriority(controlEntries.get("Priority").toString());
    }
    
    if (controlEntries.containsKey("Description")) {
      packageEntry.setDescription(controlEntries.get("Description").toString());
    }
  }

  /**
   * Parse the control file contents and update the {@link PackageEntry}.
   * 
   * @param packageEntry
   * @throws MojoExecutionException
   */
  public void handle(PackageEntry packageEntry) throws MojoExecutionException {
    parseControl(packageEntry);
  }

}
