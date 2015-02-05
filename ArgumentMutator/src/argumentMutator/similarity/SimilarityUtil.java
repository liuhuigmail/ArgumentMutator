package argumentMutator.similarity;

import java.util.ArrayList;
import java.util.List;

import argumentMutator.global.Config;

public class SimilarityUtil {

	public static double calculate(String str1, String str2) {
		List<String> words1 = WordUtil.splitWords(str1);
		List<String> words2 = WordUtil.splitWords(str2);
		return calculate(words1, words2);
	}

	private static double calculate(List<String> words1, List<String> words2) {
		if (words1.size() == 1 && words2.size() > 0
				&& words1.get(0).equals(words2.get(words2.size() - 1))) {
			return 1.0;
		}
		
		if (words2.size() == 1 && words1.size() > 0
				&& words2.get(0).equals(words1.get(words1.size() - 1))) {
			return 1.0;
		}

		List<String> uniqueWords1 = getUniqueWords(words1);
		List<String> uniqueWords2 = getUniqueWords(words2);

		int equalCount = 0;
		for (String word1 : uniqueWords1) {
			for (String word2 : uniqueWords2) {
				if (wordEqual(word1, word2)) {
					equalCount++;
					break;
				}
			}
		}

		int maxWordCount = Math.max(uniqueWords1.size(), uniqueWords2.size());
		return 1.0 * equalCount / maxWordCount;
	}

	private static List<String> getUniqueWords(List<String> words) {
		List<String> uniqueWords = new ArrayList<String>();
		for (String word : words) {
			boolean isUnique = true;
			for (String uniqueWord : uniqueWords) {
				if (wordEqual(word, uniqueWord)) {
					isUnique = false;
					break;
				}
			}
			if (isUnique) {
				uniqueWords.add(word);
			}
		}
		return uniqueWords;
	}

	private static boolean wordEqual(String word1, String word2) {
		if (word1.contains(word2) || word2.contains(word1)) {
			return true;
		}
		int distance = editDistance(word1, word2);
		int maxLength = Math.max(word1.length(), word2.length());
		return ((maxLength - distance) / maxLength) > Config.MIN_EDIT_DISTANCE_SIMILARITY;
	}

	private static int editDistance(String str1, String str2) {
		char[] s = str1.toCharArray();
		char[] t = str2.toCharArray();
		int len1 = str1.length();
		int len2 = str2.length();
		int d[][] = new int[len1 + 1][len2 + 1];
		for (int i = 0; i <= len1; i++) {
			d[i][0] = i;
		}
		for (int i = 0; i <= len2; i++) {
			d[0][i] = i;
		}
		for (int i = 1; i <= len1; i++) {
			for (int j = 1; j <= len2; j++) {
				if (s[i - 1] == t[j - 1]) {
					d[i][j] = d[i - 1][j - 1];
				} else {
					int insert = d[i][j - 1] + 1;
					int del = d[i - 1][j] + 1;
					int update = d[i - 1][j - 1] + 1;
					d[i][j] = Math.min(insert, del) > Math.min(del, update) ? Math
							.min(del, update) : Math.min(insert, del);
				}
			}
		}
		return d[len1][len2];
	}
	
	public static void main(String[] args) {
		System.out.println(editDistance("length", "enght"));
	}
	
}
