package edu.columbia.rdf.edb.ui;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Set;

import edu.columbia.rdf.edb.VfsFile;

public interface FileDownloader {
  public void downloadFile(VfsFile file, Path localFile)
      throws IOException;

  public void downloadZip(Set<VfsFile> files, Path localFile)
      throws IOException;

}
