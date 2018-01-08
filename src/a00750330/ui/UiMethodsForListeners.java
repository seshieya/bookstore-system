/**
 * Project: a00750330_Assignment2
 * File: UiMethodsForListeners.java
 * Date: Nov 22, 2017
 * Time: 9:49:43 PM
 */
package a00750330.ui;

import java.sql.SQLException;
import java.util.List;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import a00750330.book.ApplicationException;
import a00750330.book.data.Book;
import a00750330.book.data.Customer;
import a00750330.database.Database;
import a00750330.database.DbConstants;
import a00750330.database.dao.BookDao;
import a00750330.database.dao.CustomerDao;
import a00750330.database.dao.PurchaseDao;
import a00750330.ui.PurchaseDialog.Item;

/**
 * @author Hsiu-Man Angela Wu, A00750330
 *
 */
public class UiMethodsForListeners {

	private static final Logger LOG = LogManager.getLogger();

	private static Database database;
	private static CustomerDao customerDao;
	private static BookDao bookDao;
	private static PurchaseDao purchaseDao;

	/**
	 * 
	 */
	public UiMethodsForListeners(Database database, CustomerDao customerDao, BookDao bookDao, PurchaseDao purchaseDao) {
		UiMethodsForListeners.database = database;
		UiMethodsForListeners.customerDao = customerDao;
		UiMethodsForListeners.bookDao = bookDao;
		UiMethodsForListeners.purchaseDao = purchaseDao;
	}

	static void dropTables() throws SQLException {
		database.getConnection();

		int option = JOptionPane.showConfirmDialog(null,
				"Are you sure you want to drop all tables, and exit the program?", "Drop Tables",
				JOptionPane.YES_NO_OPTION);

		if (option == JOptionPane.YES_OPTION) {
			if (Database.tableExists(DbConstants.CUSTOMERS_TABLE_NAME)) {
				customerDao.drop();
				LOG.debug(DbConstants.CUSTOMERS_TABLE_NAME + " dropped");
			}
			if (Database.tableExists(DbConstants.BOOKS_TABLE_NAME)) {
				bookDao.drop();
				LOG.debug(DbConstants.BOOKS_TABLE_NAME + " dropped");
			}
			if (Database.tableExists(DbConstants.PURCHASES_TABLE_NAME)) {
				purchaseDao.drop();
				LOG.debug(DbConstants.PURCHASES_TABLE_NAME + " dropped");
			}

			database.shutdown();

			System.exit(0);

		} else {
			database.shutdown();

		}

	}

	static void quit() {
		System.exit(0);
	}

	static void countBooks() throws SQLException {
		// database.getConnection();

		List<Long> bookIds = bookDao.getIds();

		int bookCount = bookIds.size();

		JOptionPane.showMessageDialog(null, "Total book count: " + bookCount, "Book Count",
				JOptionPane.INFORMATION_MESSAGE);

		database.shutdown();
	}

	static void showListBooks(JCheckBoxMenuItem author, JCheckBoxMenuItem descending) throws Exception {
		List<Book> books = bookDao.getAllBooks();

		BookDialog dialog = new BookDialog();
		dialog.setBookList(books, author, descending);
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		dialog.setVisible(true);

		database.shutdown();

	}

	static void countCustomers() throws SQLException {
		// database.getConnection();

		List<Long> customerIds = customerDao.getIds();

		int customerCount = customerIds.size();

		JOptionPane.showMessageDialog(null, "Total customer count: " + customerCount, "Customer Count",
				JOptionPane.INFORMATION_MESSAGE);

		database.shutdown();
	}

	static void showListCustomers(JCheckBoxMenuItem joinDate) throws Exception {

		List<Customer> customersList = customerDao.getAllCustomers();

		try {
			CustomerDialog dialog = new CustomerDialog();
			dialog.setCustomerList(customersList, joinDate, customerDao);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			LOG.debug(e);
		}

		database.shutdown();

	}

	static void updateCustomer(Customer customer) {
		try {
			customerDao.update(customer);
		} catch (SQLException e) {
			LOG.debug(e);
		}

		database.shutdown();
	}

	static void totalPurchases() throws SQLException, ApplicationException {

		List<Item> itemsList = purchaseDao.getPurchaseDetails();

		float purchaseTotal = 0;

		for (Item items : itemsList) {
			float price = items.getPrice();

			purchaseTotal += price;
		}

		JOptionPane.showMessageDialog(null, "Total purchases: $" + purchaseTotal, "Purchase Total",
				JOptionPane.INFORMATION_MESSAGE);

		database.shutdown();
	}

	static void showListPurchases(JCheckBoxMenuItem lastName, JCheckBoxMenuItem title, JCheckBoxMenuItem descending)
			throws Exception {

		List<Item> purchaseDetailsList = purchaseDao.getPurchaseDetails();

		PurchaseDialog dialog = new PurchaseDialog();
		dialog.setPurchaseList(purchaseDetailsList, lastName, title, descending);
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		dialog.setVisible(true);

		database.shutdown();
	}

	static void filterListByCustomerId(JCheckBoxMenuItem lastName, JCheckBoxMenuItem title,
			JCheckBoxMenuItem descending) throws Exception {

		String customerId = JOptionPane.showInputDialog(null, "Enter Customer ID:", "Filter By Customer ID",
				JOptionPane.QUESTION_MESSAGE);
		boolean customerExists = false;

		if (customerId != null) {

			List<Item> purchaseDetailsList = purchaseDao.getPurchaseDetails();

			for (Item purchases : purchaseDetailsList) {
				if (Long.toString(purchases.getCustomerId()).equals(customerId)) {
					customerExists = true;
					LOG.debug("Customer " + customerId + " found");

					purchaseDetailsList = purchaseDao.getPurchaseDetailsById(Long.parseLong(customerId));
					LOG.debug("There are " + purchaseDetailsList.size() + " purchases found for customer " + customerId
							+ " : \n" + purchaseDetailsList);

					break;
				}
			}

			if (!customerExists) {
				LOG.debug("Customer " + customerId + " not found");
				JOptionPane.showMessageDialog(null,
						"The customer you entered does not exist, or you did not enter a customer. The full list of purchases will be displayed.",
						"Error", JOptionPane.ERROR_MESSAGE);
			}

			PurchaseDialog dialog = new PurchaseDialog();
			dialog.setPurchaseList(purchaseDetailsList, lastName, title, descending);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		}

		database.shutdown();
	}

}
