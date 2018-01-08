/**
 * Project: Books
 * File: CustomerReader.java
 */

package a00750330.book.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.DateTimeException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import a00750330.book.ApplicationException;
import a00750330.book.data.Customer;

/**
 * @author Sam Cirka, A00123456
 *
 */
public class CustomerReader extends Reader {

	public static final String FILENAME = "customers.dat";
	public static final String RECORD_DELIMITER = ":";
	public static final String FIELD_DELIMITER = "\\|";

	private static final Logger LOG = LogManager.getLogger();

	/**
	 * private constructor to prevent instantiation
	 */
	private CustomerReader() {
	}

	/**
	 * Read the customer input data.
	 * 
	 * @return A collection of customers.
	 * @throws ApplicationException
	 */
	
	//BELOW IS SAM'S METHOD:
	
	/*public static Map<Long, Customer> read() throws ApplicationException {
		File customerDataFile = new File(FILENAME);
		BufferedReader customerReader = null;
		LOG.debug("Reading" + customerDataFile.getAbsolutePath());

		Map<Long, Customer> customers = new HashMap<>();
		try {
			customerReader = new BufferedReader(new FileReader(customerDataFile));

			String line = null;
			line = customerReader.readLine(); // skip the header line
			while ((line = customerReader.readLine()) != null) {
				try {
					Customer customer = readCustomerString(line);
					customers.put(customer.getId(), customer);
					LOG.debug("Added " + customer.toString() + " as " + customer.getId());
				} catch (ApplicationException e) {
					LOG.error(e.getMessage());
				}
			}
		} catch (IOException e) {
			throw new ApplicationException(e.getMessage());
		} finally {
			try {
				if (customerReader != null) {
					customerReader.close();
				}
			} catch (IOException e) {
				throw new ApplicationException(e.getMessage());
			}
		}

		return customers;
	}*/
	
	
	//BELOW IS MY METHOD:
	public static Map<Long, Customer> read() throws ApplicationException {
		
		File customerDataFile = new File(FILENAME);
		
		Map<Long, Customer> customers = new HashMap<Long, Customer>();

		Scanner scanner;
		try {
			// throws FileNotFoundException at this line if file not found:
			scanner = new Scanner(customerDataFile, "UTF-8");
			LOG.info("Starting scanner");
		} catch (FileNotFoundException e) {
			throw new ApplicationException(e);
		}

		// skip first line:
		scanner.nextLine();

		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();
			Customer customer = CustomerReader.readCustomerString(line);
			customers.put(customer.getId(), customer);
			LOG.debug("Added Customer: " + customer);
		}

		scanner.close();
		LOG.info("Closing scanner");

		return customers;
	}

	/**
	 * Parse a Customer data string into a Customer object;
	 * 
	 * @param row
	 * @throws ApplicationException
	 */
	private static Customer readCustomerString(String data) throws ApplicationException {
		String[] elements = data.split(FIELD_DELIMITER);
		if (elements.length != Customer.ATTRIBUTE_COUNT) {
			throw new ApplicationException(String.format("Expected %d but got %d: %s", Customer.ATTRIBUTE_COUNT, elements.length, Arrays.toString(elements)));
		}

		int index = 0;
		long id = Integer.parseInt(elements[index++]);
		String firstName = elements[index++];
		String lastName = elements[index++];
		String street = elements[index++];
		String city = elements[index++];
		String postalCode = elements[index++];
		String phone = elements[index++];
		// should the email validation be performed here or in the customer class? I'm leaning towards putting it here.
		String emailAddress = elements[index++];
		if (!Validator.validateEmail(emailAddress)) {
			throw new ApplicationException(String.format("Invalid email: %s", emailAddress));
		}
		String yyyymmdd = elements[index];
		if (!Validator.validateJoinedDate(yyyymmdd)) {
			throw new ApplicationException(String.format("Invalid joined date: %s for customer %d", yyyymmdd, id));
		}
		int year = Integer.parseInt(yyyymmdd.substring(0, 4));
		int month = Integer.parseInt(yyyymmdd.substring(4, 6));
		int day = Integer.parseInt(yyyymmdd.substring(6, 8));

		Customer customer = null;
		try {
			customer = new Customer.Builder(id, phone).setFirstName(firstName).setLastName(lastName).setStreet(street).setCity(city).setPostalCode(postalCode)
					.setEmailAddress(emailAddress).setJoinedDate(year, month, day).build();
		} catch (DateTimeException e) {
			throw new ApplicationException(e.getMessage());
		}

		return customer;
	}

	private static class Validator {

		private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
		private static final String YYYYMMDD_PATTERN = "(20\\d{2})(\\d{2})(\\d{2})"; // valid for years 2000-2099

		private static Pattern emailPattern;
		private static Pattern yyyymmddPattern;

		private Validator() {
		}

		/**
		 * Validate an email string.
		 * 
		 * @param email
		 *            the email string.
		 * @return true if the email address is valid, false otherwise.
		 */
		public static boolean validateEmail(final String email) {
			if (emailPattern == null) {
				emailPattern = Pattern.compile(EMAIL_PATTERN);
			}

			Matcher matcher = emailPattern.matcher(email);
			return matcher.matches();
		}

		public static boolean validateJoinedDate(String yyyymmdd) {
			if (yyyymmddPattern == null) {
				yyyymmddPattern = Pattern.compile(YYYYMMDD_PATTERN);
			}

			Matcher matcher = yyyymmddPattern.matcher(yyyymmdd);
			return matcher.matches();
		}

	}
}
