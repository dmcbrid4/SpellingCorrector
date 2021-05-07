package spell;

public class Node implements INode {
  Node[] children = new Node[Trie.ALPHABET_SIZE];
  int count = 0;

  public int add(String key) {
    key = key.toLowerCase();
    int t = 0;
    if (children[key.charAt(0)- 'a'] == null) {
      children[key.charAt(0) - 'a'] = new Node();
      t++;
    }
    if (key.length() == 1) {
      children[key.charAt(0) - 'a'].count++;
    } else {
      String newKey = key.substring(1);
      t += children[key.charAt(0) - 'a'].add(newKey);
    }
    return t;
  }

  public Node search(String key) {
    key = key.toLowerCase();
    if (key.length() == 0) {
      return null;
    }
    Node pCrawl = children[key.charAt(0) - 'a'];
    if (key.length() == 1) {
      if (pCrawl != null && pCrawl.count != 0) {
        return pCrawl;
      } else {
        return null;
      }
    }
    if (pCrawl == null) {
      return null;
    }
    return pCrawl.search(key.substring(1));
  }

  public Node[] getNodes() {
    return children;
  }

  public int getNonNull() {
    int i = 0;
    while (children[i] == null) {
      i++;
    }
    return i;
  }

  public boolean equals(Object obj){
    if (obj == null) return false;
    if (this == obj) return true;
    if (this.getClass() != obj.getClass()) return false;

    Node temp = (Node) obj;
    if (this.count != temp.count) return false;
    for (int i = 0; i < 26; i++) {
      if (this.children[i] != null && temp.children[i] != null)
        if (!this.children[i].equals(temp.children[i])) return false;
        else if (this.children[i] == null && temp.children[i] != null) return false;
        else if (this.children[i] != null && temp.children[i] == null) return false;
    }
    return true;
  }

  @Override
  public int getValue() {
    return count;
  }

  @Override
  public void incrementValue() {

  }

  @Override
  public INode[] getChildren() {
    return new INode[0];
  }
}