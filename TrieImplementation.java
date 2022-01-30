import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
/*
 *
 *  Implement Trie data structure.
	Implement Add, Delete and Search function.
    Add support to save the Trie to file and also load a Trie from file.

 * @author Manish Kumar
 */

class TrieNode{
	//Here the key is character and value is trie node.
	//key will store the character and value will store the
	//reference of the trie node.
	Map<Character, TrieNode> children;
	//It will indicate that the character representing this
	//trie node is a end of word or not.
	boolean endOfWord;
	
	public TrieNode() {
		//children can store multiple characters and references
		children = new HashMap<>();
		endOfWord = false;
	}
	
}

class Trie{
	private final TrieNode root;//root Trie Node
	public Trie() {
		root=new TrieNode();
	}
	
	//Insert word in trie
	
	private void insert(TrieNode current, String word, int charIndex) {
		
		//if end of word is reached then mark the endOfWord as true for current node
		if(charIndex==word.length()) {
			current.endOfWord=true;
			//stop
			return;
		}
		
		//Get the character of current index
		char ch=word.charAt(charIndex);
		//if the character is present at current node then
		//get the reference of the next trie node
		TrieNode node = current.children.get(ch);
		//if the character is not present in the current node 
		//then it will not contain any reference in that case
		//create new node and put it into map
		if(node==null) {
			//create new trie node
			node = new TrieNode();
			//put new node into map
			current.children.put(ch, node);
		}
		//if the character is present in current node then follow the path
		insert(node,word,++charIndex);
	}
	
	public void insert(String word) {
		insert(root,word,0);
	}
	
	
	//Search word in trie
	
	private boolean search(TrieNode current, String word, int charIndex) {
		
		//if we reached at the last index of the word then
		//return endOfWord value if it is true then whole word is found
		//if it is false then only prefix matched
		
		if(charIndex==word.length()) {
			return current.endOfWord;
		}
		
		//Put the current character into ch
		char ch=word.charAt(charIndex);
		//if the character is present at current node then
		//get the reference of the next trie node
		TrieNode node = current.children.get(ch);
		//if the character is not present in the current node 
		//then it will not contain any reference that means the 
		//word is not present so return false
		if(node==null) {
			return false;
		}
		
		//if the character is present in current node then follow the path
		return search(node, word, ++charIndex);
	}
	
	public boolean search(String word) {
		return search(root,word,0);
	}
	
	//Delete word in trie
	
	private boolean delete(TrieNode current, String word, int charIndex) {
		
		//when end of word is reached then delete only if endOfWord is true
		//if it is false then it will contains the references of the other characters i.e. mapping
		//so the delete operation cannot be done, if we do so we will not find other words
		if(charIndex==word.length()) {
			//if(endOfWord is false) then do not perform delete operation
			if(!current.endOfWord) {
				return false;
			}
			//if(endOfWord is true) and it contains other mapping 
			//then mark current endOfWord false due to which the current word
			//will not found if searched
			current.endOfWord=false;
			//if current has no other mapping then delete operation can be done
			//so return true
			return current.children.size()==0;
			
		}
		
		char ch = word.charAt(charIndex);
		//if the character is present at current node then
		//get the reference of the next trie node
		TrieNode node = current.children.get(ch);
		//if current has no other mapping then delete operation cannot be done
		//so return false
		if(node==null) {
			return false;
		}
		
		//check whether the delete operation can perform
		//if true then deletion occur else not
		
		boolean performDeletion = delete(node, word, ++charIndex);
		
		//if(performDeletion==true) then delete the mapping of character
		//and TrieNode references
		if(performDeletion) {
			//delete the children
			current.children.remove(ch);
			//return true if no mapping are left in the map
			return current.children.size()==0;
		}
		
		return false;
		
	}
	
	public void delete(String word) {
		delete(root,word,0);
	}
	
