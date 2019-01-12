package edu.columbia.rdf.edb.ui;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.jebtk.bioinformatics.annotation.Genome;
import org.jebtk.bioinformatics.annotation.Type;
import org.jebtk.core.collections.CollectionUtils;
import org.jebtk.core.json.Json;
import org.jebtk.core.path.Path;
import org.jebtk.core.search.SearchStackElement;
import org.jebtk.core.text.TextUtils;

import edu.columbia.rdf.edb.Experiment;
import edu.columbia.rdf.edb.Group;
import edu.columbia.rdf.edb.Person;
import edu.columbia.rdf.edb.Sample;
import edu.columbia.rdf.edb.SampleSet;
import edu.columbia.rdf.edb.SampleTags;
import edu.columbia.rdf.edb.Species;
import edu.columbia.rdf.edb.VfsFile;

/**
 * Represents a database of experiments and samples. These can be from caArray
 * or some other database.
 *
 * @author Antony Holmes
 *
 */
public abstract class Repository implements Serializable {

  private static final long serialVersionUID = 1L;
  public static final Path ALL_PATH = new Path("/All");
  public static final Collection<Person> ALL_PERSONS = Collections.emptySet();
  public static final Collection<Type> ALL_TYPES = Collections.emptySet();
  public static final Collection<Species> ALL_ORGANISMS = Collections
      .emptySet();
  public static final Collection<Group> ALL_GROUPS = Collections
      .emptySet();
  
  public static final Collection<SampleSet> NO_SAMPLE_SETS = Collections
      .emptySet();

  public abstract Experiment getExperiment(int id);

  public abstract Sample getSample(int id) throws IOException;

  public abstract Sample getSample(String name) throws IOException;

  public abstract List<VfsFile> getExperimentFiles(int experimentId)
      throws IOException;

  public VfsFile getExperimentFilesDir(Experiment experiment)
      throws IOException {
    return getExperimentFilesDir(experiment.getId());
  }

  public abstract VfsFile getExperimentFilesDir(int experimentId)
      throws IOException;

  public Set<VfsFile> getSampleFiles(Collection<Sample> samples)
      throws IOException {
    Set<VfsFile> ret = new TreeSet<VfsFile>();

    for (Sample sample : samples) {
      for (VfsFile fc : getSampleFiles(sample)) {
        ret.add(fc);
      }
    }

    return ret;
  }

  public List<VfsFile> getSampleFiles(Sample sample) throws IOException {
    return getSampleFiles(sample.getId());
  }

  /**
   * Downloads the file descriptors for a given sample.
   * 
   * @param id
   * @return
   * @throws IOException
   * @throws ParseException
   */
  public abstract List<VfsFile> getSampleFiles(int id)
      throws IOException;

  /**
   * Should return a file downloader.
   * 
   * @return
   * @throws UnsupportedEncodingException
   */
  public abstract FileDownloader getFileDownloader()
      throws UnsupportedEncodingException;

  public abstract Vfs vfs();

  public abstract void saveSession(File sessionFile) throws IOException;

  public SearchResults getAllSamples(int page) throws IOException {
    return getAllSamples(NO_SAMPLE_SETS, page);
  }

  public SearchResults getAllSamples(Collection<SampleSet> sets, int page) throws IOException {
    return searchSamples(TextUtils.EMPTY_STRING, sets, page);
  }

  /**
   * Given a search stack and an annotation path, find all the samples matching
   * some criteria.
   * 
   * @param searchStack
   * @param path
   * @return
   */
  public abstract List<Sample> searchSamples(
      List<SearchStackElement> searchQueue,
      Path path);

  public SearchResults searchSamples(String query, int page) throws IOException {
    return searchSamples(query, NO_SAMPLE_SETS, page);
  }
  
  public SearchResults searchSamples(String query, Collection<SampleSet> sets, int page) throws IOException {
    return searchSamples(query, ALL_PATH, ALL_PERSONS, ALL_TYPES, ALL_ORGANISMS, ALL_GROUPS, sets, page);
  }

  public SearchResults searchSamples(String query, Path path, Type dataType)
      throws IOException {
    return searchSamples(query, path, dataType, ALL_ORGANISMS);
  }

  public SearchResults searchSamples(String query,
      Path path,
      Type dataType,
      Collection<Species> organisms) throws IOException {
    return searchSamples(query,
        path,
        ALL_PERSONS,
        CollectionUtils.asSet(dataType),
        organisms,
        ALL_GROUPS,
        NO_SAMPLE_SETS,
        1);
  }

  /**
   * Search samples filtering only by organisms and groups.
   * 
   * @param query
   * @param organisms
   * @param groups
   * @return
   * @throws IOException
   */
  public SearchResults searchSamples(String query,
      Collection<Species> organisms,
      Collection<Group> groups) throws IOException {
    return searchSamples(query, ALL_PATH, ALL_PERSONS, ALL_TYPES, organisms, groups, NO_SAMPLE_SETS, 1);
  }

