package b12.panik.config;

import java.io.*;
import java.util.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.xerces.parsers.DOMParser;
import org.w3c.dom.*;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * This class provides convenience methods to access XML content.
 */
public class XmlParser
{
   private Document document;
   private static Document reusableDocument;
   private static DocumentBuilder reusableDocBuilder;
   private static Transformer transformer;
   private static DOMParser reusableParser;

   private static final CustomizedErrorHandler HANDLER = new CustomizedErrorHandler(true);   
   
   static Document parse(InputSource inputSource, boolean validation)
      throws IOException, SAXException, ParserConfigurationException
   {
      Document d = null;
      if (validation)
      {
         DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
         //Configure DocumentBuilderFactory to generate a 
         //namespace-aware, validating parser that uses XML Schema  
         factory.setNamespaceAware(true);
         factory.setValidating(true);
         factory.setAttribute(
            "http://java.sun.com/xml/jaxp/properties/schemaLanguage",
            "http://www.w3.org/2001/XMLSchema");

         DocumentBuilder builder = factory.newDocumentBuilder();
         CustomizedErrorHandler eh = new CustomizedErrorHandler(false);
         builder.setErrorHandler(eh);
         d = builder.parse(inputSource);
         if (!eh.isValid())
         {
            throw new SAXException("Document not valid: \n" + eh.getErrors());
         }
      } else
      {
         if (reusableParser == null)
         {
            reusableParser = new DOMParser();
         }
      
         synchronized (reusableParser)
         {
            HANDLER.reset();
            reusableParser.setErrorHandler(HANDLER);
            reusableParser.parse(inputSource);
            d = reusableParser.getDocument();
         }
      }      
      return d;
   }

   /**
    * Creates a new <code>Document</code> without using validation.
    * @return An instance of <code>Document</code>.
    * @throws ParserConfigurationException if the underlying XML implementation
    * throws errors.
    */
   public static Document newDocument() throws ParserConfigurationException
   {
      return getReusableBuilder().newDocument();
   }

   /**
    * Loads the document from a specified filename. This method does not
    * validate the contents of the xml file.
    * @param uri The location of the file to load.
    * @return Document An instance of the Document representing the contents
    *          of the file.
    * @throws ParserConfigurationException if the underlying XML implementation
    *          throws errors.
    * @throws SAXException if a parsing error has occured in the process of
    *          reading the file.
    * @throws IOException if the file cannot be found, is not accessible or
    *          cannot be read.
    */
   public static Document loadDocument(String uri)
      throws ParserConfigurationException, SAXException, IOException
   {
      DocumentBuilder db = getReusableBuilder();
      Document d;
      synchronized (db)
      {
         d = db.parse(uri);
      }
      return d;
   }

   
   /**
    * Loads the document from a specified input stream. This method does not
    * validate the contents of the xml file.
    * 
    * @param is the input stream with the data
    * 
    * @return Document An instance of the Document representing the contents
    *         of the file.
    * @throws ParserConfigurationException if the underlying XML implementation
    *         throws errors.
    * @throws SAXException if a parsing error has occured in the process of
    *         reading the file.
    * @throws IOException if the file cannot be found, is not accessible or
    *         cannot be read.
    */
   public static Document loadDocument(InputStream is)
      throws SAXException, IOException, ParserConfigurationException
   {
      DocumentBuilder db = getReusableBuilder();
      Document d;
      synchronized (db)
      {
         d = db.parse(is);
      }
      return d;
   }   

   /**
    * Loads the document from a specified input stream. This method does not
    * validate the contents of the xml file.
    * 
    * @param f the file containing the data.
    * 
    * @return Document An instance of the Document representing the contents
    *          of the file.
    * @throws ParserConfigurationException if the underlying XML implementation
    *          throws errors.
    * @throws SAXException if a parsing error has occured in the process of
    *          reading the file.
    * @throws IOException if the file cannot be found, is not accessible or
    *          cannot be read.
    */
   public static Document loadDocument(File f)
      throws SAXException, IOException, ParserConfigurationException
   {
      DocumentBuilder db = getReusableBuilder();
      Document d;
      synchronized (db)
      {
         d = db.parse(f);
      }
      return d;
   }

   /**
    * Loads the document from a specified filename. This method validates the
    * contents of the xml file, if the parameter <code>validation</code> is set
    * to <code>true</code>.
    * @param filename The filename of the file to load.
    * @param validation A boolean indicating the request for validation.
    *         <code>true</code> enables validation, while <code>false</code> may
    *         be compared to the method {@link #loadDocument(String)}.
    * @return Document An instance of the Document representing the contents
    *          of the file.
    * @throws ParserConfigurationException if the underlying XML implementation
    *          throws errors.
    * @throws SAXException if a parsing error has occured in the process of
    *          reading the file.
    * @throws IOException if the file cannot be found, is not accessible or
    *          cannot be read.
    */
   public static Document loadDocument(String filename, boolean validation)
      throws ParserConfigurationException, SAXException, IOException
   {
      return parse(new InputSource(filename), validation);
   }

   static class CustomizedErrorHandler extends DefaultHandler
   {
      private boolean valid = true;
      private boolean verbose;
      StringBuffer errors = new StringBuffer();

      CustomizedErrorHandler(boolean output)
      {
         this.verbose = output;
      }

