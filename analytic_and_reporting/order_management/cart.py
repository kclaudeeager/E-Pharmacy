from .product import Product
from .stock import Stock
import json
import os

# Dieudonne

productFile = "data/products.json"
cartFile = "data/cart.json"


class Cart:
    """Represents a cart with a list of products and quantity

    Attributes:
        products: a dictionary with the key being the ID of the products, and the value being the quantity
        added
    """

    def returnProduct(self, id: str) -> dict:
        stock = Stock.load(productFile)
        foundProj = {}
        print(len(stock.products), "len")
        for i in stock.products:
            print()
            if str(i.code) == id:
                foundProj = {
                    "code": i.code,
                    "name": i.name,
                    "brand": i.brand,
                    # "description": i.description,
                    "quantity": i.quantity,
                    "price": i.price,
                    # "dosage_instruction": i.dosage_instruction,
                    # "requires_prescription": i.requires_prescription,
                    # "category": i.category
                }
        return foundProj

    def savetofile(self, filePath, arr: [dict]):
        with open(filePath, "w") as outputfile:
            outputfile.write(arr)
        print("Updated cart successfully")

    def readFromFile(self, filePath) -> [dict]:
        with open(filePath, "r") as outputfile:
            return outputfile.read()

    def __init__(self, stock: Stock) -> None:
        self.products = {}
        self.stock = stock

    def add(self, productCode: str, quantity: int):
        """Adds a product to the cart with the specified quantity

        Args:
            productCode: the identifier of the product
            quantity: quantity to add

        Returns: None
        """
        stock = self.stock

        # result = [item for item in stock.products if item.code == productCode]
        s: Product
        for item in stock.products:
            if item.code == productCode:
                s = item
        cart_product = self.returnProduct(productCode)

        if quantity <= 0:
            print("Please enter a valid quantity ")
            return
        elif quantity > s.quantity:
            print(f"Sorry, {s.name}'s quantity is out of stock. Please try again.")
            return
        else:
            cart_product["quantity"] = quantity

        # write to the json file
        if os.path.isfile(cartFile):
            # if the file exists
            with open(cartFile, 'r') as json_file:
                json_data = json_file.read()

            existing_list = json.loads(json_data)
            existing_list.append(cart_product)
            savedList = json.dumps(existing_list, indent=1)
            self.savetofile(cartFile, savedList)

        else:
            f = open("data/cart.json", "w")
            print("doesn't exist")
            foundProduct = self.returnProduct(productCode)
            self.savetofile(cartFile, json.dumps([foundProduct]))

        # f = open("data/cart.json", "r")
        # f.write("[")
        # f.write("Test")
        # f.write("]")

        print(productCode, type(quantity), "Parameter passed ")
        # TODO: Make sure the quantity is valid (> 0 and <= to the quantity in the stock)
        # TODO: If the product was already in the cart, increment the quantity

        # TODO: After the checks, add the product to the dictionary

    def __str__(self) -> str:
        """String representation of the cart
        """
        self.stock.load("data/products.json")
        print(self.stock, "stocks")
        return ""
        # TODO: Return a string representation of a cart that shows the products, their quantity, unit price, total price. And also the total price of the cart
        # Feel free to format it the way you want to
        # y = json.loads("data/products.json")
        # print(y)

    def remove(self, code):
        """
        Removes a specific product from the cart """
        cartList = json.loads(self.readFromFile(cartFile))
        # print(cartList[0]['code'], "the code")
        existing = [item for item in cartList if item['code']
                    == code]
        if len(existing) == 0:
            return

        result = [item for item in cartList if item['code']
                  != code]
        self.savetofile(cartFile, json.dumps(result))
        # TODO: Removes a product from the cart. safely fail if the product code is not found

    def clear(self):
        """Clears up the cart.
        """
        res = str(
            input(f"Are you want to empty your cart? \nPlease enter'y' or yes and 'n' for no : "))
        if res == 'y':
            cartList = json.loads(self.readFromFile(cartFile))
            cartList.clear()
            self.savetofile(cartFile, json.dumps(cartList))

    @property
    def cost(self):
        """Returns the total cost of the cart"""
        # TODO: implement the function
