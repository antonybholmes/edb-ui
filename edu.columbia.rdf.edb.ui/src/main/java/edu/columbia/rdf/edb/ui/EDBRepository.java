package edu.columbia.rdf.edb.ui;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.jebtk.bioinformatics.annotation.Type;
import org.jebtk.core.NetworkFileException;
import org.jebtk.core.io.ByteStreams;
import org.jebtk.core.json.Json;
import org.jebtk.core.json.JsonParser;
import org.jebtk.core.network.UrlBuilder;
import org.jebtk.core.path.Path;
import org.jebtk.core.search.SearchStackElement;

import edu.columbia.rdf.edb.EDB;
import edu.columbia.rdf.edb.EDBWLogin;
import edu.columbia.rdf.edb.Experiment;
import edu.columbia.rdf.edb.FileDescriptor;
import edu.columbia.rdf.edb.FileType;
import edu.columbia.rdf.edb.GEO;
import edu.columbia.rdf.edb.Group;
import edu.columbia.rdf.edb.Groups;
import edu.columbia.rdf.edb.Person;
import edu.columbia.rdf.edb.Sample;
import edu.columbia.rdf.edb.SampleTag;
import edu.columbia.rdf.edb.SampleTags;
import edu.columbia.rdf.edb.Species;
import edu.columbia.rdf.edb.Tag;
import edu.columbia.rdf.edb.ui.cache.CacheRepository;
import edu.columbia.rdf.edb.ui.cache.SampleAutoCache;

/**
 * Maintains a connection to a caArray server.
 *
 * @author Antony Holmes Holmes
 */
public class EDBRepository extends CacheRepository {
  private static final long serialVersionUID = 1L;

  public static final Type CHIP_SEQ_TYPE = new Type(2, "ChIP-seq");

  private FileDownloader mFileDownloader;

  private Vfs mVfs;

  public EDBRepository(EDBWLogin login) throws IOException {
    super(login);

    mFileDownloader = new EDBFileDownloader(mLogin);
    mVfs = new Vfs(mLogin);
  }

  @Override
  public List<Sample> searchSamples(
      List<SearchStackElement<Sample>> searchStack,
      Path path) {
    return null;
  }

  protected UrlBuilder getSearchSamplesUrl()
      throws UnsupportedEncodingException {
    return getSamplesUrl().resolve("search");
  }

  private UrlBuilder getExperimentsUrl() throws UnsupportedEncodingException {
    return mLogin.getOTKAuthUrl().resolve("experiments");
  }

  private UrlBuilder getExperimentsUrl(int id)
      throws UnsupportedEncodingException {
    return getExperimentsUrl().resolve(id);
  }

  private UrlBuilder getExperimentFilesUrl(int id)
      throws UnsupportedEncodingException {
    return getExperimentsUrl(id).resolve("files");
  }

  private UrlBuilder getExperimentFilesDirUrl(int id)
      throws UnsupportedEncodingException {
    return getExperimentFilesUrl(id).resolve("dir");
  }

  private UrlBuilder getSamplesUrl() throws UnsupportedEncodingException {
    return mLogin.getOTKAuthUrl().resolve("samples");
  }

  private UrlBuilder getSamplesUrl(int id) throws UnsupportedEncodingException {
    return getSamplesUrl().resolve(id);
  }

  private UrlBuilder getSampleFilesUrl(int id)
      throws UnsupportedEncodingException {
    return getSamplesUrl(id).resolve("files");
  }

  private UrlBuilder getSampleTagsUrl(int id)
      throws UnsupportedEncodingException {
    return getSamplesUrl(id).resolve("tags");
  }

  private UrlBuilder getSampleTagUrl(int sampleId, int tagId)
      throws UnsupportedEncodingException {
    return getSamplesUrl(sampleId).resolve("tags").resolve(tagId);
  }

  private UrlBuilder getSamplePersonsUrl(int sampleId)
      throws UnsupportedEncodingException {
    return getSamplesUrl(sampleId).resolve("persons");
  }

  private UrlBuilder getSampleGeoUrl(Sample sample)
      throws UnsupportedEncodingException {
    return getSampleGeoUrl(sample.getId());
  }

  private UrlBuilder getSampleGeoUrl(int id)
      throws UnsupportedEncodingException {
    return getSamplesUrl(id).resolve("geo");
  }

  private UrlBuilder getSamplesUrl(String name)
      throws UnsupportedEncodingException {
    return getSamplesUrl().resolve("alias").resolve(name);
  }

