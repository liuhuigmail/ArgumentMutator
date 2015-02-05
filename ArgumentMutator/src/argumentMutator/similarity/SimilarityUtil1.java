package argumentMutator.similarity;

import java.util.ArrayList;
import java.util.List;

public class SimilarityUtil1 {

	public static double calculate(String par, String arg) {
		//List<String> parWords = WordUtil1.splitWords(par);
		//List<String> argWords = WordUtil1.splitWords(arg);
		//return calculate(parWords, argWords);
		return sim(par, arg);
	}

	private static double calculate(List<String> parWords, List<String> argWords) {
		
		double simSum = 0.0;
		for(String argWord : argWords){
			simSum = simSum + maxSim(argWord, parWords);
		}
		
		return simSum  / argWords.size();
				
	}
	
	private static double maxSim(String argWord, List<String> parWords){
		double maxSim = 0.0;
		for(String parWord : parWords){
			if(maxSim < sim(argWord, parWord)){
				maxSim = sim(argWord, parWord);
			}
			
		}
		return maxSim;
	}
	
	private static double sim(String str1, String str2){
		
		return 1.00- LevenshteinDistance.computeLevenshteinDistance(str1, str2)/(1.0*(Math.max(str1.length(),str2.length())));
	}
	
	
	private static int editDistance(String str1, String str2) {
		ArrayList<Character> list1 = new ArrayList<Character>();
 	    ArrayList<Character> list2 = new ArrayList<Character>();
 	    for(int i=0; i< str1.length(); i++ ){
 	    	list1.add(str1.charAt(i));
 	    }
 	    for(int i=0; i< str2.length(); i++ ){
 	    	list2.add(str2.charAt(i));
 	    }
 	    
 	    Diff diff = new Diff(list1, list2);
		diff.diff();
		int totalDiff = diff.AmoutofDiffernce();
		return(totalDiff);
	}
	
	public static void main(String[] args) {
		System.out.println( calculate("endModelIndex", "length"));
	}
	
	
}
