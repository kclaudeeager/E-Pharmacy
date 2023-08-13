from __future__ import annotations

import json
from datetime import datetime

from typing import List

from . import Prescription
from .sale import Sale


# Claude


class BookRecords:
    """A record of all the sales made through the application.

    Attributes:
        transactions: a list of the transactions
    """

    def __init__(self, transactions: List[Sale]) -> None:
        self.transactions = transactions

    def __str__(self) -> str:
        """Returns a string representation of a record.

        Args:

        Returns: A string
        """

        # TODO: In the format below, return a representation of the records
        # |      # | Date                | Customer   | Medication | Quantity | Purchase Price | Prescription |
        # |      1 | 2023-06-03 21:23:25 | doe        | Quinine    |        3 |       1400 RWF | PHA1         |

        header = "|   # |       Date      | Customer | Medication | Quantity | Purchase Price | Prescription|"
        header += "\n"
        stringed_transactions = []
        for index, transaction in enumerate(self.transactions, start=1):
            stringed_transactions.append(stringify_transaction(index, transaction))

        return header + "\n".join(stringed_transactions)

    def reportOnPrescriptions(self) -> str:
        """Reports on prescription sales.

        Args: 

        Returns: A string report of the prescriptions processed
        """
        # TODO: From the transactions data, retrieve for each prescription, the actual medications that were processed
        # and aggregate for each, the corresponding total price.

        # TODO: output in the following format, the results:
        # |    # | Prescription ID | Total Price |
        # |    1 | PHA1            |       1400 RWF |

        header = "|    # | Prescription ID | Total Price |"
        header += "\n"
        stringed_transactions = []
        processed_transactions_dicts = []

        for index, transaction in enumerate(self.transactions, start=1):
            processed_transactions_dict = process_transaction_on_prescription(transaction)
            if processed_transactions_dict:
                processed_transactions_dicts.append(processed_transactions_dict)

        # Create a string representation for each prescription entry
        for index, processed_transaction in enumerate(processed_transactions_dicts, start=1):
            stringed_transaction = f"| {index:4d} | {processed_transaction['Prescription ID']:16s} | {processed_transaction['Total Price']:11.2f} RWF |"
            stringed_transactions.append(stringed_transaction)

        return header + "\n".join(stringed_transactions)

    def purchasesByUser(self, customerID: str):
        """Reports on the sales performed by a customer.

        Args:
            customerID: Username of the customer.

        Returns: A string representation of the corresponding transactions.
        """
        return self.reportSales('customerID', customerID, "No purchases found for this customer.")

    def salesByAgent(self, salesperson: str):
        """Reports on the sales performed by a pharmacist.

        Args:
            salesperson: Username of the pharmacist.

        Returns: A string representation of the corresponding transactions.
        """
        return self.reportSales('salesperson', salesperson, "No purchases found for this SalesPerson.")

    def reportSales(self, filter_key: str, filter_value: str, no_results_msg: str):
        """Reports on the sales based on a specific filter either customer or salesPerson.

        Args:
            filter_key: Key to filter transactions (e.g., 'customerID', 'salesperson').
            filter_value: Value to use for filtering.
            no_results_msg: Message to return when no purchases are found.

        Returns: A formatted string representation of the corresponding transactions.
        """
        transactions = [trans for trans in self.transactions if getattr(trans, filter_key) == filter_value]
        if not transactions:
            return no_results_msg

        header = "|   # |       Date      | Medication | Quantity | Purchase Price | Prescription |"
        header += "\n"
        stringed_transactions = []

        for index, transaction in enumerate(transactions, start=1):
            stringed_transaction = f"| {index:4d} | {transaction.timestamp} | {transaction.name:10s} | {transaction.quantity:8d} | {transaction.price:13.2f} RWF | {transaction.prescriptionID:13s} |"
            stringed_transactions.append(stringed_transaction)

        return header + "\n".join(stringed_transactions)

    def topNSales(self, start: datetime = datetime.strptime('1970-01-02', '%Y-%m-%d'), end: datetime = datetime.now(),
                  n=10) -> str:
        """Return the top n sales ordered by the total price of purchases.

        Args:
            start: a datetime representing the start period to consider (datetime, default to 01 Jan 1970)
            end: a datetime representing the end period to consider (datetime, default to current timestamp)
            n: number of records to consider (int, default to 10)

        Returns:
        A string representation of the top n 
        """
        # TODO: Query the top transactions and save them to the variable `transactions` below
        transactions = None
        # return the string representation of the transactions.
        transactions = [
            transaction for transaction in self.transactions
            if start.timestamp() <= transaction.timestamp <= end.timestamp()
        ]
        if not transactions:
            return "No sales found."

        top_transactions = sorted(
            transactions,
            key=lambda transaction: transaction.price * transaction.quantity, reverse=True
        )[:n]

        header = "|   # |       Date      | Medication | Quantity | Total Price | Prescription |"
        header += "\n"
        stringed_transactions = []

        for index, transaction in enumerate(top_transactions, start=1):
            total_price = transaction.price * transaction.quantity
            stringed_transaction = f"| {index:4d} | {transaction.timestamp} | {transaction.name:10s} | {transaction.quantity:8d} | {total_price:11.2f} RWF | {transaction.prescriptionID:13s} |"
            stringed_transactions.append(stringed_transaction)

        return header + "\n".join(stringed_transactions)

    def totalTransactions(self) -> float:
        """Returns the total cost of the transactions considered.

        Args:

        Returns: A floating number representing the total price
        """
        return sum([transaction.purchase_price for transaction in self.transactions])

    @classmethod
    def load(cls, infile: str) -> BookRecords:
        """Loads a JSON file containing a number of sales object

        Args:
            infile: path to the file to be read
        Returns: A new object with the transactions in the file
        """
        # TODO: Implement the function. Make sure to handle the cases where
        # the file does not exist.
        try:
            with open(infile, 'r') as f:
                data = json.load(f)
                transactions = []
                for transaction in data:
                    transactions.append(Sale.from_dict(transaction))
                return BookRecords(transactions)

        except FileNotFoundError:
            raise FileNotFoundError(f"Unable to load data from file: {infile}")
        except json.JSONDecodeError:
            raise ValueError(f"Error decoding JSON data in file: {infile}")


def stringify_transaction(index, transaction):
    """Returns a string representation of a transaction.

    Args:
        index (int): The index of the transaction
        transaction (Sale): The transaction to be formatted

    Returns: A string
    """
    # TODO: In the format below, return a representation of the transactions
    # |# 1| 2023-06-03 21:23:25 | doe        | Quinine    |        3 |       1400 RWF | PHA1         |
    return f"|# {index:4d} | {str(transaction.timestamp):19s} | {transaction.customerID:9s} | {transaction.name:10s} | {transaction.quantity:8d} | {transaction.price:13.2f} RWF | {transaction.prescriptionID:12s} |"


def process_transaction_on_prescription(transaction):
    """Process a transaction and return relevant data for the report.

    Args:
        transaction: The transaction to be processed.

    Returns: A dictionary with processed data or an empty dictionary if not applicable.
    """
    processed_transaction = {}
    prescription = Prescription.get("data/prescription.json", transaction.prescriptionID)

    if prescription is not None:
        total_processed_medications_amount = 0
        for medication in prescription.medications:
            if medication.get("ProcessedStatus", False):
                total_processed_medications_amount += medication.get("price", 0)

        if total_processed_medications_amount > 0:
            processed_transaction["Prescription ID"] = transaction.prescriptionID
            processed_transaction["Total Price"] = total_processed_medications_amount

    return processed_transaction
