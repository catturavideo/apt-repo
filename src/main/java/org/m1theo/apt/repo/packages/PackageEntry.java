/**
 * Copyright (c) 2010-2013, theo@m1theo.org.
 * 
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.m1theo.apt.repo.packages;

/**
 * Represents an entry in the Packages file.
 * 
 * @author Theo Weiss
 * @since 0.1.0
 * 
 */
public class PackageEntry {
  private String packageName;
  private String version;
  private String architecture;
  private String maintainer;
  private String installed_size;
  private String depends;
  private String recommends;
  private String conflicts;
  private String replaces;
  private String suggests;
  private String enhances;
  private String breaks;
  private String pre_depends;
  private String provides;
  private String filename;
  private long size;
  private String md5sum;
  private String sha1;
  private String sha256;
  private String sha512;
  private String section;
  private String priority;
  private String description;


  @Override
  public String toString() {
    StringBuffer buffer = new StringBuffer();
    buffer.append("Package: " + packageName + "\n" + "Version: " + version + "\n"
        + "Architecture: " + architecture + "\n" + "Maintainer: " + maintainer + "\n");
    if (installed_size != null) {
      buffer.append("Installed-Size: " + installed_size + "\n");
    }    
    if (depends != null) {
      buffer.append("Depends: " + depends + "\n");
    }
    if (recommends != null) {
      buffer.append("Recommends: " + recommends + "\n");
    }
    if (conflicts != null) {
      buffer.append("Conflicts: " + conflicts + "\n");
    }
    if (replaces != null) {
      buffer.append("Replaces: " + replaces + "\n");
    }
    if (suggests != null) {
      buffer.append("Suggests: " + suggests + "\n");
    }
    if (enhances != null) {
      buffer.append("Enhances: " + enhances + "\n");
    }
    if (breaks != null) {
      buffer.append("Breaks: " + breaks + "\n");
    }
    if (pre_depends != null) {
      buffer.append("Pre-Depends: " + pre_depends + "\n");
    }
    if (provides != null) {
      buffer.append("Provides: " + provides + "\n");
    }
    buffer.append("Filename: " + filename + "\n" + "Size: " + size + "\n" + "MD5sum: " + md5sum
        + "\n" + "SHA1: " + sha1 + "\n" + "SHA256: " + sha256 + "\n" + "SHA512: " + sha512 + "\n" + "Section: " + section + "\n"
        + "Priority: " + priority + "\n" + "Description: " + description + "\n");

    return buffer.toString();
  }

  public void setPackageName(String packageName) {
    this.packageName = packageName;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  public void setArchitecture(String architecture) {
    this.architecture = architecture;
  }

  public void setMaintainer(String maintainer) {
    this.maintainer = maintainer;
  }

  public void setInstalled_size(String installed_size) {
    this.installed_size = installed_size;
  }

  public void setDepends(String depends) {
    this.depends = depends;
  }
  
  public void setRecommends(String recommends) {
    this.recommends = recommends;
  }
  
  public void setConflicts(String conflicts) {
    this.conflicts = conflicts;
  }
  
  public void setReplaces(String replaces) {
    this.replaces = replaces;
  }
  
  public void setSuggests(String suggests) {
    this.suggests = suggests;
  }
  
  public void setEnhances(String enhances) {
    this.enhances = enhances;
  }
  
  public void setBreaks(String breaks) {
    this.breaks = breaks;
  }
  
  public void setPre_depends(String pre_depends) {
    this.pre_depends = pre_depends;
  }
  
  public void setProvides(String provides) {
    this.provides = provides;
  }
  
  public void setFilename(String filename) {
    this.filename = filename;
  }

  public void setSize(long size) {
    this.size = size;
  }

  public void setMd5sum(String md5sum) {
    this.md5sum = md5sum;
  }

  public void setSha1(String sha1) {
    this.sha1 = sha1;
  }

  public String getSha512() {
    return sha512;
  }

  public void setSha512(String sha512) {
    this.sha512 = sha512;
  }

  public void setSha256(String sha256) {
    this.sha256 = sha256;
  }

  public void setSection(String section) {
    this.section = section;
  }

  public void setPriority(String priority) {
    this.priority = priority;
  }

  public void setDescription(String description) {
    this.description = description;
  }


}
