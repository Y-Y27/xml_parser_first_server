package com.example.xml_parser_first_server.service;

import com.example.xml_parser_first_server.model.DataToTransfer;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Service
public class ZipHandler {

    private final DomParser domParser;
    private final JaxbParser jaxbParser;

    public ZipHandler(DomParser domParser, JaxbParser jaxbParser) {
        this.domParser = domParser;
        this.jaxbParser = jaxbParser;
    }

    public DataToTransfer testZip(MultipartFile multipartFile) throws IOException, ParserConfigurationException, XMLStreamException, SAXException, JAXBException {
        final String OUTPUT_FOLDER = "src/main/resources/xml";

        File folder = new File(OUTPUT_FOLDER);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        byte[] buffer = new byte[1024];

        ZipInputStream zipIs = null;
        try {
            InputStream inputStream = multipartFile.getInputStream();

            zipIs = new ZipInputStream(inputStream);

            ZipEntry entry;

            while ((entry = zipIs.getNextEntry()) != null) {
                String entryName = entry.getName();
                String outFileName = OUTPUT_FOLDER + File.separator + entryName;
                System.out.println("Unzip: " + outFileName);

                if (entry.isDirectory()) {
                    new File(outFileName).mkdirs();
                } else {
                    FileOutputStream fos = new FileOutputStream(outFileName);

                    int len;

                    while ((len = zipIs.read(buffer)) > 0) {
                        fos.write(buffer, 0, len);
                    }

                    fos.close();
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                zipIs.close();
            } catch (Exception e) {
            }
        }
//        return domParser.domParser();
        return jaxbParser.jaxbParser();
    }
}
