package com.tutorialspoint;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;
import net.sf.jasperreports.export.type.PdfVersionEnum;
import net.sf.jasperreports.export.type.PdfaConformanceEnum;

public class JasperReportCompileAndCreatePDFA {

	public static void main(String[] args) {
		compile();
		createBDF();
	}

	private static void createBDF() {
		try {
			String sourceFileName = "jasper_report_template.jasper";
			DataBeanList DataBeanList = new DataBeanList();
			ArrayList<DataBean> dataList = DataBeanList.getDataBeanList();

			JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(dataList);
			Map parameters = new HashMap();

			JasperPrint jasperPrint = JasperFillManager.fillReport(sourceFileName, parameters, beanColDataSource);
			if (jasperPrint != null) {

				/**
				 * try to export to pdf a1
				 */
				JRPdfExporter Exporter = new JRPdfExporter();
				SimplePdfExporterConfiguration configuracio = new SimplePdfExporterConfiguration();
				configuracio.setPdfaConformance(PdfaConformanceEnum.PDFA_1A);
				configuracio.setIccProfilePath("sRGB2014.icc");
				configuracio.setPdfVersion(PdfVersionEnum.VERSION_1_6);
				
				List<JasperPrint> jasperPrintList = new ArrayList<JasperPrint>();
				jasperPrintList.add(jasperPrint);
				Exporter.setExporterInput(SimpleExporterInput.getInstance(jasperPrintList));
				java.io.ByteArrayOutputStream outputStream = new java.io.ByteArrayOutputStream();
				Exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputStream));
				Exporter.setConfiguration(configuracio);
			

				Exporter.exportReport();

				OutputStream fileoutputStream = new FileOutputStream("jasper_report_template.pdf");
				outputStream.writeTo(fileoutputStream);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void compile() {
		String sourceFileName = "jasper_report_template.jrxml";

		System.out.println("Compiling Report Design ...");
		try {
			/**
			 * Compile the report to a file name same as the JRXML file name
			 */
			JasperCompileManager.compileReportToFile(sourceFileName);
		} catch (JRException e) {
			e.printStackTrace();
		}
		System.out.println("Done compiling!!! ...");
	}
}