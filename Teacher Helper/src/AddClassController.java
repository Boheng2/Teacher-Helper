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
import javafx.stage.Stage;

public class AddClassController { // Controller class for "AddClass.fxml" (controls the screen where you insert classes)

	@FXML
	GridPane createClassGrid = new GridPane();// Call fxml for GridPane (this will be where the textfields are added
	@FXML
	Label errorWarning = new Label();// Call fxml since this will only need to show when error check is not passed
	@FXML
	ArrayList<TextField> addClassList = new ArrayList();// The array of textfields that will be added to the gridpane and
	private int count;// Just a counter, useful when counting rows and other stuff

	public void initialize (){// Initialize method

		count = 0;
		errorWarning.setVisible(false);// Error message should be hidden

	}// end initialize method

	public void addClassOnList (ActionEvent e){// When the add button is pressed, a new textfield will be added to the grid

		addClassList.add(new TextField());
		createClassGrid.add(addClassList.get(count),0,count);// Only changes y since only 1 column is needed;
															 // increase y by 1 everytime a textfield is added
		count++;

	}// end method


	public void confirm (ActionEvent e) throws ClassNotFoundException, SQLException, IOException{

		Statement s = makeCon();// Sets connection

		String regex = "^[a-zA-Z0-9]+$";// Regex for only alphanumeric input and spaces

		for (int n = 0; n < addClassList.size(); n++){

			if (addClassList.get(n).getText() == null || Pattern.matches(regex, addClassList.get(n).getText()) == false){// If the textfield is empty
																														 // or doesn't go trough the error check
				errorWarning.setVisible(true);// Error message now shows
				NewStorer.wrong();// Tells the storer that ther was an error
				break;// Loop ends

			}

			addTable(addClassList.get(n).getText(),"Students",s);// If input is good the classes will be added as tables
			addValue("ClassList","Classes",addClassList.get(n).getText(),s);// The table will also be added to a table called class list
			NewStorer.notWrong();// The input is good

		}

		if (NewStorer.getWrong() == false) {// Since it was not a good input, it obvious will not switch back to main screen
											// but if it is it will

			sScenes(1025,610,"Home.fxml",e);

		}

	}// end method

	public Statement makeCon() throws ClassNotFoundException, SQLException {// refer to HomeController.makeCon

		String url = "jdbc:mysql://127.0.0.1:3306/TeacherHelper";
		String username = "Wade";
		String password = "123456";
		Connection co = DriverManager.getConnection(url, username, password);
		Statement st = co.createStatement();

		return st;

	}// end method

	public void addTable(String n, String c, Statement s) throws SQLException {// refer to HomeController.addTable

		String q = "CREATE TABLE IF NOT EXISTS " + n + " ( " + c + "  VARCHAR(255) NOT NULL)";

		s.execute(q);

	}// end method

	public void addValue(String n, String c, String v, Statement s) throws SQLException {// Inserts a value to a column
																					 	 // All other columns of the same row is null

		String q = "INSERT INTO " + n + " (" + c + ")\r\n" + "VALUES ('" + v + "');";// n = table, c = column, v = value that will be added

		s.execute(q);

	}// end method

	public void sScenes(int x, int y, String fxml, ActionEvent e) throws IOException {// refer to HomeController.sScenes

		Node n = (Node) e.getSource();
		Stage st = (Stage) n.getScene().getWindow();
		Parent root = FXMLLoader.load(getClass().getResource(fxml));
		Scene s = new Scene(root, x, y);
		st.setScene(s);
		st.show();

	}// end method

}// end controller class