package edu.columbia.rdf.edb.ui.microarray;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.jebtk.core.io.PathUtils;
import org.xml.sax.SAXException;

import edu.columbia.rdf.edb.DataView;
import edu.columbia.rdf.edb.ui.DataViewXmlHandler;

/**
 * Plugin for display of microarray data.
 * 
 * @author Antony Holmes
 *
 */
public class MicroarrayDataView extends DataView {

  public static final java.nio.file.Path XML_VIEW_FILE = PathUtils
      .getPath("res/modules/microarray.xml");

  public MicroarrayDataView() {
    super("Microarray");

    try {
      DataViewXmlHandler.loadXml(XML_VIEW_FILE, this);
    } catch (SAXException | IOException | ParserConfigurationException e) {
      e.printStackTrace();
    }

    /*
     * DataViewSection section;
     * 
     * section = new DataViewSection("Source");
     * section.addField(Path.create("/Microarray/Sample/Source/Source_Name"),
     * "Source Name");
     * section.addField(Path.create("/Microarray/Sample/Source/Material_Type"),
     * "Material Type" ); section.addField(Path.create(
     * "/Microarray/Sample/Source/Characteristic/Organism"), "Organism");
     * section.addField(Path.create(
     * "/Microarray/Sample/Source/Characteristic/Organism_Part"),
     * "Organism Part"); section.addField(Path.create(
     * "/Microarray/Sample/Source/Characteristic/Disease_State"),
     * "Disease State"); section.addField(Path.create(
     * "/Microarray/Sample/Source/Characteristic/Cell_Type"), "Cell Type");
     * section.addField(Path.create(
     * "/Microarray/Sample/Source/Characteristic/Treatment"), "Treatment");
     * section.addField(Path.create(
     * "/Microarray/Sample/Source/Characteristic/Time_Point"), "Time Point");
     * section.addField(Path.create(
     * "/Microarray/Sample/Source/Characteristic/Percent_Tumor_Cells"),
     * "% Tumor Cells"); section.addField(Path.create(
     * "/Microarray/Sample/Source/Characteristic/Disease_Status"),
     * "Disease Status"); section.addField(Path.create(
     * "/Microarray/Sample/Source/Characteristic/Gender"), "Gender");
     * section.addField(Path.create(
     * "/Microarray/Sample/Source/Characteristic/Age"), "Age");
     * section.addField(Path.create(
     * "/Microarray/Sample/Source/Characteristic/Clinical_Parameters"),
     * "Clinical Parameters"); section.addField(Path.create(
     * "/Microarray/Sample/Source/Characteristic/Cytogenetics"),
     * "Cytogenetics"); section.addField(Path.create(
     * "/Microarray/Sample/Source/Characteristic/IgV_Status"), "IgV Status");
     * section.addField(Path.create(
     * "/Microarray/Sample/Source/Characteristic/GEP_Based_Classification"),
     * "GEP based classification"); section.addField(Path.create(
     * "/Microarray/Sample/Source/Characteristic/BioSourceProvider"),
     * "BioSourceProvider"); section.addField(Path.create(
     * "/Microarray/Sample/Source/Characteristic/Source_Protocol"),
     * "Source Protocol"); addSection(section);
     * 
     * section = new DataViewSection("Sample");
     * section.addField(Path.create("/Microarray/Sample/Sample/Sample_Name"),
     * "Sample Name");
     * section.addField(Path.create("/Microarray/Sample/Sample/Material_Type"),
     * "Material Type"); section.addField(Path.create(
     * "/Microarray/Sample/Sample/Characteristic/Sample_Protocol"),
     * "Sample Protocol"); section.addField(Path.create(
     * "/Microarray/Sample/Sample/Characteristic/Sample_Operator"),
     * "Sample Operator"); addSection(section);
     * 
     * section = new DataViewSection("Extract");
     * section.addField(Path.create("/Microarray/Sample/Extract/Extract_Name"),
     * "Extract Name");
     * section.addField(Path.create("/Microarray/Sample/Extract/Material_Type"),
     * "Material Type"); section.addField(Path.create(
     * "/Microarray/Sample/Extract/Characteristic/Extract_Protocol"),
     * "Extract Protocol"); section.addField(Path.create(
     * "/Microarray/Sample/Extract/Characteristic/Extract_Operator"),
     * "Extract Operator"); addSection(section);
     * 
     * section = new DataViewSection("Labeled Extract");
     * section.addField(Path.create(
     * "/Microarray/Sample/Labeled_Extract/Labeled_Extract_Name"),
     * "Labeled Extract Name");
     * section.addField(Path.create("/Microarray/Sample/Labeled_Extract/Label"),
     * "Label"); section.addField(Path.create(
     * "/Microarray/Sample/Labeled_Extract/Characteristic/Labeled_Extract_Protocol")
     * , "Labeled Extract Protocol"); section.addField(Path.create(
     * "/Microarray/Sample/Labeled_Extract/Characteristic/Labeled_Extract_Operator")
     * , "Labeled Extract Operator"); section.addField(Path.create(
     * "/Microarray/Sample/Labeled_Extract/Material_Type"), "Material Type");
     * section.addField(Path.create(
     * "/Microarray/Sample/Labeled_Extract/Characteristic/Array_Platform"),
     * "Array Platform"); section.addField(Path.create(
     * "/Microarray/Sample/Labeled_Extract/Characteristic/Hybridization_Protocol"
     * ), "Hybridization Protocol"); section.addField(Path.create(
     * "/Microarray/Sample/Labeled_Extract/Characteristic/Scanning_Protocol"),
     * "Scanning Protocol"); section.addField(Path.create(
     * "/Microarray/Sample/Labeled_Extract/Characteristic/CHP_Normalization_Method")
     * , "CHP Normalization Method"); section.addField(Path.create(
     * "/Microarray/Sample/Labeled_Extract/Characteristic/Hybridization_Facility"
     * ), "Hybridization Facility"); section.addField(Path.create(
     * "/Microarray/Sample/Labeled_Extract/Characteristic/MAS5_Normalization"),
     * "MAS5 Normalization"); section.addField(Path.create(
     * "/Microarray/Sample/Labeled_Extract/Characteristic/MAS5_Normalization_File"
     * ), "MAS5 Normalization File"); section.addField(Path.create(
     * "/Microarray/Sample/Labeled_Extract/Characteristic/RMA_Normalization"),
     * "RMA Normalization"); section.addField(Path.create(
     * "/Microarray/Sample/Labeled_Extract/Characteristic/RMA_Normalization_File"
     * ), "RMA Normalization File"); section.addField(Path.create(
     * "/Microarray/Sample/Labeled_Extract/Characteristic/GEO_Series_Accession")
     * , "GEO Series Accession"); section.addField(Path.create(
     * "/Microarray/Sample/Labeled_Extract/Characteristic/GEO_Accession"),
     * "GEO Accession"); section.addField(Path.create(
     * "/Microarray/Sample/Labeled_Extract/Characteristic/GEO_Platform"),
     * "GEO Platform"); addSection(section);
     * 
     * section = new DataViewSection("Hybridization");
     * section.addField(Path.create(
     * "/Microarray/Sample/Hybridization/Hybridization_Name"),
     * "Hybridization Name"); section.addField(Path.create(
     * "/Microarray/Sample/Hybridization/Array_Data_File"), "Array Data File");
     * section.addField(Path.create(
     * "/Microarray/Sample/Hybridization/Derived_Array_Data_File"),
     * "Derived Array Data File"); addSection(section);
     */
  }

}