      public void warning(SAXParseException ex)
      {
         logError("Warning:\t(line " + ex.getLineNumber() + ") " + ex.getMessage());
      }
      public void error(SAXParseException ex)
      {
         logError("Error:\t(line " + ex.getLineNumber() + ") " + ex.getMessage());
      }
      public void fatalError(SAXParseException ex)
      {
         logError("Fatal Error:\t(line " + ex.getLineNumber() + ") " + ex.getMessage());
      }
      public boolean isValid()
      {
         return valid;
      }

      private void logError(String message)
      {
         errors.append(message);
         errors.append("\n");
         if (verbose)
         {
            System.err.println(message);
         }
         valid = false;
      }

      public String getErrors()
      {
         return errors.toString();
      }
      
      void reset()
      {
         errors = new StringBuffer();
         valid = true;
      }

   }

   /**
    * Writes a <code>ParsedObject</code> with its contents to an
    * <code>OutputStream</code>. The given <code>ParsedObject</code> will be the
    * root element of the output. 
    * 
    * @param o a <code>ParsedObject</code> that should be saved.
    * @param writer the output writer.
    * @param indentation whether the output will be indented.
    * 
    * @throws IOException If there occured an error accessing the file system.
    * @throws ParserConfigurationException If there is an error with the
    *         underlying XML configuration.
    * @throws ParserConfigurationException
    */
   public static void writeParsedObject(ParsedObject o, Writer writer, boolean indentation)
      throws IOException, ParserConfigurationException
   {
      Properties p = null;
      try
      {
         if (transformer == null)
         {
            transformer = TransformerFactory.newInstance().newTransformer();
         }
         if (reusableDocument == null)
         {
            reusableDocument = newDocument();
         }
         Element root = o.getXmlRepresentation();

         // get copy of old properties
         synchronized (transformer)
         {
            p = transformer.getOutputProperties();
            transformer.setOutputProperty(OutputKeys.ENCODING, "ISO-8859-1");
            transformer.setOutputProperty(OutputKeys.STANDALONE, "yes");
            if (indentation)
            {
               transformer.setOutputProperty(OutputKeys.INDENT, "yes");
               transformer.setOutputProperty(
                  "{http\u003a//xml.apache.org/xslt}indent-amount",
                  "2");
            }
            transformer.transform(new DOMSource(root), new StreamResult(writer));
         }
      } catch (TransformerException e)
      {
         throw new IOException(e.getMessage());
      } finally
      {
         if (p != null)
         {
            // reset properties
            synchronized (transformer)
            {
               transformer.setOutputProperties(p);
            }
         }
      }
   }

   /**
    * Creates a <code>List</code> of <code>ParsedObject</code>s contained under
    * the root of a given file.
    * 
    * @param filename The name of the file to read data from.
    * 
    * @return A <code>List</code> of <code>ParsedObject</code>s.
    * @throws SAXException If the given file does not containt well-formed XML
    *          data.
    * @throws ParserConfigurationException If an error occured in the
    *          underlying XML configuration.
    * @throws IOException If there occured an error accessing the file system. 
    */
   public static List readParsedObjects(String filename)
      throws SAXException, ParserConfigurationException, IOException
   {

      return ParsedObject.loadFromFile(filename).getChildren();
   }

   /**
    * Returns the current XML document.
    * @return the <code>Document</code> of this parser.
    */
   public Document getDocument()
   {
      return document;
   }

   /**
    * Returns a reusable XML document.
    * 
    * @return A reusable <code>Document</code>.
    * @throws ParserConfigurationException if no reusable document exists and
    *          no new document could be created.
    */
   public static Document getReuseableDocument() throws ParserConfigurationException
   {
      if (reusableDocument == null)
      {
         reusableDocument = newDocument();
      }
      return reusableDocument;
   }

   /**
    * Returns a reusable <code>DocumentBuilder</code>. If no instance exists yet
    * a new object will be instantiated.
    * 
    * @return A new <code>DocumentBuilder</code> or a reference to an already
    *          existing.
    * 
    * @throws ParserConfigurationException If there is an error with the
    *          underlying XML configuration.
    */
   private static DocumentBuilder getReusableBuilder() throws ParserConfigurationException
   {
      if (reusableDocBuilder == null)
      {
         reusableDocBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
      }
      return reusableDocBuilder;
   }

   /**
    * Returns the child elements of a specified <code>Node</code> with a
    * given name.
    * 
    * @param parentNode the node which will be searched.
    * @param nodeName the name of the nodes to search for.
    * 
    * @return a <code>List</code> of DOM elements, that are direct children of
    *          the given parent node.
    */
   public static List getChildElements(Node parentNode, String nodeName)
   {
      NodeList nodes = parentNode.getChildNodes();
      List children = new ArrayList();
      for (int i = 0; i < nodes.getLength(); i++)
      {
         Node n = nodes.item(i);
         if (n instanceof Element)
         {
            Element e = (Element) n;
            if (e.getNodeName().equals(nodeName))
            {
               children.add(e);
            }
         }
      }
      return children;
   }

   /**
    * Returns all child elements of a specified <code>Node</code> that are
    * direct children of this <code>Node</code>.
    * @return a <code>List</code> of DOM elements, that are direct children of
    *          the given parent node.
    * @param parentNode the node which will be searched.
    */
   public static List getAllChildElements(Node parentNode)
   {
      NodeList nodes = parentNode.getChildNodes();
      List children = new ArrayList();
      for (int i = 0; i < nodes.getLength(); i++)
      {
         Node n = nodes.item(i);
         if (n instanceof Element)
         {
            children.add(n);
         }
      }
      return children;
   }
}