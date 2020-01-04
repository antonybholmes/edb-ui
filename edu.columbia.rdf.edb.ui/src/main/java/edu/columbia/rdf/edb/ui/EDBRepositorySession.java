package edu.columbia.rdf.edb.ui;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.jebtk.core.http.URLPath;
import org.jebtk.core.json.Json;
import org.jebtk.core.json.JsonParser;

import edu.columbia.rdf.edb.EDBWLogin;
import edu.columbia.rdf.edb.ui.cache.CacheRepository;

public class EDBRepositorySession extends RepositorySession {
  protected EDBWLogin mLogin;
  // private UrlBuilder mRestAuthUrl;
  private URLPath mVersionUrl;

  public EDBRepositorySession(EDBWLogin login)
      throws UnsupportedEncodingException {
    mLogin = login;

    // mRestAuthUrl = login.getAuthUrl();

    mVersionUrl = login.getURL().join("version");
  }

  @Override
  public CacheRepository restore(File sessionFile)
      throws IOException, ClassNotFoundException {
    EDBRepository repository = new EDBRepository(mLogin);

    repository.cache();

    return repository;
  }

  public int getCurrentDbVersion() throws IOException {
    Json json = new JsonParser().parse(mVersionUrl);

    return json.get(0).get("version").getInt();
  }
}
