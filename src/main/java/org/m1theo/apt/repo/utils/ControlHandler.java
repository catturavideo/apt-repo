/**
 * Copyright (c) 2010-2013, theo@m1theo.org.
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.m1theo.apt.repo.utils;

import java.util.Optional;
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

    getField(controlEntries, "Package").ifPresent(packageEntry::setPackageName);
    getField(controlEntries, "Version").ifPresent(packageEntry::setVersion);
    getField(controlEntries, "Architecture").ifPresent(packageEntry::setArchitecture);
    getField(controlEntries, "Maintainer").ifPresent(packageEntry::setMaintainer);
    getField(controlEntries, "Installed-Size").ifPresent(packageEntry::setInstalled_size);
    getField(controlEntries, "Depends").ifPresent(packageEntry::setDepends);
    getField(controlEntries, "Recommends").ifPresent(packageEntry::setRecommends);
    getField(controlEntries, "Conflicts").ifPresent(packageEntry::setConflicts);
    getField(controlEntries, "Replaces").ifPresent(packageEntry::setReplaces);
    getField(controlEntries, "Suggests").ifPresent(packageEntry::setSuggests);
    getField(controlEntries, "Enhances").ifPresent(packageEntry::setEnhances);
    getField(controlEntries, "Breaks").ifPresent(packageEntry::setBreaks);
    getField(controlEntries, "Pre-Depends").ifPresent(packageEntry::setPre_depends);
    getField(controlEntries, "Provides").ifPresent(packageEntry::setProvides);
    getField(controlEntries, "Section").ifPresent(packageEntry::setSection);
    getField(controlEntries, "Priority").ifPresent(packageEntry::setPriority);
    getField(controlEntries, "Description").ifPresent(packageEntry::setDescription);
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

  private static Optional<String> getField (final Map<String, StringBuilder> $entries, final String $key) {
    return Optional.ofNullable($entries.get($key)).filter($builder -> $builder.length() > 0).map(StringBuilder::toString);
  }

}
