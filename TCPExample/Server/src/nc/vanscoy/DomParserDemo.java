package nc.vanscoy;

import java.io.File;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

public class DomParserDemo {

   public static void main(String[] args) {

      try {
         File inputFile = new File("buisnesses.xml");             
         DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
         DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
         Document doc = dBuilder.parse(inputFile);         
         doc.getDocumentElement().normalize();
         System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
         NodeList nList = doc.getElementsByTagName("business");
         System.out.println(nList.getLength());
         System.out.println("----------------------------");
         
         for (int i = 0; i < nList.getLength(); i++) {
            Node nNode = nList.item(i);
            System.out.println("\nCurrent Element :" + nNode.getNodeName());
            
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
               Element eElement = (Element) nNode;
               /*System.out.println("Student roll no : " 
                  + eElement.getAttribute("rollno"));*/
               System.out.println("Name : " 
                  + eElement
                  .getElementsByTagName("name")
                  .item(0)
                  .getTextContent());
               System.out.println("Postal Code : " 
                  + eElement
                  .getElementsByTagName("postal")
                  .item(0)
                  .getTextContent());
               System.out.println("Website : " 
                  + eElement
                  .getElementsByTagName("website")
                  .item(0)
                  .getTextContent());
               NodeList feedback = eElement.getElementsByTagName("feedback");
               for(int j=0; j < feedback.getLength(); j++)
               {
            	   System.out.println("Date : " 
                           + eElement
                           .getElementsByTagName("date")
                           .item(0)
                           .getTextContent());
            	   
            	   System.out.println("Rating : " 
                           + eElement
                           .getElementsByTagName("rating")
                           .item(0)
                           .getTextContent());
            	   
            	   System.out.println("Comment : " 
                           + eElement
                           .getElementsByTagName("comment")
                           .item(0)
                           .getTextContent());
               }
            }
         }
      } catch (Exception e) {
         e.printStackTrace();
      }         
   }
}
