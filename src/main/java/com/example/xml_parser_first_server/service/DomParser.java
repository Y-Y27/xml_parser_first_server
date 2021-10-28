package com.example.xml_parser_first_server.service;

import com.example.xml_parser_first_server.model.DataToTransfer;
import com.example.xml_parser_first_server.model.PayDoc;
import com.example.xml_parser_first_server.model.ReportDoc;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class DomParser {

    private final DataTransfer dataTransfer;

    public DomParser(DataTransfer dataTransfer) {
        this.dataTransfer = dataTransfer;
    }

    public DataToTransfer domParser() throws ParserConfigurationException, IOException, SAXException {

        File repository =
                new File("src/main/resources/xml/");
        File[] files = repository.listFiles();

        ReportDoc reportDoc = new ReportDoc();
        PayDoc payDoc = new PayDoc();
        Document currentDocument;

        if (files != null) {
            for (File file : files) {

                DocumentBuilderFactory dBfactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = dBfactory.newDocumentBuilder();

                currentDocument = builder.parse(new File(String.valueOf(file)));
                currentDocument.normalize();

                Element root = currentDocument.getDocumentElement();
                if (root.getNodeName().equalsIgnoreCase("SKP_REPORT_KS")) {
                    reportDoc = parseReportDoc(currentDocument);
                }
                if (root.getNodeName().equalsIgnoreCase("Inf_Pay_Doc")) {
                    payDoc = parsePayDoc(currentDocument);
                }
            }
        }
        return dataTransfer.checkGUIDAndReportTypeAndSendData(reportDoc, payDoc);
    }

    public ReportDoc parseReportDoc(Document currentDocument) {
        NodeList docs = currentDocument.getElementsByTagName("Doc");
        NodeList report_type_flag = currentDocument.getElementsByTagName("Report_type_flag");
        List<ReportDoc.Doc> docsList = new ArrayList<>();
        ReportDoc reportDocOuter = new ReportDoc();

        for (int temp = 0; temp < docs.getLength(); temp++) {

            Node item = docs.item(temp);

            if (item.getNodeType() == Node.ELEMENT_NODE) {
                ReportDoc.Doc reportDoc = new ReportDoc.Doc();

                Element element = (Element) item;

                String reportTypeFlag = report_type_flag.item(0).getTextContent();
                String DocGUID = element.getElementsByTagName("DocGUID").item(0).getTextContent();
                String docNum = element.getElementsByTagName("DocNum").item(0).getTextContent();
                String docDate = element.getElementsByTagName("DocDate").item(0).getTextContent();
                String operType = element.getElementsByTagName("OperType").item(0).getTextContent();
                String amountOut = element.getElementsByTagName("AmountOut").item(0).getTextContent();

                reportDocOuter.setReport_type_flag(reportTypeFlag);
                reportDoc.setDocGUID(DocGUID);
                reportDoc.setDocNum(docNum);
                reportDoc.setDocDate(docDate);
                reportDoc.setOperType(operType);
                reportDoc.setAmountOut(amountOut);
                docsList.add(reportDoc);
            }
        }
        reportDocOuter.setDocsList(docsList);
        return reportDocOuter;
    }

    public PayDoc parsePayDoc(Document currentDocument) {

        PayDoc payDoc = new PayDoc();
        List<PayDoc.Doc> innerDocList = new ArrayList<>();

        NodeList docs = currentDocument.getElementsByTagName("Doc");

        for (int currentNumberOfDoc = 0; currentNumberOfDoc < docs.getLength(); currentNumberOfDoc++) {
            PayDoc.Doc payDocDoc = new PayDoc.Doc();

            Node item = docs.item(currentNumberOfDoc);

            if (item.getNodeType() == Node.ELEMENT_NODE) {

                Element element = (Element) item;
                String purpose = element.getElementsByTagName("Purpose").item(0).getTextContent();
                payDocDoc.setPurpose(purpose);

                String GUID = element.getElementsByTagName("GUID").item(0).getTextContent();
                payDocDoc.setGUID(GUID);

                NodeList infPayNodeList = currentDocument.getElementsByTagName("Inf_PAY");
                PayDoc.Doc.InfPay infPay = getInnKppCnameInfPay(infPayNodeList, currentNumberOfDoc);

                NodeList infRcpNodeList = currentDocument.getElementsByTagName("Inf_RCP");
                PayDoc.Doc.InfRcp infRcp = getInnKppCnameInfRcp(infRcpNodeList, currentNumberOfDoc);

                NodeList bank_payNodeList = currentDocument.getElementsByTagName("Bank_PAY");
                PayDoc.Doc.BankPay bankPay = getBspayBicpayAndBsKsPayBankPay(bank_payNodeList, currentNumberOfDoc);

                NodeList bank_rcpNodeList = currentDocument.getElementsByTagName("Bank_RCP");
                PayDoc.Doc.BankRCP bankRcp = getBspayBicpayAndBsKsPayBankRCP(bank_rcpNodeList, currentNumberOfDoc);

                payDocDoc.setInfPay(infPay);
                payDocDoc.setInfRcp(infRcp);
                payDocDoc.setBankPay(bankPay);
                payDocDoc.setBankRCP(bankRcp);
                innerDocList.add(payDocDoc);
            }
        }
        payDoc.setDocsList(innerDocList);

        return payDoc;
    }

    public PayDoc.Doc.InfPay getInnKppCnameInfPay(NodeList nodeList, int currentNumberOfDoc) {

        PayDoc.Doc paydocDoc = new PayDoc.Doc();
        PayDoc.Doc.InfPay infPay = new PayDoc.Doc.InfPay();

        Node item = nodeList.item(currentNumberOfDoc); // TODO DRY

        if (item.getNodeType() == Node.ELEMENT_NODE) {

            Element element = (Element) item;

            String INN_PAY = element.getElementsByTagName("INN_PAY").item(0).getTextContent();
            String KPP_PAY = element.getElementsByTagName("KPP_PAY").item(0).getTextContent();
            String CName_PAY = element.getElementsByTagName("CName_PAY").item(0).getTextContent();

            infPay.setINN_PAY(INN_PAY);
            infPay.setKPP_PAY(KPP_PAY);
            infPay.setCName_PAY(CName_PAY);
        }
        paydocDoc.setInfPay(infPay);
        return infPay;
    }

    public PayDoc.Doc.InfRcp getInnKppCnameInfRcp(NodeList nodeList, int currentNumberOfDoc) {

        PayDoc.Doc paydocDoc = new PayDoc.Doc();
        PayDoc.Doc.InfRcp infRcp = new PayDoc.Doc.InfRcp();

        Node item = nodeList.item(currentNumberOfDoc);
        if (item.getNodeType() == Node.ELEMENT_NODE) {
            Element element = (Element) item;

            String INN_PAY = element.getElementsByTagName("INN_PAY").item(0).getTextContent();
            String KPP_PAY = element.getElementsByTagName("KPP_PAY").item(0).getTextContent();
            String CName_PAY = element.getElementsByTagName("CName_PAY").item(0).getTextContent();

            infRcp.setINN_PAY(INN_PAY);
            infRcp.setKPP_PAY(KPP_PAY);
            infRcp.setCName_PAY(CName_PAY);
        }
        paydocDoc.setInfRcp(infRcp);
        return infRcp;
    }

    public PayDoc.Doc.BankPay getBspayBicpayAndBsKsPayBankPay(NodeList nodeList, int currentNumberOfDoc) {

        PayDoc.Doc paydocDoc = new PayDoc.Doc();
        PayDoc.Doc.BankPay bankPay = new PayDoc.Doc.BankPay();

        Node item = nodeList.item(currentNumberOfDoc);

        if (item.getNodeType() == Node.ELEMENT_NODE) { // TODO DRY
            Element element = (Element) item;

            String BS_PAY = element.getElementsByTagName("BS_PAY").item(0).getTextContent();
            String BIC_PAY = element.getElementsByTagName("BIC_PAY").item(0).getTextContent();
            String BS_KS_PAY = element.getElementsByTagName("BS_KS_PAY").item(0).getTextContent();

            bankPay.setBS_PAY(BS_PAY);
            bankPay.setBIC_PAY(BIC_PAY);
            bankPay.setBS_KS_PAY(BS_KS_PAY);
        }

        paydocDoc.setBankPay(bankPay);
        return bankPay;
    }

    public PayDoc.Doc.BankRCP getBspayBicpayAndBsKsPayBankRCP(NodeList nodeList, int currentNumberOfDoc) {

        PayDoc.Doc paydocDoc = new PayDoc.Doc();
        PayDoc.Doc.BankRCP bankRCP = new PayDoc.Doc.BankRCP();

        Node item = nodeList.item(currentNumberOfDoc);

        if (item.getNodeType() == Node.ELEMENT_NODE) {
            Element element = (Element) item;

            String BS_PAY = element.getElementsByTagName("BS_PAY").item(0).getTextContent();
            String BIC_PAY = element.getElementsByTagName("BIC_PAY").item(0).getTextContent();
            String BS_KS_PAY = element.getElementsByTagName("BS_KS_PAY").item(0).getTextContent();

            bankRCP.setBS_PAY(BS_PAY);
            bankRCP.setBIC_PAY(BIC_PAY);
            bankRCP.setBS_KS_PAY(BS_KS_PAY);
        }
        paydocDoc.setBankRCP(bankRCP);
        return bankRCP;
    }
}
