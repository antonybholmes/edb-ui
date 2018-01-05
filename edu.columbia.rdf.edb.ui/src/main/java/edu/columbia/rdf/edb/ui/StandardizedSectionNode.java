package edu.columbia.rdf.edb.ui;

/**
 * Sections are hierarchical orderings of key/value pairs.
 * 
 * @author Antony Holmes Holmes
 *
 */
public class StandardizedSectionNode extends SectionNode {
  private static final long serialVersionUID = 1L;

  public StandardizedSectionNode(String name) {
    super(standardize(name));
  }

  public StandardizedSectionNode(String name, String value) {
    super(standardize(name), value);
  }
}
