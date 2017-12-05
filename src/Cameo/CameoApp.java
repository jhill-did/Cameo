// Created by: Jordan Hill
// Contact me at http://www.github.com/puddin482/Cameo

package Cameo;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Optional;

import Cameo.Model.*;
import javafx.application.*;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.beans.binding.*;
import javafx.beans.property.*;
import javafx.event.*;
import javafx.scene.input.*;
import javafx.collections.*;
import javafx.beans.value.*;

// TODO:
// |	Add an open file tip when no tabs are open.

public class CameoApp extends Application
{
	public static void main(String[] args)
	{
		CameoApp.launch(args);
	}

	public Stage mainStage;
	ApplicationModel model = new ApplicationModel();
	DocumentArea documentArea;
	public BorderPane root; 
	
	@Override
	public void start(Stage primaryStage) throws Exception
	{		
		mainStage = primaryStage;

		root = new BorderPane();
		
		primaryStage.setTitle("Cameo");
		primaryStage.setMaximized(false);
		
		Scene scene = new Scene(root, 600, 600);
		scene.getStylesheets().add("style/core.css");
		scene.getStylesheets().add("style/materialTheme.css");
		primaryStage.setScene(scene);
		
		CameoMenuBar menuBar = new CameoMenuBar(this);
		
		root.setTop(menuBar);

		documentArea = new DocumentArea(this, model);
		
		root.setCenter(documentArea);
		
		// Setup status bar.
		model.selectedDocumentIndex.addListener(new ChangeListener<Number>()
		{
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue)
			{
				if (model.selectedDocumentIndex.get() > -1)
				{
					String currentFilePath = model.documents.get(model.selectedDocumentIndex.get()).fullPath.get();
					model.statusMessage.set(currentFilePath);
				}
				else
				{
					model.statusMessage.set("");
				}		
			}
		});

		Label statusLabel = new Label();
		statusLabel.textProperty().bind(model.statusMessage);

		HBox statusBar = new HBox();
		statusBar.getStyleClass().add("status-bar");
		statusBar.getChildren().add(statusLabel);
		
		root.setBottom(statusBar);
		
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>()
		{
			@Override
			public void handle(WindowEvent e)
			{
				if (e.getTarget() == primaryStage)
				{
					for (int index = model.documents.size() - 1; index >= 0; index--)
					{
						model.selectedDocumentIndex.set(index);
						closeCurrentTab();
					}
				}
			}
		});
		
		// Show me what you got.
		primaryStage.show();

		// Test automatically open file.
		openDocument(new File("./src/Cameo/CameoApp.java"));
	}
    
    void openDocument()
    {
    	openDocument(null);
    }

	void openDocument(File file)
	{
		Document temp = new Document();
		if (file == null)
		{
			// If no file was specified let's make a new document.
			temp = new Document();
			temp.filename.set("Untitled");
			temp.fullPath.setValue(null);
			temp.content = "";
		}
		else
		{
			// Load file into document.
			temp = loadDocument(file);
		}

		temp.requiresSave.set(false);
		
		model.documents.add(temp);
		model.selectedDocumentIndex.set(model.documents.size() - 1);
	}
	
	private Document loadDocument(File file)
	{
		Document output = new Document();
		output.filename.set(file.getName());
		output.fullPath.set(file.getPath());
		try
		{
			output.content = String.join("\n", Files.readAllLines(file.toPath()));
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		return output;
	}
	
	public void undoCurrentTab()
	{
		DocumentTab currentDocumentTab = documentArea.getCurrentDocumentTab();
		if (currentDocumentTab != null)
		{
			documentArea.getCurrentDocumentTab().undo();
		}
	}
	
	public void redoCurrentTab()
	{
		DocumentTab currentDocumentTab = documentArea.getCurrentDocumentTab();
		if (currentDocumentTab != null)
		{
			documentArea.getCurrentDocumentTab().redo();
		}
	}
	
	public void closeCurrentTab()
	{
		Document currentDocument = model.documents.get(model.selectedDocumentIndex.get());
		if (currentDocument.requiresSave.get())
		{
			if (showRequestSaveDialog(currentDocument, null) == true)
			{
				// If this file requires a save, and the user doesn't hit cancel on the request dialog, then we should close it.
				model.documents.remove(model.selectedDocumentIndex.get());
			}
			return;
		}
		// If the file wasn't changed, then just close it without asking.
		model.documents.remove(model.selectedDocumentIndex.get());
	}
	
	public void saveCurrentTab()
	{
		if (model.selectedDocumentIndex.get() > 0)
		{
			Document selectedDocument = model.documents.get(model.selectedDocumentIndex.get());
			saveDocument(selectedDocument);
		}
	}
	
	public void saveAsCurrentTab()
	{
		if (model.selectedDocumentIndex.get() > 0)
		{
			Document selectedDocument = model.documents.get(model.selectedDocumentIndex.get());
			saveAsDocument(selectedDocument);
		}
	}
	
	public void saveDocument(Document document)
	{
		if (document.fullPath.get() == null)
		{
			saveAsDocument(document);
			return;
		}
		
		String currentContent = document.content;
		try
		{
			Files.write(Paths.get(document.fullPath.get()), currentContent.getBytes(), StandardOpenOption.CREATE);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		document.requiresSave.set(false);
	}
	
	public void saveAsDocument(Document document)
	{
		FileChooser saveAsDialog = new FileChooser();
		File saveLocation = saveAsDialog.showSaveDialog(mainStage);
		if (saveLocation != null)
		{
			document.fullPath.set(saveLocation.getPath());
			document.filename.set(saveLocation.getName());
			saveDocument(document);
		};
	}
	
	// Returns whether or not to close the document.
	// If the user selects cancel this will return false, and any relevant event will be consumed.
	public boolean showRequestSaveDialog(Document document, Event consumableEvent)
	{
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Save Changes");
		alert.setHeaderText(null);
		alert.setContentText(document.filename.get() + " has been modified. Save changes?");
		ButtonType yesButton = new ButtonType("Yes");
		ButtonType noButton = new ButtonType("No");
		ButtonType cancelButton = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
		alert.getButtonTypes().setAll(yesButton, noButton, cancelButton);
		
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == cancelButton)
		{
			if (consumableEvent != null)
			{
				consumableEvent.consume();
			}
			return false;
		}
		
		if (result.get() == yesButton)
		{
			saveDocument(document);
		}
		
		return true;
	}
}
