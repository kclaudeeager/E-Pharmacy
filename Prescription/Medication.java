public class Medication {

   private String ID;
   private String name;
   private String details;
   private String dosage;
   private int quantity;
   private Boolean processedStatus;

   public Medication() {
	   this.processedStatus = false;
   }

   public Medication(String _id,String _name,int qty ) {
      this.ID = _id;
      this.name =_name;
      this.quantity = qty;
      this.processedStatus = false;
   }

   public Medication(String medicationID, String medicationName, String medicationDetails, String dosage, int quantity) {

   }

   // TODO: Add code to help you to create object/instance for this class in different way

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


   // TODO: Add code to help you to access or modify data members for this class


   public String toString() {
      return this.ID + "," + this.name + "," + this.quantity + "," + this.processedStatus;
   }
}