  @Override
  public List<Sample> searchSamples(String query,
      Path path,
      Collection<Type> dataTypes,
      Collection<Species> organisms,
      Groups groups) throws IOException {
    UrlBuilder url = getSearchSamplesUrl().param("p", path.toString())
        .param("q", query);
    // .param("v", "all")
    // .param("m", 1000)

    if (dataTypes.size() > 0) {
      for (Type d : dataTypes) {
        url = url.param("t", d.getId());
      }
    }

    if (organisms.size() > 0) {
      for (Type o : organisms) {
        url = url.param("o", o.getId());
      }
    }

    if (groups.size() > 0) {
      for (Type g : groups) {
        url = url.param("g", g.getId());
      }

      if (groups.getAllMode()) {
        url = url.param("gm", "all");
      }
    }

    System.err.println(url);

    Json json = new JsonParser().parse(url.toUrl());

    List<Sample> ret = parseSampleJson(json);

    return ret;
  }

  @Override
  public List<Sample> parseSampleJson(Json json) {
    return parseSampleJson(json, SearchMode.FULL);
  }

  public List<Sample> parseSampleJson(Json json, SearchMode searchMode) {
    List<Sample> samples = new ArrayList<Sample>(1000);

    Json sampleJSON;
    Json tempJSON;

    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    for (int i = 0; i < json.size(); ++i) {
      sampleJSON = json.get(i);

      int sampleId = sampleJSON.get(EDB.HEADING_ID).getAsInt();

      Experiment experiment = mExperiments
          .get(sampleJSON.get(EDB.HEADING_EXPERIMENT).getAsInt());

      Type expression = mDataTypes
          .get(sampleJSON.get(EDB.HEADING_TYPE).getAsInt());

      Species species = mSpecies
          .get(sampleJSON.get(EDB.HEADING_ORGANISM).getAsInt());

      String name = sampleJSON.get(EDB.HEADING_NAME_SHORT).getAsString();

      // String description = sampleJSON.get("description").getAsString();

      Date date = null;

      try {
        date = formatter.parse(sampleJSON.get(EDB.HEADING_DATE).getAsString());
      } catch (ParseException e) {
        e.printStackTrace();
      }

      // System.err.println(name + " " + state);

      /*
       * SampleStateType stateType = mSampleStates.get(sampleId);
       * 
       * if (stateType != null) { state = stateType.getType(); } else { state =
       * SampleState.UNLOCKED; }
       */

      Sample sample = new SampleAutoCache(sampleId, experiment, expression,
          name, species, date);

      tempJSON = sampleJSON.get("geo");

      if (tempJSON != null) {
        GEO geo = new GEO(tempJSON.get(EDB.HEADING_ID).getAsInt(),
            tempJSON.get("geo_series_accession").getAsString(),
            tempJSON.get("geo_accession").getAsString(),
            tempJSON.get("geo_platform").getAsString());

        sample.setGEO(geo);
      }

      //
      // Tags
      //

      /*
       * Json tagsJson = sampleJSON.get("tags");
       * 
       * if (tagsJson != null) { for (int j = 0; j < tagsJson.size(); ++j) {
       * Json tagJson = tagsJson.get(j);
       * 
       * int id = tagJson.get("id").getAsInt();
       * 
       * Tag field = mTags.get(id);
       * 
       * SampleTag sampleTag = new SampleTag(id, field,
       * tagJson.get("value").getAsString());
       * 
       * //System.err.println("tags " + field.toString());
       * //System.err.println("tags " + tagJson.get("value").getAsString());
       * 
       * sample.getTags().add(sampleTag); } }
       */

      //
      // Groups
      //

      Json groupsJson = sampleJSON.get(EDB.HEADING_GROUPS);

      if (groupsJson != null) {
        List<Group> groups = new ArrayList<Group>(10);

        for (int j = 0; j < groupsJson.size(); ++j) {
          int id = groupsJson.get(j).getAsInt();

          Group group = mGroups.get(id);

          groups.add(group);
        }

        // Sort the groups
        Collections.sort(groups);

        sample.getGroups().addAll(groups);
      }

      //
      // Add persons
      //

      for (Person person : mSamplePersons.get(sampleId)) {
        sample.getPersons().add(person);
      }

      /*
       * Json personsJson = sampleJSON.get("persons");
       * 
       * if (personsJson != null) { for (int j = 0; j < personsJson.size(); ++j)
       * { Json personJSON = personsJson.get(j);
       * 
       * Person person = mPersons.get(personJSON.get("id").getAsInt());
       * 
       * sample.getPersons().add(person); } }
       */

      //
      // Add files
      //

      /*
       * Json filesJson = sampleJSON.get("files");
       * 
       * if (filesJson != null) { for (int j = 0; j < filesJson.size(); ++j) {
       * Json fileJSON = filesJson.get(j);
       * 
       * String fileName = fileJSON.get("name").getAsString();
       * 
       * FileDescriptor file = new FileDescriptor(fileJSON.getAsInt("id"),
       * fileName, FileType.parse(fileJSON.get("type").getAsInt()),
       * formatter.parse(fileJSON.get("created").getAsString()));
       * 
       * sample.getFiles().add(file); } }
       */

      // cacheGEO(sample);

      // for (SectionType sectionType : SectionType.values()) {
      // cacheSampleSection(sample, sectionType);
      // }

      samples.add(sample);
    }

    return samples;
  }

