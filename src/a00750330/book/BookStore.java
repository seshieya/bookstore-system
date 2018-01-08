package a00750330.book;

import java.awt.EventQueue;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.Properties;

import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import org.apache.commons.cli.ParseException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.core.config.DefaultConfiguration;

import a00750330.book.ApplicationException;
import a00750330.book.data.AllData;
import a00750330.book.data.Book;
import a00750330.book.data.Customer;
import a00750330.book.data.Purchase;
import a00750330.database.Database;
import a00750330.database.DbConstants;
import a00750330.database.dao.CustomerDao;
import a00750330.database.dao.Dao;
import a00750330.database.dao.PurchaseDao;
import a00750330.database.dao.BookDao;
import a00750330.ui.MainFrame;
import a00750330.ui.UiMethodsForListeners;

/**
 * Project: Books
 * File: BookStore.java
 */

/**
 * @author Sam Cirka, A00123456
 *
 */
public class BookStore {

	private Database database;
	private CustomerDao customerDao;
	private BookDao bookDao;
	private PurchaseDao purchaseDao;

	private static final String LOG4J_CONFIG_FILENAME = "log4j2.xml";
	static {
		configureLogging();
	}
	private static final Logger LOG = LogManager.getLogger();

	/**
	 * Book Constructor. Processes the commandline arguments ex. -inventory
	 * -make=honda -by_count -desc -total -service
	 * 
	 * @throws ApplicationException
	 * @throws ParseException
	 */
	public BookStore(String[] args) throws ApplicationException {
		LOG.debug("Input args: " + Arrays.toString(args));
		BookOptions.process(args);
	}

	public BookStore() throws ApplicationException {
	}

	/**
	 * Entry point to GIS
	 * 
	 * @param args
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public static void main(String[] args) {
		LOG.info("Starting Books");
		Instant startTime = Instant.now();
		LOG.info(startTime);

		BookStore bookStore = null;

		// start the Bookstore System
		try {
			bookStore = new BookStore();
			bookStore.run();
		} catch (ApplicationException | IOException | SQLException e) {
			LOG.debug(e);

			JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);

		} finally {
			bookStore.createUI();

		}

		Instant endTime = Instant.now();
		LOG.info(endTime);
		LOG.info(String.format("Duration: %d ms", Duration.between(startTime, endTime).toMillis()));
		LOG.info("BookStore has stopped");
	}

	/**
	 * Configures log4j2 from the external configuration file specified in
	 * LOG4J_CONFIG_FILENAME. If the configuration file isn't found then log4j2's
	 * DefaultConfiguration is used.
	 */
	private static void configureLogging() {
		ConfigurationSource source;
		try {
			source = new ConfigurationSource(new FileInputStream(LOG4J_CONFIG_FILENAME));
			Configurator.initialize(null, source);
		} catch (IOException e) {
			System.out.println(String.format(
					"WARNING! Can't find the log4j logging configuration file %s; using DefaultConfiguration for logging.",
					LOG4J_CONFIG_FILENAME));
			Configurator.initialize(new DefaultConfiguration());
		}
	}

	/**
	 * @throws ApplicationException
	 * @throws SQLException
	 * @throws FileNotFoundException
	 * 
	 */
	private void run() throws ApplicationException, IOException, SQLException {
		LOG.debug("run()");

		AllData.loadDataFromFile();

		database = connect();
		customerDao = new CustomerDao(database);
		bookDao = new BookDao(database);
		purchaseDao = new PurchaseDao(database);

		loadDataToDb(database);

		closeDatabaseConnection(database);

	}

	private Database connect() throws ApplicationException, IOException, SQLException {
		Properties properties = new Properties();
		File dbProperties = new File(DbConstants.DB_PROPERTIES_FILENAME);

		properties.load(new FileReader(dbProperties));
		Database db = new Database(properties);

		db.getConnection();

		return db;
	}

	private void loadDataToDb(Database db) throws SQLException {

		// load Customers
		if (!Database.tableExists(DbConstants.CUSTOMERS_TABLE_NAME)) {
			createTable(customerDao);
			LOG.debug("Customers table created");

			insertCustomers(customerDao);
		} else {
			LOG.debug("Existing " + DbConstants.CUSTOMERS_TABLE_NAME + " table found.");
		}

		// load Books
		if (!Database.tableExists(DbConstants.BOOKS_TABLE_NAME)) {
			createTable(bookDao);
			LOG.debug("Books table created");

			insertBooks(bookDao);

		} else {
			LOG.debug("Existing " + DbConstants.BOOKS_TABLE_NAME + " table found.");
		}

		// load Purchases
		if (!Database.tableExists(DbConstants.PURCHASES_TABLE_NAME)) {
			createTable(purchaseDao);
			LOG.debug("Purchases table created");

			insertPurchases(purchaseDao);

		} else {
			LOG.debug("Existing " + DbConstants.PURCHASES_TABLE_NAME + " table found.");
		}

	}

	private void createTable(Dao dao) throws SQLException {
		dao.create();
	}

	private void insertCustomers(CustomerDao customerDao) throws SQLException {

		for (Customer customerData : AllData.getCustomers().values()) {
			customerDao.add(customerData);
		}
		LOG.debug("Added all customers");
	}

	private void insertBooks(BookDao bookDao) throws SQLException {

		for (Book bookData : AllData.getBooks().values()) {
			bookDao.add(bookData);
		}
		LOG.debug("Added all books");
	}

	private void insertPurchases(PurchaseDao purchaseDao) throws SQLException {

		for (Purchase purchaseData : AllData.getPurchases().values()) {
			purchaseDao.add(purchaseData);
		}
		LOG.debug("Added all purchases");
	}

	private void closeDatabaseConnection(Database database) {
		database.shutdown();
	}

	public void createUI() {
		LOG.debug("Creating the UI");
		// set the Nimbus look and feel...
		try {
			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (Exception e) {
			// If Nimbus is not available, use the default.
		}

		// create and show the user interface
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					@SuppressWarnings("unused")
					UiMethodsForListeners listeners = new UiMethodsForListeners(database, customerDao, bookDao,
							purchaseDao);
					MainFrame mainFrame = new MainFrame();
					mainFrame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		LOG.debug("UI created");
	}
}
