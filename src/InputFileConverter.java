import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class InputFileConverter {
	
	String[] all_files;
	
	public InputFileConverter(String input_file_path) {

		String file_contents = "";

	    try {
	    	File file = new File(input_file_path);
	        Scanner sc = new Scanner(file);

	        while (sc.hasNextLine()) {
	        	file_contents += sc.nextLine();
	        	file_contents += "&";
	        }
	        sc.close();
	        
	    } 
	    catch (FileNotFoundException e) {
	        e.printStackTrace();
	    }

		all_files = file_contents.split("&");
		
	}
	
	public String[] get_all_files() {
		return all_files;
	}

}