	//Display all words
	private void display(TrieNode current, String word, String prefix) {
		
		//if Trie node is null
		if(current==null) {
			//stop
			return;
		}
		//If one word is completed print it
		if(current.endOfWord) {
			System.out.println(word);
		}
		
		Set<Character> chHash = current.children.keySet();
		
		for(char ch: chHash) {
				prefix=word;
				word+=ch;//concatenation
				display(current.children.get(ch), word, prefix);
				word=prefix;
		}
	}
	
	public void display() {
		display(root,"","");
	}
	
	
	//Save to file
	
	
	private void saveToFile(TrieNode current, String word,String prefix, FileWriter myWriter) throws IOException {
		
		//if Trie node is null then stop
		if(current==null) {
			//stop
			return;
		}
		//If one word is completed then write to file
		if(current.endOfWord) {
			String sWord=word;
			sWord+="\n";
			myWriter.write(sWord);
		}
		
		Set<Character> chHash = current.children.keySet();
		
		for(char ch: chHash) {
				prefix=word;
				word+=ch;//concatenation
				saveToFile(current.children.get(ch), word,prefix, myWriter);
				word=prefix;
		}
	}
	
	
	public void saveTrie(String filename) throws IOException {
		String fname = filename+".txt";
		File myFile = new File(fname);
		if(myFile.createNewFile()) {
			FileWriter writeToFile = new FileWriter(fname, true);
			saveToFile(root,"","",writeToFile);
			writeToFile.close();
			System.out.println("Data has been written to file.");
		}
		else {
			System.out.println("File already exists, try another name...");
		}
		
	}
	
	
	//Load from file
	
	public void loadFromFile(String fileName) throws FileNotFoundException {
		 
			  String fName=fileName+".txt";	
		      File myObj = new File(fName);
		      Scanner myReader = new Scanner(myObj);
		      while (myReader.hasNextLine()) {
		        String word = myReader.nextLine();
		        insert(word);
		      }
		      myReader.close();
		    
	}
	
}


public class TrieImplementation {

	public static void main(String[] args) {
		
		Trie tr = new Trie();
		boolean exit=false;
		Scanner sc = new Scanner(System.in);
		
		while(!exit) { 
			System.out.println("-------------Trie Menu-----------------");
			System.out.println(" 1. Insert");
			System.out.println(" 2. Search");
			System.out.println(" 3. Delete");
			System.out.println(" 4. Display");
			System.out.println(" 5. Save the Trie to file");
			System.out.println(" 6. Load the Trie from file");
			System.out.println(" 7. Exit");
			System.out.println("---------------------------------------");
			System.out.println();
			System.out.println("Enter your choice: ");
			switch(sc.nextInt()) {
			case 1:
				System.out.println("Enter the word to insert: ");
				String word = sc.next();
				tr.insert(word);
				System.out.println(word+ " is inserted into trie.");
				break;
			case 2:
				System.out.println("Enter the word to search: ");
				String words = sc.next();
				if(tr.search(words)) {
					System.out.println(words+" is found in the Trie.");
				}
				else {
					System.out.println(words+" is not found in the Trie.");
				}
				break;
			case 3:
				System.out.println("Enter the word to delete: ");
				String w = sc.next();
				if(tr.search(w)) {
					//if found perform deletion 
					tr.delete(w);
					System.out.println(w+" is deleted from the Trie.");
				}
				else {
					System.out.println(w+" is not found in the Trie.");
				}
				break;
			case 4:
					tr.display();
				break;
			case 5:
				
				try {
					System.out.println("Enter the filename(e.g. file): ");
					tr.saveTrie(sc.next());
				} catch (IOException e) {
					System.out.println(e);
				}
				break;
			case 6:
				try {
					System.out.println("Enter the filename(e.g. file): ");
					tr.loadFromFile(sc.next());
					System.out.println("File has loaded...");
				} catch (FileNotFoundException e) {
					System.out.println(e);
				}
				break;
			case 7:
				exit=true;
				break;
			default:
				System.out.println("Please, enter correct choice...");
				break;
					
			}
		}
		sc.close();

	}

}
