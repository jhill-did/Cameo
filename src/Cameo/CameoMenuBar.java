package Cameo;
import javafx.scene.control.*;

public class CameoMenuBar extends MenuBar 
{
	public CameoMenuBar()
	{
		Menu file = new Menu("File");
		MenuItem newFile = new MenuItem("New File");
		MenuItem openFile = new MenuItem("Open File");
		
		Menu edit = new Menu("Edit");
		MenuItem undo = new MenuItem("Undo");
		MenuItem redo = new MenuItem("Redo");

		file.getItems().addAll(newFile, openFile);
		getMenus().add(file);
		edit.getItems().addAll(undo, redo);
		getMenus().add(edit);
	}
}
