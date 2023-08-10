import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Objects;


public class Prescription {
	   private String prescriptionID;
	   private String customerID;
	   private String doctorName;
	   private String prescriptionFileLocation;
	   private ArrayList<Medication> medications;
	   private LocalDate date;
	   private static JSONArray prescriptionList;
	   private static FileHandler fileHandler;



	public Prescription(String prescriptionID, String customerID, String doctorName, LocalDate dateToPrint, ArrayList<Medication> medications, String prescriptionFileLocation) {
		this.prescriptionID = prescriptionID;
		this.customerID = customerID;
		this.doctorName = doctorName;
		this.prescriptionFileLocation = prescriptionFileLocation;
		this.medications = medications;
		this.date = dateToPrint;
	}

	public void setCustomerID(String customerID) {
		this.customerID = customerID;
	}

	public String getPrescriptionFileLocation() {
		return prescriptionFileLocation;
	}

	@Override
	public String toString() {
		return "Prescription{" +
				"prescriptionID='" + prescriptionID + '\'' +
				", customerID='" + customerID + '\'' +
				", doctorName='" + doctorName + '\'' +
				", prescriptionFileLocation='" + prescriptionFileLocation + '\'' +
				", medications=" + medications +
				", date=" + date +
				'}';
	}

	public void setPrescriptionFileLocation(String prescriptionFileLocation) {
		this.prescriptionFileLocation = prescriptionFileLocation;
	}


	public JSONObject toJson() {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("prescriptionID", this.getPrescriptionID());
		jsonObject.put("customerID", this.getCustomerID());
		jsonObject.put("doctorName", this.getDoctorName());
		jsonObject.put("prescription_file_location",this.prescriptionFileLocation);
		JSONArray medicationsArray = new JSONArray();
		for (Medication medication : this.medications) {
			medicationsArray.add(medication.toJson());
		}
		jsonObject.put("medications", medicationsArray);
		jsonObject.put("date", this.getDate().toString());

		return jsonObject;
	}

	public Prescription() {
		   prescriptionList = new JSONArray();
		   fileHandler = new FileHandler();

	   }

	public Prescription(String _prescriptionID, String _customerID, String _doctorName, ArrayList<Medication> _medication)
	   {
	       prescriptionID = _prescriptionID;
	       customerID = _customerID;
	       doctorName = _doctorName;
	       medications = _medication;
	       date = LocalDate.now();
		   fileHandler = new FileHandler();
	   }



	// TODO: Add code to help you to create object/instance for this class in different way

	public Prescription(String _prescriptionID, String _customerID, String _doctorName, LocalDate date,ArrayList<Medication> _medication)
	{
		prescriptionID = _prescriptionID;
		customerID = _customerID;
		doctorName = _doctorName;
		medications = _medication;
		this.date=date;
		fileHandler = new FileHandler();
	}