  @Override
  public FileDescriptor getExperimentFilesDir(int experimentId)
      throws IOException {
    URL url = getExperimentFilesDirUrl(experimentId).toUrl();

    return getFiles(url, true).get(0);
  }

  @Override
  public List<FileDescriptor> getExperimentFiles(int experimentId)
      throws IOException {
    URL url = getExperimentFilesUrl(experimentId).toUrl();

    return getFiles(url);
  }

  @Override
  public List<FileDescriptor> getSampleFiles(int sampleId) throws IOException {
    URL url = getSampleFilesUrl(sampleId).toUrl();

    List<FileDescriptor> files = null;

    files = getFiles(url);

    return files;
  }

  private static List<FileDescriptor> getFiles(URL url) throws IOException {
    return getFiles(url, false);
  }

  /**
   * List files and or directories from a URL
   * 
   * @param url
   * @param showDirs
   * @return
   * @throws IOException
   * @throws ParseException
   */
  private static List<FileDescriptor> getFiles(URL url, boolean showDirs)
      throws IOException {
    System.err.println(url);

    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    List<FileDescriptor> files = new ArrayList<FileDescriptor>();

    Json json = new JsonParser().parse(url);

    for (int j = 0; j < json.size(); ++j) {
      Json fileJSON = json.get(j);

      String fileName = fileJSON.get(EDB.HEADING_NAME_SHORT).getAsString();

      Date date = null;

      try {
        date = formatter.parse(fileJSON.get(EDB.HEADING_DATE).getAsString());
      } catch (ParseException e) {
        e.printStackTrace();
      }

      FileDescriptor file = new FileDescriptor(
          fileJSON.getAsInt(EDB.HEADING_ID), fileName,
          FileType.parse(fileJSON.get(EDB.HEADING_TYPE).getAsInt()), date);

      // Don't show directories
      if (showDirs || file.getType() == FileType.FILE) {
        files.add(file);
      }
    }

    return files;
  }

  /*
   * private void cacheArrayDesign(Sample sample) throws IOException { URL url =
   * new UrlBuilder(sampleUrl).addPath(sample.getUid()).addPath("gep").addPath(
   * "array" ).toUrl();
   * 
   * JsonValue json = mJsonParser.parse(url);
   * 
   * for (int i = 0; i < json.size(); ++i) { JsonValue arrayJSON = json.get(i);
   * 
   * //String id = arrayJSON.get("id").getAsString(); String name =
   * arrayJSON.get("name").getAsString(); String assay =
   * arrayJSON.get("assay_name").getAsString(); String provider =
   * arrayJSON.get("provider_name").getAsString();
   * 
   * ArrayDesign arrayDesign = new ArrayDesign(name, assay, provider);
   * 
   * sample.setArrayDesign(arrayDesign); } }
   */

  @Override
  public void cachePersons(int sampleId, Collection<Person> persons)
      throws IOException {
    URL url = getSamplePersonsUrl(sampleId).toUrl(); // ("p",
                                                     // path.toString()).addParam("q",
                                                     // query).toUrl();

    // System.err.println(url);

    Json json = new JsonParser().parse(url);

    if (json != null) {
      for (int j = 0; j < json.size(); ++j) {
        Json personJSON = json.get(j);

        Person person = mPersons.get(personJSON.get("id").getAsInt());

        persons.add(person);
      }
    }
  }

  /**
   * Cache all of the annotation sections associated with the samples.
   * 
   * @param sample
   * @return
   * @throws IOException
   */
  @Override
  public SampleTags cacheSampleFields(Sample sample) throws IOException {
    return null;
  }

  public void downloadFile(URL urlFile, File localFile)
      throws NetworkFileException, IOException {

    FileOutputStream output = new FileOutputStream(localFile);

    InputStream input = urlFile.openStream();

    try {
      ByteStreams.copy(input, output);
    } finally {
      input.close();
      output.close();
    }
  }

  @Override
  public FileDownloader getFileDownloader()
      throws UnsupportedEncodingException {
    return mFileDownloader;
  }

  @Override
  public List<Sample> getAllSamples() {
    return null;
  }

