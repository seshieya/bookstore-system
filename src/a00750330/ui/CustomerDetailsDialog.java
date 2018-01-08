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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import a00750330.book.ApplicationException;
import a00750330.book.data.Customer;
import net.miginfocom.swing.MigLayout;
import javax.swing.JLabel;
import java.awt.Color;

/**
 * @author Hsiu-Man Angela Wu, A00750330
 *
 */
@SuppressWarnings("serial")
public class CustomerDetailsDialog extends JDialog {

	private static final Logger LOG = LogManager.getLogger();

	private final JPanel contentPanel = new JPanel();
	private JTextField idField;
	private JTextField firstNameField;
	private JTextField lastNameField;
	private JTextField streetField;
	private JTextField cityField;
	private JTextField postalCodeField;
	private JTextField phoneField;
	private JTextField emailField;
	private JTextField joinedDateField;

	/**
	 * Create the dialog.
	 */
	public CustomerDetailsDialog() {
		setBounds(100, 100, 450, 396);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setName("");
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new MigLayout("", "[69.00][grow]", "[][][][][][][][][]"));
		{
			JLabel lblId = new JLabel("ID");
			contentPanel.add(lblId, "cell 0 0,alignx trailing");
		}
		{
			idField = new JTextField();
			idField.setBackground(new Color(230, 230, 250));
			idField.setForeground(new Color(128, 128, 128));
			idField.setFocusable(false);
			idField.setName("ID");
			contentPanel.add(idField, "cell 1 0,growx");
			idField.setColumns(10);
		}
		{
			JLabel lblFirstName = new JLabel("First Name");
			contentPanel.add(lblFirstName, "cell 0 1,alignx trailing");
		}
		{
			firstNameField = new JTextField();
			contentPanel.add(firstNameField, "cell 1 1,growx");
			firstNameField.setColumns(10);
		}
		{
			JLabel lblLastName = new JLabel("Last Name");
			contentPanel.add(lblLastName, "cell 0 2,alignx trailing");
		}
		{
			lastNameField = new JTextField();
			contentPanel.add(lastNameField, "cell 1 2,growx");
			lastNameField.setColumns(10);
		}
		{
			JLabel lblStreet = new JLabel("Street");
			contentPanel.add(lblStreet, "cell 0 3,alignx trailing");
		}
		{
			streetField = new JTextField();
			contentPanel.add(streetField, "cell 1 3,growx");
			streetField.setColumns(10);
		}
		{
			JLabel lblCity = new JLabel("City");
			contentPanel.add(lblCity, "cell 0 4,alignx trailing");
		}
		{
			cityField = new JTextField();
			contentPanel.add(cityField, "cell 1 4,growx");
			cityField.setColumns(10);
		}
		{
			JLabel lblNewLabel = new JLabel("Postal Code");
			contentPanel.add(lblNewLabel, "cell 0 5,alignx trailing");
		}
		{
			postalCodeField = new JTextField();
			contentPanel.add(postalCodeField, "cell 1 5,growx");
			postalCodeField.setColumns(10);
		}
		{
			JLabel lblPhone = new JLabel("Phone");
			contentPanel.add(lblPhone, "cell 0 6,alignx trailing");
		}
		{
			phoneField = new JTextField();
			phoneField.setRequestFocusEnabled(false);
			contentPanel.add(phoneField, "cell 1 6,growx");
			phoneField.setColumns(10);
		}
		{
			JLabel lblEmail = new JLabel("Email");
			contentPanel.add(lblEmail, "cell 0 7,alignx trailing");
		}
		{
			emailField = new JTextField();
			contentPanel.add(emailField, "cell 1 7,growx");
			emailField.setColumns(10);
		}
		{
			JLabel lblJoinedDate = new JLabel("Joined Date");
			contentPanel.add(lblJoinedDate, "cell 0 8,alignx trailing");
		}
		{
			joinedDateField = new JTextField();
			contentPanel.add(joinedDateField, "cell 1 8,growx");
			joinedDateField.setColumns(10);
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
						setUpdatedDetails();
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
		
		LOG.debug("Dialog created");
	}

	public void setCustomerDetails(Customer customer) throws SQLException, ApplicationException {
		idField.setText(Long.toString(customer.getId()));
		firstNameField.setText(customer.getFirstName());
		lastNameField.setText(customer.getLastName());
		streetField.setText(customer.getStreet());
		cityField.setText(customer.getCity());
		postalCodeField.setText(customer.getPostalCode());
		phoneField.setText(customer.getPhone());
		emailField.setText(customer.getEmailAddress());
		joinedDateField.setText(customer.getJoinedDate().format(DateTimeFormatter.ofPattern("yyyy'-'MM'-'dd")));
	}

	
	public void setUpdatedDetails() {
		
		Customer customer = new Customer.Builder(Long.parseLong(idField.getText()), phoneField.getText())
				.setFirstName(firstNameField.getText())
				.setLastName(lastNameField.getText())
				.setStreet(streetField.getText())
				.setCity(cityField.getText())
				.setPostalCode(postalCodeField.getText())
				.setEmailAddress(emailField.getText())
				.setJoinedDate(LocalDate.parse(joinedDateField.getText()))
				.build();
		
		UiMethodsForListeners.updateCustomer(customer);
	}
}