	public void setPrescriptionID(String prescriptionID) {
		this.prescriptionID = prescriptionID;
	}

	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}

	public void setMedications(ArrayList<Medication> medications) {
		this.medications = medications;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public static void setPrescriptionList(JSONArray prescriptionList) {
		Prescription.prescriptionList = prescriptionList;
	}

	public void setFileHandler(FileHandler fileHandler) {
		this.fileHandler = fileHandler;
	}


// TODO: Add code to help you to access or modify data members for this class

	public String getPrescriptionID() {
		return prescriptionID;
	}

	public String getCustomerID() {
		return customerID;
	}

	public String getDoctorName() {
		return doctorName;
	}

	public ArrayList<Medication> getMedications() {
		return medications;
	}

	public LocalDate getDate() {
		return date;
	}

	public static JSONArray getPrescriptionList() {
		return prescriptionList;
	}

	public FileHandler getFileHandler() {
		return fileHandler;
	}




	// TODO: Add code needed to be able to add prescription in the file
		// While adding the prescription in the file, please follow the format shown below
		// Format for the prescription: {"DoctorName":"Yves","PrescriptionID":"TA3","Medications":[{"quantity":2,"processedStatus":false,"name":"IBUPROFEN","id":"IB7"}],"CustomerID":"GR","Date":"2023-08-07"}

	    public void addPrescription() throws Exception {
	        JSONArray existingPrescriptions = fileHandler.readJSONArrayFromFile();
			System.out.println(existingPrescriptions.toJSONString());
			// TODO: Add code to add prescription in the file
	        existingPrescriptions.add(this.toJson());
		    setPrescriptionList(existingPrescriptions);
	        fileHandler.writeJSONArrayToFile(existingPrescriptions);
	    }

	   
		// TODO: Add code needed to be able to get all medications on the prescription  
		// TODO: You must return an array of medications!

		private JSONArray  getMedicationsOnPrescription(Prescription prescription) {
			JSONArray jsonArray = new JSONArray();

			// TODO: Add code to get medications on the prescription
			prescription.medications.forEach(medication -> {
				jsonArray.add(medication.toJson());
			});
			return jsonArray;
		}
	    
	   
		// TODO: Add code to help you viewing all prescriptions in the file
		// You must return an array of prescriptions

	   	public static ArrayList<Prescription> viewPrescription() throws IOException, ParseException {
			// TODO: Add code to help you reading from the prescriptions.json file

	        JSONArray jsonArray = fileHandler.readJSONArrayFromFile();

	        // TODO: Add code to help you creating an array of prescriptions
			ArrayList<Prescription> prescriptions = new ArrayList<>();


	        for (Object obj : jsonArray) {
	            JSONObject jsonObject = (JSONObject) obj;
                
                String doctorName = (String) jsonObject.get("doctorName");
                String prescriptionID = (String) jsonObject.get("prescriptionID");
                String customerID = (String) jsonObject.get("customerID");
                String date = (String) jsonObject.get("date");
				String prescriptionFileLocation = (String) jsonObject.get("prescription_file_location") ;
                LocalDate dateToPrint = LocalDate.parse(date);
                ArrayList<Medication> medications = new ArrayList<>();
                
                JSONArray medicationsArray = (JSONArray) jsonObject.get("medications");

                for (Object medObj : medicationsArray) {
                    JSONObject medication = (JSONObject) medObj;

					// TODO: Add code to get medication ID, name and quantity
					// medication quantity should be casted to int
                    // also medication ID and name should be casted to String
					String medicationID= medication.get("ID").toString();
					String medicationName = medication.get("name").toString();
					Integer quantity = Integer.parseInt(medication.get("quantity").toString());
					boolean processedStatus = Boolean.parseBoolean(medication.get("processedStatus").toString());
					String details = medication.get("details").toString();
					String dosage = medication.get("dosage").toString();
					medications.add(new Medication(medicationID,medicationName,details,dosage,quantity,processedStatus));
                }

                prescriptions.add(new Prescription(prescriptionID,customerID, doctorName, dateToPrint, medications,prescriptionFileLocation));

            }
            return prescriptions;
	    }

		// TODO: Add code to help you deleting a specific prescription
		public void deletePrescription() throws IOException, ParseException {
			// TODO: Add code to help you reading from the prescriptions.json file
           JSONArray existingPrescriptions = fileHandler.readJSONArrayFromFile();
			int indexToDelete = -1;
			for (int i = 0; i < existingPrescriptions.size(); i++) {
				JSONObject jsonObject = (JSONObject)existingPrescriptions.get(i);
				String existingPrescriptionID = (String) jsonObject.get("PrescriptionID");
				// TODO: Add code to check if the prescription you want to delete is similar to one exists
				if (Objects.equals(existingPrescriptionID, this.prescriptionID)) {
					indexToDelete = i;
					break;
				}
			}

			if (indexToDelete != -1) {
				existingPrescriptions.remove(indexToDelete);
				fileHandler.writeJSONArrayToFile(existingPrescriptions);
			}
		}
}

