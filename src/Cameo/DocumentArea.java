package Cameo;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.*;
import Cameo.Model.ApplicationModel;
import Cameo.Model.Document;
import javafx.beans.property.Property;
import javafx.beans.value.*;
import javafx.event.*;
import javafx.collections.*;

public class DocumentArea extends StackPane
{
	public DocumentArea(ApplicationModel model)
	{		
		final TabPane tabPane = new TabPane();
		
		// It's pretty stupid, but this connects model.selectedDocument to the tabPane's selectedDocument value.
		tabPane.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>()
		{
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue)
			{
				model.selectedDocument.set(newValue.intValue());
			}
		});
		model.selectedDocument.addListener(new ChangeListener<Number>()
		{
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue)
			{
				tabPane.getSelectionModel().select(newValue.intValue());
			}
		});
		
		tabPane.getTabs().addListener(new ListChangeListener<Tab>()
		{
			@Override
			public void onChanged(Change<? extends Tab> change)
			{
				while (change.next())
				{
					if (change.wasRemoved())
					{
						// System.out.println(change.getRemoved().get(0).getText());
					}
				}
			}
		});
		
		model.documents.addListener(new ListChangeListener<Document>()
		{
			@Override
			public void onChanged(Change<? extends Document> change)
			{
				while (change.next())
				{
					if (change.wasAdded())
					{
						tabPane.getTabs().add(new DocumentTab(change.getAddedSubList().get(0)));
					}
				}
			}
		});
		
		this.getChildren().add(tabPane);
	}
}
