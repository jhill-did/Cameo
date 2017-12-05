package Cameo;
import java.io.File;

import javax.swing.JPopupMenu.Separator;
import javax.swing.plaf.metal.MetalPopupMenuSeparatorUI;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.*;
import javafx.stage.*;
import javafx.scene.input.*;

public class CameoMenuBar extends MenuBar 
{
	public CameoMenuBar(CameoApp parent)
	{
		
		MenuItem newFile = new MenuItem("New File");
		newFile.setAccelerator(new KeyCodeCombination(KeyCode.N, KeyCombination.CONTROL_DOWN));
		newFile.setOnAction(new EventHandler<ActionEvent>()
		{
	        @Override
	        public void handle(ActionEvent e) 
	        {
	        	parent.openDocument();
	        }
	    });
		
		MenuItem openFile = new MenuItem("Open File...");
		openFile.setAccelerator(new KeyCodeCombination(KeyCode.O, KeyCombination.CONTROL_DOWN));
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
		
		MenuItem save = new MenuItem("Save");
		save.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN));
		save.setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent arg0)
			{
				parent.saveCurrentTab();
			}
		});
		
		MenuItem saveAs = new MenuItem("Save As...");
		saveAs.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN, KeyCombination.SHIFT_DOWN));
		saveAs.setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent arg0)
			{
				parent.saveAsCurrentTab();
			}
		});
		
		MenuItem closeTab = new MenuItem("Close Tab");
		closeTab.setAccelerator(new KeyCodeCombination(KeyCode.W, KeyCombination.CONTROL_DOWN));
		closeTab.setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent e)
			{
				parent.closeCurrentTab();
			}
		});
		
		MenuItem closeAllTabs = new MenuItem("Close All Tabs");
		closeAllTabs.setAccelerator(new KeyCodeCombination(KeyCode.W, KeyCombination.CONTROL_DOWN, KeyCombination.SHIFT_DOWN));
		closeAllTabs.setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent e)
			{
				while (parent.model.documents.size() > 0)
				{
					parent.closeCurrentTab();
				}
			}
		});

		MenuItem exit = new MenuItem("Exit");
		exit.setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent e)
			{
				Platform.exit();
				System.exit(0);
			}
		});
		
		Menu file = new Menu("File");
		file.getItems().addAll(newFile, openFile, save, saveAs, new SeparatorMenuItem(), closeTab, closeAllTabs, new SeparatorMenuItem(), exit);
		getMenus().add(file);
		
		MenuItem undo = new MenuItem("Undo");
		undo.setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent e)
			{
				parent.undoCurrentTab();
			}
		});
		
		MenuItem redo = new MenuItem("Redo");
		redo.setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent e)
			{
				parent.redoCurrentTab();
			}
		});
		
		MenuItem preferences = new MenuItem("Preferences...");
		
		Menu edit = new Menu("Edit");
		edit.getItems().addAll(undo, redo, new SeparatorMenuItem(), preferences);
		getMenus().add(edit);

		MenuItem helpContents = new MenuItem("Help Contents...");
		helpContents.setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent e)
			{
				parent.openDocument(new File("./README.md"));
			}
		});
		
		MenuItem about = new MenuItem("About...");
		about.setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent e)
			{
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("About Cameo");
				alert.setHeaderText("Created by Jordan Hill");
				alert.setContentText("Visit www.github.com/puddin482/cameo\nSee the help contents for more information on how to use Cameo.\n");
				
				alert.showAndWait();
			}
		});

		Menu help = new Menu("Help");
		help.getItems().addAll(helpContents, new SeparatorMenuItem(), about);
		getMenus().add(help);
		
		// Handle in application state.
		// this.setVisible(false);
		// this.setManaged(false);
	}
}
