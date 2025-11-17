package com.iestpdj.iestpdjpagos.utils;

import com.iestpdj.iestpdjpagos.model.PagoReporte;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;

import java.io.File;
import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class PdfGenerator {

    private final DateTimeFormatter formatterFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private final DecimalFormat formatterMonto = new DecimalFormat("S/ 0.00");

    public void generarPDF(List<PagoReporte> pagos, String rutaSalida, String rutaLogo) {
        try {
            PdfWriter writer = new PdfWriter(rutaSalida);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            /** LOGO **/
            if (rutaLogo != null) {
                File f = new File(rutaLogo);
                if (f.exists()) {
                    Image logo = new Image(ImageDataFactory.create(rutaLogo));
                    logo.setHeight(50);
                    logo.setAutoScale(true);
                    document.add(logo);
                }
            }

            /** TÍTULO **/
            document.add(new Paragraph("Reporte de Pagos")
                    .setTextAlignment(TextAlignment.CENTER)
                    .setBold()
                    .setFontSize(16));

            /** TABLA **/
            float[] columnWidths = {60, 220, 80, 80, 70};
            Table table = new Table(columnWidths);
            table.setWidth(UnitValue.createPercentValue(100));

            // ENCABEZADOS
            table.addHeaderCell(new Cell().add(new Paragraph("ID").setBold()));
            table.addHeaderCell(new Cell().add(new Paragraph("Estudiante").setBold()));
            table.addHeaderCell(new Cell().add(new Paragraph("Monto").setBold()));
            table.addHeaderCell(new Cell().add(new Paragraph("Fecha").setBold()));
            table.addHeaderCell(new Cell().add(new Paragraph("Estado").setBold()));

            double total = 0.0;

            // FILAS
            for (PagoReporte p : pagos) {

                table.addCell(String.valueOf(p.getId()));
                table.addCell(p.getEstudiante());

                String montoFormateado = formatterMonto.format(p.getMonto());
                table.addCell(montoFormateado);

                String fechaFormateada = p.getFechaPago()
                        .toLocalDate()
                        .format(formatterFecha);
                table.addCell(fechaFormateada);

                table.addCell(p.getEstado());

                // SUMAR
                total += p.getMonto();
            }

            /** FILA DE TOTAL **/
            Cell totalCellLabel = new Cell(1, 2)
                    .add(new Paragraph("TOTAL"))
                    .setBold()
                    .setTextAlignment(TextAlignment.RIGHT);

            Cell totalCellMonto = new Cell(1, 1)
                    .add(new Paragraph(formatterMonto.format(total)))
                    .setBold();

            // Unir columnas necesarias
            table.addCell(totalCellLabel);
            table.addCell(totalCellMonto);

            // Celdas vacías para completar (fecha, estado)
            table.addCell(new Cell().add(new Paragraph("")));
            table.addCell(new Cell().add(new Paragraph("")));

            document.add(table);

            document.close();

            System.out.println("PDF generado en: " + rutaSalida);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}