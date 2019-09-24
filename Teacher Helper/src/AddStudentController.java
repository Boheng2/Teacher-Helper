import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.regex.Pattern;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AddStudentController {//Controller class for inserting students

	/* Pretty much everything here is the same to "AddClassController" with the variable names switched.
	 * Thus refer to "AddClassController" for everything here
	 * One exception is the confirm method, it does one extra step and that will be commented
	 * Another is the addColumn method, which will also be commented
	 */

	@FXML
	GridPane createStudentGrid = new GridPane();
	@FXML
	ArrayList<TextField> addStudentList = new ArrayList();
	@FXML
	Label errorWarning = new Label();
	private int count;

	public void initialize() {

		count = 0;
		errorWarning.setVisible(false);

	}

	public void addStudentOnList(ActionEvent e) {

		addStudentList.add(new TextField());
		createStudentGrid.add(addStudentList.get(count), 0, count);
		count++;

	}

	public void confirm(ActionEvent e) throws ClassNotFoundException, SQLException, IOException {// Regex changes and one more step;
																								 //otherwise the same as "AddClassController"

		Statement s = makeCon();

		String regex = "^[a-zA-Z]+$"; //Only alphanumeric letters is can be inpputed

		for (int n = 0; n < addStudentList.size(); n++) {

			//I did replace all to remove white space because I forgot the regex for whitespace (obviously white space should be allowed)
			if (addStudentList.get(n).getText() == null || Pattern.matches(regex, addStudentList.get(n).getText().replaceAll(" ","")) == false){
				errorWarning.setVisible(true);
				NewStorer.wrong();
				break;

			}

			addValue (NewStorer.getCurrentClass(),"Students",addStudentList.get(n).getText(),s);

			addTable(addStudentList.get(n).getText(), "row", s);// This is basically the row id. It won't be seen but it is very helpful for
																// updating values since it will be a reference

			addColumn(addStudentList.get(n).getText(), "ex", s);// Expectations column

			addColumn(addStudentList.get(n).getText(), "r-", s);// Columns for all the marks
			addColumn(addStudentList.get(n).getText(), "r", s);
			addColumn(addStudentList.get(n).getText(), "r+", s);
			addColumn(addStudentList.get(n).getText(), "1-", s);
			addColumn(addStudentList.get(n).getText(), "1", s);
			addColumn(addStudentList.get(n).getText(), "1+", s);
			addColumn(addStudentList.get(n).getText(), "2-", s);
			addColumn(addStudentList.get(n).getText(), "2", s);
			addColumn(addStudentList.get(n).getText(), "2+", s);
			addColumn(addStudentList.get(n).getText(), "3-", s);
			addColumn(addStudentList.get(n).getText(), "3", s);
			addColumn(addStudentList.get(n).getText(), "3+", s);
			addColumn(addStudentList.get(n).getText(), "4--", s);
			addColumn(addStudentList.get(n).getText(), "4-", s);
			addColumn(addStudentList.get(n).getText(), "4-/4", s);
			addColumn(addStudentList.get(n).getText(), "4", s);
			addColumn(addStudentList.get(n).getText(), "4/4+", s);
			addColumn(addStudentList.get(n).getText(), "4+", s);
			addColumn(addStudentList.get(n).getText(), "4++", s);

			NewStorer.notWrong();


		}

		if (NewStorer.getWrong() == false) {

			sScenes(1025,610,"Home.fxml",e);

		}

	}// end method

	public Statement makeCon() throws ClassNotFoundException, SQLException {

		String url = "jdbc:mysql://127.0.0.1:3306/TeacherHelper";
		String username = "Wade";
		String password = "123456";
		Connection co = DriverManager.getConnection(url, username, password);
		Statement st = co.createStatement();

		return st;

	}

	public void addTable(String n, String c, Statement s) throws SQLException {

		String q = "CREATE TABLE IF NOT EXISTS " + n + " ( `" + c + "`  VARCHAR(255) NOT NULL)";

		s.execute(q);

	}

	public void sScenes(int x, int y, String fxml, ActionEvent e) throws IOException {

		Node n = (Node) e.getSource();
		Stage st = (Stage) n.getScene().getWindow();
		Parent root = FXMLLoader.load(getClass().getResource(fxml));
		Scene s = new Scene(root, x, y);
		st.setScene(s);
		st.show();

	}

	public void addColumn(String n, String c, Statement s) throws SQLException {// Goes into the specified table and add a specified column

		String q = "ALTER TABLE " + n + " ADD COLUMN `" + c + "` VARCHAR(255);";// c = column, n = table

		s.execute(q);

	}

	public void addValue(String n, String c, String v, Statement s) throws SQLException {

		String q = "INSERT INTO " + n + " (" + c + ")\r\n" + "VALUES ('" + v + "');";

		s.execute(q);

	}

}// end controller class