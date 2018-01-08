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
import a00750330.book.data.util.Common;

/**
 * @author Hsiu-Man Angela Wu, A00750330
 *
 */
@SuppressWarnings("serial")
public class PurchaseDialog extends JDialog {

	private static final Logger LOG = LogManager.getLogger();

	private final JPanel contentPanel = new JPanel();

	private JTable table;

	/**
	 * Create the dialog.
	 */
	public PurchaseDialog() {
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

	public void setPurchaseList(List<Item> itemsList, JCheckBoxMenuItem lastName, JCheckBoxMenuItem title,
			JCheckBoxMenuItem descending) throws SQLException, ApplicationException {

		if (lastName.isSelected()) {
			if (descending.isSelected()) {
				Collections.sort(itemsList, new CompareByLastNameDescending());
			} else {
				Collections.sort(itemsList, new CompareByLastName());
			}
		}

		if (title.isSelected()) {
			if (descending.isSelected()) {
				Collections.sort(itemsList, new CompareByTitleDescending());
			} else {
				Collections.sort(itemsList, new CompareByTitle());
			}
		}

		String[][] rowData = new String[itemsList.size()][4];

		for (int i = 0; i < itemsList.size(); i++) {
			rowData[i][0] = itemsList.get(i).getFirstName();
			rowData[i][1] = itemsList.get(i).getLastName();
			rowData[i][2] = itemsList.get(i).getTitle();
			rowData[i][3] = Float.toString(itemsList.get(i).getPrice());
		}

		String[] columnNames = new String[4];
		columnNames[0] = "First Name";
		columnNames[1] = "Last Name";
		columnNames[2] = "Title";
		columnNames[3] = "Price";

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

	public static class CompareByLastName implements Comparator<Item> {
		@Override
		public int compare(Item item1, Item item2) {
			return item1.lastName.compareTo(item2.lastName);
		}
	}

	public static class CompareByLastNameDescending implements Comparator<Item> {
		@Override
		public int compare(Item item1, Item item2) {
			return item2.lastName.compareTo(item1.lastName);
		}
	}

	public static class CompareByTitle implements Comparator<Item> {
		@Override
		public int compare(Item item1, Item item2) {
			return item1.title.compareToIgnoreCase(item2.title);
		}
	}

	public static class CompareByTitleDescending implements Comparator<Item> {
		@Override
		public int compare(Item item1, Item item2) {
			return item2.title.compareToIgnoreCase(item1.title);
		}
	}

	public static class Item {
		private String firstName;
		private String lastName;
		private String title;
		private float price;
		private long customerId;

		/**
		 * @param firstName
		 * @param lastName
		 * @param title
		 * @param price
		 */
		public Item() {
		}

		public Item(String firstName, String lastName, String title, float price, long customerId) {
			this.firstName = firstName;
			this.lastName = lastName;
			this.title = Common.truncateIfRequired(title, 80);
			this.price = price;
			this.customerId = customerId;
		}

		/**
		 * @return the firstName
		 */
		public String getFirstName() {
			return firstName;
		}

		/**
		 * @param firstName
		 *            the firstName to set
		 * @return
		 */
		public Item setFirstName(String firstName) {
			this.firstName = firstName;
			return this;
		}

		/**
		 * @return the lastName
		 */
		public String getLastName() {
			return lastName;
		}

		/**
		 * @param lastName
		 *            the lastName to set
		 * @return
		 */
		public Item setLastName(String lastName) {
			this.lastName = lastName;
			return this;
		}

		/**
		 * @return the title
		 */
		public String getTitle() {
			return title;
		}

		/**
		 * @param title
		 *            the title to set
		 * @return
		 */
		public Item setTitle(String title) {
			this.title = Common.truncateIfRequired(title, 80);
			;
			return this;
		}

		/**
		 * @return the price
		 */
		public float getPrice() {
			return price;
		}

		/**
		 * @param price
		 *            the price to set
		 */
		public Item setPrice(float price) {
			this.price = price;
			return this;
		}

		/**
		 * @return the customerId
		 */
		public long getCustomerId() {
			return customerId;
		}

		/**
		 * @param customerId
		 *            the customerId to set
		 */
		public void setCustomerId(long customerId) {
			this.customerId = customerId;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return "Item [firstName=" + firstName + ", lastName=" + lastName + ", title=" + title + ", price=" + price
					+ "]";
		}

	}

}
