import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class EditController {// Controller for the editing fxml where you can edit the marking grid

	// An arraylist of arraylists of textfields
	// Each arraylist in this arraylist represent a column and each textfield is a value
	ArrayList<ArrayList<TextField>> marks = new ArrayList();
	@FXML
	GridPane grid = new GridPane();// All the marks on the textfields will be here
	private int count = 0;

	public void initialize() throws SQLException, ClassNotFoundException {// Initialize method

		Statement s = makeCon();

		String q = "CREATE TEMPORARY TABLE temp SELECT * FROM " + NewStorer.getCurrentStudent() + ";";// Create temporary table from the student table

		s.executeUpdate(q);

		q = "ALTER TABLE temp DROP COLUMN `row`;";// Drop the "row" column as that's not for editing or for showing, only for reference

		s.executeUpdate(q);

		q = "SELECT * FROM temp ";// Select the table without the "row" column

		ResultSet r = s.executeQuery(q);// Resultset of the data in the table

		for (int n = 0; n < r.getMetaData().getColumnCount(); n++) {// Limit is the number of columns

			marks.add(new ArrayList<TextField>());// Makes a new arraylist of textfields when a new column is read
												  // Adds it to the parent arraylist

			r = s.executeQuery(q);

			while (r.next()) {// While there's still data

				marks.get(n).add(new TextField(r.getString(r.getMetaData().getColumnName(n + 1))));// Add the data into the child arraylist
																								   // Once again +1 because sql index starts at 1

			}

		}

		for (int n = 0; n < marks.size(); n++) {// Per each column (their arraylist)

			for (int y = 0; y < marks.get(n).size(); y++) {// Dump all the textfields that has the values of the column to the grid

				grid.add(marks.get(n).get(y), n, y);// x-axis is n since x-axis is basicallt the columns, and y-axis is y since y-axis is the values

			}

		}

		q = "DROP TABLE temp;";// Delete the cemporary tables
		s.executeUpdate(q);

		q = "SELECT * FROM "  + NewStorer.getCurrentStudent() + ";";// Gets the table data from the student that is currently selected
		r = s.executeQuery(q);

		while (r.next()) {

			count = Integer.parseInt(r.getString("row")) + 1;// Counts the number of rows; +1 again due to different indexes

		}

	}// end initialize method

	public void removeExpectations(ActionEvent e) throws IOException, ClassNotFoundException, SQLException {// Goes to the removeExpectation fxml file
																											// to select what expectations to remove

		Statement s = makeCon();

		sScenes(600, 240, "RemoveExpectation.fxml", e);

	}// end method

	public void addExpectations(ActionEvent e) throws SQLException, ClassNotFoundException {// Add a row by adding a value
																		       // which is the row number on the row column

		Statement s = makeCon();

		addValue(NewStorer.getCurrentStudent(), "row", "" + count, s);
		count++;

		for (int n = 0; n < marks.size(); n++) {// Limit is the size of the parent arraylist

			marks.get(n).add(new TextField());// All the arraylist adds one textfield
			grid.add(marks.get(n).get(marks.get(n).size() - 1), n, marks.get(n).size() - 1);// Add the textfield to the gridpane;
																							//-1 due to index differences

		}

	}// end method

	public void confirm(ActionEvent e) throws SQLException, ClassNotFoundException, IOException {// When the confirm button is clicked, the edited
																								 // values from the textfields will be added to the table

		Statement s = makeCon();

		String q = "CREATE TEMPORARY TABLE temp SELECT * FROM " + NewStorer.getCurrentStudent() + ";";// Temporary table is created

		s.executeUpdate(q);

		q = "ALTER TABLE temp DROP COLUMN `row`;";// Drops the row since it shouldn't be edited or shown

		s.executeUpdate(q);

		q = "SELECT * FROM temp ";

		ResultSet r = s.executeQuery(q);// Select and get all the data from the temporary table

		ArrayList<String> Columns = new ArrayList();

		for (int u = 0; u < r.getMetaData().getColumnCount(); u++) {// Initialize a columns arraylist and add all the column anmes to it
																	// getColumnCount() is getting the number of columns

			Columns.add(r.getMetaData().getColumnName(u + 1));// getColumnname(index) gets the column name of that index; +1 due to index differences

		}

		for (int n = 0; n < marks.size(); n++) {// Repeats until the arraylist size (the column count)

			for (int y = 0; y < marks.get(n).size(); y++) {// Array will take all the textfields and insert their values into the columns that
														   // each arraylist represents

				updateValue(NewStorer.getCurrentStudent(),Columns.get(n), "" + y, marks.get(n).get(y).getText(), s);
				//First input is the table name, which is why it is current student
				//Second input is the column, which can be referenced through the columns array
				//Third input is the current row number, which is represented by the for loop integer
				//Fourth input is the value that will be added

			}

		}

		q = "DROP TABLE temp;";// Delete the temporary table
		s.executeUpdate(q);

		sScenes(1025,610,"Home.fxml",e);// Go back to main screen


	}// end method

	public Statement makeCon() throws ClassNotFoundException, SQLException {// Refer to HomeController.makeCon

		String url = "jdbc:mysql://127.0.0.1:3306/TeacherHelper";
		String username = "Wade";
		String password = "123456";
		Connection co = DriverManager.getConnection(url, username, password);
		Statement st = co.createStatement();

		return st;

	}// end method

	public void addValue(String n, String c, String v, Statement s) throws SQLException {// Refer to AddClassController.adddValue

		String q = "INSERT INTO " + n + " (`" + c + "`) VALUES (\"" + v + "\"); ";

		s.execute(q);

	}// end method

	//Provide table name, column name, and value as usual. This time it needs a reference value. We use the row column with the row number
	//that's the same with the arraylist element reference number as they are the same since
	//arraylists are really similar in the sense of data storing
	public void updateValue(String n, String c, String r, String v, Statement s) throws SQLException {

		String q = "UPDATE " + n + " SET `" + c + "` = \"" + v + "\" WHERE `row` = " + r + ";";

		s.execute(q);

	}// end method

	public void sScenes(int x, int y, String fxml, ActionEvent e) throws IOException {// Refer to HomeController.sScenes

		Node n = (Node) e.getSource();
		Stage st = (Stage) n.getScene().getWindow();
		Parent root = FXMLLoader.load(getClass().getResource(fxml));
		Scene s = new Scene(root, x, y);
		st.setScene(s);
		st.show();

	}// end method

}// end controller class
