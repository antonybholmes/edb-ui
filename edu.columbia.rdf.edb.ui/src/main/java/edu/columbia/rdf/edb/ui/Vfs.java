package edu.columbia.rdf.edb.ui;

import java.io.IOException;
import java.net.MalformedURLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jebtk.core.http.UrlBuilder;
import org.jebtk.core.json.Json;
import org.jebtk.core.json.JsonParser;
import org.jebtk.core.text.DateUtils;

import edu.columbia.rdf.edb.EDBWLogin;
import edu.columbia.rdf.edb.FileType;
import edu.columbia.rdf.edb.Tag;
import edu.columbia.rdf.edb.VfsFile;

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
  public List<VfsFile> ls() throws MalformedURLException, IOException {
    return ls(-1);
  }

  /**
   * List files in a VFS directory.
   * @param vfsId
   * @return
   * @throws ParseException
   * @throws MalformedURLException
   * @throws IOException
   */
  public List<VfsFile> ls(int vfsId) throws MalformedURLException, IOException {
    UrlBuilder urlFile = getVFSURL().resolve("ls")
        .param("parent", vfsId)
        .param("page", 1);

    System.err.println(urlFile);

    Json json = new JsonParser().parse(urlFile.toURL());

    List<VfsFile> files = new ArrayList<VfsFile>();

    for (int i = 0; i < json.size(); ++i) {
      Json fileJson = json.get(i);

      VfsFile f;
      try {
        f = new VfsFile(fileJson.getInt("id"),
            fileJson.getString("n"), 
            FileType.parse(fileJson.getInt("t")),
            DateUtils.parseRevFormattedDate(fileJson.getString("d")));
        
        files.add(f);
      } catch (ParseException e) {
        e.printStackTrace();
      }

      
    }

    Collections.sort(files);

    return files;
  }

  public List<Tag> tags()
      throws MalformedURLException, IOException, ParseException {
    UrlBuilder urlFile = getVFSURL().resolve("tags");

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
  
  private UrlBuilder getVFSURL() {
    return mLogin.getURL().resolve("vfs");
  }
}
