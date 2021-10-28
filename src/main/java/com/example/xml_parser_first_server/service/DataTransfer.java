package com.example.xml_parser_first_server.service;

import com.example.xml_parser_first_server.model.DataToTransfer;
import com.example.xml_parser_first_server.model.PayDoc;
import com.example.xml_parser_first_server.model.ReportDoc;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class DataTransfer {

    public DataToTransfer checkGUIDAndReportTypeAndSendData(ReportDoc reportDoc, PayDoc payDoc) {
        DataToTransfer dataToTransfer = new DataToTransfer();
        List<DataToTransfer.InnerData> innerDataList = new ArrayList<>();
        dataToTransfer.setInnerDataList(innerDataList);

        for (ReportDoc.Doc docReport : reportDoc.getDocsList()) {
            for (PayDoc.Doc docPay : payDoc.getDocsList()) {
                if (docReport.getDocGUID().equalsIgnoreCase(docPay.getGUID())) {
                    DataToTransfer.InnerData innerData = new DataToTransfer.InnerData();
                    innerData.setReportDoc(docReport);
                    innerData.setPayDoc(docPay);
                    innerDataList.add(innerData);
                }
            }
        }

        if (reportDoc.getReport_type_flag().equalsIgnoreCase("Итоговая")) {
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.postForEntity("http://localhost:8085/getData", dataToTransfer, DataToTransfer.class);
        }
        return dataToTransfer;
    }
}
