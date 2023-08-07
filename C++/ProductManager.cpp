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

         cout<<"Product here: "<<endl<<prod.toJson();
         FileHandler fHandler;
         fHandler.saveToJsonFile(prod);

    }

    // TODO Add code for Updating a product

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
                cout<<"Search Product By Name";
                break;
            case 3:
                cout<<"Search Product By Category";
                break;
            case 4:
                cout<<"Search Product By Brand";
                break;
            case 5:
                cout<<"Update Product";
                break;
            case 6:
                cout<<"Delete Product";
                break;
            default:
                cout<<"choice not available now!";
                break;

        }
    }


    return 0;
}
