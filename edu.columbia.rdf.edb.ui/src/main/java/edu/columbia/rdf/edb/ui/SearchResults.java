package edu.columbia.rdf.edb.ui;

import java.util.List;

import edu.columbia.rdf.edb.Sample;

public class SearchResults  {

  public final List<Sample> samples;
  public final SearchMetaData metaData;

  public SearchResults(List<Sample> samples, SearchMetaData metaData) {
    this.samples = samples;
    this.metaData = metaData;
  }

}
