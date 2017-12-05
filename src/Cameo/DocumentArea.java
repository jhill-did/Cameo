package Cameo;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.*;
import Cameo.Model.ApplicationModel;
import Cameo.Model.Document;
import javafx.beans.value.*;
import javafx.collections.*;

public class DocumentArea extends StackPane
{
	ApplicationModel model;
	final TabPane tabPane = new TabPane();
	CameoApp parent;
	
	public DocumentArea(CameoApp parent, ApplicationModel model)
	{		
		this.parent = parent;
		this.model = model;
		
		// It's pretty stupid, but this connects model.selectedDocument to the tabPane's selectedDocument value.
		tabPane.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>()
		{
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue)
			{
				model.selectedDocumentIndex.set(newValue.intValue());
			}
		});

		model.selectedDocumentIndex.addListener(new ChangeListener<Number>()
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
						DocumentTab removedDocumentTab = (DocumentTab) change.getRemoved().get(0);
						int targetDocumentIndex = -1;
						for (int index = 0; index < model.documents.size(); index++)
						{
							if (model.documents.get(index).id.equals(removedDocumentTab.document.id))
							{
								targetDocumentIndex = index;
								break;
							}
						}
						
						if (targetDocumentIndex >= 0)
						{
							model.documents.remove(targetDocumentIndex);
						}
					}
				}
			}
		});
		
		
		DocumentArea self = this;
		model.documents.addListener(new ListChangeListener<Document>()
		{
			@Override
			public void onChanged(Change<? extends Document> change)
			{
				while (change.next())
				{
					if (change.wasAdded())
					{
						tabPane.getTabs().add(new DocumentTab(self, change.getAddedSubList().get(0)));
					}
					
					if (change.wasRemoved())
					{
						String removedDocumentId = change.getRemoved().get(0).id;

						int targetTabIndex = -1;
						for (int index = 0; index < tabPane.getTabs().size(); index++)
						{
							DocumentTab currentTab = (DocumentTab) tabPane.getTabs().get(index);
							if (currentTab.document.id.equals(removedDocumentId))
							{
								targetTabIndex = index;
								break;
							}
						}
						
						if (targetTabIndex >= 0)
						{
							tabPane.getTabs().remove(targetTabIndex);
						}
					}
				}
			}
		});
		
		this.getChildren().add(tabPane);
	}
	
	public DocumentTab getCurrentDocumentTab()
	{
		if (model.selectedDocumentIndex.get() > -1)
		{
			return (DocumentTab) tabPane.getTabs().get(model.selectedDocumentIndex.get());
		}
		
		return null;
	}
}
