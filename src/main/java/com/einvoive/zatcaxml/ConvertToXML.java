package com.einvoive.zatcaxml;

import com.einvoive.constants.Constants;
import com.einvoive.model.Customer;
import com.einvoive.zatcaxml.InvoiceXML;
import org.springframework.core.io.InputStreamResource;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import java.io.File;
import java.io.FileInputStream;
import java.io.StringWriter;

/**
 *
 * @author ABsiddik
 */
public class ConvertToXML {

    public String ConvertCustomerToXML(Customer customer) throws Exception
    {
        File file = new File(Constants.CUSTOMER_XML_PATH);
        JAXBContext jaxbContext = JAXBContext.newInstance(Customer.class);
        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        jaxbMarshaller.marshal(customer, file);// this line create customer.xml file in specified path.
        StringWriter sw = new StringWriter();
        jaxbMarshaller.marshal(customer, sw);
        return file.getAbsolutePath();
    }

    public File ConvertInvoiceToXML(InvoiceXML Invoice) throws Exception
    {
        File file = new File(Constants.INVOICE_XML_PATH);
        JAXBContext jaxbContext = JAXBContext.newInstance(InvoiceXML.class);
        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        jaxbMarshaller.marshal(Invoice, file);// this line create invoice.xml file in specified path.
        StringWriter sw = new StringWriter();
        jaxbMarshaller.marshal(Invoice, sw);
        String xmlString = sw.toString();
        System.out.println(xmlString);
        return file;
//        }
    }

}