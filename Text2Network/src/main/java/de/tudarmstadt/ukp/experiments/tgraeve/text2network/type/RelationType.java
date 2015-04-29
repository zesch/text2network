

/* First created by JCasGen Tue Mar 10 12:24:25 CET 2015 */
package de.tudarmstadt.ukp.experiments.tgraeve.text2network.type;

import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;
import org.apache.uima.jcas.tcas.Annotation;


/** 
 * Updated by JCasGen Wed Apr 29 03:29:58 CEST 2015
 * XML source: /Users/Tobias/Dropbox/Studium/BA II/Programmierung/de.tudarmstadt.ukp.experiments.tgraeve.text2network/src/main/resources/desc/type/Concept.xml
 * @generated */
public class RelationType extends Annotation {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(RelationType.class);
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
  protected RelationType() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated
   * @param addr low level Feature Structure reference
   * @param type the type of this Feature Structure 
   */
  public RelationType(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated
   * @param jcas JCas to which this Feature Structure belongs 
   */
  public RelationType(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated
   * @param jcas JCas to which this Feature Structure belongs
   * @param begin offset to the begin spot in the SofA
   * @param end offset to the end spot in the SofA 
  */  
  public RelationType(JCas jcas, int begin, int end) {
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
  //* Feature: label

  /** getter for label - gets 
   * @generated
   * @return value of the feature 
   */
  public String getLabel() {
    if (RelationType_Type.featOkTst && ((RelationType_Type)jcasType).casFeat_label == null)
      jcasType.jcas.throwFeatMissing("label", "de.tudarmstadt.ukp.experiments.tgraeve.text2network.type.RelationType");
    return jcasType.ll_cas.ll_getStringValue(addr, ((RelationType_Type)jcasType).casFeatCode_label);}
    
  /** setter for label - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setLabel(String v) {
    if (RelationType_Type.featOkTst && ((RelationType_Type)jcasType).casFeat_label == null)
      jcasType.jcas.throwFeatMissing("label", "de.tudarmstadt.ukp.experiments.tgraeve.text2network.type.RelationType");
    jcasType.ll_cas.ll_setStringValue(addr, ((RelationType_Type)jcasType).casFeatCode_label, v);}    
   
    
  //*--------------*
  //* Feature: rType

  /** getter for rType - gets 
   * @generated
   * @return value of the feature 
   */
  public Annotation getRType() {
    if (RelationType_Type.featOkTst && ((RelationType_Type)jcasType).casFeat_rType == null)
      jcasType.jcas.throwFeatMissing("rType", "de.tudarmstadt.ukp.experiments.tgraeve.text2network.type.RelationType");
    return (Annotation)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((RelationType_Type)jcasType).casFeatCode_rType)));}
    
  /** setter for rType - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setRType(Annotation v) {
    if (RelationType_Type.featOkTst && ((RelationType_Type)jcasType).casFeat_rType == null)
      jcasType.jcas.throwFeatMissing("rType", "de.tudarmstadt.ukp.experiments.tgraeve.text2network.type.RelationType");
    jcasType.ll_cas.ll_setRefValue(addr, ((RelationType_Type)jcasType).casFeatCode_rType, jcasType.ll_cas.ll_getFSRef(v));}    
  }

    