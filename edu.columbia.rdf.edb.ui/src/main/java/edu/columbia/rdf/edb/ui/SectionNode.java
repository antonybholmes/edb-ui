package edu.columbia.rdf.edb.ui;

import org.jebtk.core.KeyValueNode;
import org.jebtk.core.settings.KeyNode;

import edu.columbia.rdf.edb.DataViewField;

/**
 * Sections are hierarchical orderings of key/value pairs.
 * 
 * @author Antony Holmes Holmes
 *
 */
public class SectionNode extends KeyValueNode<String> {
  private static final long serialVersionUID = 1L;

  public SectionNode(String name) {
    super(name);
  }

  public SectionNode(String name, String value) {
    super(name, value);
  }

  public KeyValueNode<String> getChild(DataViewField field) {
    return getChild(field.getName());
  }

  public KeyValueNode<String> getChild(String sectionType) {
    return getChild(standardize(sectionType));
  }

  public static String standardize(String name) {
    return KeyNode.standardize(name);
  }
}
