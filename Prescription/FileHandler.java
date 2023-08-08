import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class FileHandler {
	
	private String filePath = "prescriptions.json";
	

    // TODO:  Add missing code to be able to handle file

    public JSONArray readJSONArrayFromFile() throws IOException, ParseException {
        JSONParser parser = new JSONParser();
        try (FileReader fileReader = new FileReader(filePath)) {
            if (fileReader.read() == -1) {
                return new JSONArray(); 
            } else {
                fileReader.close();
                return (JSONArray) parser.parse(new FileReader(filePath));
            }
        } catch (IOException e) {
            return new JSONArray();
        }
    }

    // TODO:  Add missing code to be able to handle file

    public void writeJSONArrayToFile(JSONArray jsonArray) throws IOException {
        try (FileWriter fileWriter = new FileWriter(filePath)) {
            fileWriter.write(jsonArray.toJSONString());
            fileWriter.flush();
            System.out.println("Data written to " + filePath + " successfully!");
        } catch (IOException e) {
            System.out.println("Error while writing data to file: " + e.getMessage());
        }
    }
}