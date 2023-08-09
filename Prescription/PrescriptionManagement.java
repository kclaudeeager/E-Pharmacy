import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Random;

import org.json.simple.parser.ParseException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import org.json.simple.parser.JSONParser;

public class PrescriptionManagement {

  

   public static void main(String[] args) throws IOException, ParseException {
	   
	   
	   BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
       int choice = -1, numMedications;
       Prescription prescription = new Prescription();

        	 
         while(true) {

            // TODO: Add code to display the menu and get the number(choice) a user slected
             System.out.println("choose what you need to do from the following choices:");
             System.out.println(" 1: Add prescription\n 2: View prescriptions and\n 3: Process prescription");
             System.out.print("Enter your choice: ");
             choice = reader.read();
            switch (choice) {
               case 1:
            	   
                    // TODO: Add code to get Prescription ID, Customer ID,  Doctor's Name
                    // Don't forget to add code to save these information in the prescription

                   numMedications = getIntPrompt("Enter the number of medications to add:",reader);

                   ArrayList<Medication> medications = new ArrayList<>();
                   String  dosage = null, medicationID = null;
                   int quantity = 0;
                   String medicationName,medicationDetails;
                   // TODO: Add code to display available products/medications before adding them on the prescription
                   String medicationsFilePath = "products.json";

                   
                   for (int i = 1; i <= numMedications; i++) {
                	   
                       System.out.println("Enter details for Medication " + i + ":");
                       
                        // TODO: Add code to get Medication ID, Name, Details, Dosage and Quantity
                       medicationName = getStringPrompt("Enter medication name:",reader);
                       medicationDetails = getStringPrompt("Enter medication details:",reader);
                       dosage = getStringPrompt("Enter medecine dosage:",reader);
                       Medication medication = new Medication();
                       medications.add(medication);
                    }
                   
                    // TODO: Add code to save all medications inserted by the user on the prescription


                   prescription.setDate(LocalDate.now());
                   prescription.addPrescription(prescription);
                   
                   break;
               case 2:
                    // TODO: Add code to retrieve all prescriptions in the file
                    // Prescriptions must be returned in the array
                   ArrayList<Prescription> prescriptions = new ArrayList<>();
                   if(prescriptions.size()==0) {
                       System.out.println("No precriptions available\n");
                   }
                   else {
                       System.out.println("| PrescriptionID |  DoctorName   |    CustomerID | \tDate\t | ");
                       System.out.println("******************************************************************");
                       
                       for(Prescription p: prescriptions) 
                       {
                           System.out.println("|\t  "+ p.getPrescriptionID()+"\t\t"+ p.getDoctorName()+ "\t\t  " + p.getCustomerID()+"\t\t" + p.getDate());
                           
                           System.out.println("");
                           System.out.println("| MedicationID |  \tName    | \t Quantity | ");
                           for(Medication med : p.getMedications()) 
                           {
                               System.out.println("|\t  "+ med.getID()+"\t\t"+ med.getName()+ "\t\t " + med.getQuantity() );
                           }
                       
                           System.out.print("\n");
                           System.out.println("*****************************************************************");    
                       }
                       
                       System.out.println("");
                   }
                   
            	   break;
               case 3:
                    // TODO: Add code to get the ID of the prescription you want to delete
                   prescription.deletePrescription();
                  break;
               case 4:
                   System.out.println("Exiting the Precription Management section...");
                   System.exit(0);
               default:
                  System.out.println("Invalid choice. Please try again.");
            }
            
            
         }
         
         

   }

    private static String generateId(int size){
        byte[] array = new byte[size]; // length is bounded by 7
        new Random().nextBytes(array);
        return new String(array, StandardCharsets.UTF_8);
    }
    private static String getStringPrompt(String prompt,BufferedReader reader) throws IOException {
        System.out.println(prompt);
        return reader.readLine();
    }
    private static Integer getIntPrompt(String prompt, BufferedReader reader) throws IOException {
        System.out.println(prompt);
        return reader.read();
    }

   
   public static void displayMedications(String filePath) throws FileNotFoundException, IOException, ParseException {
	   
	      JSONParser parser = new JSONParser();
	      try(FileReader fileReader = new FileReader(filePath)){
	          if (fileReader.read() == -1) {

              }
	          else {
	              fileReader.close();
	              JSONArray jsonArray = (JSONArray) parser.parse(new FileReader(filePath));
	              
                  System.out.println("---------------------------------------------------------------------------------------");
                  System.out.println("|\t"  + "\t\t  "  + "\t\t\t\t");
                  System.out.println("|\t" + "\t\t"  +  "Available Medications" + "\t\t");
                  System.out.println("|\t"  + "\t\t  "  + "\t\t\t\t");
                  System.out.println("---------------------------------------------------------------------------------------");
                  System.out.println("| Medication ID |  Medication Name   |    Medication Price ||    Medication Quantity |");
                  System.out.println("---------------------------------------------------------------------------------------");
                  
	              for (Object obj: jsonArray) {
	                  JSONObject jsonObject = (JSONObject) obj;
	                  
                    // TODO: Add code to get medication ID (it's named as code from medications/products file), name, price and quantity
                    // medication ID, name, price and quantity should be casted to String

                      
                      //System.out.println("|\t" + medicationID + "\t\t" + medicationName + "\t\t  " + medicationPrice + "\t\t\t  " + medicationQuantity + "\t\t");
	                  
	              }
                  System.out.println("---------------------------------------------------------------------------------------");

	              
	          }  
	      }
	   
   } 

}
