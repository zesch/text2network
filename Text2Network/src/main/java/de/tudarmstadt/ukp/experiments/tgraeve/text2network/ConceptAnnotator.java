package de.tudarmstadt.ukp.experiments.tgraeve.text2network;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.component.JCasConsumer_ImplBase;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;

import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Sentence;
import de.tudarmstadt.ukp.dkpro.core.api.syntax.type.chunk.Chunk;
import de.tudarmstadt.ukp.dkpro.core.api.syntax.type.chunk.NC;
import de.tudarmstadt.ukp.experiments.tgraeve.text2network.components.Nounphrase;
import de.tudarmstadt.ukp.experiments.tgraeve.text2network.type.Concept;

public class ConceptAnnotator extends JCasAnnotator_ImplBase
{
	public static final String PARAM_CONCEPT_TYPE = "PARAM_CONCEPT_TYPE";
	@ConfigurationParameter(name = PARAM_CONCEPT_TYPE, mandatory = true)
	protected Chunk conceptType;
	
	protected String outputFile;
	protected List<Nounphrase> nounphrases;

	@Override
	public void initialize(UimaContext context) throws ResourceInitializationException
	{
		
		super.initialize(context);

	}
	
	@Override
	public void process(JCas aJCas) throws AnalysisEngineProcessException
	{
		
		for (NC chunk : JCasUtil.select(aJCas, NC.class ))
		{
			if (chunk.getChunkValue().equals("NP"))
			{
				Concept concept = new Concept(aJCas);
				concept.setBegin(chunk.getBegin());
				concept.setEnd(chunk.getEnd());
				concept.addToIndexes();
				System.out.println(chunk.getCoveredText());
			}
			if (chunk.getChunkValue().equals("VP"))
			{
				System.out.println(chunk.getCoveredText());
			}
		}
		
		
//		for (Sentence sentence : JCasUtil.select(aJCas, Sentence.class))
//		{
//			
//			for (Chunk chunk : JCasUtil.selectCovered(Chunk.class, sentence))
//			{
//				if (chunk.getChunkValue().equals("NP"))
//				{
//					Network network = new Network(aJCas);
//					network.setIsNode(true);
//					network.addToIndexes();
//				}
//			}
//		}
	}
	
	@Override
	public void destroy()
	{
		super.destroy();
	}

	protected List<Nounphrase> getNounphrase(Sentence sentence)
	{
		
		List<Nounphrase> nounphrases = new ArrayList<Nounphrase>();
		
		for (Chunk chunk : JCasUtil.selectCovered(Chunk.class, sentence))
		{
			if (chunk.getChunkValue().equals("NP"))
			{
				nounphrases.add(new Nounphrase(chunk.getCoveredText()));
			}
		}
		return nounphrases;	
	}
	
	protected void export(String outputFile)
	{
		
		FileWriter writer = null;
		
		try
		{
			 writer = new FileWriter(outputFile);
			 
			 for(int i = 0; i <nounphrases.size(); i = i+1)
			 {
				 writer.write(nounphrases.get(i).getText());
				 writer.write(System.lineSeparator());
			 }
			 
			 
		}
		catch (IOException e)
		{
			// TODO Automatisch generierter Erfassungsblock
			e.printStackTrace();
		}
		finally
		{
			try
			{
				writer.close();
			}
			catch (IOException e)
			{
				// TODO Automatisch generierter Erfassungsblock
				e.printStackTrace();
			}
		}
		
		
	}

}
