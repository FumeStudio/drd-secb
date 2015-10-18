package com.secb.android.controller.backend;

import android.util.Log;

import com.secb.android.model.User;

import net.comptoirs.android.common.helper.Logger;
import net.comptoirs.android.common.helper.Utilities;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class LoginXmlGenerator {
    private static final String TAG = "LoginXmlGenerator";
    User user;
    HashMap<String,String>rootAttributes ;


    public LoginXmlGenerator(User user) {
        this.user = user;
        rootAttributes = new HashMap<>();
        rootAttributes.put("xmlns:xsi","http://www.w3.org/2001/XMLSchema-instance");
        rootAttributes.put("xmlns:xsd","http://www.w3.org/2001/XMLSchema");
        rootAttributes.put("xmlns:soap", "http://schemas.xmlsoap.org/soap/envelope/");
    }

    public String getLoginXml() {
        String loginOutputXml = "";
        try
        {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            // root element
            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement(ServerKeys.LOGEN_KEY_SOAP_ENVELOPE);
            doc.appendChild(rootElement);


            //add attribute to root element
            if(rootAttributes!=null && rootAttributes.size()>0)
            {
                Set<String> keys = rootAttributes.keySet();
                if(keys!=null && keys.size()>0)
                {
                    for (String key : keys)
                    {
                        if (rootAttributes.containsKey(key) &&
                                !(Utilities.isNullString(rootAttributes.get(key))))
                        {
                            rootElement.setAttribute(key, rootAttributes.get(key));
//                            Attr attr = doc.createAttribute(key);
//                            attr.setValue(rootAttributes.get(key));
//                            rootElement.setAttributeNode(attr);
                        }
                    }
                }

            }

            //add child <soap:Body> to rootElement
            Element soapBody = doc.createElement(ServerKeys.LOGEN_KEY_SOAP_BODY);
            rootElement.appendChild(soapBody);

            //add <Login > to  <soap:Body>
            Element login = doc.createElement(ServerKeys.LOGEN_KEY_LOGIN);
            soapBody.appendChild(login);
            login.setAttribute("xmlns", "http://schemas.microsoft.com/sharepoint/soap/");

            //add <username> to <login>
            Element userName = doc.createElement(ServerKeys.LOGEN_KEY_USER_NAME);
            userName.appendChild(doc.createTextNode(user.userName));
            login.appendChild(userName);

            //add <username> to <login>
            Element password = doc.createElement(ServerKeys.LOGEN_KEY_PASSWORD);
            password.appendChild(doc.createTextNode(user.password));
            login.appendChild(password);

            //output xml

            DOMSource source = new DOMSource(doc);
            StringWriter writer = new StringWriter();
            StreamResult result =  new StreamResult(writer);

            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            transformer.transform(source, result);
            loginOutputXml = writer.getBuffer().toString().replaceAll("\n|\r", "");

            Logger.instance().v(TAG,""+loginOutputXml);

        } catch (Exception e) {
            e.printStackTrace();
            Logger.instance().v(TAG, "" + Log.getStackTraceString(e));
        }

        return loginOutputXml;
    }

    public String getCookieFromLoginXml(String loginXml)
    {
        XmlParser parser = new XmlParser();
        Document doc = parser.getDomElement(loginXml);
        NodeList nodeList= doc.getElementsByTagName(ServerKeys.LOGEN_KEY_LOGIN_RESULT);
        Element loginResult = (Element) nodeList.item(0);
        String cookievalue = parser.getValue(loginResult,ServerKeys.LOGEN_KEY_COOKIE);
        return cookievalue;
    }
}
