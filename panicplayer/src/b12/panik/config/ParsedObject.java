package b12.panik.config;

import java.io.*;
import java.net.URL;
import java.util.*;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import b12.panik.util.*;

/**
 * This Object is used to easily convert from XML DOM to a simple data structure
 * using Collection classes. DOM Elements are represented by a
 * <code>ParsedObject</code> and their associated information is saved in
 * structures from the collection classes. Child elements are represented in a
 * <code>List</code> of <code>ParsedObject</code>s. Attributes are saved in a
 * <code>Map</code> conaining a name-value pair for each Attribute.
 *
 * @author Kariem Hussein
 * @version 10.12.2002
 */
public class ParsedObject
{

   /** Owner document of this instance's XML representation */
   Document document;

   /**
    * List of <code>ParsedObject</code>s representing children of this
    * <code>Node</code> in the DOM tree.
    */
   List children;

   /** <code>Map</code> that holds information this object's attributes */
   Map attributes;

   /** String representing this object's unique id */
   String id = null;

   /** Name of this <code>ParsedObject</code> */
   String name;

   /** Text this <code>ParsedObject</code> holds */
   String text;

   /** Parent of the current node  */
   ParsedObject parent;

   /**
    * Builds a new <code>ParsedObject</code> from an XML element. This
    * constructor builds the fields name, text, attributes and children from
    * the information conained in the XML element. An attribute with the name
    * <code>id</code> will not be added to the list of attributes. Instead the
    * field id will be set with the adequate information.
    * 
    * @param parentObject sets a parent that may differ from the DOM Element's
    *         parent
    * @param element the element that holds the data for this ParsedObject
    */
   public ParsedObject(ParsedObject parentObject, Element element)
   {
      document = element.getOwnerDocument();
      //xmlElement = element;
      // set name of this Object to the tagname of the element
      name = element.getNodeName();
      //initialize with correct sizes
      int nbAttrs = element.getAttributes().getLength();
      int nbChildren = element.getChildNodes().getLength();
      init(parentObject, nbAttrs, nbChildren);
      // read attributes and map them and their values accordingly
      NamedNodeMap nodeMap = element.getAttributes();
      if (nodeMap.getLength() > 0)
      {
         for (int i = 0; i < nodeMap.getLength(); i++)
         {
            Node current = nodeMap.item(i);
            if (current.getNodeName().equals("id"))
            {
               id = current.getNodeValue();
            }
            attributes.put(current.getNodeName(), current.getNodeValue());
         }
      }
      // read child elements and add them as ParsedObjects
      List childElements = XmlParser.getAllChildElements(element);
      if (childElements.size() > 0)
      {
         for (Iterator i = childElements.iterator(); i.hasNext();)
         {
            Element child = (Element) i.next();
            children.add(new ParsedObject(this, child));
         }
      }
      // set Text to possible value of a containing text node
      NodeList elementNodes = element.getChildNodes();
      for (int j = 0; j < elementNodes.getLength(); j++)
      {
         Node n = elementNodes.item(j);
         if (n.getNodeType() == Node.TEXT_NODE)
         {
            String content = n.getNodeValue().trim();
            if (content.length() > 0)
            {
               text = content;
            }
            break; // if content.length < 1 then text will remain null;
         }
      }
   }

   /**
    * Builds a new <code>ParsedObject</code> from an DOM Element. The parent is
    * set to <code>null</code>.
    * 
    * @param element the element that holds the data for this ParsedObject
    */
   public ParsedObject(Element element)
   {
      this(null, element);
   }

   /**
    * Builds a new <code>ParsedObject</code> with the given name. No new
    * document is being created for this instance, which saves some resources.
    * 
    * @param theName The name of the new <code>ParsedObject</code>.
    * 
    * @throws ParserConfigurationException If there is a problem in the XML
    *          configuration
    * @see ParsedObject#ParsedObject(String, boolean)
    */
   public ParsedObject(String theName) throws ParserConfigurationException
   {
      this(theName, true);
   }

