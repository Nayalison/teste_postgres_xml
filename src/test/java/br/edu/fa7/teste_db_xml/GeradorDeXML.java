package br.edu.fa7.teste_db_xml;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class GeradorDeXML {
	
	private static int numeroCategoria = 1;
	private static int produtosPorCategoria = 30;
	
	public static List<String> gerarXML(int numeroDeItens) {
		 DocumentBuilderFactory icFactory = DocumentBuilderFactory.newInstance();
	        DocumentBuilder icBuilder;
	        try {
	        	List<String> lista = new ArrayList<String>();
	            icBuilder = icFactory.newDocumentBuilder();
	            
	            for(int i=1; i <=numeroDeItens; i++) {
	            	Document doc = icBuilder.newDocument();
	            	Element mainRootElement = doc.createElement("Produto");
	            	doc.appendChild(mainRootElement);
	            	mainRootElement.appendChild(getNode(doc, mainRootElement, "nome", String.format("Produto %s", i)));
	            	mainRootElement.appendChild(getNode(doc, mainRootElement, "preco", String.valueOf(1.5 +i)));
	            	
	            	if(i % produtosPorCategoria == 0) {
	        			numeroCategoria++;
	        		}
	            	mainRootElement.appendChild(getNode(doc, mainRootElement, "categoria", String.format("Categoria %s", numeroCategoria)));
	            	
	            	
	            	
	            	Transformer transformer = TransformerFactory.newInstance().newTransformer();
	            	transformer.setOutputProperty(OutputKeys.INDENT, "yes"); 
	            	DOMSource source = new DOMSource(doc);
	            	ByteArrayOutputStream ou = new ByteArrayOutputStream();
	            	StreamResult console = new StreamResult(ou);
	            	transformer.transform(source, console);
	            	
	            	lista.add(new String(ou.toByteArray()));
	            }
	 

	            return lista;
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return null;
	}

	private static Node getNode(Document doc, Element element, String name, String valor) {
		Element node = doc.createElement(name);
		node.appendChild(doc.createTextNode(valor));
		return node;
	}

}
