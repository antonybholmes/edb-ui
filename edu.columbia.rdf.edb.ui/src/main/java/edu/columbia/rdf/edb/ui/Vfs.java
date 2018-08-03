package edu.columbia.rdf.edb.ui;

import java.io.IOException;
import java.net.MalformedURLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jebtk.core.json.Json;
import org.jebtk.core.json.JsonParser;
import org.jebtk.core.network.UrlBuilder;
import org.jebtk.core.text.DateUtils;

import edu.columbia.rdf.edb.EDBWLogin;
import edu.columbia.rdf.edb.VfsFile;
import edu.columbia.rdf.edb.FileType;
import edu.columbia.rdf.edb.Tag;

public class Vfs {
  private EDBWLogin mLogin;

  public Vfs(EDBWLogin login) {
    mLogin = login;
  }

  /**
   * Return the VFS root.
   * 
   * @return
   * @throws ParseException
   * @throws MalformedURLException
   * @throws IOException
   * @throws java.text.ParseException
   */
  public List<VfsFile> ls() throws ParseException, MalformedURLException,
      IOException, java.text.ParseException {
    return ls(-1);
  }

  public List<VfsFile> ls(int vfsId)
      throws ParseException, MalformedURLException, IOException {
    UrlBuilder urlFile = mLogin.getURL().resolve("vfs").resolve("ls")
        .resolve(vfsId);

    System.err.println(urlFile);

    Json json = new JsonParser().parse(urlFile.toURL());

    List<VfsFile> files = new ArrayList<VfsFile>();

    for (int i = 0; i < json.size(); ++i) {
      Json fileJson = json.get(i);

      VfsFile f = new VfsFile(fileJson.getInt("id"),
          fileJson.getString("n"), FileType.parse(fileJson.getInt("t")),
          DateUtils.parseRevFormattedDate(fileJson.getString("d")));

      files.add(f);
    }

    Collections.sort(files);

    return files;
  }

  public List<Tag> tags()
      throws MalformedURLException, IOException, ParseException {
    UrlBuilder urlFile = mLogin.getURL().resolve("vfs").resolve("tags");

    System.err.println(urlFile);

    Json json = new JsonParser().parse(urlFile.toURL());

    List<Tag> tags = new ArrayList<Tag>();

    for (int i = 0; i < json.size(); ++i) {
      Json tagJson = json.get(i);

      // System.err.println("vfs " + i + " " + json.get(i).getString("n"));

      Tag tag = new Tag(tagJson.getInt("id"), tagJson.getString("n"));

      tags.add(tag);
    }

    return tags;
  }
}
