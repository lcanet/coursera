

/**
 * Dictionary used by the boggle solver. Based on Ternary Search Trie, but with a new optional
 * operation to detect if a prefix is present in the trie (without necessary being associated
 * to a value)
 * 
 * @author lc
 *
 */
public class BoggleDictionary {
  private int N;       // size
  private Node root;   // root of TST

  private class Node {
      private char c;                 // character
      private Node left, mid, right;  // left, middle, and right subtries
      private String val;              // value associated with string
  }

  // return number of key-value pairs
  public int size() {
      return N;
  }

 /**************************************************************
  * Is string key in the symbol table?
  **************************************************************/
  public boolean contains(String key) {
      return get(key) != null;
  }

  public String get(String key) {
      if (key == null) throw new NullPointerException();
      if (key.length() == 0) throw new IllegalArgumentException("key must have length >= 1");
      Node x = get(root, key, 0);
      if (x == null) return null;
      return x.val;
  }

  // return subtrie corresponding to given key
  private Node get(Node x, String key, int d) {
      if (key == null) throw new NullPointerException();
      if (key.length() == 0) throw new IllegalArgumentException("key must have length >= 1");
      if (x == null) return null;
      char c = key.charAt(d);
      if      (c < x.c)              return get(x.left,  key, d);
      else if (c > x.c)              return get(x.right, key, d);
      else if (d < key.length() - 1) return get(x.mid,   key, d+1);
      else                           return x;
  }


 /**************************************************************
  * Insert string s into the symbol table.
  **************************************************************/
  public void add(String s) {
      if (!contains(s)) N++;
      root = put(root, s, s, 0);
  }

  private Node put(Node x, String s, String val, int d) {
      char c = s.charAt(d);
      if (x == null) {
          x = new Node();
          x.c = c;
      }
      if      (c < x.c)             x.left  = put(x.left,  s, val, d);
      else if (c > x.c)             x.right = put(x.right, s, val, d);
      else if (d < s.length() - 1)  x.mid   = put(x.mid,   s, val, d+1);
      else                          x.val   = val;
      return x;
  }


  public boolean hasPrefix(String s) {
  	Node n = get(root, s, 0);
  	return n != null;
  }


}
