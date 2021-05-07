package spell;

public class Trie implements ITrie {
  static final int ALPHABET_SIZE = 26;
  Node root = new Node();
  int wordCount = 0;
  int nodeCount = 1;

  @Override
  public void add(String word) {
    if (root.search(word) == null) {
      wordCount++;
    }
    nodeCount += root.add(word);
  }

  @Override
  public INode find(String word) {
    return root.search(word);
  }

  @Override
  public int getWordCount() {
    return wordCount;
  }

  @Override
  public int getNodeCount() {
    return nodeCount;
  }

  public int hashCode() {
    int t = root.getNonNull();
    return nodeCount * wordCount * t;
  }

  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (this == obj) {
      return true;
    }
    if (this.getClass() != obj.getClass()) {
      return false;
    }
    Trie t = (Trie) obj;
    return this.root.equals(t.root) && this.wordCount == t.wordCount && this.nodeCount == t.nodeCount;
  }

  public String toString() {
    StringBuilder key = new StringBuilder();
    StringBuilder t = new StringBuilder();
    toString(root, key, t);
    return key.toString();
  }

  private void toString(Node pCrawl, StringBuilder key, StringBuilder t) {
    if (pCrawl.count != 0) {
      key.append(t.toString());
      key.append("\n");
    }
    char parse = 'a';
    for (Node node : pCrawl.getNodes()) {
      if (node != null) {
        t.append(parse);
        toString(node, key, t);
      }
      parse++;
    }
    if (t.length() != 0) {
      t.delete(t.length() - 1, t.length());
    }
  }
}