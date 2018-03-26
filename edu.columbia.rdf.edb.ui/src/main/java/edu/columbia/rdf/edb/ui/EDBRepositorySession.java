package edu.columbia.rdf.edb.ui;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;

import org.jebtk.core.json.Json;
import org.jebtk.core.json.JsonParser;
import org.jebtk.core.network.UrlBuilder;

import edu.columbia.rdf.edb.EDBWLogin;
import edu.columbia.rdf.edb.ui.cache.CacheRepository;

public class EDBRepositorySession extends RepositorySession {
  protected EDBWLogin mLogin;
  // private UrlBuilder mRestAuthUrl;
  private UrlBuilder mVersionUrl;

  public EDBRepositorySession(EDBWLogin login)
      throws UnsupportedEncodingException {
    mLogin = login;

    // mRestAuthUrl = login.getAuthUrl();

    mVersionUrl = login.getURL().resolve("version");
  }

  @Override
  public CacheRepository restore(File sessionFile)
      throws IOException, ClassNotFoundException {
    EDBRepository repository = new EDBRepository(mLogin);

    repository.cache();

    return repository;
  }

  public int getCurrentDbVersion() throws IOException {
    URL url = mVersionUrl.toURL();

    Json json;

    json = new JsonParser().parse(url);

    return json.get(0).get("version").getAsInt();
  }
}