  public SearchResults searchSamples(String query,
      Collection<Type> dataTypes,
      Collection<Species> organisms,
      Collection<Group> groups) throws IOException {
    return searchSamples(query, ALL_PATH, ALL_PERSONS, dataTypes, organisms, groups, NO_SAMPLE_SETS, 1);
  }
  
  public SearchResults searchSamples(String query,
      Collection<Person> persons,
      Collection<Type> dataTypes,
      Collection<Species> organisms,
      Collection<Group> groups) throws IOException {
    return searchSamples(query, ALL_PATH, persons, dataTypes, organisms, groups, NO_SAMPLE_SETS, 1);
  }
  
  public SearchResults searchSamples(String query,
      Path path,
      Collection<Type> dataTypes,
      Collection<Species> organisms,
      Collection<Group> groups) throws IOException {
    return searchSamples(query, path, dataTypes, organisms, groups, NO_SAMPLE_SETS);
  }
  
  public SearchResults searchSamples(String query,
      Path path,
      Collection<Type> dataTypes,
      Collection<Species> organisms,
      Collection<Group> groups,
      Collection<SampleSet> sets) throws IOException {
    return searchSamples(query, path, dataTypes, organisms, groups, sets, 1);
  }
  
  public SearchResults searchSamples(String query,
      Path path,
      Collection<Type> dataTypes,
      Collection<Species> organisms,
      Collection<Group> groups,
      Collection<SampleSet> sets,
      int page) throws IOException {
    return searchSamples(query, path, ALL_PERSONS, dataTypes, organisms, groups, sets, page);
  }

  public abstract SearchResults searchSamples(String query,
      Path path,
      Collection<Person> persons,
      Collection<Type> dataTypes,
      Collection<Species> organisms,
      Collection<Group> groups,
      Collection<SampleSet> sets,
      int page) throws IOException;

  /**
   * Returns the peaks associated with a list of samples. Duplicates are
   * removed.
   * 
   * @param samples
   * @return
   * @throws ParseException
   * @throws IOException
   * @throws java.text.ParseException
   */
  /*
   * public List<Peaks> searchChipSeqPeaks(List<Sample> samples) throws
   * ParseException, IOException, java.text.ParseException { List<Peaks>
   * allPeaks = new UniqueList<Peaks>();
   * 
   * for (Sample sample : samples) { for (Peaks peaks :
   * searchChipSeqPeaks(sample)) { allPeaks.add(peaks); } }
   * 
   * return allPeaks; }
   */

  // public abstract List<Peaks> searchChipSeqPeaks(Sample sample) throws
  // MalformedURLException, UnsupportedEncodingException, ParseException,
  // IOException, java.text.ParseException;

  public SearchResults parseSampleJson(Json json) throws IOException {
    return null;
  }

  public void cacheTags(int sampleId, SampleTags tags) throws IOException {
    // Do nothing
  }

  public void cacheTag(int sampleId, int tagId, SampleTags sampleTags)
      throws IOException {
    // TODO Auto-generated method stub

  }

  public void cacheGEO(Sample sample) throws IOException {
    // TODO Auto-generated method stub
  }

  public void cachePersons(int sampleId, Collection<Person> persons)
      throws IOException {
    // TODO Auto-generated method stub
  }

  /**
   * Should return the data types supported by this database (e.g. ChIP-seq or
   * microarray etc).
   * 
   * @return A collection of types.
   */
  public Collection<Type> getDataTypes() {
    return Collections.emptyList();
  }

  public Collection<Species> getOrganisms() {
    return Collections.emptyList();
  }
  
  public Collection<Person> getPersons() {
    return Collections.emptyList();
  }

  public Collection<Group> getUserGroups() {
    return Collections.emptyList();
  }

  public Collection<Group> getGroups() {
    return Collections.emptyList();
  }
  
  public Iterable<SampleSet> getSets() {
    return Collections.emptyList();
  }
  
  public Iterable<Genome> getGenomes() {
    return Collections.emptyList();
  }

  public Iterable<Genome> getGenomes(Sample sample) throws IOException {
    return getGenomes(sample.getId());
  }

  /**
   * Get the genomes associated with a sample.
   * 
   * @param sid
   * @return
   * @throws IOException
   */
  public Iterable<Genome> getGenomes(int sid) throws IOException {
    return Collections.emptyList();
  }
  
  public Iterable<VfsFile> getGenomeFiles(Sample sample, Genome genome) throws IOException {
    return getGenomeFiles(sample.getId(), genome.getId());
  }

  /**
   * Get the genomes associated with a sample.
   * 
   * @param sid
   * @return
   * @throws IOException
   */
  public Iterable<VfsFile> getGenomeFiles(int sid, int gid) throws IOException {
    return Collections.emptyList();
  }

  
}
