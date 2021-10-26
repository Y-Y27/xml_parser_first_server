package com.example.xml_parser_first_server.service;

import com.example.xml_parser_first_server.model.DataToTransfer;
import com.example.xml_parser_first_server.model.PayDoc;
import com.example.xml_parser_first_server.model.ReportDoc;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

@Service
public class JaxbParser {

    private final DataTransfer dataTransfer;
    private final DomParser domParser;

    public JaxbParser(DataTransfer dataTransfer, DomParser domParser) {
        this.dataTransfer = dataTransfer;
        this.domParser = domParser;
    }

    public DataToTransfer jaxbParser() throws JAXBException, IOException, ParserConfigurationException, SAXException {

        ReportDoc reportDoc = null;
        PayDoc payDoc = null;

        JAXBContext jaxbContext = JAXBContext.newInstance(PayDoc.class, ReportDoc.class);

        File repository =
                new File("src/main/resources/xml");
        File[] files = repository.listFiles();

        if (files != null) {
            for (File file : files) {
                DocumentBuilderFactory dBfactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = dBfactory.newDocumentBuilder();

                Document document = builder.parse(new File(String.valueOf(file)));
                document.getDocumentElement().normalize();

                Element root = document.getDocumentElement();
                if (root.getNodeName().equalsIgnoreCase("SKP_REPORT_KS")) {
                    reportDoc = (ReportDoc) jaxbContext.createUnmarshaller().unmarshal(file);
                }
                if (root.getNodeName().equalsIgnoreCase("Inf_Pay_Doc")) {
                    payDoc = (PayDoc) jaxbContext.createUnmarshaller().unmarshal(file);
                }
            }
        }
        return dataTransfer.checkGUIDAndReportTypeAndSendData(reportDoc, payDoc);
    }

}
