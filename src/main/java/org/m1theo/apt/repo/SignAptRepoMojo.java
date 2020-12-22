/**
 * Copyright (c) 2010-2013, theo@m1theo.org.
 * 
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.m1theo.apt.repo;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.bouncycastle.bcpg.HashAlgorithmTags;
import org.bouncycastle.openpgp.PGPException;
import org.codehaus.plexus.util.FileUtils;
import org.m1theo.apt.repo.signing.PGPSigner;

import java.io.*;
import java.security.GeneralSecurityException;

/**
 * Goal which creates an apt repository.
 * 
 * @author Theo Weiss
 * @since 0.1.0
 * 
 */
@Mojo(
    name="sign-apt-repo",
    defaultPhase=LifecyclePhase.PACKAGE
)
public class SignAptRepoMojo extends AbstractMojo {
  private static final String RELEASE = "Release";
  private static final String RELEASEGPG = "Release.gpg";
  private static final String INRELEASE = "InRelease";
  private static final String FAILED_TO_CREATE_APT_REPO = "Failed to sign apt-repo: ";
  
  /**
   * The keyring to use for signing operations.
   */
  @Parameter(readonly = true, property = "apt-repo.keyring")
  private File keyring;

  /**
   * The key to use for signing operations.
   */
  @Parameter(property = "apt-repo.key")
  private String key;

  /**
   * The passphrase to use for signing operations.
   */
  @Parameter(property = "apt-repo.passphrase")
  private String passphrase;

  /**
   * A file containg the passphrase to use for signing operations.
   * The passphrase must be in the first line of the file.
   */
  @Parameter(readonly = true, property = "apt-repo.passphrase-file")
  private File passphraseFile;

  /**
   * The digest algorithm to use.
   *
   * @see org.bouncycastle.bcpg.HashAlgorithmTags
   */
  @Parameter(defaultValue = "SHA256", property = "apt-repo.digest")
  private String digest;

  /**
   * Location of the apt repository.
   */
  @Parameter(defaultValue = "${project.build.directory}/apt-repo", property = "apt-repo.repoDir", required = true)
  private File repoDir;

  public SignAptRepoMojo () {}
  public SignAptRepoMojo (File keyring, String key, String passphrase, File passphraseFile, String digest, File repoDir) {
      this.keyring = keyring;
      this.key = key;
      this.passphrase = passphrase;
      this.passphraseFile = passphraseFile;
      this.digest = digest;
      this.repoDir = repoDir;
  }
  
  public void execute() throws MojoExecutionException {
    if (keyring == null || !keyring.exists()){
      getLog().error("Signing requested, but no or invalid keyrring supplied");
      throw new MojoExecutionException(FAILED_TO_CREATE_APT_REPO + "keyring invalid or missing");
    }
    if (key == null){
      getLog().error("Signing requested, but no key supplied");
      throw new MojoExecutionException(FAILED_TO_CREATE_APT_REPO + "key is missing");
    }
    if (passphrase == null && passphraseFile == null){
      getLog().error("Signing requested, but no passphrase or passphrase file supplied");
      throw new MojoExecutionException(FAILED_TO_CREATE_APT_REPO + "passphrase or passphrase file must be specified");
    }
    if (passphraseFile != null && ! passphraseFile.exists()){
      getLog().error("Signing requested, passphrase file does not exist: " + passphraseFile.getAbsolutePath());
      throw new MojoExecutionException(FAILED_TO_CREATE_APT_REPO + "passphrase file does not exist " + passphraseFile.getAbsolutePath());
    }
    getLog().info("repo dir: " + repoDir.getPath());
    if (!repoDir.exists()) {
        getLog().error("Signing requested, repo directory file does not exist: " + repoDir.getAbsolutePath());
        throw new MojoExecutionException(FAILED_TO_CREATE_APT_REPO + "repo directory does not exist " + repoDir.getAbsolutePath());
    }

    try {
      final File releaseFile = repoDir.isFile() ? repoDir : new File(repoDir, RELEASE);
      final String release = FileUtils.fileRead(releaseFile);
      if (passphraseFile != null){
        getLog().debug("passphrase file will be used " + passphraseFile.getAbsolutePath());
        BufferedReader pwReader = new BufferedReader(new FileReader(passphraseFile));
        passphrase = pwReader.readLine();
        pwReader.close();
      }
      final File inReleaseFile = new File(repoDir, INRELEASE);
      final File releaseGpgFile = new File(repoDir, RELEASEGPG);
      PGPSigner signer = new PGPSigner(new FileInputStream(keyring), key, passphrase, getDigestCode(digest));
      signer.clearSignDetached(release, new FileOutputStream(releaseGpgFile));
      signer.clearSign(release, new FileOutputStream(inReleaseFile));
    } catch (IOException e) {
      throw new MojoExecutionException("writing files failed", e);
    } catch (PGPException e) {
      throw new MojoExecutionException("gpg signing failed",e);
    } catch (GeneralSecurityException e) {
      throw new MojoExecutionException("generating release failed",e);
    }
  }
  
  static int getDigestCode(String digestName) throws MojoExecutionException {
    if ("SHA1".equals(digestName)) {
      return HashAlgorithmTags.SHA1;
    } else if ("MD2".equals(digestName)) {
      return HashAlgorithmTags.MD2;
    } else if ("MD5".equals(digestName)) {
      return HashAlgorithmTags.MD5;
    } else if ("RIPEMD160".equals(digestName)) {
      return HashAlgorithmTags.RIPEMD160;
    } else if ("SHA256".equals(digestName)) {
      return HashAlgorithmTags.SHA256;
    } else if ("SHA384".equals(digestName)) {
      return HashAlgorithmTags.SHA384;
    } else if ("SHA512".equals(digestName)) {
      return HashAlgorithmTags.SHA512;
    } else if ("SHA224".equals(digestName)) {
      return HashAlgorithmTags.SHA224;
    } else {
      throw new MojoExecutionException("unknown hash algorithm tag in digestName: " + digestName);
    }
  }

}