   /**
    * Builds a new <code>ParsedObject</code> with the given name.
    * 
    * @param theName The name of the new <code>ParsedObject</code>.
    * @param reuseOldDocument Indicates if a new <code>Document</code> should
    *         be created or not.
    * 
    * @throws ParserConfigurationException If there is a problem in the XML
    *          configuration
    */
   public ParsedObject(String theName, boolean reuseOldDocument)
      throws ParserConfigurationException
   {
      this(
         null,
         reuseOldDocument ? XmlParser.getReuseableDocument() : XmlParser.newDocument(),
         theName);
   }

   ParsedObject(ParsedObject parentObject, Document ownerDocument, String theName)
   {
      init(parentObject);
      document = ownerDocument;
      this.name = theName;
   }

   private void init(ParsedObject parentObject)
   {
      init(parentObject, 0, 0);
   }

   private void init(ParsedObject parentObject, int nbAttrs, int nbChildren)
   {
      this.parent = parentObject;
      // intialize attributes
      attributes = nbAttrs < 1 ? new HashMap() : new HashMap(nbChildren);
      // initialize children
      children = nbChildren < 1 ? new ArrayList() : new ArrayList(nbChildren);
   }

   /**
    * May be used to detect empty children lists.
    * 
    * @return <code>false</code> if the children list is <code>null</code> or
    *          empty, otherwise <code>true</code>.
    */
   public boolean hasChildren()
   {
      return children == null ? false : children.size() > 0 ? true : false;
   }

   /**
    * Shows the existence of attributes.
    * 
    * @return <code>false</code>, if the attribute map is <code>null</code> or
    *          empty, otherwise <code>true</code>.
    */
   public boolean hasAttributes()
   {
      return attributes == null ? false : attributes.size() > 0 ? false : true;
   }

   /**
    * Indicates if this instance has the attribute identified by
    * <code>attrName</code>.
    * 
    * @param attrName The name of the attribute.
    * 
    * @return <code>true</code>, if this instance has this attribute, 
    *          <code>false</code> otherwise.
    */
   public boolean hasAttribute(String attrName)
   {
      return attributes == null ? false : attributes.containsKey(attrName);
   }

   /**
    * This method returns the attributes of this Object as a <code>map</code>.
    * The attribute names are in the keyset and their values map to them
    * accordingly. <br>
    * <em>Note:</em> The attribute <code>id</code> is not included in this
    * <code>Map</code>. It may be accessed via the method {@link #getId()}.
    * 
    * @return the attributes of this Object.
    */
   public Map getAttributes()
   {
      return attributes;
   }

   /**
    * Returns the value of an attribute identified by its <code>name</code>.
    * 
    * @param attrName The name of the attribute.
    * 
    * @return A String containing the value of the attribute identified by
    *          <code>attrName</code>. Returns <code>null</code> if no attribute
    *          by this name can be found.
    */
   public String getAttribute(String attrName)
   {
      return attributes == null ? null : (String) attributes.get(attrName);
   }

   /**
    * Returns the value of an attribute identified by its <code>name</code> as
    * an <code>int</code>.
    * 
    * @param attrName The name of the attribute.
    * @return An <code>int</code> containing the value of the attribute
    *          identified by <code>attrName</code>. If no such attribute can be
    *          found <code>0</code> is returned.
    * 
    * @throws NumberFormatException if the string does not contain a parseable
    *          integer.
    * 
    * @see #getAttributeInt(String, int) 
    * 
    * @since 12 Jul 2003
    */
   public int getAttributeInt(String attrName) throws NumberFormatException
   {
      return getAttributeInt(attrName, 0);
   }

   /**
    * Returns the value of an attribute identified by its <code>name</code> as
    * an <code>int</code>.
    * 
    * @param attrName The name of the attribute.
    * @param defaultInt the default value, if <code>attrName</code> was not
    *         found.
    * @return An <code>int</code> containing the value of the attribute
    *          identified by <code>attrName</code>. If no such attribute can be
    *          found <code>defaultInt</code> is returned.
    * 
    * @throws NumberFormatException if the string does not contain a parseable
    *          integer.
    * 
    * @see #getAttributeInt(String)
    *  
    * @since 18 Jul 2003
    */
   public int getAttributeInt(String attrName, int defaultInt) throws NumberFormatException
   {
      String s = attributes == null ? null : (String) attributes.get(attrName);
      if (s == null)
      {
         return defaultInt;
      }
      return Integer.parseInt(s);
   }

