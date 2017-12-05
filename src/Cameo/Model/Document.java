package Cameo.Model;

import java.util.UUID;
import javafx.beans.property.*;

public class Document
{
	public String id = UUID.randomUUID().toString();
	public SimpleStringProperty filename = new SimpleStringProperty();
	public SimpleStringProperty fullPath = new SimpleStringProperty();
	public String content;
}
