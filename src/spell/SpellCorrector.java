package spell;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;

public class SpellCorrector implements ISpellCorrector {
  Trie trie = new Trie();

  @Override
  public void useDictionary(String dictionaryFileName) throws IOException {
    Scanner scan = new Scanner(new FileInputStream(dictionaryFileName));
    while (scan.hasNext()) {
      String key = scan.next();
      trie.add(key);
    }
    scan.close();
  }

  @Override
  public String suggestSimilarWord(String inputWord) {
    ArrayList<String> correct = new ArrayList<>();
    ArrayList<String> edited = new ArrayList<>();
    String key = inputWord.toLowerCase();

    if (trie.find(inputWord) != null) {
      return key;
    }

    deletion(key, correct, edited, false);
    transposition(key, correct, edited, false);
    alteration(key, correct, edited, false);
    insertion(key, correct, edited, false);

    if (correct.isEmpty()) {
      for (int i = 0; i < edited.size(); i++) {
        deletion(edited.get(i), correct, edited, true);
        transposition(edited.get(i), correct, edited, true);
        alteration(edited.get(i), correct, edited, true);
        insertion(edited.get(i), correct, edited, true);
      }
    }
    correct.sort(new SortList());
    ArrayList<String> finalWords = new ArrayList<>();
    for (String correctWord : correct) {
      Node start=(Node) trie.find(correct.get(0));
      Node end=(Node) trie.find(correctWord);
      if (start.count == end.count) {
        finalWords.add(correctWord);
      }
    }
    Collections.sort(finalWords);
    if (finalWords.size() == 0) {
      return null;
    } else {
      return finalWords.get(0);
    }
  }

  class SortList implements Comparator<String> {
    @Override
    public int compare(String o1, String o2) {
      Node start = (Node) trie.find(o1);
      Node end = (Node) trie.find(o2);
      if (start.count < end.count) {
        return 1;
      } else {
        return -1;
      }
    }
  };

  private void deletion(String key, ArrayList<String> correct, ArrayList<String> edited, boolean cycled) {
    for (int i = 0; i <= key.length(); i++) {
      String start;
      String end;
      String tempKey;
      if (i == 0) {
        if (key.length() <= 1) {
          tempKey = "";
        } else {
          tempKey = key.substring(1);
        }
      } else if (i == key.length()) {
        tempKey = key.substring(0, i);
      } else {
        start = key.substring(0, i);
        end = key.substring(i + 1);
        StringBuilder newKey = new StringBuilder();
        newKey.append(start);
        newKey.append(end);
        tempKey = newKey.toString();
      }
      if (trie.find(tempKey) == null) {
        if (!cycled) {
          edited.add(tempKey);
        }
      } else {
        correct.add(tempKey);
      }
    }
  }

  private void transposition(String key, ArrayList<String> correct, ArrayList<String> edited, boolean cycled) {
    for (int i = 0; i < key.length() - 1; i++) {
      char[] letterArray = key.toCharArray();
      char parseLetters = letterArray[i];
      letterArray[i] = letterArray[i + 1];
      letterArray[i + 1] = parseLetters;
      String newKey = new String(letterArray);
      if (trie.find(newKey) == null) {
        if (!cycled) {
          edited.add(newKey);
        }
      } else {
        correct.add(newKey);
      }
    }
  }

  private void alteration(String key, ArrayList<String> correct, ArrayList<String> edited, boolean cycled) {
    for (int i = 0; i < key.length(); i++) {
      char parseLetters = 'a';
      for (int j = 0; j < Trie.ALPHABET_SIZE; j++) {
        char[] letterArray = key.toCharArray();
        letterArray[i] = parseLetters;
        String newKey = new String(letterArray);
        parseLetters++;
        if (trie.find(newKey) == null) {
          if (!cycled) {
            edited.add(newKey);
          }
        } else {
          correct.add(newKey);
        }
      }
    }
  }

  private void insertion(String key, ArrayList<String> correct, ArrayList<String> edited, boolean cycled) {
    for (int i = 0; i < key.length(); i++) {
      char parseLetters = 'a';
      for (int j = 0; j < Trie.ALPHABET_SIZE; j++) {
        StringBuilder tempKey = new StringBuilder();
        String newKey;
        if (i == 0) {
          tempKey.append(parseLetters);
          tempKey.append(key);
        } else {
          tempKey.append(key, 0, i);
          tempKey.append(parseLetters);
          tempKey.append(key.substring(i));
        }
        newKey = tempKey.toString();
        if (trie.find(newKey) == null) {
          if (!cycled) {
            edited.add(newKey);
          }
        } else {
          correct.add(newKey);
        }
        parseLetters++;
      }
    }
    StringBuilder key2 = new StringBuilder();
    for (int i = 0; i < Trie.ALPHABET_SIZE; i++) {
      char parse = 'a';
      key2.append(key);
      key2.append(parse);
      String newKey2 = key2.toString();
      parse++;
      if (trie.find(newKey2) == null) {
        if (!cycled) {
          edited.add(newKey2);
        }
      } else {
        correct.add(newKey2);
      }
    }
  }
}