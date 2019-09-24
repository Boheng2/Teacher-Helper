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

public class RemoveExpectationsController {

	/* Everything except for removeRow and confirm is the same as
	 * "RemoveClassController" (other than variable name changes).
	 * Those will be commented; everything else
	 * can be referred to "RemoveClassController".
	 */

	@FXML
	GridPane removeExpectationGrid = new GridPane();

	@FXML
	ArrayList<TextField> addExpectationList = new ArrayList();
	private int count;

	public void initialize() {

		count = 0;

	}

	public void addExpectationOnList(ActionEvent e) {

		addExpectationList.add(new TextField());
		removeExpectationGrid.add(addExpectationList.get(count), 0, count);
		count++;

	}

	public void confirm(ActionEvent e) throws ClassNotFoundException, SQLException, IOException {// Only thing changes is the method
																								 // it uses in the for loop

		Statement s = makeCon();

		for (int n = 0; n < addExpectationList.size(); n++) {

			removeRow(addExpectationList.get(n).getText());

		}

		sScenes(910, 610, "Grid.fxml", e);

	}// end method

	public Statement makeCon() throws ClassNotFoundException, SQLException {

		String url = "jdbc:mysql://127.0.0.1:3306/TeacherHelper";
		String username = "Wade";
		String password = "123456";
		Connection co = DriverManager.getConnection(url, username, password);
		Statement st = co.createStatement();

		return st;

	}

	public void removeRow(String n) throws SQLException, ClassNotFoundException {// Deletes the expectation which will be inputed

		Statement s = makeCon();

		String q = "SELECT * FROM " + n;

		q = "DELETE FROM " + NewStorer.getCurrentStudent() + " WHERE Ex = \"" + n + "\";";// It also deletes the whole row
																						  // which is purposefully done;
																						  // otherwise I would've just used
																						  // update value
		s.executeUpdate(q);


	}

	public void sScenes(int x, int y, String fxml, ActionEvent e) throws IOException {

		Node n = (Node) e.getSource();
		Stage st = (Stage) n.getScene().getWindow();
		Parent root = FXMLLoader.load(getClass().getResource(fxml));
		Scene s = new Scene(root, x, y);
		st.setScene(s);
		st.show();

	}

}// end controller class