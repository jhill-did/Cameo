package Cameo;
import java.io.File;

import javax.swing.event.HyperlinkEvent.EventType;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.*;
import javafx.stage.*;

public class CameoMenuBar extends MenuBar 
{
	public CameoMenuBar(CameoApp parent)
	{
		Menu file = new Menu("File");
		MenuItem newFile = new MenuItem("New File");
		MenuItem openFile = new MenuItem("Open File");
		
		openFile.setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent e) 
			{
				FileChooser fileChooser = new FileChooser();
				fileChooser.setTitle("Open File");
				File selectedFile = fileChooser.showOpenDialog(parent.mainStage);
				if (selectedFile != null)
				{
					parent.openDocument(selectedFile);
				}				
			}
		});
		
		Menu edit = new Menu("Edit");
		MenuItem undo = new MenuItem("Undo");
		MenuItem redo = new MenuItem("Redo");

		file.getItems().addAll(newFile, openFile);
		getMenus().add(file);
		edit.getItems().addAll(undo, redo);
		getMenus().add(edit);
	}
}
