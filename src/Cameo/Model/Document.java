package Cameo.Model;

import java.util.UUID;

public class Document
{
	public String id = UUID.randomUUID().toString();
	public String filename;
	public String fullPath;
	public String content;
}
