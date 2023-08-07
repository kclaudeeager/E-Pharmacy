#include "SearchProduct.cpp"

class ProductManager
{
private:
    Product prod;

public:
     int getMenu()
    {

        // TODO Add code to display Menu
        // Menu should have
        // Add Product
        // Search Product By Name
        // Search Product By Category
        // Search Product By Brand
        // Update Product
        // Delete Product
        int chosen=-1;
        while(chosen<0 || chosen>6){
             cout<<"Select your option"<<endl<< "1:Add product \n 2:Search product by name\n 3:Search Product By Category \n 4:Search Product By Brand\n 5:Update Product \n 6:Delete Product\n and 0 to quit\n";
             cout<<"Enter your choice: ";
             cin>>chosen;
             if(chosen<0 || chosen>6)
                 cout<<"Invalid choice. please choose between 1..6 \n";
        }
        return  chosen;
    }

    void addProduct()
    {
        // TODO add code to add product and
        // store the product to products.json file by using Product class and FileHandler class
         prod.createProduct();
         FileHandler fHandler;
         fHandler.saveToJsonFile(prod);

    }


   void searchProduct(string by){
       string searchText=  prod.promptTextField("Enter product's "+by);
       SearchProduct search;
       vector<Product> products;
         if(by=="name"){
          products= search. searchByName(searchText);
         }
         else if(by=="category"){
             products= search.searchByCategory(searchText);
         }
         else if(by=="brand"){
             products= search. searchByBrand(searchText);
         }
         else{
             cout<<"Choice not provided"<<endl;
         }
       search.showSearchResult(products);
     }
    // TODO Add code for Updating a product
    void deleteProduct(){
        string productName;
        cout<<"Enter the name product you want to delete: ";
        cin>>productName;
        FileHandler file;
        file.removeProduct(productName);

    }
    // TODO Add code for deleting a product
};

int main()
{

    // ADD Code for displaying a welcome Menu
    // and handle all required logic to add, search, update, and delete product

    ProductManager prodManager;
    bool isExit=false;
    while(!isExit){
        int menuChoice = prodManager.getMenu();
        switch (menuChoice) {
            case 0:
                cout<<"Exiting the application...\n";
                isExit = true;
                break;
            case 1:
                prodManager.addProduct();
                break;
            case 2:
                prodManager.searchProduct("name");
                break;
            case 3:
                prodManager.searchProduct("category");
                break;
            case 4:
                prodManager.searchProduct("brand");
                break;
            case 5:
                cout<<"Update Product";
                break;
            case 6:
                prodManager.deleteProduct();
                break;
            default:
                cout<<"choice not available now!";
                break;
        }
    }

    return 0;
}
