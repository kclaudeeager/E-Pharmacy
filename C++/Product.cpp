#include <iostream>
#include <string.h>
#include <chrono>
#include <random>
#include <sstream>
#include <iomanip>


using namespace std;
using namespace std::chrono;

enum ans {
    julius
};

class Product{

    private:
    int quantity;
    string name;
    string brand;
    string description;
    string code;
    float price;
    string dosageInstruction;
    string category;
    bool requires_prescription;

    public:

    string getName(){
        //TODO Add code that return the Product Name
        return name;
    }

    string getBrand(){
        //TODO Add code that return the Product Brand
        return brand;
    }

    void setName(string newName ){
        name = newName;
    };

    void setQuantity(string newQty ){
        name = newQty;
    };

    void setBrand(string newBrand){
        brand = newBrand;
    };

    void setDescription(string newDesc){
        description = newDesc;
    }

    void setPrice (float newPrice){
        price = newPrice;
    }

    void setDosageInt(string newDosage){
        dosageInstruction = newDosage;
    }

    void setCategory(string newCategory){
        dosageInstruction = newCategory;
    }

    void setPrescription(int ans){
        requires_prescription = ans;
    }

    string getDecription(){
        //TODO Add code that return the Product Description
        return description;
    }

    string getDosageInstraction(){
        //TODO Add code that return the Product Dosage Instruction
        return dosageInstruction;
    }

    string getCategory(){
        //TODO Add code that return the Product Category
        return category;
    }
    
    int getQuantity(){
        //TODO Add code that return the Product Quantity
        return quantity;
    }

    float getPrice(){
        //TODO Add code that return the Product Price
        return price;
    }

    int getRequiresPrescription(){
        //TODO Add code that return Product Requires Prescription status
        return requires_prescription;
    }
    string getCode(){
        return code;
    }

    string generateUniqueCode()
    {
        string characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

        string uniqueCode = "";
        auto now = system_clock::now();
        auto millis = duration_cast<milliseconds>(now.time_since_epoch());
        mt19937 generator(millis.count());
        uniform_int_distribution<int> distribution(0, 100000);

        // generate 10 characters long unique string

        for (int i = 0; i <= 10; i++)
        {
            int random_index = distribution(generator) % characters.length();
            uniqueCode += characters[random_index];
        }

        return uniqueCode;
    };



    static string promptTextField(string promptText){

        // TODO Add code to prompt user for input for any Product string field
        // method takes text to display e.g: "Enter Product Name:"
        // it prompts a user and return user input in form of text. Text can be made by multiple words.
        cout<<promptText<<": ";
        string inputText;
        getline(cin >> std::ws, inputText);
        return inputText;
    }

    static float promptNumberField(string promptText){
        // TODO Add code to prompt user for input for any Product Numeric field
        // method takes text to display e.g: "Enter Product Name:"
        // it prompts a user and return user input in form of text. Text can be made by multiple words.

        cout<<promptText<<": ";
        float inputFloat;
        cin>>inputFloat;
        return inputFloat;
    }

    static bool promptRequirePrescription()
    {
        // TODO Add code to prompt user for choosing if Product requires prescription.
        // User can type 1 or 0. 
        // it prompts a user and return user input in form of boolean. 
        int userChoise=-1;
        bool ans = false;
        bool asking = true;
        string input;

        while (userChoise != 1 && userChoise != 0) {
            cout << "Does this product require prescription? Answer 1 for yes, 0 for no: ";

            cin >> input;

            if (input == "1") {
                userChoise = 1;
                ans = true;
            } else if (input == "0") {
                userChoise = 0;
                ans = false;
            } else {
                cout << "Invalid input. Please enter 1 or 0." << endl;
                promptRequirePrescription();
            }
            return ans;
        }

//        while(asking) {
//            cout << "Does this product require prescription? answer 1 for yes, 0 for no" << ": first";
//            cin >> userChoise;
//
//            if (!(cin >> userChoise)) {
//                // If input was not an integer
//                cin.clear(); // Clear the error state
//                cin.ignore(numeric_limits<streamsize>::max(), '\n'); // Ignore the invalid input
//                cout << "Invalid input. Please enter an integer (1 for yes or 0 for no)." << ": ";
//                cin >> userChoise;
//                ans = userChoise == 1;
//                asking = false;
//            }else if( userChoise < 0 || userChoise >> 1){
//                cout << "Invalid input. Please enter an integer (1 for yes or 0 for no)." << ": ";
//                cin >> userChoise;
//                ans = userChoise == 1;
//                asking = false;
//            }
//            else if (userChoise == 1) {
//                ans = true;
//                asking = false;
//            }else if(userChoise == 0) {
//                ans = false;
//                asking = false;
//            }else{
//                cout << "Does this product require prescription? answer 1 for yes, 0 for no" << ": old";
//                cin >> userChoise;
//                ans = userChoise == 1;
//                asking = false;
//            };
//        }
    }

