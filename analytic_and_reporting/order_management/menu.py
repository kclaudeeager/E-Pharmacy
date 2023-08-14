from . import Stock, Cart, User, UserManagement, BookRecords, Wrapper, Prescription
import json
# Julius
MSG_WRONG_INPUT = "Wrong input. Try again!"


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

    def __init__(self, stock: Stock, profiles: UserManagement, pharmacist: User, records_file: str, prescriptions_file: str, stock_file: str) -> None:
        self.stock = stock
        self.profiles = profiles
        self.pharmacist = pharmacist
        self.cart = Cart(stock=stock)
        # use the file instead of the object so that we can keep track
        self.records_file = records_file
        self.prescriptions_file = prescriptions_file
        self.stock_file = stock_file

    # def startMenu
    def header(self, str):
        if str == 1:
            str == "order"
        elif str == "2":
            str == "analytics"
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
                print("analytics input ")
        except ValueError:
            print(MSG_WRONG_INPUT)

    # print cart
    def displayCart(self):
        stock = Stock.load("data/products.json")
        if len(stock.products) == 0:
            print("No product available")
            return
        print("--------------------------")
        print("Avalable products in Cart")
        print("--------------------------")
        sampleHeader = ["id", "name", "brand",
                        "quantity", "category", "desc", "price"]
        self.tableHead(sampleHeader)
        index = 0
        for p in self.stock.products:
            index += 1
            print(f"|{index:<15}{p}")
        print("---------------------------------------------------")

    def askforChoice(self) -> int:
        try:
            selectedInput = int(
                input("Enter the Id of the product or 0 to go back : "))
            if selectedInput >= len(self.stock.products) or selectedInput == 0:
                print("Please try again")
                return
            else:
                return selectedInput
        except ValueError:
            print(MSG_WRONG_INPUT)

    def orderMenu(self):
        try:
            self.header("order")
            print("1. Add to cart")
            print("2. Remove from cart")
            print("3. Clear cart")
            print("4. Checkout")
            print("0. Back")
            choice = int(input("Enter your choice: "))
            print(choice, "choice")
            # cart = Cart(self.stock)

            if choice == 1:
                # read the file
                self.displayCart()
                selectedInput = self.askforChoice()

                selectedProduct = self.stock.products[selectedInput]

                self.cart.add(selectedProduct.code, selectedProduct.quantity)

            elif choice == 2:
                self.displayCart()
                selectedInput = self.askforChoice()
                selectedProduct = self.stock.products[selectedInput]
                self.cart.remove(selectedProduct.code)
            elif choice == 3:
                self.cart.clear()
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
