#include <sstream>
#include <vector>
#include <string.h>
#include <fstream>
#include <stdio.h>
#include <iostream>
#include <cctype>
#include <algorithm>

//Importing Product class
#include "Product.cpp"

using namespace std;

class FileHandler{
    public:
    string filename;

    vector<Product> readJsonFile(){

        // Add code here
        vector<Product> prodList;
        vector<string> prodLines;
        string prodLine;
        Product manProd;
        

        if (filename.empty()){
            filename = "data/products.json";
        }

        cout<<"Reading "<<filename<< " File........."<<endl;

        ifstream prodsFile(filename);

        while (getline(prodsFile, prodLine)){
             
                prodLines.push_back(prodLine);

                if(prodLine.substr(0,1) == "{"){

                    manProd.productFromJson(prodLine);
                    prodList.push_back(manProd);

                }
        }          
        
        cout<<"Finished Reading "<<filename<< " File........."<<endl;
         prodsFile.close();
        return prodList;
    };

    void saveToJsonFile(Product p){

        vector<Product> pList;
        
        pList = readJsonFile();

        pList.push_back(p);
          //Check if the file exists.
        ifstream input_file(filename);

        if (!input_file.good()) {
            // The file does not exist.
            cout << "First Record ........." << endl;

            ofstream jsonFile(filename);

            jsonFile<<"["<<endl;
            jsonFile<< p.toJson()<<endl;
            jsonFile<<"]"<<endl;
            jsonFile.close();
            input_file.close();
            return;

        }

        // Delete the file.
    int ret = remove(filename.c_str());
    if (ret != 0) {
        std::cout << "Error deleting file: " << strerror(errno) << "\n";
        return ;
    }

    ofstream jsonFile(filename);
    jsonFile<<"["<<endl;
    for(int i=0; i<pList.size(); i++){

        if(i< pList.size() -1){
            jsonFile<< pList.at(i).toJson()<<","<<endl;
        }
        else{
            jsonFile<< pList.at(i).toJson()<<endl;
        }
    }
      jsonFile<<"]"<<endl;
        jsonFile.close();
        input_file.close();
    }

    void removeProduct(string name) {
        vector<Product> list = readJsonFile();
        int ret = remove(filename.c_str());
        if (ret != 0) {
            std::cout << "Error deleting file: " << strerror(errno) << "\n";
            return;
        }
        for (auto product: list) {
            if (product.getName() == name) {
                break;
            }
            saveToJsonFile(product);
        }
    }

    void prompt (string info ){
        cout<<"==================================================="<<endl;
        cout<<info<<endl;
        cout<<"==================================================="<<endl;
    }

    void updateProduct(string name){
        cout<<"Entered here"<<endl;
        vector<Product> list = readJsonFile();
        vector<Product> newlist;
        Product foundItem;
        bool exist = false;
        for (Product i : list){
            if (i.getName() == name){
                foundItem = i;
                exist = true;
                break;
            }
        }

        if (!exist){
            cout<<"Product name with "<<name<<" doesn't exist !!!"<<endl;
            return;
        }

        prompt("Default value for each field is shown in bracket ");
//        bool asking = true;
        string questions;
//        Name
        cout<<"Enter a new product name ("<<foundItem.getName()<<") or 's' to save default : ";
        getline(cin >> ws, questions);
        foundItem.setName(questions == "s" ? foundItem.getName() : questions );
//        Brand Name
        cout<<"Enter a new brand name:("<<foundItem.getBrand()<<") or 's' to save default : ";
        getline(cin >> ws, questions);
        foundItem.setBrand(questions == "s" ? foundItem.getBrand() : questions );
//        description
        cout<<"Enter a new description name:("<<foundItem.getDecription()<<") or 's' to save default: ";
        getline(cin >> ws, questions);
        foundItem.setDescription(questions == "s" ? foundItem.getDecription() : questions);
//      dosage
        cout<<"Enter a new dosage Instruction name ("<<foundItem.getDosageInstraction()<<") or 's' to save default ";
        getline(cin >> ws, questions);
        foundItem.setDosageInt(questions == "s" ? foundItem.getDosageInstraction() : questions);
//      Price
        cout<<"Enter a new price ("<<foundItem.getPrice()<<") or 's' to save default ";
        getline(cin >> ws, questions);
//        cout<<typeid(questions).name()<<"this is th data type "<<endl;
        foundItem.setPrice( questions == "s" ?  foundItem.getPrice() : stof(questions));
//      Category
        cout<<"Enter a new category:("<<foundItem.getCategory()<<") or 's' to save default ";
        getline(cin >> ws, questions);
        foundItem.setCategory(questions == "s" ? foundItem.getCategory() :  questions);
//      Category
        cout<<"Update prescription ("<<foundItem.getRequiresPrescription()<<"). \n Enter '1' for yes and '2' for no : ";
        getline(cin >> ws, questions);
        foundItem.setPrescription( questions == "s" ? foundItem.getRequiresPrescription() : stoi(questions));


//       declare a vector with Product instance type
        for (Product i : list){
            if (i.getName() == name){
                continue;
            }
            saveToJsonFile(i);
        }
        saveToJsonFile(foundItem);
    }
};