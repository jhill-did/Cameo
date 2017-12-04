package Cameo;
import java.io.File;

import javax.swing.JPopupMenu.Separator;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.*;
import javafx.stage.*;
import javafx.scene.input.*;

public class CameoMenuBar extends MenuBar 
{
	public CameoMenuBar(CameoApp parent)
	{
		Menu file = new Menu("File");
		MenuItem newFile = new MenuItem("New File");
		newFile.setAccelerator(new KeyCodeCombination(KeyCode.N, KeyCombination.CONTROL_DOWN));
		MenuItem openFile = new MenuItem("Open File");
		openFile.setAccelerator(new KeyCodeCombination(KeyCode.O, KeyCombination.CONTROL_DOWN));
		
		SeparatorMenuItem separator = new SeparatorMenuItem();
		
		MenuItem closeTab = new MenuItem("Close Tab");
		closeTab.setAccelerator(new KeyCodeCombination(KeyCode.W, KeyCombination.CONTROL_DOWN));
		
		newFile.setOnAction(new EventHandler<ActionEvent>()
		{
	        @Override
	        public void handle(ActionEvent e) 
	        {
	        	parent.openDocument();
	        }
	    });
		
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
		
		closeTab.setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent e)
			{
				parent.closeCurrentTab();
			}
		});
		
		Menu edit = new Menu("Edit");
		MenuItem undo = new MenuItem("Undo");
		MenuItem redo = new MenuItem("Redo");
		
		Menu font = new Menu("Font");
		
		Menu Color = new Menu("Color");
		
		Menu help = new Menu("Help");

		file.getItems().addAll(newFile, openFile, separator, closeTab);
		getMenus().add(file);
		edit.getItems().addAll(undo, redo);
		getMenus().add(edit);
		
		// Handle in application state.
		// this.setVisible(false);
		// this.setManaged(false);
	}
}
