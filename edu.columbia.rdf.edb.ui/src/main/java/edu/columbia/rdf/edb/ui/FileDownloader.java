package edu.columbia.rdf.edb.ui;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Set;

import edu.columbia.rdf.edb.FileDescriptor;

public interface FileDownloader {
  public void downloadFile(FileDescriptor file, Path localFile) throws IOException;

  public void downloadZip(Set<FileDescriptor> files, Path localFile) throws IOException;

}
