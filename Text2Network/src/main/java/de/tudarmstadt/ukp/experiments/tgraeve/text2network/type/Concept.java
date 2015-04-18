

/* First created by JCasGen Mon Mar 02 01:35:08 CET 2015 */
package de.tudarmstadt.ukp.experiments.tgraeve.text2network.type;

import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;
import org.apache.uima.jcas.tcas.Annotation;


/** 
 * Updated by JCasGen Sun Apr 19 01:02:38 CEST 2015
 * XML source: /Users/Tobias/Dropbox/Studium/BA II/Programmierung/de.tudarmstadt.ukp.experiments.tgraeve.text2network/src/main/resources/desc/type/Concept.xml
 * @generated */
public class Concept extends Annotation {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(Concept.class);
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int type = typeIndexID;
  /** @generated
   * @return index of the type  
   */
  @Override
  public              int getTypeIndexID() {return typeIndexID;}
 
  /** Never called.  Disable default constructor
   * @generated */
  protected Concept() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated
   * @param addr low level Feature Structure reference
   * @param type the type of this Feature Structure 
   */
  public Concept(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated
   * @param jcas JCas to which this Feature Structure belongs 
   */
  public Concept(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated
   * @param jcas JCas to which this Feature Structure belongs
   * @param begin offset to the begin spot in the SofA
   * @param end offset to the end spot in the SofA 
  */  
  public Concept(JCas jcas, int begin, int end) {
    super(jcas);
    setBegin(begin);
    setEnd(end);
    readObject();
  }   

  /** 
   * <!-- begin-user-doc -->
   * Write your own initialization here
   * <!-- end-user-doc -->
   *
   * @generated modifiable 
   */
  private void readObject() {/*default - does nothing empty block */}
     
  //*--------------*
  //* Feature: text

  /** getter for text - gets 
   * @generated
   * @return value of the feature 
   */
  public String getText() {
    if (Concept_Type.featOkTst && ((Concept_Type)jcasType).casFeat_text == null)
      jcasType.jcas.throwFeatMissing("text", "de.tudarmstadt.ukp.experiments.tgraeve.text2network.type.Concept");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Concept_Type)jcasType).casFeatCode_text);}
    
  /** setter for text - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setText(String v) {
    if (Concept_Type.featOkTst && ((Concept_Type)jcasType).casFeat_text == null)
      jcasType.jcas.throwFeatMissing("text", "de.tudarmstadt.ukp.experiments.tgraeve.text2network.type.Concept");
    jcasType.ll_cas.ll_setStringValue(addr, ((Concept_Type)jcasType).casFeatCode_text, v);}    
   
    
  //*--------------*
  //* Feature: URI

  /** getter for URI - gets 
   * @generated
   * @return value of the feature 
   */
  public String getURI() {
    if (Concept_Type.featOkTst && ((Concept_Type)jcasType).casFeat_URI == null)
      jcasType.jcas.throwFeatMissing("URI", "de.tudarmstadt.ukp.experiments.tgraeve.text2network.type.Concept");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Concept_Type)jcasType).casFeatCode_URI);}
    
  /** setter for URI - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setURI(String v) {
    if (Concept_Type.featOkTst && ((Concept_Type)jcasType).casFeat_URI == null)
      jcasType.jcas.throwFeatMissing("URI", "de.tudarmstadt.ukp.experiments.tgraeve.text2network.type.Concept");
    jcasType.ll_cas.ll_setStringValue(addr, ((Concept_Type)jcasType).casFeatCode_URI, v);}    
  }

    