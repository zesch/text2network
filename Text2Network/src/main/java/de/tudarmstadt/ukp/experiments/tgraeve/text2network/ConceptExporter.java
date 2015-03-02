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
import org.apache.uima.fit.component.JCasConsumer_ImplBase;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;

import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Sentence;
import de.tudarmstadt.ukp.dkpro.core.api.syntax.type.chunk.Chunk;
import de.tudarmstadt.ukp.experiments.tgraeve.text2network.type.Concept;

public class ConceptExporter extends JCasConsumer_ImplBase 
{
	
	protected String outputFile;
	protected List<Nounphrase> nounphrases;

	@Override
	public void initialize(UimaContext context) throws ResourceInitializationException
	{
		
		super.initialize(context);
		
		for (int i = 0; i< context.getConfigParameterNames().length; i = i+1)
		{
			System.out.println(context.getConfigParameterNames()[i]);
		}
		
		this.outputFile = context.getConfigParameterValue(context.getConfigParameterNames()[0]).toString();

	}
	
	@Override
	public void process(JCas aJCas) throws AnalysisEngineProcessException
	{
		
		nounphrases = new ArrayList<Nounphrase>();
		
		for (Concept concept : JCasUtil.select(aJCas, Concept.class))
		{
			nounphrases.add(new Nounphrase(concept.getCoveredText()));
			System.out.println(concept.getCoveredText());
		}
		
		this.export(this.outputFile);
		
	}
	
	@Override
	public void destroy()
	{
		super.destroy();
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
	
//	protected List<Nounphrase> getNounphrase(Sentence sentence)
//	{
//		
//		List<Nounphrase> nounphrases = new ArrayList<Nounphrase>();
//		
//		for (Chunk chunk : JCasUtil.selectCovered(Chunk.class, sentence))
//		{
//			if (chunk.getChunkValue().equals("NP"))
//			{
//				System.out.println(chunk.getCoveredText());
//				nounphrases.add(new Nounphrase(chunk.getCoveredText()));
//			}
//		}
//		return nounphrases;	
//	}

}
