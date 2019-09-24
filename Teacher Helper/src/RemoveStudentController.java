import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
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

public class RemoveStudentController {// Controller class for removing students

	/* Everything except for drop table and confirm is the same as
	 * "RemoveClassController" (other than variable name changes).
	 * Those will be commented; everything else
	 * can be referred to "RemoveClassController".
	 */

	@FXML
	GridPane removeStudentGrid = new GridPane();

	@FXML
	ArrayList<TextField> addStudentList = new ArrayList();
	private int count;

	public void initialize() {

		count = 0;

	}

	public void addStudentOnList(ActionEvent e) {

		addStudentList.add(new TextField());
		removeStudentGrid.add(addStudentList.get(count), 0, count);
		count++;

	}

	public void confirm(ActionEvent e) throws ClassNotFoundException, SQLException, IOException {// Only thing here is just it initializes
																								 // drop table instead, and no error check.
																								 // The error check is sql side this time

		Statement s = makeCon();

		for (int n = 0; n < addStudentList.size(); n++) {

			dropTable(addStudentList.get(n).getText());

		}

		sScenes(1025, 610, "Home.fxml", e);

	}// end method

	public Statement makeCon() throws ClassNotFoundException, SQLException {

		String url = "jdbc:mysql://127.0.0.1:3306/TeacherHelper";
		String username = "Wade";
		String password = "123456";
		Connection co = DriverManager.getConnection(url, username, password);
		Statement st = co.createStatement();

		return st;

	}

	public void dropTable(String n) throws SQLException, ClassNotFoundException {// Drops a selected table; n == table name

		Statement s = makeCon();

		String q = "DROP TABLE IF EXISTS `" + n + "`";// Drop the selected table if it exists
		s.executeUpdate(q);
		q = "DELETE FROM " + NewStorer.getCurrentClass() + " WHERE Students = \"" + n + "\";";// Delete the student from the studentlist
																							  // in the class table
		s.executeUpdate(q);


	}// end method

	public void sScenes(int x, int y, String fxml, ActionEvent e) throws IOException {

		Node n = (Node) e.getSource();
		Stage st = (Stage) n.getScene().getWindow();
		Parent root = FXMLLoader.load(getClass().getResource(fxml));
		Scene s = new Scene(root, x, y);
		st.setScene(s);
		st.show();

	}

}// end controller class