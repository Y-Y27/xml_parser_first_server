package com.example.xml_parser_first_server.controller;

import com.example.xml_parser_first_server.model.DataToTransfer;
import com.example.xml_parser_first_server.service.DomParser;
import com.example.xml_parser_first_server.service.JaxbParser;
import com.example.xml_parser_first_server.service.ZipHandler;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import java.io.IOException;

@Controller
public class MainController {

    private final ZipHandler zipHandler;

    public MainController(ZipHandler zipHandler) {
        this.zipHandler = zipHandler;
    }

    @GetMapping("/")
    public String mainPage() {
        return "mainPage";
    }

    @PostMapping(value = "/")
    @ResponseBody
    public DataToTransfer zipHandler(@RequestParam("file") MultipartFile multipartFile) throws IOException, ParserConfigurationException, JAXBException, SAXException, XMLStreamException {

        return zipHandler.testZip(multipartFile);
    }


}
