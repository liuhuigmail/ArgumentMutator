package argumentMutator.similarity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import uk.ac.open.crc.intt.IdentifierNameTokeniser;
import uk.ac.open.crc.intt.IdentifierNameTokeniserFactory;

public class WordUtil {

	public static List<String> splitWords(String string) {
		List<String> lowerCaseWords = new ArrayList<String>(); 
		List<String> parts = Arrays.asList(string.split("_"));
		for (String part : parts) {
			List<String> words = spliteWordsByHump(part);
			for (String word : words) {
				lowerCaseWords.add(word.toLowerCase());
			}
		}
		return lowerCaseWords;
	}
	
	private static List<String> spliteWordsByHump(String string) {
		List<String> words = new ArrayList<String>();
		
		char[] charArray = string.toCharArray();
        StringBuilder stringBuilder = new StringBuilder();
        
        for (char c : charArray) {
                if (Character.isDigit(c)) {
                        String word = stringBuilder.toString();
                        if (isWordLegal(word)) {
                                words.add(word);
                        }
                        stringBuilder = new StringBuilder();
                } else if (Character.isUpperCase(c)) {
                        String word = stringBuilder.toString();
                        if (isWordLegal(word)) {
                                words.add(word);
                        }
                        stringBuilder = new StringBuilder();
                        stringBuilder.append(c);
                } else {
                        stringBuilder.append(c);
                }
        }
        
        String word = stringBuilder.toString();
        if (isWordLegal(word)) {
                words.add(stringBuilder.toString());
        }
        
        return words;
	}
	
	private static boolean isWordLegal(String word) {
		return word.length() >= 3;
	}
}