   /**
    * Returns the value of an attribute identified by its <code>name</code> as
    * a <code>long</code>.
    * 
    * @param attrName The name of the attribute.
    * @return An <code>int</code> containing the value of the attribute
    *          identified by <code>attrName</code>. If no such attribute can be
    *          found <code>0</code> is returned.
    * 
    * @throws NumberFormatException if the string does not contain a parseable
    *          long.
    */
   public long getAttributeLong(String attrName) throws NumberFormatException
   {
      String s = attributes == null ? null : (String) attributes.get(attrName);
      if (s == null)
      {
         return 0;
      }
      return Long.parseLong(s);
   }

   /**
    * Returns the value of an attribute identified by its <code>name</code> as
    * <code>boolean</code>. If the attribute cannot be found <code>false</code>
    * ist returned.
    * 
    * @param attrName The name of the attribute.
    * @return A <code>boolean</code> value containing the value of the
    *          attribute identified by . If <code>attrName</code> can not be
    *          found, <code>false</code> is returned.
    *
    * @see #getAttributeBoolean(String, boolean) 
    * 
    * @since 12 Jul 2003
    */
   public boolean getAttributeBoolean(String attrName)
   {
      return getAttributeBoolean(attrName, false);
   }

   /**
    * Returns the value of an attribute identified by its <code>name</code> as
    * <code>boolean</code>. If the attribute cannot be found <code>false</code>
    * ist returned.
    * 
    * @param attrName The name of the attribute.
    * @param defaultBool the default value to return if <code>name</code> is
    *         not an attribute of this object.
    * 
    * @return A <code>boolean</code> value containing the value of the
    *          attribute identified by . If <code>attrName</code> can not be
    *          found <code>defaultBool</code> is returned.
    * 
    * @see Boolean#valueOf(java.lang.String)
    * 
    * @since 12 Jul 2003
    */
   public boolean getAttributeBoolean(String attrName, boolean defaultBool)
   {
      String s = attributes == null ? null : (String) attributes.get(attrName);
      if (s == null)
      {
         return defaultBool;
      }
      return Boolean.valueOf(s).booleanValue();
   }

   /**
    * Adds an attribute to this instance of <code>ParsedObject</code>.
    * @param attrName The name of the newly created attribute.
    * @param value The value of the attribute.
    */
   public void addAttribute(String attrName, String value)
   {
      attributes.put(attrName, value);
   }

   /**
    * Returns the name of this <code>ParsedObject</code>. This name corresponds
    * to the tagname of the DOM Element.
    * 
    * @return the name of this Object.
    */
   public String getName()
   {
      return name;
   }

   /**
    * Sets the name of this <code>ParsedObject</code>.
    * 
    * @param newName the new name.
    */
   public void setName(String newName)
   {
      name = newName;
   }

   /**
    * Provides a list of <code>ParsedObject</code> that correspond to the child
    * DOM Elements of the DOM Element that is described by this
    * <code>ParsedObject</code>
    * 
    * @return a list of <code>ParsedObject</code> representing the children
    *          of this Object.
    */
   public List getChildren()
   {
      return children;
   }

   /**
    * May be used to set the children of this <code>ParsedObject</code>.
    * 
    * @param c a list of <code>ParsedObjects</code>.
    */
   public void setChildren(List c)
   {
      children = c;
   }

   /**
    * Adds a new child to the list of children.
    * 
    * @param newChild the new child.
    */
   public void addChild(ParsedObject newChild)
   {
      newChild.setParent(this);
      children.add(newChild);
   }