    void createProduct()
    {

        // TODO Add code that calls promptTextField() method and prompt user for entering product name and update the name field.
        // TODO Add code that calls promptTextField() method and prompt user for entering product brand and update the brand field.
        // TODO Add code that calls promptTextField() method and prompt user for entering product description and update the decription field.
        // TODO Add code that calls promptTextField() method and prompt user for entering product category and update the category field.
        // TODO Add code that calls promptTextField() method and prompt user for entering product dosageInstruction and update the dosage instruction field.
        // TODO Add code that calls promptNumberField() method and prompt user for entering product quantity and update the quantity field.
        // TODO Add code that calls promptNumberField() method and prompt user for entering product price and update the price field.
        // TODO Add code that calls promptRequirePrescription() method and prompt user for entering product requires presc and update the requiresprescription field.

        // Add code to generate Unique code for product using generateUniqueCode method
        cout<<"---------------------------------------------------"<<endl;
        cout<<"Provide answers to the prompt below to add a product"<<endl;
        cout<<"---------------------------------------------------"<<endl;
        name = promptTextField("Enter product name");
        brand =  promptTextField("Enter product brand");
        description =  promptTextField("Enter product description");
        category =  promptTextField("Enter product category");
        dosageInstruction = promptTextField("Enter product dosage instractions");
        quantity = int(promptNumberField("Enter the quantity"));
        price = promptNumberField("Enter the price");
        requires_prescription = promptRequirePrescription();
        code = generateUniqueCode();
   };

    string toJson()
    {
        std::stringstream productInJsonStream;
      // TODO Add code for converting a product to json form from the private declared attributes.
      // The Output should look like:
      //{"code":"tgtwdNbCnwx","name":"name 1","brand":"br 2","description":"df","dosage_instruction":"dfg","price":123.000000,"quantity":13,"category":"des","requires_prescription":1}
        productInJsonStream << "{\"code\":\"" << code << "\",";
        productInJsonStream << "\"name\":\"" << name << "\",";
        productInJsonStream << "\"brand\":\"" << brand << "\",";
        productInJsonStream << "\"description\":\"" << description << "\",";
        productInJsonStream << "\"dosage_instruction\":\"" << dosageInstruction << "\",";
        productInJsonStream << "\"price\":" << fixed << setprecision(6) << price << ",";
        productInJsonStream << "\"quantity\":" << quantity << ",";
        productInJsonStream << "\"category\":\"" << category << "\",";
        productInJsonStream << "\"requires_prescription\":" << requires_prescription << "}";

        return productInJsonStream.str();
    };

    void productFromJson(string txt)
    {
        //TODO Add code to convert a json string product to product object
        // string is in the form below
        //{"code":"tgtwdNbCnwx","name":"name 1","brand":"br 2","description":"df","dosage_instruction":"dfg","price":123.000000,"quantity":13,"category":"des","requires_prescription":1}
        // You need to extract value for each field and update private attributes declared above.
        unordered_map<string,string> keyValuePairs;

       string temp=txt.substr(1,txt.length()-2);
        istringstream iss(temp);
        string key, value;

        while (getline(iss, key, ':') && getline(iss, value, ',')) {
            key.erase(key.find_last_not_of(" \t") + 1);
            value.erase(0, value.find_first_not_of(" \t"));
            if (!value.empty() && value.front() == '"' && value.back() == '"') {
                value = value.substr(1, value.length() - 2);
            }
            keyValuePairs[key] = value;
        }

        code = keyValuePairs["\"code\""];
        name = keyValuePairs["\"name\""];
        brand = keyValuePairs["\"brand\""];
        description = keyValuePairs["\"description\""];
        dosageInstruction = keyValuePairs["\"dosage_instruction\""];
        price = stof(keyValuePairs["\"price\""]);
        quantity = stoi(keyValuePairs["\"quantity\""]);
        category = keyValuePairs["\"category\""];
        requires_prescription = stoi(keyValuePairs["\"requires_prescription\""]);
    };
};
