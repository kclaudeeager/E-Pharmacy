#include "FileHandler.cpp"

class SearchProduct
{
private:
    string filename;

public:
    string searchText;
    FileHandler fHandler;

    string to_lowercase(const string& text) {
        string lowercase_text;
        for (char c : text) {
            lowercase_text += tolower(c);
        }
        return lowercase_text;
    }



    vector<Product> searchByName(string name){

        // TODO
        //Add code to search by name. Searching is not case sensitive it means 
        //for input like: "name" products with names like "Name 1", "Product name" needs to included in the found results.
        vector<Product> foundProducts;
        vector<Product> allProducts=fHandler.readJsonFile();

        for(auto product:allProducts){
            if(searchFound(product.getName(),name)){
                foundProducts.push_back(product);
            }
        }
        return foundProducts;
    };
    bool searchFound(string ar1,string arg2){
        return to_lowercase(ar1).find( to_lowercase(arg2)) != std::string::npos;
    }
    vector<Product> searchByCategory(string categ){

        // TODO
        //Add code to search by category. Searching is not case sensitive it means 
        //for input like: "categ" products with category like "category 1", "Product category" needs to included in the found results.
        vector<Product> foundProducts;
        vector<Product> allProducts=fHandler.readJsonFile();
        for(auto product:allProducts){
            if(searchFound(product.getCategory(),categ)){
                foundProducts.push_back(product);
            }
        }
        return foundProducts;
    };

    vector<Product> searchByBrand(string brand){
      // TODO
        //Add code to search by brand. Searching is not case sensitive it means 
        //for input like: "br" products with names like "Brand 1", "brand name" needs to included in the found results.
        vector<Product> foundProducts;
        vector<Product> allProducts=fHandler.readJsonFile();
        for(auto product:allProducts){
            if(searchFound(product.getCategory(),brand)){
                foundProducts.push_back(product);
            }
        }
        return foundProducts;
    };

    void showSearchResult(vector<Product> plist)
    {
        // TODO
        //Add code to display Search results
        if(plist.empty()){
            cout<<"no matches for the search text"<<endl;
        }
        else{
            cout<<"Found products:\n ______________________________\n";
            for(Product product:plist){
                cout<<product.toJson()<<endl;
            }
        }

    }
};