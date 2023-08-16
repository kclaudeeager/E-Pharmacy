from . import Stock, Cart, User, UserManagement, BookRecords, Wrapper, Prescription
import json
import os
# Julius
MSG_WRONG_INPUT = "Wrong input. Try again!"

productFile = "data/products.json"
cartFile = "data/cart.json"


class Menu:
    """Represents the menu class for the project

    Attributes:
        stock: stock variable
        profiles: user management module
        pharmacist: account of the salesperson
        records_file: path to the file containing the sales
        prescriptions_file: path to the file containing the prescriptions.
        stock_file: path to the file containing the stock data
    """

    def __init__(self, stock: Stock, profiles: UserManagement, pharmacist: User, records_file: str,
                 prescriptions_file: str, stock_file: str) -> None:
        self.stock = stock
        self.profiles = profiles
        self.pharmacist = pharmacist
        self.cart = Cart(stock=stock)
        # use the file instead of the object so that we can keep track
        self.records_file = records_file
        self.prescriptions_file = prescriptions_file
        self.stock_file = stock_file

    # def startMenu

    def header(self, str="."):
        if str == 1:
            str = "order"
        elif str == "2":
            str = "analytics"
        print("***********************************")
        print("Order Management and analytic menu")
        print(f"[loc: .{str}]")
        print("***********************************")

    # display head
    def tableHead(self, titleList: list):
        for x in titleList:
            print(f"|{ x.capitalize():<15}", end="")
        print("")
        print("-------------------------------------------------------------------------------------------------------")
    # starter Menu

    def starterMenu(self):
        try:
            print("1. Order Management")
            print("2. Get analytics")
            print("0. Back")
            choice = int(input("Enter your choice: "))
            if choice == 1:
                self.orderMenu()
            elif choice == 2:
                self.analyticMenu()
        except ValueError:
            print(MSG_WRONG_INPUT)

    def readFromFile(self, filePath) -> [dict]:
        with open(filePath, "r") as outputfile:
            return outputfile.read()

    def analyticMenu(self):
        try:
            book = BookRecords.load(self.records_file)
            self.header("analytics")
            print("1. Total income from purchases")
            print("2. Prescription statistics")
            print("3. Purchases for a user")
            print("4. Sales by an agent")
            print("5. Top sales")
            print("0. Back")
            choice = int(input("Enter your choice: "))
            print(choice, "choice")
            if choice == 1:
                self.header(".analytics.sales")
                result = book.totalTransactions()
                print(f"Total sales made is {result}")
            elif choice == 2:
                self.header(".analytics.prescriptions")
                print(book.reportOnPrescriptions())
            elif choice == 3:
                # display a tabl of purchase by user
                self.header(".analytics.purchasebyuser")
                self.displaySales()
                print(book.purchasesByUser("John"))
                # print(existingSales, "existing Sales")
            elif choice == 4:
                # print a
                self.header(".analytics.salesbyagent")
                print(book.salesByAgent("AGT1"))
            elif choice == 5:
                self.header(".analytics.topsales")

                print(book.topNSales())
        except ValueError:
            print(MSG_WRONG_INPUT)

    # print productss
    def displayProduct(self):
        self.stock.load(self.stock_file)
        if len(self.stock.products) == 0:
            print("No product available")
            return
        print("--------------------------")
        print("Available products")
        print("--------------------------")
        sampleHeader = ["id", "name", "brand",
                        "quantity", "category", "desc", "price"]
        self.tableHead(sampleHeader)
        index = 0
        for p in self.stock.products:
            index += 1
            print(f"|{index:<15}{p}")
        print("---------------------------------------------------")
    # ask for choice

    def askforChoice(self) -> int:
        try:
            selectedInput = int(
                input("Enter the Id of the product or 0 to go back : "))
            if selectedInput > len(self.stock.products):
                print("ID doesn't exist")
            elif selectedInput == 0:
                selectedInput = 0
            else:
                return selectedInput
        except ValueError:
            print(MSG_WRONG_INPUT)
        return selectedInput

    def displaySales(self):
        salesArr = json.loads(self.readFromFile(self.records_file))
        if len(salesArr) == 0:
            print("No sales record")
            return
        print("--------------------------")
        print("Sales record")
        print("--------------------------")
        sampleHeader = ["id", "name", "brand",
                        "quantity", "category", "desc", "price"]
        self.tableHead(salesArr[0].keys())
        index = 0
        for p in salesArr:
            index += 1
            print(
                f"|{index:<15}|{p['name']:<15}|{p['quantity']:<15}|{p['price']:<15}|{p['purchase_price']:<15}|{p['timestamp']:<15}|")
        print("---------------------------------------------------")

    def displayPrescription(self) -> list:
        prescriptionList = json.loads(
            self.readFromFile(self.prescriptions_file))

        head = ["id", "DoctorName", "PrescriptionID",
                "Medications", "CustomerID", "Date"]
        self.tableHead(head)
        index = 0
        for item in prescriptionList:
            index += 1
            print(
                f"|{index:<15}|{item['DoctorName']:<15}|{item['PrescriptionID']:<15}|{len(item['Medications']):<15}|{item['CustomerID']:<15}|{item['Date']:<15}")
        return prescriptionList

    def orderMenu(self):
        try:
            self.header("order")
            print("1. Add to cart")
            print("2. Remove from cart")
            print("3. Clear cart")
            print("4. Checkout")
            print("0. Back")
            choice = int(input("Enter your choice: "))
            # print(choice, "choice")
            # cart = Cart(self.stock)

            if choice == 1:
                try:
                    self.header("order.addtocart")
                    # list prescription
                    prescriptionList = self.displayPrescription()

                    if len(prescriptionList) == 0:
                        print("No prescription has been added. Please try again.")
                        return
                    # select a prescription
                    selectedInput = int(
                        input("Enter the Id of the prescrition or 0 to go back : "))

                    if selectedInput > len(self.stock.products):
                        print("ID doesn't exist")
                        return

                    if selectedInput == 0:
                        return

                    selectedPrescription = prescriptionList[selectedInput - 1]
                    print(selectedPrescription, "pres")
                    # check if the prescription has a medication
                    if len(selectedPrescription['Medications']) == 0:
                        print("No medication for this prescription")
                        return
                    # pass each medication to addcart
                    for med in selectedPrescription['Medications']:
                        print("print")
                        self.cart.add(med['id'], med['quantity'])

                except ValueError:
                    print(MSG_WRONG_INPUT)
                except IndexError:
                    print(MSG_WRONG_INPUT)
            elif choice == 2:
                self.header("order.removefromcart")
                self.displayProduct()
                selectedInput = self.askforChoice()
                if selectedInput == 0:
                    return
                selectedInput -= 1
                selectedProduct = self.stock.products[selectedInput]
                self.cart.remove(selectedProduct.code)
            elif choice == 3:
                self.header("order.clearcart")
                self.cart.clear()
            elif choice == 4:
                try:
                    self.header("order.checkout")
                    prescriptionList = self.displayPrescription()
                    # self.tableHead(prescriptionList)
                    user = self.profiles.get_logged_in_user()
                    selectedInput = int(
                        input("Enter the Id of the prescrition or 0 to go back : "))

                    if selectedInput > len(self.stock.products):
                        print("ID doesn't exist")
                        return

                    if selectedInput == 0:
                        return

                    selectedPrescription = prescriptionList[selectedInput - 1]
                    wrapper = Wrapper(
                        self.stock.products, user.username)

                    cart = Cart(self.stock.products)
                    wrapper.checkout(cart, user.username, selectedPrescription)
                except ValueError:
                    print(MSG_WRONG_INPUT)
                except IndexError:
                    print(MSG_WRONG_INPUT)
            elif choice == 0:
                return

        except ValueError:
            print(MSG_WRONG_INPUT)
    #
    # TODO: Create all the necessary functions/method to create and manage the menu using the
    # available variables and all the attributes of the class

    # Make sure to dump the prescriptions, stock, and sale data after every sale.

    # Your menu should have two main options with suboptions. Such as
    """
    1. Order management
        1.1. Adding to a cart (you need to show the list of products and ask the user to select one with ID. Bonus: Can you display with numbers and ask user to choose a number instead?
                Also ask for quantity.)
        1.2. Remove from a cart (display the cart and ask the user to select the element to remove. Remove by ID or by index (bonus))
        1.3. Clear the cart (self explanatory)
        1.4. Checkout (displays the cart with the total and ask for a prescription element. Proceed to checkout and show a message is successful or not).
    2. Analytics
        2.1. Total income from purchases
        2.2. Prescription statistics
        2.3. Purchases for a user
        2.4. Sales by an agent
        2.5. Top sales

    * For each of the menu items, when necessary, display a success or error message to guide the user.
    """

    # **CHALLENGE** (BONUS): Can you implement the menu to work as a USSD application? Implement and show your design
