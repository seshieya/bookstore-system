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
import a00750330.book.data.Book;
import a00750330.database.Database;
import a00750330.database.DbConstants;

/**
 * @author Hsiu-Man Angela Wu, A00750330
 *
 */
public class BookDao extends Dao {

	public static final String TABLE_NAME = DbConstants.BOOKS_TABLE_NAME;

	private static final Logger LOG = LogManager.getLogger();

	/**
	 * @param database Database object
	 */
	public BookDao(Database database) {
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
				+ "%s INT, " // BOOK_ID
				+ "%s VARCHAR(20), " // ISBN
				+ "%s VARCHAR(100), " // AUTHOR
				+ "%s INT, " // ORIGINAL_PUBLICATION_YEAR
				+ "%s VARCHAR(100), " // ORIGINAL TITLE
				+ "%s FLOAT, " // AVERAGE_RATING
				+ "%s INT, " // RATINGS_COUNT
				+ "%s VARCHAR(100), " // IMAGE_URL
				+ "primary key (%s) )", // BOOK_ID
				tableName, // 1
				Fields.BOOK_ID.getName(), //
				Fields.ISBN.getName(), //
				Fields.AUTHOR.getName(), //
				Fields.ORIGINAL_PUBLICATION_YEAR.getName(), //
				Fields.ORIGINAL_TITLE.getName(), //
				Fields.AVERAGE_RATING.getName(), //
				Fields.RATINGS_COUNT.getName(), //
				Fields.IMAGE_URL.getName(), //
				Fields.BOOK_ID.getName()); // 
		LOG.debug(sql);
		super.create(sql);
	}

	/**
	 * @param book Book object
	 * @throws SQLException SQL Exception
	 */
	public void add(Book book) throws SQLException {
		Statement statement = null;
		try {
			Connection connection = database.getConnection();
			statement = connection.createStatement();
			String sql = String.format("insert into %s values(" // 1 tableName
					+ "'%s', " // 2 bookId
					+ "'%s', " // 3 isbn
					+ "'%s', " // 4 author
					+ "'%s', " // 5 originalPublicationYear
					+ "'%s', " // 6 originalTitle
					+ "'%s', " // 7 averageRating
					+ "'%s', " // 8 ratingsCount
					+ "'%s')", // 9 imageUrl

					tableName, // 1
					book.getId(), // 2
					book.getIsbn(), // 3
					book.getAuthors(),// 5
					book.getYear(), // 6
					book.getTitle(), // 6
					book.getRating(), // 7
					book.getRatingsCount(), // 8
					book.getImageUrl() ); // 9
			LOG.debug(sql);
			statement.executeUpdate(sql);
		} finally {
			close(statement);
		}
	}

	/**
	 * @param bookId Book's Id
	 * @return Book Object
	 * @throws SQLException SQL Exception
	 * @throws ApplicationException Application Exception
	 */
	public Book getBook(Long bookId) throws SQLException, ApplicationException {
		Connection connection;
		Statement statement = null;
		Book book = null;
		try {
			connection = database.getConnection();
			statement = connection.createStatement();
			// Execute a statement
			String sql = String.format("SELECT * FROM %s WHERE %s = '%s'", tableName, Fields.BOOK_ID.getName(),
					bookId);
			LOG.debug(sql);
			ResultSet resultSet = statement.executeQuery(sql);

			// get the Book
			// throw an exception if we get more than one result
			int count = 0;
			while (resultSet.next()) {
				count++;
				if (count > 1) {
					throw new ApplicationException(String.format("Expected one result, got %d", count));
				}
				book = new Book.Builder(bookId).build();
				book.setIsbn(resultSet.getString(Fields.ISBN.getName()));
				book.setAuthors(resultSet.getString(Fields.AUTHOR.getName()));
				book.setYear(resultSet.getInt(Fields.ORIGINAL_PUBLICATION_YEAR.getName()));
				book.setTitle(resultSet.getString(Fields.ORIGINAL_TITLE.getName()));
				book.setRating(resultSet.getFloat(Fields.AVERAGE_RATING.getName()));
				book.setRatingsCount(resultSet.getInt(Fields.RATINGS_COUNT.getName()));
				book.setImageUrl(resultSet.getString(Fields.IMAGE_URL.getName()));
			}
		} finally {
			close(statement);
		}

		return book;
	}

	/**
	 * @return List<String> 
	 * @throws SQLException SQL Exception
	 */
	public List<Long> getIds() throws SQLException {
		Connection connection;
		Statement statement = null;
		List<Long> bookIds = new ArrayList<>();
		try {
			connection = database.getConnection();
			statement = connection.createStatement();
			String sql = String.format("SELECT %s FROM %s", Fields.BOOK_ID.getName(),
					DbConstants.BOOKS_TABLE_NAME);
			LOG.debug("Getting all customer IDs from the database:");
			LOG.debug(sql);
			ResultSet resultSet = statement.executeQuery(sql);

			while (resultSet.next()) {
				bookIds.add(resultSet.getLong(Fields.BOOK_ID.getName()));
			}

		} finally {
			close(statement);
		}

		return bookIds;

	}
	
	
	public List<Book> getAllBooks() throws SQLException {
		Connection connection;
		Statement statement = null;
		List<Book> books = new ArrayList<>();
		try {
			connection = database.getConnection();
			statement = connection.createStatement();
			String sql = String.format("SELECT * FROM %s", DbConstants.BOOKS_TABLE_NAME);
			LOG.debug("Getting all books from the database:");
			LOG.debug(sql);
			ResultSet resultSet = statement.executeQuery(sql);

			while (resultSet.next()) {
				Book book = new Book.Builder(resultSet.getLong(Fields.BOOK_ID.getName()))
						.setIsbn(resultSet.getString(Fields.ISBN.getName()))
						.setAuthors(resultSet.getString(Fields.AUTHOR.getName()))
						.setImageUrl(resultSet.getString(Fields.IMAGE_URL.getName()))
						.setRating(resultSet.getFloat(Fields.AVERAGE_RATING.getName()))
						.setRatingsCount(resultSet.getInt(Fields.RATINGS_COUNT.getName()))
						.setTitle(resultSet.getString(Fields.ORIGINAL_TITLE.getName()))
						.setYear(resultSet.getInt(Fields.ORIGINAL_PUBLICATION_YEAR.getName()))
						.build();
				
				books.add(book);
				
				LOG.debug("Adding book to List<Book> : " + book);
			
			}

		} finally {
			close(statement);
		}

		return books;

	}

	/**
	 * @param book Book Object
	 * @throws SQLException SQL Exception
	 */
	public void update(Book book) throws SQLException {
		Connection connection;
		Statement statement = null;
		try {
			connection = database.getConnection();
			statement = connection.createStatement();
			// Execute a statement
			String sql = String.format(
					"UPDATE %s set %s='%s', %s='%s', %s='%s', %s='%s', %s='%s', %s='%s', %s='%s', %s='%s', %s='%s' WHERE %s='%s'",
					tableName, //
					Fields.BOOK_ID.getName(), book.getId(), // 2
					Fields.AUTHOR.getName(), book.getAuthors(), // 3
					Fields.ORIGINAL_PUBLICATION_YEAR.getName(), book.getYear(), // 4
					Fields.ORIGINAL_TITLE.getName(), book.getTitle(), // 5
					Fields.AVERAGE_RATING.getName(), book.getRating(), // 6
					Fields.RATINGS_COUNT.getName(), book.getRatingsCount(), // 7
					Fields.IMAGE_URL.getName(), book.getImageUrl(), // 8
					Fields.BOOK_ID.getName(), book.getId()); // 9
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
	public void delete(Book book) throws SQLException {
		Connection connection;
		Statement statement = null;
		try {
			connection = database.getConnection();
			statement = connection.createStatement();
			// Execute a statement
			String sql = String.format("DELETE from %s WHERE %s='%s'", tableName, Fields.BOOK_ID.getName(),
					book.getId());
			LOG.debug(sql);
			int rowcount = statement.executeUpdate(sql);
			LOG.debug(String.format("Deleted %d rows", rowcount));
		} finally {
			close(statement);
		}
	}

	public enum Fields {

		BOOK_ID("bookId", "VARCHAR", 3, 1), //
		ISBN("isbn", "VARCHAR", 20, 2), //
		AUTHOR("author", "VARCHAR", 20, 3), // TODO check if should change to 100 to make name fit
		ORIGINAL_PUBLICATION_YEAR("publicationYear", "YEAR", 4, 4), //
		ORIGINAL_TITLE("title", "VARCHAR", 20, 5), //
		AVERAGE_RATING("averageRating", "DECIMAL", 4, 6), //
		RATINGS_COUNT("ratingsCount", "INT", 7, 7), // TODO: check if smallint would work
		IMAGE_URL("imageUrl", "VARCHAR", 60, 8); //

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
