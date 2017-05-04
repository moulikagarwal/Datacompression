import java.io.*;
import java.util.*;
class encoder{
	
	private static String encode_file_name = "encoded.bin";
	private static String code_table_file_name = "code_table.txt";
	
	public static class Node implements Comparable{
	     private final int digit;
	     private final int frequency;
	     private final Node left;
	     private final Node right;
	     
	     Node(int digit, int frequency, Node left, Node right){
	         this.digit = digit;
	         this.frequency = frequency;
	         this.left = left;
	         this.right = right;
	     }
	     
	     public boolean isLeaf(){
	         return (left == null  &&  right == null);
	     }
	     @Override
	     public int compareTo(Object o) {
             return this.frequency - ((Node) o).frequency;
	     }
	 }
	
	private static Node buildHuffmanTree( HashMap<Integer, Integer> freqs ) {
		BinaryHeap<Node> prioQ=new BinaryHeap<Node>();
		//PairingHeap<Node> prioQ=new PairingHeap<Node>();
		//CacheHeap<Node> prioQ= new CacheHeap<Node>(1000000);
        for (int c: freqs.keySet()) 
                prioQ.insert( new Node(c,freqs.get(c),null,null));
        
        while (prioQ.size() > 1) {
            Node left = prioQ.removeMin();
            Node right = prioQ.removeMin();
            prioQ.insert( new Node( '#', left.frequency + right.frequency, left, right ) );
        }
        
        return prioQ.removeMin();
	}
	private static void getHuffmanCode(Node node, String code, HashMap<Integer, String> encoding) {
        if (node.isLeaf()) 
        	encoding.put(node.digit,code);
        	
        else {
            if (node.left != null) 
            	getHuffmanCode(node.left,code+"0",encoding );
            if (node.right != null)
            	getHuffmanCode(node.right,code+"1",encoding );
        }
	}
	
	public static HashMap<Integer, Integer> parseFile(String fileName) throws IOException{
		BufferedReader input=null;
        FileReader fr = new FileReader(fileName);
        HashMap<Integer, Integer> freqMap = new HashMap<Integer, Integer>();
        input = new BufferedReader(fr);
        String line = "";
        while (( line = input.readLine()) != null && !line.trim().equals("")){
            int c = Integer.parseInt(line);
            freqMap.put(c,freqMap.getOrDefault(c,0)+1);
        }
        input.close();
	    return freqMap;
	}
	
	private static void generateCodeTable(HashMap<Integer, String> encoding) throws IOException {
		PrintWriter writer = null;
		writer = new PrintWriter(code_table_file_name, "UTF-8");
		StringBuilder codeBuilder=new StringBuilder();
		for(Map.Entry<Integer,String> entry:encoding.entrySet()){
			codeBuilder.append(entry.getKey()+" "+entry.getValue()+"\n");
        }
		writer.println(codeBuilder.toString().trim());
		writer.close();
		
	}
	
	private static void generateEncodedBinFile(HashMap<Integer, String> encoding,String fileName) throws IOException {
		
		BufferedOutputStream bufferedOutput = new BufferedOutputStream(new FileOutputStream(encode_file_name));
		ByteArrayOutputStream bOutput = new ByteArrayOutputStream();
		StringBuilder strBuilder=new StringBuilder();
		String byteString="";
		Integer parsedByte=0;
		BufferedReader input=null;
        FileReader fr = new FileReader(fileName);
        input = new BufferedReader(fr);
        String line = "";
        while (( line = input.readLine()) != null && !line.trim().equals("")){
        	int val = Integer.parseInt(line);
            strBuilder.append(encoding.get(val));
            while(strBuilder.length()>=8){
            	byteString = strBuilder.toString().substring(0,8); 
    	        parsedByte =Integer.parseInt(byteString, 2);
    	        byte b=parsedByte.byteValue();
    	        bOutput.write(b);
    	        strBuilder.delete(0,8);
            }
        }
        input.close();
		byte b_arr [] = bOutput.toByteArray();
		bufferedOutput.write(b_arr);
		bufferedOutput.flush();
		bufferedOutput.close();
	}
	
	public static void main(String[] args) throws IOException {
		
		// Run some code;
		if(args.length < 1){
			System.out.println("Please enter the file as a command line argument");
		}
		
		String fileName = args[0];
		HashMap<Integer,Integer> freqs = parseFile(fileName);
		
		Node root =buildHuffmanTree( freqs );
		HashMap<Integer, String> encoding = new HashMap<Integer, String>();
		getHuffmanCode( root, "", encoding);
		generateEncodedBinFile(encoding,fileName);
		generateCodeTable(encoding);
	}
}
