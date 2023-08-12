import java.awt.*;
import java.awt.desktop.SystemSleepEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.UUID;

public class PrescriptionManagement {


   public static void main(String[] args) throws NumberFormatException, IOException {

       try{
           BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
           int choice, numMedications;
           String customerId = generateId();
           ArrayList<Medication> availableMedications = Medication.getAvailableMedications("products.json");
//           displayMedications(availableMedications);
           while(true) {
               // TODO: Add code to display the menu and get the number(choice) a user slected
               System.out.println("=======================================================");
               System.out.println(" \uD83D\uDC68\u200D⚕\uFE0F Welcome to the Prescription management System \uD83D\uDC68\u200D⚕\uFE0F");
               System.out.println("-------------------------------------------------------");
               System.out.println(" Choose what you need to do from the following choices:");
               System.out.println("=======================================================");
               System.out.println(" \uD83E\uDEF1 1: Add prescription\n \uD83E\uDEF1 2: View prescriptions and\n \uD83E\uDEF1 3: Delete prescription \n \uD83E\uDEF1 4. Search Prescription  \n \uD83E\uDEF1 5. Exit application");
               System.out.println("-------------------------------------------------------");
               System.out.print(" \uD83E\uDEF1 Enter your choice: ");

               choice = Integer.parseInt(reader.readLine());
               switch (choice) {
                   case 1 -> {
                       // TODO: Add code to get Prescription ID, Customer ID,  Doctor's Name
                       // Don't forget to add code to save these information in the prescription
                       Prescription prescription = new Prescription();
                       System.out.println("-------------------------------------------------------");
                       numMedications = getIntPrompt("How many medication or drugs \uD83D\uDC8A, would you like to add to your prescription:", reader);
                       System.out.println("-------------------------------------------------------");

                       if (availableMedications.size() == 0){
                           System.out.println("** We don't have any medication in the store at the moment. **");
                           break;
                       }

                       ArrayList<Medication> medications = new ArrayList<>();
                       String medicationName;
                       // TODO: Add code to display available products/medications before adding them on the prescription
                       for (int i = 1; i <= numMedications; i++) {
                           System.out.println("Enter the details for Medication \uD83D\uDC8A " + i + ":");
                           // TODO: Add code to get Medication ID, Name, Details, Dosage and Quantity
                           System.out.println("How would you like the medication store.\n\uD83E\uDEF1 1 by name\n\uD83E\uDEF1 2 by brand\n\uD83E\uDEF1 3 by category");
                           System.out.println("-------------------------------------------------------");
                           int searchMethod = getIntPrompt("Enter your search choice: ",reader);
                           String searchBy = Medication.getSearchChoice(searchMethod);
                           medicationName = getStringPrompt("Enter medication "+ searchBy+" :", reader);
                           ArrayList<Medication> matchingMedications = Medication.searchMedications(availableMedications,searchBy,medicationName);

                           if(matchingMedications.size() == 0){
                               System.out.println("No matching result for '" + medicationName + "'");
                           }else{

                               String[] header = {"SN", "Name", "Category", "dosage", "Processed", "Quantity", "Price"};
                               StringBuilder concatenatedOutput = new StringBuilder();
                               System.out.println("------------------------------------------------------------------------");
                               for(String h: header ){
                                   concatenatedOutput.append("|").append(Prescription.cutAndPadLeftAlign(h));
                               }
                               System.out.println(concatenatedOutput);
                               System.out.println("------------------------------------------------------------------------");
                           }
                           int count = 1;

                           for (Medication m : matchingMedications){
                               System.out.println("|" + Prescription.cutAndPadLeftAlign(String.valueOf(count++)) +
                                       "|" + Prescription.cutAndPadLeftAlign(m.getName()) +
                                               "|" + Prescription.cutAndPadLeftAlign(m.getCategory()) +
                                               "|" + Prescription.cutAndPadLeftAlign(m.getDosage()) +
                                               "|" + Prescription.cutAndPadLeftAlign(m.getProcessedStatus() ? "yes" : "no") +
                                               "|" + Prescription.cutAndPadLeftAlign(String.valueOf(m.getQuantity())) +
                                               "|" + Prescription.cutAndPadLeftAlign(String.valueOf(m.getPrice()))
                               );
                           }
//                           for (int j=0; j<matchingMedications.size();j++){
//                               System.out.println("Enter "+(j+1)+" for "+matchingMedications.get(j).toJson());
//                            }
                           int chosenMedication = getIntPrompt("Enter the serial of the medication (shown in the 'SN' column above): ",reader);
                           if(chosenMedication <= 0 || chosenMedication>matchingMedications.size()){
                               System.err.println("Invalid choice");
                           } else {
                               Medication medication = matchingMedications.get(chosenMedication-1);
                               int quantity = getIntPrompt("Enter the quantity:",reader);
                               medication.setQuantity(quantity);
                               medication.setProcessedStatus(false);
                               medications.add(medication);
                           }
//
                       }

                       System.out.println("------------------------------------------------------------------------");
                       String doctorName = getStringPrompt("Enter doctor name \uD83D\uDC68\u200D⚕\uFE0F: ", reader);
                       String fileLocation = getStringPrompt("Enter prescription file location \uD83D\uDCC2: ", reader);
                       prescription.setDoctorName(doctorName);
                       prescription.setPrescriptionFileLocation(fileLocation);
                       prescription.setCustomerID(customerId);
                       // TODO: Add code to save all medications inserted by the user on the prescription
                       prescription.setDate(LocalDateTime.now());
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
                        int index = 1;
                       for (Prescription p : resultList){
                           System.out.println("ID: " + index++);
                           System.out.println("Doctor's name: " + p.getDoctorName());
                           System.out.println("Prescription of Dr." + p.getDoctorName() + " are shown below.");
                           System.out.println("--------------------------------------------");
                           if (p.getMedications().size() == 0){
                               System.out.println("No medication has been given for this prescription.");
                           }else{
                               for (Medication m : p.getMedications()){
                                   System.out.println("|" + Prescription.cutAndPadLeftAlign("Name") +
                                           "|" + Prescription.cutAndPadLeftAlign("Dosage") +
                                           "|" + Prescription.cutAndPadLeftAlign("Quantity") +
                                           "|" + Prescription.cutAndPadLeftAlign("Processed")
                                   );
                                   System.out.println("---------------------------------------------");
                                   System.out.println(
                                           "|" + Prescription.cutAndPadLeftAlign(m.getName()) +
                                                   "|" + Prescription.cutAndPadLeftAlign(m.getDosage()) +
                                                   "|" + Prescription.cutAndPadLeftAlign(String.valueOf(m.getQuantity())) +
                                                   "|" + Prescription.cutAndPadLeftAlign(m.getProcessedStatus() ? "yes" : "no")
                                   );
                                   System.out.println("==============================================");
                               }
                               int option = 0;
                               Scanner input = new Scanner(System.in);
                               System.out.println("Enter doctor's ID: ");
                               option = input.nextInt();
                               while (option <= 0 || option > resultList.size()){
                                   System.out.println("Enter doctor's ID: ");
                                   option = input.nextInt();
                               }

                               resultList.get(option - 1).deletePrescription();
                               System.out.println("Prescription deleted successfully");
                           }
                       }


//                       for (Prescription p : resultList){
//                           System.out.println(p.getDoctorName() + " doctor's name");
//                       }
//                        if (resultList.size() == 1) {
//                            resultList.get(0).deletePrescription();
//                        }
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
                            userInput = getIntPrompt("Enter your choice 1 or 2: ", reader);
                            System.out.println("----------------------------");
                            // search for product
                        }
                        ArrayList<Prescription> resultList = Prescription.searchPrescription(userInput);
                        if (resultList.size() == 0) {
                            System.out.println("Not match found");
                            System.out.println("==============================");
                        }else {
                            for (Prescription p : resultList){
                                System.out.println("Doctor's name: " + p.getDoctorName());
                                System.out.println("Prescription of Dr." + p.getDoctorName() + " are shown below.");
                                System.out.println("-----------------------------------------------");
                                if (p.getMedications().size() == 0){
                                    System.out.println("No medication has been given for this prescription.");
                                }else{
                                    for (Medication m : p.getMedications()){
                                    System.out.println("|" + Prescription.cutAndPadLeftAlign("Name") +
                                            "|" + Prescription.cutAndPadLeftAlign("Dosage") +
                                            "|" + Prescription.cutAndPadLeftAlign("Quantity") +
                                            "|" + Prescription.cutAndPadLeftAlign("Processed")
                                    );
                                    System.out.println("---------------------------------------------");
                                    System.out.println(
                                            "|" + Prescription.cutAndPadLeftAlign(m.getName()) +
                                            "|" + Prescription.cutAndPadLeftAlign(m.getDosage()) +
                                            "|" + Prescription.cutAndPadLeftAlign(String.valueOf(m.getQuantity())) +
                                            "|" + Prescription.cutAndPadLeftAlign(m.getProcessedStatus() ? "yes" : "no")
                                    );
                                    System.out.println("==============================================");
                                }
                                }
                            }
                        }
                    }

                    case 5 -> {
                        System.out.println("Exiting the Precription Management section...");
                        System.exit(0);
                    }

                    default -> System.out.println("Invalid choice. Please try again.");
                }
            }
        } catch (Exception e) {
            System.err.println("Exception: " + e.getMessage());
        }

    }

    private static String generateId(int size) {
        byte[] array = new byte[size];
        new Random().nextBytes(array);
        return new String(array, StandardCharsets.UTF_8);
    }

    public static String generateId() {
        String uuid = UUID.randomUUID().toString();
        return uuid.substring(0, 8);
    }

    private static String getStringPrompt(String prompt, BufferedReader reader) throws IOException {
        System.out.println(prompt);
        return reader.readLine();
    }

    private static Integer getIntPrompt(String prompt, BufferedReader reader) throws IOException {
        System.out.println(prompt);
        return Integer.valueOf(reader.readLine());
    }


    public static void displayMedications(ArrayList<Medication> availableMedications) {

        System.out.println("---------------------------------------------------------------------------------------");
        System.out.println("|\t" + "\t\t  " + "\t\t\t\t");
        System.out.println("|\t" + "\t\t" + "Available Medications" + "\t\t");
        System.out.println("|\t" + "\t\t  " + "\t\t\t\t");
        System.out.println("---------------------------------------------------------------------------------------");
        //System.out.println("| Medication ID |  Medication Name   |    Medication Price ||    Medication Quantity |   | Details    |");
        System.out.println("---------------------------------------------------------------------------------------");
        availableMedications.forEach(medication -> System.out.println(medication.Description()));
        System.out.println("---------------------------------------------------------------------------------------");
    }


}
