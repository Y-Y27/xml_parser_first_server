package com.example.xml_parser_first_server.model;


import lombok.Data;

import javax.xml.bind.annotation.*;
import java.util.List;

@XmlRootElement(name = "SKP_REPORT_KS")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class ReportDoc {

    @XmlElement(name = "SKP_REPORT_KS")
    private String SKP_REPORT_KS;

    @XmlElement(name = "Report_type_flag")
    private String Report_type_flag;

    @XmlElementWrapper(name = "Docs")
    @XmlElement(name = "Doc")
    private List<Doc> docsList;

    @Data
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Doc {
        @XmlElement(name = "DocNum")
        private String DocNum;
        @XmlElement(name = "DocDate")
        private String DocDate;
        @XmlElement(name = "DocGUID")
        private String DocGUID;
        @XmlElement(name = "OperType")
        private String OperType;
        @XmlElement(name = "AmountOut")
        private String AmountOut;
    }

}
