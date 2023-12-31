from .product import Product
from .stock import Stock
import json
import os
from .prescription import Prescription

# Dieudonne

productFile = "data/products.json"
cartFile = "data/cart.json"
MSG_WRONG_INPUT = "Wrong input. Try again!"


class Cart:
    """Represents a cart with a list of products and quantity

    Attributes:
        products: a dictionary with the key being the ID of the products, and the value being the quantity
        added
    """

    def tableHead(self, titleList: list):
        for x in titleList:
            print(f"|{x.capitalize():<15}", end="")
        print("")
        print("--------------------------------------------------------------------------------------------")

    # display cart
    def displayCart(self):
        cartList = json.loads(self.readFromFile(cartFile))
        if len(cartList) == 0:
            print("No product in cart ")
            return
        print("--------------------------")
        print("Avalable products in cart")
        print("--------------------------")
        sampleHeader = ["id", "name", "brand",
                        "quantity", "price", "total"]
        self.tableHead(sampleHeader)
        index = 0
        for p in cartList:
            index += 1
            print(
                f"|{index:<15}|{p['name']:<15}|{p['brand']:<15}|{p['quantity']:<15}|{p['price']:<15}|{float(p['price']) * float(p['quantity'])}")
        print("---------------------------------------------------")

    def returnProduct(self, id: str) -> dict:
        stock = Stock.load(productFile)
        foundProj = {}
        for i in stock.products:
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

        # if not any(product.code == productCode for product in stock.products):
        #     return

        s: Product
        for item in stock.products:
            if item.code == productCode:
                s = item

        # TODO: Make sure the quantity is valid (> 0 and <= to the quantity in the stock)
        if quantity > s.quantity:
            print(
                f"Sorry, the quantity you entered is more than the available stock.")
            return
        elif quantity == 0:
            print(
                f"Quantity must be more than or equal to 1.")
            return
        # TODO: If the product was already in the cart, increment the quantity
        # if file exist
        if os.path.isfile(cartFile):
            # if file is empty
            existing_list = json.loads(
                self.readFromFile("data/prescriptions.json"))
            newArr = json.loads(self.readFromFile(cartFile))
            print(newArr, "len")
            for item in existing_list:
                for i in item['Medications']:
                    if productCode == i['id']:
                        i['quantity'] += quantity
                        newArr.append(i)
                newArr.append(i)

            self.savetofile(cartFile, json.dumps(newArr))
            return

        # TODO: After the checks, add the product to the dictionary

        # update the quantity in file

        # write to the json file
        # if os.path.isfile(cartFile):
        #     # if the file exists
        #     existing_list = json.loads(self.readFromFile(cartFile))
        #     # print(existing_list, "existing list ")
        #     if any(value['code'] == productCode for value in existing_list):
        #         for item in existing_list:
        #             if item['code'] == productCode:
        #                 item['quantity'] += askQty
        #         self.savetofile(cartFile, json.dumps(existing_list, indent=1))
        #     else:
        #         existing_list.append(cart_product)
        #         savedList = json.dumps(existing_list, indent=1)
        #         self.savetofile(cartFile, savedList)

        # else:
        #     foundProduct = self.returnProduct(productCode)
        #     self.savetofile(cartFile, json.dumps([foundProduct]))

    def updateCartQty(self, code: str, qty: int):
        # search for product in the cart
        cartList = json.loads(self.readFromFile(cartFile))
        for p in cartList:
            if p['code'] == code:
                p['quantity'] += qty
        self.savetofile(cartFile, json.dumps(cartList))

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
        self.savetofile(cartFile, json.dumps(result, indent=1))
        # TODO: Removes a product from the cart. safely fail if the product code is not found

    def clear(self):
        """Clears up the cart.
        """
        res = str(
            input(f"Are you want to empty your cart? \nPlease enter 'y' or yes and 'n' for no : "))
        if res == 'y':
            cartList = json.loads(self.readFromFile(cartFile))
            cartList.clear()
            self.savetofile(cartFile, json.dumps(cartList))

    def askforChoice(self, arr) -> int:
        try:

            selectedInput = int(
                input("Enter the Id of the product or 0 to go back : "))
            if selectedInput > len(arr):
                print("ID doesn't exist")
            elif selectedInput == 0:
                selectedInput == 0
            else:
                return selectedInput
            return selectedInput
        except ValueError:
            print(MSG_WRONG_INPUT)
        except TypeError:
            print(MSG_WRONG_INPUT)

    @property
    def cost(self):
        """Returns the total cost of the cart"""
        # TODO: implement the function
        try:
            totalCost = sum(
                map(lambda x: x['price'], json.loads(self.readFromFile(cartFile))))
            return totalCost
        except ValueError:
            print(MSG_WRONG_INPUT)
        except IndexError:
            print(MSG_WRONG_INPUT)
        except TypeError:
            print(MSG_WRONG_INPUT)
