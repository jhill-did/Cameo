package Cameo.Model;
import javafx.beans.property.*;
import javafx.collections.*;

public class ApplicationModel
{
	public SimpleIntegerProperty selectedDocument = new SimpleIntegerProperty(0);
	
	public ObservableList<Document> documents = FXCollections.observableArrayList();
	
	public SimpleStringProperty statusMessage = new SimpleStringProperty("");
}

// Application
//  projectDirectory: ""
//	preferences:
//	[
//		font : {}
//		theme : {} 
// 	]
//	documents :
//	[
//		document1 :
// 		{
//			title: 
//			path:
//			content:
//		}
//	]