   /**
    * <p>Returns the <code>ParsedObject</code> representing an element
    * identified its <code>childName</code>. This method allows for quick
    * traversal of a <code>ParsedObject</code> to find a single child and
    * returns the first child encountered with <code>childName</code> as its
    * name.</p>
    * 
    * <p>If the intention is to retrieve different children please use the
    * method {@link #getChildren()} and traverse the returned
    * <code>List</code>.</p> 
    * 
    * @param childName the name of the child.
    * @return <code>ParsedObject</code> representing an element identified by
    *          <code>childName</code>, or <code>null</code> if no element with
    *          the given name can be found.
    */
   public ParsedObject getChild(String childName)
   {
      for (Iterator iter = getChildren().iterator(); iter.hasNext();)
      {
         ParsedObject element = (ParsedObject) iter.next();
         if (element.getName().equalsIgnoreCase(childName))
         {
            return element;
         }
      }
      return null;
   }
   
   /**
    * Removes <code>o</code> from the list of children.
    * 
    * @param o the object to remove.
    * @return see {@link List#remove(int)}.
    */
   public boolean removeChild(ParsedObject o)
   {
      return children.remove(o);
   }

   private static Element createXml(ParsedObject o, Document d)
   {
      Element e = d.createElement(o.getName());
      Map attrs = o.getAttributes();
      // set attributes
      for (Iterator i = attrs.entrySet().iterator(); i.hasNext();)
      {
         Map.Entry entry = (Map.Entry) i.next();
         e.setAttribute((String) entry.getKey(), (String) entry.getValue());
      }
      // set children
      for (Iterator i = o.getChildren().iterator(); i.hasNext();)
      {
         ParsedObject child = (ParsedObject) i.next();
         e.appendChild(createXml(child, d));
      }
      String t = o.getText();
      if ((t != null) && (!t.equals("")))
      {
         e.appendChild(d.createTextNode(t));
      }
      return e;
   }

   /**
    * Adds a new empty child with the given name and returns the newly created
    * child.
    * 
    * @param childName the name of the child that is created.
    * 
    * @return the newly created child.
    */
   public ParsedObject addChild(String childName)
   {
      // create new ParsedObject
      ParsedObject newChild = new ParsedObject(this, document, childName);
      children.add(newChild);
      return newChild;
   }

   /**
    * Updates an existing child, overwriting its position with a new one.
    * 
    * @param pos the position of the old object
    * @param updatedChild the new object.
    */
   public void setChild(int pos, ParsedObject updatedChild)
   {
      if ((pos < children.size() - 1) || (pos > -1))
      {
         // update ParsedObject information
         updatedChild.setParent(this);
         // better than delete + add for ArrayList implementation
         children.set(pos, updatedChild);
      }
   }

   /**
    * Creates an empty child of this <code>ParsedObject</code>.
    * 
    * @param childName The name of the newly created child.
    * 
    * @return A new child of this <code>ParsedObject</code> with the given
    *          name.
    */
   public ParsedObject getEmptyChild(String childName)
   {
      return new ParsedObject(this, document, childName);
   }

   /**
    * Returns the object saved in the <code>parent</code> field of this
    * <code>ParsedObject</code>
    * 
    * @return the parent of this <code>ParsedObject</code>.
    */
   public ParsedObject getParent()
   {
      return parent;
   }

   /**
    * Sets the <code>parent</code> field of this object.
    * 
    * @param newParent the parent of this <code>ParsedObject</code>.
    */
   public void setParent(ParsedObject newParent)
   {
      parent = newParent;
   }

   /**
    * Returns the unique id of this object.
    * @return The unique id of this object. Returns <code>null</code>, if the id
    * was not set.
    */
   public String getId()
   {
      return id;
   }

   /**
    * This method returns the text between the enclosing opening and closing tag
    * elements of an Element. This is the text that this
    * <code>ParsedObject</code> holds.<br>
    * Example:<br>
    *   <pre>
    *       &lt;strip&gt;
    *          &lt;A&gt;value 1&lt;/A&gt;
    *          &lt;B&gt;value 2&lt;/B&gt;
    *          &lt;C&gt;value 3&lt;/C&gt;
    *          &lt;D&gt;&lt;/D&gt;
    *       &lt;/strip>
    *   </pre>
    *
    * <ul>
    * <li> A <code>ParsedObject</code> of element <code>&lt;A&gt;</code> returns
    * "value 1" as a result of calling this method.
    * <li> A <code>ParsedObject</code> of element <code>&lt;D&gt;</code> returns
    * <code>null</code>.
    * <li> A <code>ParsedObject</code> of element <code>&lt;strip&gt;</code>
    * returns <code>null</code>.
    * </ul>
    *
    * @return the text of this <code>ParsedObject</code>. If this ParsedObject
    *          does not hold any text, <code>null</code> is returned.
    */
   public String getText()
   {
      return text;
   }

