package Cameo;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import Cameo.Model.*;
import javafx.application.*;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.beans.binding.*;
import javafx.beans.property.*;
import javafx.event.*;
import javafx.scene.input.*;
import javafx.collections.*;
import javafx.beans.value.*;

// TODO:
// |	Add save, save as menu options.
// |	Add dialog for changing preferences:
// |		Theme
// |		Font style
// |	Add Help submenu dialogs.
// |	Disable mouse wheel scrolling on line numbers.
// | 	Ask for save when closing.

public class CameoApp extends Application
{
	
	public static void main(String[] args)
	{
		CameoApp.launch(args);
	}

	public Stage mainStage;
	ApplicationModel model = new ApplicationModel();
	DocumentArea documentArea;
	
	@Override
	public void start(Stage primaryStage) throws Exception
	{		
		mainStage = primaryStage;
		model.documents.addListener(new ListChangeListener<Document>()
		{
			@Override
			public void onChanged(Change<? extends Document> change)
			{
				while (change.next())
				{
					if (change.wasRemoved())
					{
						// Document removed = change.getRemoved().get(0);
					}
				}
			}
		});

		BorderPane root = new BorderPane();
		
		primaryStage.setTitle("Cameo");
		primaryStage.setMaximized(false);
		
		Scene scene = new Scene(root, 600, 600);
		scene.getStylesheets().add("style/core.css");
		scene.getStylesheets().add("style/materialTheme.css");
		primaryStage.setScene(scene);
		
		CameoMenuBar menuBar = new CameoMenuBar(this);
		
		root.setTop(menuBar);

		documentArea = new DocumentArea(model);
		
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
		model.documents.remove(model.selectedDocumentIndex.get());
	}
	
	public void saveCurrentTab()
	{
		if (model.selectedDocumentIndex.get() > 0)
		{
			Document selectedDocument = model.documents.get(model.selectedDocumentIndex.get());
			if (selectedDocument.fullPath.get() == null)
			{
				saveAsCurrentTab();
				return;
			}
			else
			{
				saveDocument(selectedDocument);
			}
		}
	}
	
	public void saveAsCurrentTab()
	{
		if (model.selectedDocumentIndex.get() > 0)
		{
			Document selectedDocument = model.documents.get(model.selectedDocumentIndex.get());
			FileChooser saveAsDialog = new FileChooser();
			File saveLocation = saveAsDialog.showSaveDialog(mainStage);
			if (saveLocation != null)
			{
				selectedDocument.fullPath.set(saveLocation.getPath());
				selectedDocument.filename.set(saveLocation.getName());
				saveDocument(selectedDocument);
			};
		}
	}
	
	public void saveDocument(Document document)
	{
		String currentContent = document.content;
		try
		{
			Files.write(Paths.get(document.fullPath.get()), currentContent.getBytes(), StandardOpenOption.CREATE);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
