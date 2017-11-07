package Cameo;
import org.omg.CosNaming.NamingContextExtPackage.AddressHelper;

import Cameo.Model.Document;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class DocumentTab extends Tab
{
	Document document;
	TextArea textArea = new TextArea();
	VBox lineNumbers = new VBox();
	
	public DocumentTab(Document document)
	{
		this.document = document;
		setText(document.filename);
		
		textArea.textProperty().addListener(new ChangeListener<String>()
		{
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue)
			{
				// TODO: This is jacked up but kind of the right idea.
				int newLineCount = newValue.length() - newValue.replace("\n", "").length();
				while (lineNumbers.getChildren().size() - 1 != newLineCount)
				{
					int currentLineCount = lineNumbers.getChildren().size() - 1;
					if (currentLineCount < newLineCount)
					{
						lineNumbers.getChildren().add(new Label(currentLineCount + 2 + ""));
					}
					else
					{
						lineNumbers.getChildren().remove(currentLineCount - 1);
					}
				}
			}
		});

		textArea.setText(document.content);

		BorderPane container = new BorderPane();
		container.setLeft(lineNumbers);
		container.setCenter(textArea);
		
		setContent(container);
		
	}
	
	public String getDocumentId()
	{
		return document.id;
	}
	
	public String getDocumentPath()
	{
		return document.fullPath;
	}
	
	public String getDocumentFilename()
	{
		return document.filename;
	}
	
	public String getDocumentText()
	{
		return document.content;
	}
}
