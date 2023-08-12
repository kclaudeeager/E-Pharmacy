import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.ArrayList;

public class Medication {

   private String ID;
   private String name;
   private String details;
   private String dosage;
   private int quantity;

   private String category;
   private double price;
   private String brand;

   public String getCategory() {
      return category;
   }

   public void setCategory(String category) {
      this.category = category;
   }

   public double getPrice() {
      return price;
   }

   public void setPrice(double price) {
      this.price = price;
   }

   public Medication(String ID, String name, String details, String dosage, int quantity, String category, double price,String brand) {
      this.ID = ID;
      this.name = name;
      this.details = details;
      this.dosage = dosage;
      this.quantity = quantity;
      this.category = category;
      this.price = price;
      this.processedStatus = false;
      this.brand=brand;
   }

   private Boolean processedStatus;
   //{"code":"PN9J","name":"Panadol","brand":"ChengPharma","description":"For temperature control","dosage_instruction":"2/3 per day","price":"590","quantity":"1000","category":"Flu"}

   @Override
   public String toString() {
      return "Medication{" +
              "ID='" + ID + '\'' +
              ", name='" + name + '\'' +
              ", details='" + details + '\'' +
              ", dosage='" + dosage + '\'' +
              ", quantity=" + quantity +
              ", category='" + category + '\'' +
              ", price=" + price +
              ", brand='" + brand + '\'' +
              ", processedStatus=" + processedStatus +
              '}';
   }
   public String Description(){
      return "Medication{" +
              "code='" + ID + '\'' +
              ", name='" + name + '\'' +
              ", details='" + details + '\'' +
              ", dosage='" + dosage + '\'' +
              ", quantity=" + quantity +
              ", category='" + category + '\'' +
              ", price=" + price +
              ", brand='" + brand +
              '}';
   }

   public JSONObject toJson() {
      JSONObject jsonObject = new JSONObject();
      jsonObject.put("ID", ID);
      jsonObject.put("name", name);
      jsonObject.put("details", details);
      jsonObject.put("dosage", dosage);
      jsonObject.put("quantity", quantity);
      jsonObject.put("processedStatus", processedStatus);
      jsonObject.put("category",category);
      jsonObject.put("price",price);
      return jsonObject;
   }

   public static ArrayList<Medication> getAvailableMedications(String filePath) throws IOException, ParseException {
      FileHandler fileHandler = new FileHandler(filePath);
      JSONArray jsonArray = fileHandler.readJSONArrayFromFile();
      ArrayList<Medication> availableMedications = new ArrayList<>();
      for (Object obj: jsonArray) {
         JSONObject jsonObject = (JSONObject) obj;

         // TODO: Add code to get medication ID (it's named as code from medications/products file), name, price and quantity
         String medicationID = jsonObject.get("code").toString();
         String medicationName = jsonObject.get("name").toString();
         String medicationPrice = jsonObject.get("price").toString();
         String medicationQuantity = jsonObject.get("quantity").toString();
         String medicationDetails = jsonObject.get("description").toString();
         String medicationDosage = jsonObject.get("dosage_instruction").toString();
         String medicationCategory = jsonObject.get("category").toString();
         String medicationBrand = jsonObject.get("brand").toString();
         Medication medication = new Medication(medicationID,medicationName,medicationDetails,medicationDosage,Integer.parseInt(medicationQuantity),medicationCategory,Double.parseDouble(medicationPrice),medicationBrand) ;
         availableMedications.add(medication);
      }
      return availableMedications;
   }

   public static ArrayList<Medication> searchMedications(ArrayList<Medication> allMedications,String by,String text){
      //TODO: Search medication based on by and written text
      ArrayList<Medication> matchingList = new ArrayList<>();
      switch (by){
         case "name" -> matchingList.addAll(allMedications.stream().filter(medication -> medication.name.toLowerCase().contains(text.toLowerCase())).toList());
         case "brand" -> matchingList.addAll(allMedications.stream().filter(medication -> medication.brand.toLowerCase().contains(text.toLowerCase())).toList());
         case "category" -> matchingList.addAll(allMedications.stream().filter(medication -> medication.category.toLowerCase().contains(text.toLowerCase())).toList());
         default -> System.err.println("Invalid choice");
      }
      return  matchingList;
   }
   public static String getSearchChoice(int choice){

      switch (choice){
         case 2 ->{
            return "brand";
         }
         case 3 ->{
            return "category";
         }
         default -> {
            return "name";
         }
      }

   }
   public Medication() {
	   this.processedStatus = false;
   }

   public Medication(String _id,String _name,int qty ) {
      this.ID = _id;
      this.name =_name;
      this.quantity = qty;
      this.processedStatus = false;
   }


   // TODO: Add code to help you to create object/instance for this class in different way


   public Medication(String medicationID, String medicationName,String dosage, int quantity, String details) {
      this.ID = medicationID;
      this.name =medicationName;
      this.quantity = quantity;
      this.dosage=dosage;
      this.details = details;
   }

   public Medication(String ID, String name, String details, String dosage, int quantity, Boolean processedStatus) {
      this.ID = ID;
      this.name = name;
      this.details = details;
      this.dosage = dosage;
      this.quantity = quantity;
      this.processedStatus = processedStatus;

   }
   // TODO: Add code to help you to access or modify data members for this class

   public String getID() {
      return ID;
   }

   public void setID(String ID) {
      this.ID = ID;
   }

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public String getDetails() {
      return details;
   }

   public void setDetails(String details) {
      this.details = details;
   }

   public String getDosage() {
      return dosage;
   }

   public void setDosage(String dosage) {
      this.dosage = dosage;
   }

   public int getQuantity() {
      return quantity;
   }

   public void setQuantity(int quantity) {
      this.quantity = quantity;
   }

   public Boolean getProcessedStatus() {
      return processedStatus;
   }

   public void setProcessedStatus(Boolean processedStatus) {
      this.processedStatus = processedStatus;
   }

}