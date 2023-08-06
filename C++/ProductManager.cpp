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
    }

    void addProduct()
    {
        // TODO add code to add product and
        // store the product to products.json file by using Product class and FileHandler class
        prod.createProduct();
        cout << "Product here: " << endl
             << prod.toJson();
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
    prodManager.addProduct();
    return 0;
}