  @Override
  public Sample getSample(int id) throws IOException {
    URL url = getSamplesUrl(id).toUrl(); // ("p", path.toString()).addParam("q",
                                         // query).toUrl();

    System.err.println(url);

    Json json = new JsonParser().parse(url);

    List<Sample> samples = parseSampleJson(json);

    if (samples.size() > 0) {
      return samples.get(0);
    } else {
      return null;
    }
  }

  @Override
  public Sample getSample(String name) throws IOException {
    URL url = getSamplesUrl(name).toUrl(); // ("p",
                                           // path.toString()).addParam("q",
                                           // query).toUrl();

    // System.err.println(url);

    Json json = new JsonParser().parse(url);

    List<Sample> samples = parseSampleJson(json);

    if (samples.size() > 0) {
      return samples.get(0);
    } else {
      return null;
    }
  }

  @Override
  public void cacheTags(int sampleId, SampleTags tags) throws IOException {
    URL url = getSampleTagsUrl(sampleId).toUrl();

    // System.err.println(url);

    Json json = new JsonParser().parse(url);

    for (int i = 0; i < json.size(); ++i) {
      Json tagJson = json.get(i);

      int id = tagJson.get(EDB.HEADING_ID).getAsInt();

      Tag field = mTags.get(id);

      SampleTag sampleTag = new SampleTag(id, field,
          tagJson.get(EDB.HEADING_VALUE).getAsString());

      // System.err.println("tags " + field.toString());
      // System.err.println("tags " + tagJson.get("value").getAsString());

      tags.add(sampleTag);
    }
  }

  @Override
  public void cacheTag(int sampleId, int tagId, SampleTags tags)
      throws IOException {
    URL url = getSampleTagUrl(sampleId, tagId).toUrl();

    Json json = new JsonParser().parse(url);

    for (int i = 0; i < json.size(); ++i) {
      Json tagJson = json.get(i);

      int id = tagJson.get(EDB.HEADING_ID).getAsInt();

      Tag field = mTags.get(id);

      SampleTag sampleTag = new SampleTag(id, field,
          tagJson.get(EDB.HEADING_VALUE).getAsString());

      // System.err.println("tags " + field.toString());
      // System.err.println("tags " + tagJson.get("value").getAsString());

      tags.add(sampleTag);
    }
  }

  @Override
  public void cacheGEO(Sample sample) throws IOException {
    URL url = getSampleGeoUrl(sample).toUrl();

    // System.err.println(url);

    Json json = new JsonParser().parse(url);

    if (json.size() > 0) {
      json = json.get(0);

      GEO geo = new GEO(json.get("id").getAsInt(),
          json.get("series_accession").getAsString(),
          json.get("accession").getAsString(),
          json.get("platform").getAsString());

      sample.setGEO(geo);
    }
  }

  @Override
  public Collection<Type> getDataTypes() {
    return mDataTypes.getValues();
  }

  @Override
  public Collection<Species> getOrganisms() {
    return mSpecies.getValues();
  }

  /*
   * @Override public List<Peaks> searchChipSeqPeaks(Sample sample) throws
   * ParseException, IOException, ParseException, java.text.ParseException { URL
   * url = getChipSeqPeaksUrl(sample).toUrl();
   * 
   * Json json = new JsonParser().parse(url);
   * 
   * List<Peaks> allPeaks = new ArrayList<Peaks>();
   * 
   * //System.err.println(url);
   * 
   * Json peaksJson;
   * 
   * for (int i = 0; i < json.size(); ++i) { peaksJson = json.get(i);
   * 
   * int id = peaksJson.get("id").getAsInt();
   * 
   * String name = peaksJson.get("name").getAsString();
   * 
   * String genome = peaksJson.get("genome").getAsString();
   * 
   * int readLength = peaksJson.get("read_length").getAsInt();
   * 
   * String peakCaller = peaksJson.get("peak_caller").getAsString();
   * 
   * String peakCallerParameters =
   * peaksJson.get("peak_caller_parameters").getAsString();
   * 
   * SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"); Date date
   * = formatter.parse(peaksJson.get("release_date").getAsString());
   * 
   * Peaks peaks = new Peaks(id, name, genome, readLength, peakCaller,
   * peakCallerParameters, date);
   * 
   * Json locationsJson = peaksJson.get("peak_locations");
   * 
   * for (int j = 0; j < locationsJson.size(); ++j) { Json locationJson =
   * locationsJson.get(j);
   * 
   * UCSCTrackRegion r = new
   * UCSCTrackRegion(Chromosome.parse(locationJson.get("c").getAsString()),
   * locationJson.get("s").getAsInt(), locationJson.get("e").getAsInt());
   * 
   * peaks.getRegions().add(r); }
   * 
   * allPeaks.add(peaks); }
   * 
   * return allPeaks; }
   */

  @Override
  public Vfs vfs() {
    return mVfs;
  }
}
