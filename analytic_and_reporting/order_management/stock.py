import json
from typing import List
from .product import Product


class Stock:
    """Represents the catalog of products

    Attributes:
        products: the list of products
    """

    def __init__(self, products: List[Product]) -> None:
        self.products = products

    def update(self, code: int, change: int):
        """Update the quantity of a product by adding or removing

        Args:
            code: identifier of the product
            change: the value by which the quantity should be updated (+1 adds 1, -2 removes 2 for example)
        """
        # TODO: Make sure the product exists, and that by making the change, the value is still >= 0

        # TODO: Update the quantity
        product_to_update = next(
            (product for product in self.products if product.code == code), None)

        if product_to_update is None:
            raise Exception("Product not found")

        new_quantity = product_to_update.quantity + change

        if new_quantity < 0:
            raise Exception("Negative quantity not allowed")
        self.products.remove(product_to_update)
        product_to_update.quantity = new_quantity
        self.products.append(product_to_update)
        return True

    def get_product_by_id(self, code: int) -> Product:
        """Gets a product by its ID

        Args:
            code: identifier of the product

        Returns: the product's object
        """
        # TODO: Implement te function
        return next((product for product in self.products if product.code == code), None)

    def dump(self, outfile: str):
        """Saves the stock to a JSON file"""
        # TODO: Implement the function
        with open(outfile, "w") as f:
            json.dump(self.products, f)
            return True

    @staticmethod
    def load(infile: str):
        """Loads the stock from an existing file

        Args:
            infile: input file to the function
        """
        # TODO: Implement the function
        stock = Stock([])
        with open(infile, "r") as f:
            json_data = json.load(f)
            for line in json_data:
                product = Product(
                    line["code"], line["name"], line["brand"], line["description"], line["quantity"],
                    line["price"], line["dosage_instruction"], line["requires_prescription"], line["category"]
                )
                stock.products.append(product)
            return stock

    def __str__(self) -> str:
        """Returns a string representation of the stock
        """
        # TODO: Return the description of the stock with a nice output showing the ID, Name, Brand, Description,
        #  Quantity, Price, and the requires_prescription field
        return str(self.products)
