/**
 * 
 */
package a00750330.database;

/**
 * @author scirka
 *
 */
public interface DbConstants {

	String DB_PROPERTIES_FILENAME = "db.properties";
	String DB_DRIVER_KEY = "db.driver";
	String DB_URL_KEY = "db.url";
	String DB_USER_KEY = "db.user";
	String DB_PASSWORD_KEY = "db.password";
	String TABLE_ROOT = "A00750330_";
	String CUSTOMERS_TABLE_NAME = TABLE_ROOT + "Customers";
	String BOOKS_TABLE_NAME = TABLE_ROOT + "Books";
	String PURCHASES_TABLE_NAME = TABLE_ROOT + "Purchases";
}
