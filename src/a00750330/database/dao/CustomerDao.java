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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import a00750330.book.ApplicationException;
import a00750330.book.data.Customer;
import a00750330.database.Database;
import a00750330.database.DbConstants;

/**
 * @author Hsiu-Man Angela Wu, A00750330
 *
 */
public class CustomerDao extends Dao {

	public static final String TABLE_NAME = DbConstants.CUSTOMERS_TABLE_NAME;
	
	private static int cusNumber = 1;

	private static final Logger LOG = LogManager.getLogger();

	/**
	 * @param database Database object
	 */
	public CustomerDao(Database database) {
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
				+ "%s INT, " // CUSTOMER_ID
				+ "%s VARCHAR(20), " // FIRST_NAME
				+ "%s VARCHAR(20), " // LAST_NAME
				+ "%s VARCHAR(60), " // STREET
				+ "%s VARCHAR(30), " // CITY
				+ "%s VARCHAR(10), " // POSTAL_CODE
				+ "%s VARCHAR(14), " // PHONE
				+ "%s VARCHAR(50), " // EMAIL
				+ "%s DATE, " // JOIN_DATE
				+ "primary key (%s) )", // CUSTOMER_ID
				tableName, // table
				Fields.CUSTOMER_ID.getName(), // 2
				Fields.FIRST_NAME.getName(), // 3
				Fields.LAST_NAME.getName(), // 4
				Fields.STREET.getName(), // 5
				Fields.CITY.getName(), // 6
				Fields.POSTAL_CODE.getName(), // 7
				Fields.PHONE.getName(), // 8
				Fields.EMAIL.getName(), // 9
				Fields.JOIN_DATE.getName(), // 10
				Fields.CUSTOMER_ID.getName()); // 11
		LOG.debug(sql);
		super.create(sql);
	}

	/**
	 * @param customer Customer object
	 * @throws SQLException SQL Exception
	 */
	public void add(Customer customer) throws SQLException {
		Statement statement = null;
		try {
			Connection connection = database.getConnection();
			statement = connection.createStatement();
			String sql = String.format("insert into %s values(" // 1 tableName
					+ "'%s', " // 2 StudentId
					+ "'%s', " // 3 FirstName
					+ "'%s', " // 4 LastName
					+ "'%s', " // 5 Street
					+ "'%s', " // 6 City
					+ "'%s', " // 7 Postal Code
					+ "'%s', " // 8 Phone
					+ "'%s', " // 9 Email
					+ "'%s')", // 10 Joined Date

					tableName, // 1
					customer.getId(), // 2
					customer.getFirstName(), // 3
					customer.getLastName(), // 5
					customer.getStreet(), // 6
					customer.getCity(), // 6
					customer.getPostalCode(), // 7
					customer.getPhone(), // 8
					customer.getEmailAddress(), // 9
					customer.getJoinedDate()); // 10
			LOG.debug(cusNumber++ + " " + sql);
			statement.executeUpdate(sql);
		} finally {
			close(statement);
		}
	}

	/**
	 * @param ids Customer's Id
	 * @return Customer Object
	 * @throws SQLException SQL Exception
	 * @throws ApplicationException Application Exception
	 */
	public Customer getCustomer(Long ids) throws SQLException, ApplicationException {
		Connection connection;
		Statement statement = null;
		Customer customer = null;
		try {
			connection = database.getConnection();
			statement = connection.createStatement();
			// Execute a statement
			String sql = String.format("SELECT * FROM %s WHERE %s = '%s'", tableName, Fields.CUSTOMER_ID.getName(),
					ids);
			LOG.debug(sql);
			ResultSet resultSet = statement.executeQuery(sql);

			// get the Customer
			// throw an exception if we get more than one result
			int count = 0;
			while (resultSet.next()) {
				count++;
				if (count > 1) {
					throw new ApplicationException(String.format("Expected one result, got %d", count));
				}
				customer = new Customer.Builder(ids, null).build();
				customer.setFirstName(resultSet.getString(Fields.FIRST_NAME.getName()));
				customer.setLastName(resultSet.getString(Fields.LAST_NAME.getName()));
				customer.setStreet(resultSet.getString(Fields.STREET.getName()));
				customer.setCity(resultSet.getString(Fields.CITY.getName()));
				customer.setPostalCode(resultSet.getString(Fields.POSTAL_CODE.getName()));
				customer.setPhone(resultSet.getString(Fields.PHONE.getName()));
				customer.setEmailAddress(resultSet.getString(Fields.EMAIL.getName()));
				customer.setJoinedDate(LocalDate.parse(resultSet.getString(Fields.JOIN_DATE.getName())));
			}
		} finally {
			close(statement);
		}

		return customer;
	}

	/**
	 * @return List<String> 
	 * @throws SQLException SQL Exception
	 */
	public List<Long> getIds() throws SQLException {
		Connection connection;
		Statement statement = null;
		List<Long> customerIds = new ArrayList<>();
		try {
			connection = database.getConnection();
			statement = connection.createStatement();
			String sql = String.format("SELECT %s FROM %s", Fields.CUSTOMER_ID.getName(),
					DbConstants.CUSTOMERS_TABLE_NAME);
			LOG.debug("Getting all customer IDs from the database:");
			LOG.debug(sql);
			ResultSet resultSet = statement.executeQuery(sql);

			while (resultSet.next()) {
				customerIds.add((resultSet.getLong(Fields.CUSTOMER_ID.getName())));
			}

		} finally {
			close(statement);
		}

		return customerIds;

	}
	
	
	
	public List<Customer> getAllCustomers() throws SQLException {
		Connection connection;
		Statement statement = null;
		List<Customer> customers = new ArrayList<>();
		try {
			connection = database.getConnection();
			statement = connection.createStatement();
			String sql = String.format("SELECT * FROM %s", DbConstants.CUSTOMERS_TABLE_NAME);
			LOG.debug("Getting all customers from the database:");
			LOG.debug(sql);
			ResultSet resultSet = statement.executeQuery(sql);

			while (resultSet.next()) {
				Customer customer = new Customer.Builder(resultSet.getLong(Fields.CUSTOMER_ID.getName()), resultSet.getString(Fields.PHONE.getName()))
						.setCity(resultSet.getString(Fields.CITY.getName()))
						.setEmailAddress(resultSet.getString(Fields.EMAIL.getName()))
						.setFirstName(resultSet.getString(Fields.FIRST_NAME.getName()))
						.setLastName(resultSet.getString(Fields.LAST_NAME.getName()))
						.setJoinedDate(LocalDate.parse(resultSet.getString(Fields.JOIN_DATE.getName())))
						.setPostalCode(resultSet.getString(Fields.POSTAL_CODE.getName()))
						.setStreet(resultSet.getString(Fields.STREET.getName()))
						.build();
				
				customers.add(customer);
				
				LOG.debug("Adding customer to List<Customer> : " + customer);
			
			}

		} finally {
			close(statement);
		}

		return customers;

	}
	

	/**
	 * @param customer Customer Object
	 * @throws SQLException SQL Exception
	 */
	public void update(Customer customer) throws SQLException {
		Connection connection;
		Statement statement = null;
		try {
			connection = database.getConnection();
			statement = connection.createStatement();
			// Execute a statement
			String sql = String.format(
					"UPDATE %s set %s='%s', %s='%s', %s='%s', %s='%s', %s='%s', %s='%s', %s='%s', %s='%s', %s='%s' WHERE %s='%s'",
					tableName, //
					Fields.CUSTOMER_ID.getName(), customer.getId(), // 2
					Fields.FIRST_NAME.getName(), customer.getFirstName(), // 3
					Fields.LAST_NAME.getName(), customer.getLastName(), // 4
					Fields.STREET.getName(), customer.getStreet(), // 5
					Fields.CITY.getName(), customer.getCity(), // 6
					Fields.POSTAL_CODE.getName(), customer.getPostalCode(), // 7
					Fields.PHONE.getName(), customer.getPhone(), // 8
					Fields.EMAIL.getName(), customer.getEmailAddress(), // 9
					Fields.JOIN_DATE.getName(), customer.getJoinedDate(), // 10
					Fields.CUSTOMER_ID.getName(), customer.getId()); // 11
			LOG.debug(sql);
			int rowcount = statement.executeUpdate(sql);
			LOG.debug(String.format("Updated %d rows", rowcount));
		} finally {
			close(statement);
		}
	}

	/**
	 * @param customer Customer object
	 * @throws SQLException SQL Exception
	 */
	public void delete(Customer customer) throws SQLException {
		Connection connection;
		Statement statement = null;
		try {
			connection = database.getConnection();
			statement = connection.createStatement();
			// Execute a statement
			String sql = String.format("DELETE from %s WHERE %s='%s'", tableName, Fields.CUSTOMER_ID.getName(),
					customer.getId());
			LOG.debug(sql);
			int rowcount = statement.executeUpdate(sql);
			LOG.debug(String.format("Deleted %d rows", rowcount));
		} finally {
			close(statement);
		}
	}

	public enum Fields {

		CUSTOMER_ID("customerId", "VARCHAR", 4, 1), //
		FIRST_NAME("firstName", "VARCHAR", 20, 2), //
		LAST_NAME("lastName", "VARCHAR", 20, 3), //
		STREET("street", "VARCHAR", 40, 4), //
		CITY("city", "VARCHAR", 20, 5), //
		POSTAL_CODE("postalCode", "VARCHAR", 7, 6), //
		PHONE("phone", "VARCHAR", 14, 7), //
		EMAIL("email", "VARCHAR", 40, 8), //
		JOIN_DATE("joinDate", "DATE", 8, 9);

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