   /**
    * Sets the text of this <code>ParsedObject</code>
    * @param newText The new text.
    */
   public void setText(String newText)
   {
      text = newText;
   }

   /**
    * Creates a standard XML <code>Element</code> representation of this
    * <code>ParsedObject</code>.
    * 
    * @return a newly created element that contains the same information this
    *         instance of <code>ParsedObject</code> contains.
    */
   public Element getXmlRepresentation()
   {
      return createXml(this, document);
   }

   /**
    * Creates a new <code>ParsedObject</code> from the information found in an
    * XML file. This method does not validate the XML content.
    * 
    * @param filename The filename that contains the information for the new
    *         instance.
    * 
    * @return A newly created <code>Object</code> representing the same 
    *          information found in the given XML file.
    * 
    * @throws SAXException If the XML data contains syntactic or semantic
    *          errors. These are related to non-wellformedness, because
    *          validation is disabled for this method.
    * @throws ParserConfigurationException If the underlying XML implementation
    *          and/or configuration throws errors.
    * @throws IOException If the file does not exist or is not readable.
    */
   public static ParsedObject loadFromFile(String filename)
      throws SAXException, ParserConfigurationException, IOException
   {
      return loadFromFile(new File(filename));
   }
   
   /**
    * Creates a new <code>ParsedObject</code> from the information found in an
    * XML file. This method does not validate the XML content.
    * 
    * @param file The file that contains the information for the new instance.
    * 
    * @return ParsedObject A newly created <code>Object</code> representing the
    *          same information found in the given XML file.
    *
    * @throws SAXException If the XML data contains syntactic or semantic
    *          errors. These are related to non-wellformedness, because
    *          validation is disabled for this method.
    * @throws ParserConfigurationException If the underlying XML implementation
    *          and/or configuration throws errors.
    * @throws IOException If the file does not exist or is not readable.
    * 
    * @see XmlParser#loadDocument(File). 
    */
   public static ParsedObject loadFromFile(File file)
      throws SAXException, ParserConfigurationException, IOException
   {
      return new ParsedObject(XmlParser.loadDocument(file).getDocumentElement());
   }

   /**
    * Loads a ParsedObject from an URI.
    * 
    * @param uri the address of the file to load.
    * @return A newly created <code>ParsedObject</code> representing the same 
    *          information found in the given XML file.
    * 
    * @throws SAXException If the XML data contains syntactic or semantic
    *          errors. These are related to non-wellformedness, because
    *          validation is disabled for this method.
    * @throws ParserConfigurationException If the underlying XML implementation
    *          and/or configuration throws errors.
    * @throws IOException If the file does not exist or is not readable.
    */
   public static ParsedObject loadFromURI(String uri)
      throws SAXException, ParserConfigurationException, IOException
   {
      return new ParsedObject(XmlParser.loadDocument(uri).getDocumentElement());
   }

   /**
    * Loads a ParsedObject from an URL.
    * 
    * @param url the address of the file to load.
    * @return A newly created <code>ParsedObject</code> representing the same 
    *          information found in the given XML file.
    * 
    * @throws SAXException If the XML data contains syntactic or semantic
    *          errors. These are related to non-wellformedness, because
    *          validation is disabled for this method.
    * @throws ParserConfigurationException If the underlying XML implementation
    *          and/or configuration throws errors.
    * @throws IOException If the file does not exist or is not readable.
    */
   public static ParsedObject loadFromURI(URL url)
      throws SAXException, ParserConfigurationException, IOException
   {
      return loadFromURI(url.toString());
   }

