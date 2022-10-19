
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Main extends Exception {
    public static void main(String[] args) throws ParserConfigurationException, TransformerException {
        //создаем файл data.csv
//        String[] employee = "1,John,Smith,USA,25".split(",");
//        String[] employee2 = "2,Inav,Petrov,RU,23".split(",");
//        try (CSVWriter writer = new CSVWriter(new FileWriter("data.csv"))) {
//            writer.writeNext(employee);
//            writer.writeNext(employee2);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        // создаем файл data.xml
//        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//        DocumentBuilder bulder = factory.newDocumentBuilder();
//        Document document = bulder.newDocument();
//
//        Element stuff = document.createElement("stuff");
//        document.appendChild(stuff);
//        Element employee = document.createElement("employee");
//        stuff.appendChild(employee);
//        Element id = document.createElement("id");
//        id.appendChild(document.createTextNode("1"));
//        employee.appendChild(id);
//        Element firstName = document.createElement("firstName");
//        firstName.appendChild(document.createTextNode("John"));
//        employee.appendChild(firstName);
//        Element lastName = document.createElement("lastName");
//        lastName.appendChild(document.createTextNode("Smith"));
//        employee.appendChild(lastName);
//        Element country = document.createElement("country");
//        country.appendChild(document.createTextNode("USA"));
//        employee.appendChild(country);
//        Element age = document.createElement("age");
//        age.appendChild(document.createTextNode("25"));
//        employee.appendChild(age);
//
//        Element employee2 = document.createElement("employee");
//        stuff.appendChild(employee2);
//        Element id2 = document.createElement("id");
//        id2.appendChild(document.createTextNode("2"));
//        employee2.appendChild(id2);
//        Element firstName2 = document.createElement("firstName");
//        firstName2.appendChild(document.createTextNode("Ivan"));
//        employee2.appendChild(firstName2);
//        Element lastName2 = document.createElement("lastName");
//        lastName2.appendChild(document.createTextNode("Petrov"));
//        employee2.appendChild(lastName2);
//        Element country2 = document.createElement("country");
//        country2.appendChild(document.createTextNode("RU"));
//        employee2.appendChild(country2);
//        Element age2 = document.createElement("age");
//        age2.appendChild(document.createTextNode("23"));
//        employee2.appendChild(age2);
//
//        DOMSource domSource = new DOMSource(document);
//        StreamResult streamResult = new StreamResult(new File("data.xml"));
//
//        TransformerFactory transformerFactory = TransformerFactory.newInstance();
//        Transformer transformer = transformerFactory.newTransformer();
//        transformer.transform(domSource,streamResult);
//
//
        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
        String fileNameCSV = "data.csv";
        String fileNameJson = "data1.json";
        String fileNameJson2 = "data2.json";
        String fileNameXML = "data.xml";

        List<Employee> listCsv = parseCSV(columnMapping, fileNameCSV);
        String json1 = listToJson(listCsv);
        writeString(json1, fileNameJson);

        List<Employee> listXML = parseXML(fileNameXML);
        String json2 = listToJson(listXML);
        writeString(json2, fileNameJson2);
    }

    private static List<Employee> parseCSV(String[] columnMapping, String fileName) {
        List<Employee> data = null;
        try (CSVReader csvReader = new CSVReader(new FileReader(fileName))) {
            ColumnPositionMappingStrategy<Employee> strategy = new ColumnPositionMappingStrategy<>();
            strategy.setType(Employee.class);
            strategy.setColumnMapping(columnMapping);
            CsvToBean<Employee> csv = new CsvToBeanBuilder<Employee>(csvReader)
                    .withMappingStrategy(strategy)
                    .build();
            data = csv.parse();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

    public static String listToJson(List<Employee> list) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        Type listType = new TypeToken<List<Employee>>() {}.getType();
        return gson.toJson(list, listType);
    }

    private static void writeString(String json, String fileName) {
        try (FileWriter file = new FileWriter(fileName)) {
            file.write(json);
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static List<Employee> parseXML(String xml) {
        Document doc = null;
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            doc = builder.parse(new File(xml));
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }

        Node root = doc.getDocumentElement();
        NodeList nodeList = root.getChildNodes();

        List<Employee> employees = new ArrayList<>();

        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);

            if (Node.ELEMENT_NODE == node.getNodeType()) {
                Element element = (Element) node;
                employees.add(new Employee(
                        Long.parseLong(getTextContent("id", element)),
                        getTextContent("firstName", element),
                        getTextContent("lastName", element),
                        getTextContent("country", element),
                        Integer.parseInt(getTextContent("age", element))
                ));
            }
        }
        return employees;
    }

    private static String getTextContent(String tag, Element element) {
        return element.getElementsByTagName(tag).item(0).getTextContent();
    }
}



