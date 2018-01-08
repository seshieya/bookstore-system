/**
 * Project: A00750330_lab09
 * File: Lab9.java
 * Date: Nov 14, 2017
 */

package a00750330.ui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.sql.SQLException;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.border.EmptyBorder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import a00750330.book.ApplicationException;

/**
 * @author Hsiu-Man Angela Wu, A00750330
 *
 */
@SuppressWarnings("serial")
public class MainFrame extends JFrame {

	private static final Logger LOG = LogManager.getLogger();

	private JPanel contentPane;

	// Menu bar
	private final JMenuBar mainMenuBar = new JMenuBar();

	// "File" menu
	private final JMenu fileMenu = new JMenu("File");

	// "Books" menu
	private final JMenu booksMenu = new JMenu("Books");

	// "Customers" menu
	private final JMenu customersMenu = new JMenu("Customers");

	// "Purchases" menu
	private final JMenu purchasesMenu = new JMenu("Purchases");

	// "Help" menu
	private final JMenu helpMenu = new JMenu("Help");

	// menu items
	private final JMenuItem fmDrop = new JMenuItem("Drop", 'D');
	private final JMenuItem fmQuit = new JMenuItem("Quit", 'Q');

	private final JMenuItem bmCount = new JMenuItem("Count", 'C');
	private final JCheckBoxMenuItem bmByAuthor = new JCheckBoxMenuItem("By Author");
	private final JCheckBoxMenuItem bmDescending = new JCheckBoxMenuItem("Descending");
	private final JMenuItem bmList = new JMenuItem("List", 'L');

	private final JMenuItem cmCount = new JMenuItem("Count", 'C');
	private final JCheckBoxMenuItem cmByJoinDate = new JCheckBoxMenuItem("By Join Date");
	private final JMenuItem cmList = new JMenuItem("List", 'L');

	private final JMenuItem pmTotal = new JMenuItem("Total", 'T');
	private final JCheckBoxMenuItem pmLastName = new JCheckBoxMenuItem("By Last Name");
	private final JCheckBoxMenuItem pmTitle = new JCheckBoxMenuItem("By Title");
	private final JCheckBoxMenuItem pmDescending = new JCheckBoxMenuItem("Descending");
	private final JMenuItem pmList = new JMenuItem("List", 'L');
	private final JMenuItem pmFilter = new JMenuItem("Filter List by Customer ID", 'F');

	private final JMenuItem hmAbout = new JMenuItem("About", 'A');

	/**
	 * Create the frame.
	 */
	public MainFrame() {

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		buildMenu();

		addActionListeners();

	}

	private void buildMenu() {
		setJMenuBar(mainMenuBar);

		// Add menu "File" to menu bar
		fileMenu.setMnemonic('F');
		mainMenuBar.add(fileMenu);

		// Add menu "Books" to menu bar
		booksMenu.setMnemonic('B');
		mainMenuBar.add(booksMenu);

		// Add menu "Customers" to menu bar
		customersMenu.setMnemonic('C');
		mainMenuBar.add(customersMenu);

		// Add menu "Purchases" to menu bar
		purchasesMenu.setMnemonic('P');
		mainMenuBar.add(purchasesMenu);

		// Add menu "Help" to menu bar
		helpMenu.setMnemonic('H');
		mainMenuBar.add(helpMenu);

		// Add menu items
		fileMenu.add(fmDrop);
		fileMenu.add(fmQuit);

		booksMenu.add(bmCount);
		booksMenu.add(bmByAuthor);
		booksMenu.add(bmDescending);
		booksMenu.add(bmList);

		customersMenu.add(cmCount);
		customersMenu.add(cmByJoinDate);
		customersMenu.add(cmList);

		purchasesMenu.add(pmTotal);
		purchasesMenu.add(pmLastName);
		purchasesMenu.add(pmTitle);
		purchasesMenu.add(pmDescending);
		purchasesMenu.add(pmList);
		purchasesMenu.add(pmFilter);

		helpMenu.add(hmAbout);

		// Set keyboard accelerators
		hmAbout.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0));

	}

	private void addActionListeners() {
		// Add event listeners
		fmDrop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				try {
					UiMethodsForListeners.dropTables();
				} catch (SQLException e) {
					LOG.debug(e);
					JOptionPane.showMessageDialog(null, e, "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		fmQuit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				UiMethodsForListeners.quit();
			}

		});

		bmCount.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				try {
					UiMethodsForListeners.countBooks();
				} catch (SQLException e) {
					LOG.debug(e);
					JOptionPane.showMessageDialog(null, e, "Error", JOptionPane.ERROR_MESSAGE);
				}
			}

		});

		bmList.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				try {
					UiMethodsForListeners.showListBooks(bmByAuthor, bmDescending);
				} catch (Exception e) {
					LOG.debug(e);
					JOptionPane.showMessageDialog(null, e, "Error", JOptionPane.ERROR_MESSAGE);
				}
			}

		});

		cmCount.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				try {
					UiMethodsForListeners.countCustomers();
				} catch (SQLException e) {
					LOG.debug(e);
					JOptionPane.showMessageDialog(null, e, "Error", JOptionPane.ERROR_MESSAGE);
				}
			}

		});

		cmList.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				try {
					UiMethodsForListeners.showListCustomers(cmByJoinDate);
				} catch (Exception e) {
					LOG.debug(e);
					JOptionPane.showMessageDialog(null, e, "Error", JOptionPane.ERROR_MESSAGE);
				}
			}

		});

		pmTotal.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				try {
					UiMethodsForListeners.totalPurchases();
				} catch (SQLException | ApplicationException e) {
					LOG.debug(e);
					JOptionPane.showMessageDialog(null, e, "Error", JOptionPane.ERROR_MESSAGE);
				}
			}

		});

		pmList.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				try {
					UiMethodsForListeners.showListPurchases(pmLastName, pmTitle, pmDescending);
				} catch (Exception e) {
					LOG.debug(e);
					JOptionPane.showMessageDialog(null, e, "Error", JOptionPane.ERROR_MESSAGE);
				}
			}

		});

		pmFilter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				try {
					UiMethodsForListeners.filterListByCustomerId(pmLastName, pmTitle, pmDescending);
				} catch (Exception e) {
					LOG.debug(e);
					JOptionPane.showMessageDialog(null, e, "Error", JOptionPane.ERROR_MESSAGE);
				}
			}

		});

		hmAbout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				LOG.debug("About pane appeared");
				JOptionPane.showMessageDialog(MainFrame.this, "Assignment 2\nBy Angela Wu\nA00750330",
						"About Assignment 2", JOptionPane.INFORMATION_MESSAGE);

			}

		});
	}

}
