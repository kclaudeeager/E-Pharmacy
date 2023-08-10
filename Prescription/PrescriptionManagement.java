import java.io.*;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.UUID;

import org.json.simple.parser.ParseException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import java.awt.Desktop;

import org.json.simple.parser.JSONParser;

public class PrescriptionManagement {

   public static void main(String[] args) throws NumberFormatException {

       try{
           BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
           int choice = -1, numMedications=1;
           String customerId = generateId();

           while(true) {

               // TODO: Add code to display the menu and get the number(choice) a user slected
               System.out.println("choose what you need to do from the following choices:");
               System.out.println(" 1: Add prescription\n 2: View prescriptions and\n 3: Delete prescription \n 4. Search Prescription \n 5. Exit application");
               System.out.print("Enter your choice: ");
               choice = Integer.parseInt(reader.readLine());
               switch (choice) {
                   case 1 -> {
                       // TODO: Add code to get Prescription ID, Customer ID,  Doctor's Name
                       // Don't forget to add code to save these information in the prescription
                       Prescription prescription = new Prescription();
                       numMedications = getIntPrompt("Enter the number of medications to add:", reader);
                       ArrayList<Medication> medications = new ArrayList<>();
                       String dosage = null, medicationID = null;
                       int quantity = 0;
                       String medicationName, medicationDetails;
                       // TODO: Add code to display available products/medications before adding them on the prescription
                       for (int i = 1; i <= numMedications; i++) {

                           System.out.println("Enter details for Medication " + i + ":");

                           // TODO: Add code to get Medication ID, Name, Details, Dosage and Quantity
                           medicationName = getStringPrompt("Enter medication name:", reader);
                           medicationDetails = getStringPrompt("Enter medication details:", reader);
                           dosage = getStringPrompt("Enter medecine dosage:", reader);
                           medicationID = generateId();
                           quantity = getIntPrompt("Enter quatity", reader);
                           Medication medication = new Medication(medicationID, medicationName, dosage, quantity, medicationDetails);
                           medication.setProcessedStatus(false);
                           medications.add(medication);
                       }
                       String doctorName = getStringPrompt("Enter doctor name", reader);
                       String fileLocation = getStringPrompt("Enter prescription file location", reader);
                       prescription.setDoctorName(doctorName);
                       prescription.setPrescriptionFileLocation(fileLocation);
                       prescription.setCustomerID(customerId);
                       // TODO: Add code to save all medications inserted by the user on the prescription
                       prescription.setDate(LocalDate.now());
                       prescription.setPrescriptionID(generateId());
                       prescription.setMedications(medications);
                       prescription.addPrescription();
                   }
                   case 2 -> {
                       // TODO: Add code to retrieve all prescriptions in the file
                       // Prescriptions must be returned in the array
                       ArrayList<Prescription> prescriptions = Prescription.viewPrescription();
                       if (prescriptions.size() == 0) {
                           System.out.println("No precriptions available\n");
                       } else {
                           System.out.println("| PrescriptionID |  DoctorName   |    CustomerID | \tDate\t | ");
                           System.out.println("******************************************************************");

                           for (Prescription p : prescriptions) {
                               System.out.println("|\t  " + p.getPrescriptionID() + "\t\t" + p.getDoctorName() + "\t\t  " + p.getCustomerID() + "\t\t" + p.getDate());
                               System.out.println("");
                               System.out.println("| MedicationID |  \tName    | \t Quantity | ");
                               for (Medication med : p.getMedications()) {
                                   System.out.println("|\t  " + med.getID() + "\t\t" + med.getName() + "\t\t " + med.getQuantity());
                               }

                               System.out.print("\n");
                               System.out.println("*****************************************************************");

                               System.out.println("Do you want to open prescription file? ");

                               int userChoice = getIntPrompt("Enter 1 for yes, 2 for no", reader);

                               switch (userChoice) {
                                   case 1 -> {
                                       File file = new File(p.getPrescriptionFileLocation());
                                       if (!Desktop.isDesktopSupported()) {
                                           System.err.println("not supported");
                                       } else {
                                           System.out.println("Opening file...");
                                           try {
                                               if (file.exists()) {
                                                   Desktop.getDesktop().open(file);
                                               } else {
                                                   URI uri = new URI(p.getPrescriptionFileLocation());
                                                   Desktop.getDesktop().browse(uri);
                                               }
                                               System.out.println("File opened successfully!");
                                           } catch (Exception e) {
                                               e.printStackTrace();
                                           }
                                       }
                                   }
                                   case 2 -> System.out.println("Opening file is canceled");
                                   default -> System.err.println("Choice not found");
                               }

                           }

                           System.out.println("");
                       }
                   }
                   case 3 ->{
                       // TODO: Add code to get the ID of the prescription you want to delete
                       System.out.println("You can search by: \n 1. Doctor's name \n 2. Medication name");
                       System.out.println("----------------------------");
                       int userChoice = getIntPrompt("Enter your choice 1 or 2: ",reader);
                       ArrayList<Prescription> resultList = Prescription.searchPrescription(userChoice);
                       if (resultList.size() == 0) {
                           System.out.println("Not match found");
                           System.out.println("==============================");
                       }

//          
                       if(resultList.size()==1){

                           resultList.get(0).deletePrescription();
                       }
                   }

                   case 4 -> {
                       //		get all list from the file
                       int userInput = 0;
                       System.out.println("=========================================");
                       System.out.println("Select the field your want to search with:");
                       System.out.println("=========================================");
                       while (userInput < 1 || userInput > 2) {
                           System.out.println("You can search by: \n 1. Doctor's name \n 2. Medication name");
                           System.out.println("----------------------------");
                           userInput = getIntPrompt("Enter your choice 1 or 2: ",reader);
                           System.out.println("----------------------------");
                           // search for product
                       }
                       ArrayList<Prescription> resultList = Prescription.searchPrescription(userInput);
                       if (resultList.size() == 0) {
                           System.out.println("Not match found");
                           System.out.println("==============================");
                       }
                   }

                   case 5 ->{
                       System.out.println("Exiting the Precription Management section...");
                       System.exit(0);
                   }

                   default -> System.out.println("Invalid choice. Please try again.");
               }
           }
       } catch (Exception e) {
           throw new NumberFormatException(""+e.getMessage());
       }
   }

    private static String generateId(int size){
        byte[] array = new byte[size];
        new Random().nextBytes(array);
        return new String(array, StandardCharsets.UTF_8);
    }
    public static String generateId(){
       String uuid = UUID.randomUUID().toString();
       return uuid.substring(0,8);
    }
    private static String getStringPrompt(String prompt,BufferedReader reader) throws IOException {
        System.out.println(prompt);
        return reader.readLine();
    }
    private static Integer getIntPrompt(String prompt, BufferedReader reader) throws IOException {
        System.out.println(prompt);
        return Integer.valueOf(reader.readLine());
    }

   
   public static void displayMedications(String filePath) throws FileNotFoundException, IOException, ParseException {
	   
                  FileHandler fileHandler = new FileHandler(filePath);
	              JSONArray jsonArray = fileHandler.readJSONArrayFromFile();
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
