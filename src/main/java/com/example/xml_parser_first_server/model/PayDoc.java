package com.example.xml_parser_first_server.model;


import lombok.Data;

import javax.xml.bind.annotation.*;
import java.util.List;


@XmlRootElement(name = "Inf_Pay_Doc")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class PayDoc {

    @XmlElementWrapper(name = "Docs")
    @XmlElement(name = "Doc")
    private List<Doc> docsList;

    @Data
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "InnerDoc", namespace = "InnerDoc")
    public static class Doc {

        @XmlElement(name = "GUID")
        private String GUID;

        @XmlElement(name = "Inf_PAY")
        private List<InfPay> infPayList;

        @XmlElement(name = "Bank_PAY")
        private List<BankPay> bankPayList;

        @XmlElement(name = "Inf_RCP")
        private List<InfRcp> infRcpList;

        @XmlElement(name = "Bank_RCP")
        private List<BankRCP> bankRCPList;

        @XmlElement(name = "Purpose")
        private String Purpose;

        @Data
        @XmlRootElement(name = "Inf_PAY")
        @XmlAccessorType(XmlAccessType.FIELD)
        public static class InfPay {

            @XmlElement(name = "INN_PAY")
            private String INN_PAY;

            @XmlElement(name = "KPP_PAY")
            private String KPP_PAY;

            @XmlElement(name = "CName_PAY")
            private String CName_PAY;
        }

        @Data
        @XmlRootElement(name = "Bank_PAY")
        @XmlAccessorType(XmlAccessType.FIELD)
        public static class BankPay {

            @XmlElement(name = "BS_PAY")
            private String BS_PAY;

            @XmlElement(name = "BIC_PAY")
            private String BIC_PAY;

            @XmlElement(name = "BS_KS_PAY")
            private String BS_KS_PAY;
        }

        @XmlRootElement(name = "Inf_RCP")
        @Data
        @XmlAccessorType(XmlAccessType.FIELD)
        public static class InfRcp {

            @XmlElement(name = "INN_PAY")
            private String INN_PAY;

            @XmlElement(name = "KPP_PAY")
            private String KPP_PAY;

            @XmlElement(name = "CName_PAY")
            private String CName_PAY;
        }

        @XmlRootElement(name = "Bank_RCP")
        @Data
        @XmlAccessorType(XmlAccessType.FIELD)
        public static class BankRCP {

            @XmlElement(name = "BS_PAY")
            private String BS_PAY;

            @XmlElement(name = "BIC_PAY")
            private String BIC_PAY;

            @XmlElement(name = "BS_KS_PAY")
            private String BS_KS_PAY;

        }
    }
}
