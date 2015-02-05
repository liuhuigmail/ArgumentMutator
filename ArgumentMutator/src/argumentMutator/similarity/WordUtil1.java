package argumentMutator.similarity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import uk.ac.open.crc.intt.IdentifierNameTokeniser;
import uk.ac.open.crc.intt.IdentifierNameTokeniserFactory;

public class WordUtil1 {

	public static List<String> splitWords(String string) {
		List<String> lowerCaseWords = new ArrayList<String>(); 
		
		IdentifierNameTokeniserFactory factory = new IdentifierNameTokeniserFactory();
		IdentifierNameTokeniser tokeniser = factory.create();
		String[] tokens;
		tokens = tokeniser.tokenise(string);
		for(int i = 0; i < tokens.length; i++){
			lowerCaseWords.add(tokens[i].toLowerCase());
		}
		
		return lowerCaseWords;
	}
	
	
	
}
