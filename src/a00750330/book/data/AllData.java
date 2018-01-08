/**
 * 
 */
package a00750330.book.data;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import a00750330.book.ApplicationException;
import a00750330.book.io.BookReader;
import a00750330.book.io.CustomerReader;
import a00750330.book.io.PurchaseReader;

/**
 * @author scirka
 *
 */
public class AllData {

	private static final Logger LOG = LogManager.getLogger();

	private static Map<Long, Book> books;
	private static Map<Long, Customer> customers;
	private static Map<Long, Purchase> purchases;

	private AllData() {
	}

	/**
	 * @throws ApplicationException
	 * 
	 */
	public static void loadDataFromFile() throws ApplicationException {
		LOG.debug("loading the data");
		books = BookReader.read();
		customers = CustomerReader.read();
		purchases = PurchaseReader.read();
		LOG.debug("successfully loaded the data");
	}

	/**
	 * @return the customers
	 */
	public static Map<Long, Customer> getCustomers() {
		return customers;
	}

	/**
	 * @return the books
	 */
	public static Map<Long, Book> getBooks() {
		return books;
	}

	/**
	 * @return the purchases
	 */
	public static Map<Long, Purchase> getPurchases() {
		return purchases;
	}

}
