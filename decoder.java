

import java.io.*;
import java.util.*;

class decoder{
	
	private static StringBuilder encoded_msg=new StringBuilder();
	private static HashMap<String,Integer> code_map;
	private static String encode_file_name = "";
	private static String code_table_file_name = "";
	private static String decoded_file_name = "decoded.txt";
	private static Trie trie;

	private static void createHuffmanTree() throws IOException {
		FileReader fr = new FileReader(code_table_file_name);
		BufferedReader input=new BufferedReader(fr);
        String line = "";
        code_map=new HashMap<String,Integer>();
		trie=new Trie();
        while (( line = input.readLine()) != null && !line.trim().equals("")){
    		int digit=Integer.parseInt(line.split(" ")[0]);
    		String bits=line.split(" ")[1];
    		code_map.put(bits, digit);
    		trie.insert(bits);
        }
        input.close();
	}

	private static void runDecoder() throws IOException {
        InputStream inStream = new FileInputStream(encode_file_name);
        BufferedInputStream bis = new BufferedInputStream(inStream);
        PrintWriter writer = new PrintWriter(decoded_file_name, "UTF-8");;
        StringBuilder build_result=new StringBuilder();
        
        int numByte = bis.available();
        byte[] buf = new byte[numByte];
        bis.read(buf);
        for (byte b : buf) {
    	   encoded_msg.append(String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0'));
    	   int lastValidIndex=trie.traverse(encoded_msg.toString());
    	   while(lastValidIndex !=-1 && encoded_msg.length()>0){
    		   String subStr=encoded_msg.substring(0, lastValidIndex+1);
    		   build_result.append(code_map.get(subStr)+"\n");
    		   encoded_msg=new StringBuilder(encoded_msg.substring(lastValidIndex+1));
    		   lastValidIndex=trie.traverse(encoded_msg.toString());
    	   }
    	 }
        writer.print(build_result.toString().trim());
        writer.close();
        inStream.close();
        bis.close();
		
	}
	
	public static void main(String[] args) throws IOException {
		if(args.length < 2){
			System.out.println("Please enter the file as a command line argument");
		}
		encode_file_name=args[0];
		code_table_file_name=args[1];
		
		createHuffmanTree();
		runDecoder();
	}
}
