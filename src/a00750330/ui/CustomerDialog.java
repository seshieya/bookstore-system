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
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import a00750330.book.ApplicationException;
import a00750330.book.data.Customer;
import a00750330.database.dao.CustomerDao;

/**
 * @author Hsiu-Man Angela Wu, A00750330
 *
 */
@SuppressWarnings("serial")
public class CustomerDialog extends JDialog {

	private static final Logger LOG = LogManager.getLogger();

	private JTable table;

	/**
	 * Create the dialog.
	 */
	public CustomerDialog() {
		setBounds(100, 100, 800, 469);
		getContentPane().setLayout(new BorderLayout());

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
				{

					cancelButton.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							dispose();
						}
					});

				}
			}
		}
	}

	public void setCustomerList(List<Customer> customersList, JCheckBoxMenuItem joinDate, CustomerDao customerDao)
			throws SQLException, ApplicationException {

		if (joinDate.isSelected()) {
			Collections.sort(customersList, new CompareByJoinedDate());
		}

		String[][] rowData = new String[customersList.size()][9];

		for (int i = 0; i < customersList.size(); i++) {
			rowData[i][0] = Long.toString(customersList.get(i).getId());
			rowData[i][1] = customersList.get(i).getFirstName();
			rowData[i][2] = customersList.get(i).getLastName();
			rowData[i][3] = customersList.get(i).getStreet();
			rowData[i][4] = customersList.get(i).getCity();
			rowData[i][5] = customersList.get(i).getPostalCode();
			rowData[i][6] = customersList.get(i).getPhone();
			rowData[i][7] = customersList.get(i).getEmailAddress();
			rowData[i][8] = customersList.get(i).getJoinedDate().toString();
		}

		String[] columnNames = new String[9];
		columnNames[0] = "ID";
		columnNames[1] = "First Name";
		columnNames[2] = "Last Name";
		columnNames[3] = "Street";
		columnNames[4] = "City";
		columnNames[5] = "Postal Code";
		columnNames[6] = "Phone";
		columnNames[7] = "Email";
		columnNames[8] = "Join Date";

		JScrollPane scrollPane = new JScrollPane();
		getContentPane().add(scrollPane, BorderLayout.CENTER);

		table = new JTable();

		table.setModel(new DefaultTableModel(rowData, columnNames) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		});

		table.setFillsViewportHeight(true);
		scrollPane.setViewportView(table);

		createCustomerDetailsDialog(customerDao);

	}

	private void createCustomerDetailsDialog(CustomerDao customerDao) {

		table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent evt) {

				if (evt.getValueIsAdjusting()) {

					int selectedRow = table.getSelectedRow();

					long customerId = Long.parseLong(table.getValueAt(selectedRow, 0).toString());

					try {

						Customer customer = customerDao.getCustomer(customerId);
						LOG.debug("Retrieved customer: " + customer);

						CustomerDetailsDialog dialog = new CustomerDetailsDialog();
						dialog.setCustomerDetails(customer);
						dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
						dialog.setVisible(true);
					} catch (Exception e) {
						LOG.debug(e);
					}

					LOG.debug("Customer " + customerId + " displayed");

					LOG.debug(evt.getValueIsAdjusting());
				}

			}

		});

	}

	public static class CompareByJoinedDate implements Comparator<Customer> {
		@Override
		public int compare(Customer customer1, Customer customer2) {
			return customer1.getJoinedDate().compareTo(customer2.getJoinedDate());
		}
	}

	public static class CompareByJoinedDateDescending implements Comparator<Customer> {
		@Override
		public int compare(Customer customer1, Customer customer2) {
			return customer2.getJoinedDate().compareTo(customer1.getJoinedDate());
		}
	}

}
