First server for xml parser


 The first server represents 1 web page, which will be used to upload the zip-archive to the server. In the archive will be 2 files. Both files are xml representation of electronic documents. The first file - Report.xml, it is "main", it contains service information (tags: DocNum, DocDate, DocDateOld, etc.) and the basic description of documents (tags: Line_Num, DocNum, DocDate in the Doc block). The second file, PayDocs.xml, is additional and contains service information (tags: GUID_Doc, Date, Scrc, etc.) and additional description of the same documents (tags: Num, Date, ID, etc. in Doc block).
server must find in these files pairs of Docs.Doc.DocGUID (Report.xml) and Docs.Doc.GUID (PayDocs.xml) data blocks case insensitive.
   Then it should extract the following information from the found <Doc> block pair:
 * DocNum, DocDate, DocGUID, OperType, AmountOut from Report.xml
 * All data from blocks Inf_PAY, Bank_PAY, Inf_RCP, Bank_RCP; Purpose from PayDocs.xml
   Server 1 must send all extracted data to server 2 with a REST request
   Server must only accept Report.xml/PayDocs.xml pairs, which have the word "Total" in Report_type_flag tag of Report.xml file;
   The xml parser is written in two implementations: 1 - using JAXB, 2 - using pure w3c.dom
