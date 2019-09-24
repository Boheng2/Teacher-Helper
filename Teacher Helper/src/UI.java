import java.sql.SQLException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/*
 * TeacherHelper
 * Version 2
 * Wade Huang
 * January 21st, 2019
 *
 */


public class UI extends Application { //Main Class

	@Override
	public void start(Stage Pst) throws Exception { //Starts the first fxml file (or main screen)

		Parent rootS = FXMLLoader.load(getClass().getResource("Home.fxml"));//loads the fxml file
		Scene start = new Scene(rootS,1025,610);//set to a defined scene
		Pst.setScene(start);
		Pst.fireEvent(new WindowEvent(Pst, WindowEvent.WINDOW_CLOSE_REQUEST));//Ability to close
		Pst.show();

	}

	public static void main(String[] args) throws SQLException, ClassNotFoundException {//main method

		launch(args);

	}//end main method

}//end class