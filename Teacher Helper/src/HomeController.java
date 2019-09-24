import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;

public class HomeController {// Controller class for "Home.fxml" (controls the main screen basically)

	// Initialize variables
	@FXML
	Button removeStudent = new Button();// This is called because it will be
										// hidden from view until needed

	@FXML
	VBox classList = new VBox();// Vbox holding all the classes on the left
	@FXML
	VBox studentList = new VBox();// Vbox holding all the students on the left
	@FXML
	Button newButton = new Button();// This is called because it will be hidden
									// from view until needed
	@FXML
	Label studentsLabel = new Label();// This is called because it will be
										// hidden from view until needed
	@FXML
	Button edit = new Button();// This is called because it will be hidden from
								// view until needed
	@FXML
	AnchorPane tablePane = new AnchorPane();// Tableview will be added to here
	TableView table = new TableView();// The marks grid
	ObservableList obl;// List used for tableview

	ArrayList<String> classes = new ArrayList();// Temp storage for the class
												// names
	ArrayList<Button> classesButtons = new ArrayList();// Creates the buttons
														// for all the classes

	ArrayList<String> students = new ArrayList();// Temp storage for the student
													// names
	ArrayList<Button> studentsButtons = new ArrayList();// Creates the buttons
														// for all the students

	public void initialize() throws ClassNotFoundException, SQLException {//Initialize method

		if (NewStorer.checkClass() == false) {// If a class is not clicked, the student section will not show

			newButton.setVisible(false);
			studentsLabel.setVisible(false);
			removeStudent.setVisible(false);

		}

		if (NewStorer.checkStudent() == false) {// If a student is not clicked, the marks section will not show

			edit.setVisible(false);
			table.setVisible(false);

		}

		Statement s = makeCon();//Connect to database

		addTable("ClassList", "Classes", s);// Add all the classes (tables) to the database
		classes = loadColumntoArray(classes, "ClassList", "Classes", s); // Temporarily store the class names
		classes.add(null);

		if (classes.get(0) != null) {// This along with the previous line basically checks if the class arrayList is empty

			classes.clear();
			classes = loadColumntoArray(classes, "ClassList", "Classes", s);// Clears and loads it again without the null taht was added

			classList.getChildren().clear(); // Clears the buttons on the VBox

			for (int n = 0; n < classes.size(); n++) {// Setting up all the buttons for classes

				classesButtons.add(new Button(classes.get(n)));// Sets the text of the classes to the button from the classes arraylist
				classesButtons.get(n).setPrefHeight(25.0);
				classesButtons.get(n).setPrefWidth(120.0);// Sets the size
				classList.getChildren().add(classesButtons.get(n));// add the button into the VBox
				classesButtons.get(n).setOnAction(new EventHandler<ActionEvent>() {// Sets the button on action
					@Override
					public void handle(ActionEvent event) {

						if (NewStorer.checkClass() == false) {// When clicked, the student side will show

							newButton.setVisible(true);
							studentsLabel.setVisible(true);
							removeStudent.setVisible(true);
							NewStorer.onceClass();

						}

						NewStorer.setCurrentClass(((Button) event.getSource()).getText());// Stores the current class
																						  // that the user is on (which is what it clicks)

						try {
							students = loadColumntoArray(students, NewStorer.getCurrentClass(), "Students", s);// Basically repeating the same thing
																											   // loading the list
																											   // expect for students this time
						} catch (ClassNotFoundException | SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						if (students != null) {// Same procedure except for students (check for null and then create buttons)

							students.clear();
							try {
								students = loadColumntoArray(students, NewStorer.getCurrentClass(), "Students", s);
							} catch (ClassNotFoundException | SQLException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

							studentList.getChildren().clear();
							studentsButtons.clear();

							for (int n = 0; n < students.size(); n++) {

								studentsButtons.add(new Button(students.get(n)));
								studentsButtons.get(n).setPrefHeight(25.0);
								studentsButtons.get(n).setPrefWidth(120.0);
								studentList.getChildren().add(studentsButtons.get(n));
								studentsButtons.get(n).setOnAction(new EventHandler<ActionEvent>() {
									@Override
									public void handle(ActionEvent event) {

										if (NewStorer.checkStudent() == false) {// Once clicked, the mark section will show

											table.setVisible(true);
											edit.setVisible(true);
											NewStorer.onceStudent();

										}

										NewStorer.setCurrentStudent(((Button) event.getSource()).getText());// Stores the current student once clicked
										try {
											importTable(s, NewStorer.getCurrentStudent(), table, obl);// imports table from database to tableview
											// It imports it from the table of the student button that was most recently clicked
											tablePane.getChildren().clear();// Clears the pane and then adds the tableview
											tablePane.getChildren().add(table);
										} catch (ClassNotFoundException | SQLException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}

									}
								});

							}

						}

					}
				});

			}

		}

	}// end initialize method

	public void newClass(ActionEvent e) throws IOException {// goes to the add class scene when "New" is pressed

		sScenes(600, 240, "AddClass.fxml", e);

	}// end method

	public void newStudent(ActionEvent e) throws IOException {// goes to the add student scene "New" is pressed

		sScenes(600, 240, "AddStudent.fxml", e);

	}// end method

	public void edit(ActionEvent e) throws IOException {// goes the editing marking grid scene when "Edit" is pressed

		sScenes(900, 600, "Grid.fxml", e);

	}// end method

	public void sScenes(int x, int y, String fxml, ActionEvent e) throws IOException {// Switches the fxml files by initializing new node
																					  // and then putting everything and the provided file on it
																					  // Also can specify the size of the scene

		Node n = (Node) e.getSource();
		Stage st = (Stage) n.getScene().getWindow();
		Parent root = FXMLLoader.load(getClass().getResource(fxml));
		Scene s = new Scene(root, x, y);
		st.setScene(s);
		st.show();

	}// end method

	public void removeClass(ActionEvent e) throws IOException {// goes the editing remove class scene when "Remove" is pressed

		sScenes(600, 240, "RemoveClass.fxml", e);

	}// end method

	public void removeStudent(ActionEvent e) throws IOException {// goes the editing remove student scene when "Remove" is pressed

		sScenes(600, 240, "RemoveStudent.fxml", e);

	}// end method

	public ArrayList<String> loadColumntoArray(ArrayList<String> tf, String n, String c, Statement s)
			throws ClassNotFoundException, SQLException {// loads a column to an arraylist

		tf.clear();
		ResultSet rs = s.executeQuery("SELECT " + c + " FROM " + n);// Selects a specified column (c) from a specified table (n)

		while (rs.next()) {// While there is still data left

			tf.add(rs.getString(c));// That column's values will be added to the arraylist

		}

		return tf;

	}// end method

	public Statement makeCon() throws ClassNotFoundException, SQLException {// Connects to the server through a user and makes a statement

		String url = "jdbc:mysql://127.0.0.1:3306/TeacherHelper";
		String username = "Wade";
		String password = "123456";
		Connection co = DriverManager.getConnection(url, username, password);
		Statement st = co.createStatement();

		return st;

	}// end method

	public void addTable(String n, String c, Statement s) throws SQLException {// Executes a prepared query with names of table
																			   // and columns being variables (c = column, n = table)

		String q = "CREATE TABLE IF NOT EXISTS " + n + " ( " + c + "  VARCHAR(255) NOT NULL)";// Everything will be in varchar(String)

		s.execute(q);

	}// end method

	public void importTable(Statement st, String tbn, TableView tvw, ObservableList obl)
			throws ClassNotFoundException, SQLException {// Import table method

		String q = "CREATE TEMPORARY TABLE temp SELECT * FROM " + NewStorer.getCurrentStudent() + ";";// Copies the current table
																									  // to a temporary table

		st.executeUpdate(q);

		q = "ALTER TABLE temp DROP COLUMN `row`;";// Drop the first column (this is a column that I don't want to show on tableview)

		st.executeUpdate(q);

		q = "SELECT * FROM temp ";// Select the whole table from the temporary table

		obl = FXCollections.observableArrayList();// Intialize the provided observablelist
		ResultSet r = st.executeQuery(q);// Receive the whole table into the resultset

		for (int n = 0; n < r.getMetaData().getColumnCount(); n++) {// Each column will be initialized as TableColumns as the columns in the table
																	// getColumnCount() == the name of columns in the table

			final int vl = n;
			TableColumn mks = new TableColumn(r.getMetaData().getColumnName(n + 1));// +1 due to sql index starting from 1;getColumnName(the index)
			mks.setMinWidth(100);// Sets min width

			mks.setCellValueFactory(new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {// Prepares the table
																													   //column to receive the right type of value

				public ObservableValue<String> call(CellDataFeatures<ObservableList, String> p) {

					return new SimpleStringProperty(p.getValue().get(vl).toString());

				}

			});

			tvw.getColumns().addAll(mks);// Add all table columns into the tableview

		}

		while (r.next()) {// While there is still data in the table

			ObservableList<String> r2 = FXCollections.observableArrayList();// A temporary string observablelist is added

			for (int n = 1; n <= r.getMetaData().getColumnCount(); n++) {

				r2.add(r.getString(n));// The list get's filled with data

			}

			obl.add(r2);// Adds it to the observablelist that was initialized before

		}

		tvw.setItems(obl);// Add all the items in the list to the tableview provided

		q = "DROP TABLE temp;";// Remove the temporary table
		st.executeUpdate(q);

	}// end method

}// end controller class
