package Cameo;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

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
import javafx.collections.ListChangeListener;

public class CameoApp extends Application
{
	
	public static void main(String[] args)
	{
		CameoApp.launch(args);
	}

	public Stage mainStage;
	ApplicationModel model = new ApplicationModel();
	
	@Override
	public void start(Stage primaryStage) throws Exception
	{		
		mainStage = primaryStage;
		model.documents.addListener(new ListChangeListener<Document>() {

			@Override
			public void onChanged(Change<? extends Document> change)
			{
				while (change.next())
				{
					if (change.wasRemoved())
					{
						Document removed = change.getRemoved().get(0);
						System.out.println(removed.filename);
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

		DocumentArea documentArea = new DocumentArea(model);
		
		root.setCenter(documentArea);
		
		// Setup status bar.
		Label statusLabel = new Label();
		statusLabel.textProperty().bind(model.statusMessage);
		model.statusMessage.set("Hello World");

		HBox statusBar = new HBox();
		statusBar.getStyleClass().add("status-bar");
		statusBar.getChildren().add(statusLabel);
		
		root.setBottom(statusBar);
		
		// Show me what you got.
		primaryStage.show();
		
		// Test automatically open file.
		openDocument(new File("E:/Users/Puddin/Documents/Projects/Tools/Cameo/src/Cameo/CameoApp.java"));
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
			temp.filename = "Untitled";
			temp.fullPath = null;
			temp.content = "";
		}
		else
		{
			// Load file into document.
			temp = loadDocument(file);
		}
		
		model.documents.add(temp);
		model.selectedDocument.set(model.documents.size() - 1);
	}
	
	private Document loadDocument(File file)
	{
		Document output = new Document();
		output.filename = file.getName();
		output.fullPath = file.getPath();
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
	
	public void closeCurrentTab()
	{
		model.documents.remove(model.selectedDocument.get());
	}
}
