/**
 * Project: A00750330_lab07
 * File: CustomerDao.java
 * Date: Oct 30, 2017
 * Time: 12:55:29 AM
 */
package a00750330.database.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import a00750330.book.ApplicationException;
import a00750330.book.data.Purchase;
import a00750330.database.Database;
import a00750330.database.DbConstants;
import a00750330.ui.PurchaseDialog.Item;

/**
 * @author Hsiu-Man Angela Wu, A00750330
 *
 */
public class PurchaseDao extends Dao {

	public static final String TABLE_NAME = DbConstants.PURCHASES_TABLE_NAME;

	private static final Logger LOG = LogManager.getLogger();

	/**
	 * @param database
	 *            Database object
	 */
	public PurchaseDao(Database database) {
		super(database, TABLE_NAME);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see a00750330.database.dao.Dao#create()
	 */
	@Override
	public void create() throws SQLException {
		String sql = String.format("create table %s(" // table
				+ "%s INT, " // PURCHASE_ID
				+ "%s INT, " // CUSTOMER_ID
				+ "%s INT, " // BOOK_ID
				+ "%s DECIMAL, " // PRICE
				+ "primary key (%s) )", // BOOK_ID
				tableName, // 1
				Fields.PURCHASE_ID.getName(), //
				Fields.CUSTOMER_ID.getName(), //
				Fields.BOOK_ID.getName(), //
				Fields.PRICE.getName(), //
				Fields.PURCHASE_ID.getName()); //
		LOG.debug(sql);
		super.create(sql);
	}

	/**
	 * @param purchase
	 *            Purchase object
	 * @throws SQLException
	 *             SQL Exception
	 */
	public void add(Purchase purchase) throws SQLException {
		Statement statement = null;
		try {
			Connection connection = database.getConnection();
			statement = connection.createStatement();
			String sql = String.format("insert into %s values(" // 1 tableName
					+ "'%s', " // 2 PURCHASE_ID
					+ "'%s', " // 3 CUSTOMER_ID
					+ "'%s', " // 4 BOOK_ID
					+ "'%s')", // 5 PRICE

					tableName, // 1
					purchase.getId(), // 2
					purchase.getCustomerId(), // 3
					purchase.getBookId(), // 4
					purchase.getPrice()); // 5
			LOG.debug(sql);
			statement.executeUpdate(sql);
		} finally {
			close(statement);
		}
	}

	/**
	 * @param purchaseId
	 *            Purchase's Id
	 * @return Purchase Object
	 * @throws SQLException
	 *             SQL Exception
	 * @throws ApplicationException
	 *             Application Exception
	 */
	public Purchase getPurchase(Long purchaseId) throws SQLException, ApplicationException {
		Connection connection;
		Statement statement = null;
		Purchase purchase = null;
		try {
			connection = database.getConnection();
			statement = connection.createStatement();
			// Execute a statement
			String sql = String.format("SELECT * FROM %s WHERE %s = '%s'", tableName, Fields.PURCHASE_ID.getName(),
					purchaseId);
			LOG.debug(sql);
			ResultSet resultSet = statement.executeQuery(sql);

			// get the Purchase
			// throw an exception if we get more than one result
			int count = 0;
			while (resultSet.next()) {
				count++;
				if (count > 1) {
					throw new ApplicationException(String.format("Expected one result, got %d", count));
				}

				long customerId = resultSet.getLong(Fields.CUSTOMER_ID.getName());
				long bookId = resultSet.getLong(Fields.BOOK_ID.getName());
				float price = resultSet.getFloat(Fields.PRICE.getName());

				purchase = new Purchase.Builder(purchaseId, customerId, bookId, price).build();
			}
		} finally {
			close(statement);
		}

		return purchase;
	}

	/**
	 * @return List<String>
	 * @throws SQLException
	 *             SQL Exception
	 */
	public List<Long> getIds() throws SQLException {
		Connection connection;
		Statement statement = null;
		List<Long> purchaseIds = new ArrayList<>();
		try {
			connection = database.getConnection();
			statement = connection.createStatement();
			String sql = String.format("SELECT %s FROM %s", Fields.PURCHASE_ID.getName(),
					DbConstants.PURCHASES_TABLE_NAME);
			LOG.debug("Getting all customer IDs from the database:");
			LOG.debug(sql);
			ResultSet resultSet = statement.executeQuery(sql);

			while (resultSet.next()) {
				purchaseIds.add(resultSet.getLong(Fields.PURCHASE_ID.getName()));
			}

		} finally {
			close(statement);
		}

		return purchaseIds;

	}

	public List<Item> getPurchaseDetails() throws SQLException, ApplicationException {
		Connection connection;
		Statement statement = null;
		List<Item> itemsList = new ArrayList<Item>();

		try {
			connection = database.getConnection();
			statement = connection.createStatement();

			String sql = "SELECT * FROM A00750330_Purchases LEFT JOIN A00750330_Customers ON A00750330_Purchases.customerId = A00750330_Customers.customerId LEFT JOIN A00750330_Books ON A00750330_Purchases.bookId = A00750330_Books.bookId";

			LOG.debug(sql);
			ResultSet resultSet = statement.executeQuery(sql);
			// LOG.debug("ResultSet: " + resultSet);

			while (resultSet.next()) {

				Item item = new Item();

				item.setFirstName(resultSet.getString("firstName")) //
						.setLastName(resultSet.getString("lastName")) //
						.setTitle(resultSet.getString("title")) //
						.setPrice(resultSet.getFloat("price")) //
						.setCustomerId(resultSet.getLong("customerId"));

				itemsList.add(item);

			}
		} finally {
			close(statement);

		}

		return itemsList;

	}

	public List<Item> getPurchaseDetailsById(Long customerId) throws SQLException, ApplicationException {
		Connection connection;
		Statement statement = null;
		List<Item> itemsList = new ArrayList<Item>();

		try {
			connection = database.getConnection();
			statement = connection.createStatement();

			String sql = "SELECT * FROM A00750330_Purchases LEFT JOIN A00750330_Customers ON A00750330_Purchases.customerId = A00750330_Customers.customerId LEFT JOIN A00750330_Books ON A00750330_Purchases.bookId = A00750330_Books.bookId WHERE A00750330_Purchases.customerId = '"
					+ customerId + "'";

			LOG.debug(sql);
			ResultSet resultSet = statement.executeQuery(sql);
			// LOG.debug("ResultSet: " + resultSet);

			while (resultSet.next()) {

				Item item = new Item();

				item.setFirstName(resultSet.getString("firstName")) //
						.setLastName(resultSet.getString("lastName")) //
						.setTitle(resultSet.getString("title")) //
						.setPrice(resultSet.getFloat("price"))
						.setCustomerId(resultSet.getLong("customerId"));
				

				itemsList.add(item);

			}
		} finally {
			close(statement);

		}

		return itemsList;

	}

	/**
	 * @param purchase
	 *            Purchase Object
	 * @throws SQLException
	 *             SQL Exception
	 */
	public void update(Purchase purchase) throws SQLException {
		Connection connection;
		Statement statement = null;
		try {
			connection = database.getConnection();
			statement = connection.createStatement();
			// Execute a statement
			String sql = String.format(
					"UPDATE %s set %s='%s', %s='%s', %s='%s', %s='%s', %s='%s', %s='%s', %s='%s', %s='%s', %s='%s' WHERE %s='%s'",
					tableName, //
					Fields.PURCHASE_ID.getName(), purchase.getId(), // 2
					Fields.CUSTOMER_ID.getName(), purchase.getCustomerId(), // 3
					Fields.BOOK_ID.getName(), purchase.getBookId(), // 4
					Fields.PRICE.getName(), purchase.getPrice()); // 5

			LOG.debug(sql);
			int rowcount = statement.executeUpdate(sql);
			LOG.debug(String.format("Updated %d rows", rowcount));
		} finally {
			close(statement);
		}
	}

	/**
	 * @param customer
	 *            Customer object
	 * @throws SQLException
	 *             SQL Exception
	 */
	public void delete(Purchase purchase) throws SQLException {
		Connection connection;
		Statement statement = null;
		try {
			connection = database.getConnection();
			statement = connection.createStatement();
			// Execute a statement
			String sql = String.format("DELETE from %s WHERE %s='%s'", tableName, Fields.PURCHASE_ID.getName(),
					purchase.getId());
			LOG.debug(sql);
			int rowcount = statement.executeUpdate(sql);
			LOG.debug(String.format("Deleted %d rows", rowcount));
		} finally {
			close(statement);
		}
	}

	public enum Fields {

		PURCHASE_ID("id", "VARCHAR", 3, 1), //
		CUSTOMER_ID("customerId", "VARCHAR", 20, 2), //
		BOOK_ID("bookId", "VARCHAR", 20, 3), //
		PRICE("price", "YEAR", 4, 4); //

		private final String name;
		private final String type;
		private final int length;
		private final int column;

		Fields(String name, String type, int length, int column) {
			this.name = name;
			this.type = type;
			this.length = length;
			this.column = column;
		}

		/**
		 * @return type of the enum constant
		 */
		public String getType() {
			return type;
		}

		/**
		 * @return name of the enum constant
		 */
		public String getName() {
			return name;
		}

		/**
		 * @return length of the enum constant
		 */
		public int getLength() {
			return length;
		}

		/**
		 * @return column of the enum constant
		 */
		public int getColumn() {
			return column;
		}
	}
}
