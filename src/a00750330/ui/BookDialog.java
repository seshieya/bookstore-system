/**
 * Project: A00750330_lab09
 * File: Lab9.java
 * Date: Nov 14, 2017
 */

package a00750330.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import net.miginfocom.swing.MigLayout;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import a00750330.book.ApplicationException;
import a00750330.book.data.Book;


/**
 * @author Hsiu-Man Angela Wu, A00750330
 *
 */
@SuppressWarnings("serial")
public class BookDialog extends JDialog {

	private static final Logger LOG = LogManager.getLogger();

	private final JPanel contentPanel = new JPanel();

	private JTable table;

	/**
	 * Create the dialog.
	 */
	public BookDialog() {
		setBounds(100, 100, 800, 469);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setName("");
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(
				new MigLayout("", "[37.00,grow][68.00,grow][67.00][53.00][42.00][77.00][62.00][53.00][176.00,grow]",
						"[][grow][][][][][][][]"));
		{
			JLabel lblId = new JLabel("ID");
			contentPanel.add(lblId, "cell 0 0,alignx left");
		}
		{
			JLabel lblFirstName = new JLabel("First Name");
			contentPanel.add(lblFirstName, "cell 1 0,alignx left");
		}
		{
			JLabel lblLastName = new JLabel("Last Name");
			contentPanel.add(lblLastName, "cell 2 0,alignx left");
		}
		{
			JLabel lblStreet = new JLabel("Street");
			contentPanel.add(lblStreet, "cell 3 0,alignx left");
		}
		{
			JLabel lblCity = new JLabel("City");
			contentPanel.add(lblCity, "cell 4 0,alignx left");
		}
		{
			JLabel lblNewLabel = new JLabel("Postal Code");
			contentPanel.add(lblNewLabel, "cell 5 0,alignx left");
		}
		{
			JLabel lblPhone = new JLabel("Phone");
			contentPanel.add(lblPhone, "cell 6 0,alignx left");
		}
		{
			JLabel lblEmail = new JLabel("Email");
			contentPanel.add(lblEmail, "cell 7 0,alignx left");
		}
		{
			JLabel lblJoinedDate = new JLabel("Joined Date");
			contentPanel.add(lblJoinedDate, "cell 8 0,alignx left");
		}
		{

			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);

				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						dispose();
					}
				});

			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);

				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						dispose();
					}
				});
			}
		}
	}

	public void setBookList(List<Book> booksList, JCheckBoxMenuItem author, JCheckBoxMenuItem descending)
			throws SQLException, ApplicationException {

		if (author.isSelected()) {
			if (descending.isSelected()) {
				Collections.sort(booksList, new CompareByAuthorDescending());
				LOG.debug("Sorted by author descending");
			} else {
				Collections.sort(booksList, new CompareByAuthor());
				LOG.debug("Sorted by author");

			}

		}

		String[][] rowData = new String[booksList.size()][8];

		for (int i = 0; i < booksList.size(); i++) {
			rowData[i][0] = Long.toString(booksList.get(i).getId());
			rowData[i][1] = booksList.get(i).getIsbn();
			rowData[i][2] = booksList.get(i).getAuthors();
			rowData[i][3] = booksList.get(i).getTitle();
			rowData[i][4] = Integer.toString(booksList.get(i).getYear());
			rowData[i][5] = Float.toString(booksList.get(i).getRating());
			rowData[i][6] = Integer.toString(booksList.get(i).getRatingsCount());
			rowData[i][7] = booksList.get(i).getImageUrl();
		}

		String[] columnNames = new String[8];
		columnNames[0] = "Book ID";
		columnNames[1] = "ISBN";
		columnNames[2] = "Authors";
		columnNames[3] = "Title";
		columnNames[4] = "Year";
		columnNames[5] = "Rating";
		columnNames[6] = "Ratings Count";
		columnNames[7] = "Image URL";

		JScrollPane scrollPane = new JScrollPane();
		getContentPane().add(scrollPane, BorderLayout.CENTER);

		table = new JTable();

		table.setModel(new DefaultTableModel(rowData, columnNames) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}

		});

		LOG.debug("Placed row data and column names into JTable");

		table.setFillsViewportHeight(true);
		scrollPane.setViewportView(table);

	}

	public static class CompareByAuthor implements Comparator<Book> {
		@Override
		public int compare(Book book1, Book book2) {
			return book1.getAuthors().compareToIgnoreCase(book2.getAuthors());
		}
	}

	public static class CompareByAuthorDescending implements Comparator<Book> {
		@Override
		public int compare(Book book1, Book book2) {
			return -book1.getAuthors().compareToIgnoreCase(book2.getAuthors());
		}
	}

}
