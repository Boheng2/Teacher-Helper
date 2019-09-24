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

public class RemoveClassController {// Controller class for removing classes

	/* Everything except for drop table and confirm is the same as
	 * "AddClassController" (other than variable name changes).
	 * Those will be commented; everything else
	 * can be referred to "AddClassController".
	 */

	@FXML
	GridPane removeClassGrid = new GridPane();

	@FXML
	ArrayList<TextField> addClassList = new ArrayList();
	private int count;

	public void initialize() {

		count = 0;

	}

	public void addClassOnList(ActionEvent e) {

		addClassList.add(new TextField());
		removeClassGrid.add(addClassList.get(count), 0, count);
		count++;

	}

	public void confirm(ActionEvent e) throws ClassNotFoundException, SQLException, IOException {// Only thing here is just it initializes
		 																						 // drop table instead, and no error check.
																								 // The error check is sql side this time

		Statement s = makeCon();

		for (int n = 0; n < addClassList.size(); n++) {

			dropTable(addClassList.get(n).getText());

		}

		sScenes(1025, 610, "Home.fxml", e);

	}

	public Statement makeCon() throws ClassNotFoundException, SQLException {

		String url = "jdbc:mysql://127.0.0.1:3306/TeacherHelper";
		String username = "Wade";
		String password = "123456";
		Connection co = DriverManager.getConnection(url, username, password);
		Statement st = co.createStatement();

		return st;

	}

	public void dropTable(String n) throws SQLException, ClassNotFoundException {// Deletes the class table, all the student table
																				 // that belongs to the class, as well the itself
																				 // from the class list

		Statement s = makeCon();

		String q = "SELECT * FROM " + n;// Select the table

		ResultSet rs = s.executeQuery(q);

		ArrayList<String> Students = new ArrayList();

		while (rs.next()){// Get all the students from it and add it to the students arraylist

			Students.add(rs.getString("Students"));

		}

		for (int v = 0; v < Students.size(); v++){// Drop every student in the arraylist that has a table

			q = "DROP TABLE IF EXISTS `" + Students.get(v) + "`";
			s.executeUpdate(q);

		}


		q = "DROP TABLE IF EXISTS `" + n + "`";// Drop the class table if it exists
		s.executeUpdate(q);
		q = "DELETE FROM ClassList WHERE Classes = \"" + n + "\";";// Deletes itself from the classlist
		s.executeUpdate(q);


	}//end method

	public void sScenes(int x, int y, String fxml, ActionEvent e) throws IOException {

		Node n = (Node) e.getSource();
		Stage st = (Stage) n.getScene().getWindow();
		Parent root = FXMLLoader.load(getClass().getResource(fxml));
		Scene s = new Scene(root, x, y);
		st.setScene(s);
		st.show();

	}

}// end controller class