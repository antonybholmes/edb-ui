package edu.columbia.rdf.edb.ui;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.abh.common.bioinformatics.annotation.Type;
import org.abh.common.collections.CollectionUtils;
import org.abh.common.json.Json;
import org.abh.common.path.Path;
import org.abh.common.search.SearchStackElement;

import edu.columbia.rdf.edb.Experiment;
import edu.columbia.rdf.edb.FileDescriptor;
import edu.columbia.rdf.edb.Group;
import edu.columbia.rdf.edb.Groups;
import edu.columbia.rdf.edb.Person;
import edu.columbia.rdf.edb.Sample;
import edu.columbia.rdf.edb.SampleTags;
import edu.columbia.rdf.edb.Species;

/**
 * Represents a database of experiments and samples. These can be from
 * caArray or some other database.
 *
 * @author Antony Holmes Holmes
 *
 */
public abstract class Repository implements Serializable {

	private static final long serialVersionUID = 1L;
	public static final Path ALL_PATH = new Path("/All");
	public static final Collection<Type> ALL_TYPES = Collections.emptySet();
	public static final Collection<Species> ALL_ORGANISMS = Collections.emptySet();
	public static final Groups ALL_GROUPS = new Groups(new HashSet<Group>(), true);
	
	
	public abstract Experiment getExperiment(int id);
	
	public abstract Sample getSample(int id) throws IOException;

	public abstract Sample getSample(String name) throws IOException;
	
	public abstract List<FileDescriptor> getExperimentFiles(int experimentId) throws IOException;
	
	public FileDescriptor getExperimentFilesDir(Experiment experiment) throws IOException {
		return getExperimentFilesDir(experiment.getId());
	}
	
	public abstract FileDescriptor getExperimentFilesDir(int experimentId) throws IOException;
	
	public Set<FileDescriptor> getSampleFiles(Collection<Sample> samples) throws IOException {
		Set<FileDescriptor> ret = new TreeSet<FileDescriptor>();

		for (Sample sample : samples) {
			for (FileDescriptor fc : getSampleFiles(sample)) {
				ret.add(fc);
			}
		}
		
		return ret;
	}
	
	public List<FileDescriptor> getSampleFiles(Sample sample) throws IOException {
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
	public abstract List<FileDescriptor> getSampleFiles(int id) throws IOException;
	
	/**
	 * Should return a file downloader.
	 * 
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	public abstract FileDownloader getFileDownloader() throws UnsupportedEncodingException;
	
	public abstract Vfs vfs();

	public abstract void saveSession(File sessionFile) throws IOException;

	public abstract List<Sample> getAllSamples();

	/**
	 * Given a search stack and an annotation path, find all the samples
	 * matching some criteria.
	 * 
	 * @param searchStack
	 * @param path
	 * @return
	 */
	public abstract List<Sample> searchSamples(List<SearchStackElement<Sample>> searchQueue, 
			Path path);

	public List<Sample> searchSamples(String query) throws IOException {
		 return searchSamples(query, ALL_PATH, ALL_TYPES, ALL_ORGANISMS, ALL_GROUPS);
	}
	
	public List<Sample> searchSamples(String query, 
			Path path,
			Type dataType) throws IOException {
		return searchSamples(query, path, dataType, ALL_ORGANISMS);
	}
	
	public List<Sample> searchSamples(String query, 
			Path path,
			Type dataType,
			Collection<Species> organisms) throws IOException {
		return searchSamples(query, path, CollectionUtils.asSet(dataType), organisms, ALL_GROUPS);
	}
	
	/**
	 * Search samples filtering only by groups.
	 * 
	 * @param query
	 * @param organisms
	 * @param groups
	 * @return
	 * @throws IOException
	 */
	public List<Sample> searchSamples(String query, Groups groups) throws IOException {
		 return searchSamples(query, ALL_PATH, ALL_TYPES, ALL_ORGANISMS, groups);
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
	public List<Sample> searchSamples(String query, 
			Collection<Species> organisms,
			Groups groups) throws IOException {
		 return searchSamples(query, ALL_PATH, ALL_TYPES, organisms, groups);
	}
	
	public List<Sample> searchSamples(String query, 
			Collection<Type> dataTypes,
			Collection<Species> organisms,
			Groups groups) throws IOException {
		 return searchSamples(query, ALL_PATH, dataTypes, organisms, groups);
	}
	
	public abstract List<Sample> searchSamples(String query, 
			Path path,
			Collection<Type> dataTypes,
			Collection<Species> organisms,
			Groups groups) throws IOException;

	/**
	 * Returns the peaks associated with a list of samples. Duplicates are removed.
	 * 
	 * @param samples
	 * @return
	 * @throws ParseException
	 * @throws IOException 
	 * @throws java.text.ParseException 
	 */
	/*
	public List<Peaks> searchChipSeqPeaks(List<Sample> samples) throws ParseException, IOException, java.text.ParseException {
		List<Peaks> allPeaks = new UniqueList<Peaks>();
		
		for (Sample sample : samples) {
			for (Peaks peaks : searchChipSeqPeaks(sample)) {
				allPeaks.add(peaks);
			}
		}
		
		return allPeaks;
	}
	*/
	
	//public abstract List<Peaks> searchChipSeqPeaks(Sample sample) throws MalformedURLException, UnsupportedEncodingException, ParseException, IOException, java.text.ParseException;

	public List<Sample> parseSampleJson(Json json) throws IOException {
		return null;
	}

	public void cacheTags(int sampleId, SampleTags tags) throws IOException {
		// Do nothing
	}
	
	public void cacheTag(int sampleId, int tagId, SampleTags sampleTags) throws IOException {
		// TODO Auto-generated method stub
		
	}

	public void cacheGEO(Sample sample) throws IOException {
		// TODO Auto-generated method stub
	}
	
	public void cachePersons(int sampleId, Collection<Person> persons) throws IOException {
		// TODO Auto-generated method stub
	}


	/**
	 * Should return the data types supported by this database (e.g.
	 * ChIP-seq or microarray etc).
	 * 
	 * @return 		A collection of types.
	 */
	public Collection<Type> getDataTypes() {
		return Collections.emptyList();
	}

	public Collection<Species> getOrganisms() {
		return Collections.emptyList();
	}

	public Collection<Group> getUserGroups() {
		return Collections.emptyList();
	}
	
	public Collection<Group> getGroups() {
		return Collections.emptyList();
	}
}
