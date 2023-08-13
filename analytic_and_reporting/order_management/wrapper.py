import json
import uuid
from datetime import datetime

from . import Sale
from .cart import Cart
from .prescription import Prescription
from .stock import Stock


# Claude
# would need to create a new object for each new order


class Wrapper:
    """
    Main class used to manage orders and carts.

    Attributes:
        sales: A list of the sales done during the program's execution
        stock: The stock used in the execution
        agentID: the username of the pharmacist running the program
    """

    def __init__(self, stock: Stock, agentID: str) -> None:
        self.sales = []
        self.stock = stock
        self.agentID = agentID

    def checkout(self, cart: Cart, customerID: str, prescription: Prescription = None):
        """Handles the checkout procedure of the program.

        Args:
            customerID:
            cart: The cart to pay for
            prescription: the prescription that accompanies the order (default: None)
        """

        # TODO: First check that all the product that require a prescription have all the criteria met
        # (i.e., (1) there is a prescription that (2) matches the customer's ID, and (3) contains the medication
        # in the specified quantity).
        # Raise an exception if either of those conditions is unmet.
        conditions_met = True if prescription is not None and prescription.CustomerID == customerID and len(
            prescription.Medications) > 0 else False
        if not conditions_met:
            raise Exception("The prescription does not match the customer's ID or does not contain the medication.")

        # TODO: Get the current datetime and save a Sale information for each product sold with the following schema
        #  {"name": "<name>", "quantity": <quantity>, "price": <unit price>, "purchase_price": <total price>,
        #  "timestamp": <timestamp>, "customerID": <customer username>, "salesperson": <pharmacist username>}

        # TODO: Append the list to the current sales

        # TODO: Make sure that the sold products are marked as complete in the prescriptions.

        current_datetime = datetime.datetime.now()
        sales_data = []
        sale_id = str(uuid.uuid4())
        for product_code, quantity in cart.products.items():
            if quantity > 0:
                product_info = self.stock.getProductByID(product_code)
                sales_data.append(Sale(
                    name=product_info.name,
                    quantity=quantity,
                    price=product_info.price,
                    purchase_price=product_info.price * quantity,
                    timestamp=current_datetime,
                    customerID=customerID,
                    salesperson=self.agentID,
                    id=sale_id,
                    prescriptionID=prescription.PrescriptionID
                ))
                prescription.markComplete(product_info)

        self.sales.extend(sales_data)

    def dump(self, outfile: str):
        """Dumps the current sales data to a file

        Args:
            outfile: the path to the output file
        """
        # TODO: Load the content, if any of the existing file

        # TODO: Update the content by appending the new entries to it, and save to the file
        existing_sales = []
        try:
            with open(outfile, 'r') as f:
                existing_sales = json.load(f)
        except FileNotFoundError:
            pass

        updated_sales = existing_sales + [sale.__dict__ for sale in self.sales]
        with open(outfile, 'w') as f:
            json.dump(updated_sales, f, indent=4)
            f.close()
