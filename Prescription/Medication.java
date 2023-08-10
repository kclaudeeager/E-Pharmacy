import org.json.simple.JSONObject;

public class Medication {

   private String ID;
   private String name;
   private String details;
   private String dosage;
   private int quantity;
   private Boolean processedStatus;

   public JSONObject toJson() {
      JSONObject jsonObject = new JSONObject();
      jsonObject.put("ID", ID);
      jsonObject.put("name", name);
      jsonObject.put("details", details);
      jsonObject.put("dosage", dosage);
      jsonObject.put("quantity", quantity);
      jsonObject.put("processedStatus", processedStatus);

      return jsonObject;
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

   @Override
   public String toString() {
      return "Medication{" +
              "ID='" + ID + '\'' +
              ", name='" + name + '\'' +
              ", details='" + details + '\'' +
              ", dosage='" + dosage + '\'' +
              ", quantity=" + quantity +
              ", processedStatus=" + processedStatus +
              '}';
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