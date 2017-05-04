
class TrieNode {
    TrieNode[] arr;
    boolean isEnd;
    // Initialize your data structure here.
    public TrieNode() {
        this.arr = new TrieNode[2];
    }
 
}
 
public class Trie {
    private TrieNode root;
 
    public Trie() {
        root = new TrieNode();
    }
 
    // Inserts a word into the trie.
    public void insert(String word) {
        TrieNode p = root;
        for(int i=0; i<word.length(); i++){
            char c = word.charAt(i);
            int index = c-'0';
            if(p.arr[index]==null){
                TrieNode temp = new TrieNode();
                p.arr[index]=temp;
                p = temp;
            }else{
                p=p.arr[index];
            }
        }
        p.isEnd=true;
    }
 
   
    
    public int traverse(String s){
    	TrieNode p=root;
    	int i=0;
    	for(; i<s.length(); i++){
            char c= s.charAt(i);
            int index = Integer.parseInt(c+"");
            if(p.arr[index]!=null){
            	if(p.arr[index].isEnd){
            		return i;
            	}
                p = p.arr[index];
            }
        }
    	return -1;
    }
}