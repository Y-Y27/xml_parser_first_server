package com.example.xml_parser_first_server.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@Data
@NoArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class DataToTransfer {

    List<InnerData> innerDataList;

    @Data
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class InnerData {
        @XmlElement(name = "ReportDoc.Doc")
        private ReportDoc.Doc reportDoc;

        @XmlElement(name = "PayDoc.Doc")
        private PayDoc.Doc payDoc;
    }

}