   /**
    * Creates a new <code>ParsedObject</code> from the information found in an
    * XML file.
    * 
    * @param filename The filename that contains the information for the new
    *         instance.
    * @param validation Indicates, if the content should be validated
    *  
    * @return ParsedObject A newly created <code>Object</code> representing the
    *          same information found in the given XML file.
    * 
    * @throws SAXException If the XML data contains syntactic or semantic
    *          errors. These are related to non-wellformedness, because
    *          validation is disabled for this method.
    * @throws ParserConfigurationException If the underlying XML implementation
    *          and/or configuration throws errors.
    * @throws IOException If the file does not exist or is not readable.
    */
   public static ParsedObject loadFromFile(String filename, boolean validation)
      throws SAXException, ParserConfigurationException, IOException
   {
      return new ParsedObject(
         XmlParser.loadDocument(filename, validation).getDocumentElement());
   }

   /**
    * Creates a new <code>ParsedObject</code> from the information found in an
    * input stream.
    * 
    * @param is the input stream used to retrieve data.
    * @return A newly created <code>Object</code> holding the same
    *          information found in the given XML file.
    * 
    * @throws SAXException If the XML data contains syntactic or semantic
    *          errors. These are related to non-wellformedness, because
    *          validation is disabled for this method.
    * @throws ParserConfigurationException If the underlying XML implementation
    *          and/or configuration throws errors.
    * @throws IOException If the file does not exist or is not readable.
    */
   public static ParsedObject parse(InputStream is)
      throws SAXException, IOException, ParserConfigurationException
   {
      return new ParsedObject(XmlParser.loadDocument(is).getDocumentElement());
   }

   /**
    * Creates a new <code>ParsedObject</code> from the information found in a
    * string.
    * 
    * @param string the String that holds the data.
    * @return A newly created <code>Object</code> holding the same
    *          information found in the given XML file.
    * 
    * @throws SAXException If the XML data contains syntactic or semantic
    *          errors. These are related to non-wellformedness, because
    *          validation is disabled for this method.
    * @throws ParserConfigurationException If the underlying XML implementation
    *          and/or configuration throws errors.
    * @throws IOException If the file does not exist or is not readable.
    * 
    * @see #parse(InputStream)
    */
   public static ParsedObject parse(String string)
      throws SAXException, IOException, ParserConfigurationException
   {
      InputStream is = new ByteArrayInputStream(string.getBytes());
      return parse(is);
   }


   
   /*
    * Methods to visually test internals of this object
    */

   private String toString(String prefix)
   {
      StringBuffer b = new StringBuffer(prefix + "<" + name);
      // add attributes
      if (attributes.size() > 0)
      {
         for (Iterator i = attributes.keySet().iterator(); i.hasNext();)
         {
            String key = (String) i.next();
            String value = (String) attributes.get(key);
            b.append(" " + key + "=\"" + value + "\"");
         }
      }
      b.append(">\n");
      // add children
      for (Iterator i = children.iterator(); i.hasNext();)
      {
         ParsedObject p = (ParsedObject) i.next();
         b.append(p.toString(prefix + "\t"));
      }
      // add text
      if (text != null)
      {
         b.append(prefix + "\t" + text + "\n");
      }
      b.append(prefix + "</" + name + ">\n");
      return b.toString();
   }

   /**
    * Returns a String representation of this <code>ParsedObject</code>. The
    * resulting string will not be indented.
    * 
    * @return a String that contains this <code>ParsedObject</code> with its
    *         attributes the children as XML string.
    * 
    * @see Object#toString()
    * @see #toString(boolean)
    */
   public String toString()
   {
      return toString(false);
   }

   /**
    * Returns a String representation of this <code>ParsedObject</code>.
    * 
    * @param indentation whether the output string should contain indentation.
    * 
    * @return a String that contains this <code>ParsedObject</code> with its
    *         attributes the children as XML string.
    * 
    * @see Object#toString()
    */
   public String toString(boolean indentation)
   {
      try
      {
         StringWriter w = new StringWriter();
         XmlParser.writeParsedObject(this, w, indentation);
         return w.toString();
      } catch (Exception e)
      {
         Logging.warning("Exception in ParsedObject.toString", e);
         return toString("");
      }
   }
}