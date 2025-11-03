package com.iestpdj.iestpdjpagos.utils;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.util.List;

public class PdfGenerator {

    public static class Item {
        public String nombreConcepto;
        public int cantidad;
        public BigDecimal precioUnitario;
        public BigDecimal subtotal;
        public Item(String n, int c, BigDecimal p, BigDecimal s){
            this.nombreConcepto=n; this.cantidad=c; this.precioUnitario=p; this.subtotal=s;
        }
    }

    public static void generateBoletaPdf(String numeroBoleta, String estudianteNombre,
                                         List<Item> items, BigDecimal total, Path outputFile) throws IOException, IOException {
        try (PDDocument doc = new PDDocument()) {
            PDPage page = new PDPage();
            doc.addPage(page);

            try (PDPageContentStream cs = new PDPageContentStream(doc, page)) {
                cs.beginText();
                cs.setFont(PDType1Font.HELVETICA_BOLD, 14);
                cs.newLineAtOffset(50, 750);
                cs.showText("Boleta: " + numeroBoleta);
                cs.endText();

                cs.beginText();
                cs.setFont(PDType1Font.HELVETICA, 12);
                cs.newLineAtOffset(50, 730);
                cs.showText("Estudiante: " + estudianteNombre);
                cs.endText();

                // encabezado tabla
                float y = 700;
                cs.setFont(PDType1Font.HELVETICA_BOLD, 11);
                cs.beginText();
                cs.newLineAtOffset(50, y);
                cs.showText("Concepto");
                cs.newLineAtOffset(250, 0);
                cs.showText("Cant.");
                cs.newLineAtOffset(60, 0);
                cs.showText("P.unit");
                cs.newLineAtOffset(80, 0);
                cs.showText("Subtotal");
                cs.endText();

                y -= 20;
                cs.setFont(PDType1Font.HELVETICA, 11);
                for (Item it : items) {
                    cs.beginText();
                    cs.newLineAtOffset(50, y);
                    cs.showText(it.nombreConcepto);
                    cs.newLineAtOffset(250, 0);
                    cs.showText(String.valueOf(it.cantidad));
                    cs.newLineAtOffset(60, 0);
                    cs.showText(it.precioUnitario.toString());
                    cs.newLineAtOffset(80, 0);
                    cs.showText(it.subtotal.toString());
                    cs.endText();
                    y -= 18;
                }

                // total
                cs.beginText();
                cs.setFont(PDType1Font.HELVETICA_BOLD, 12);
                cs.newLineAtOffset(50, y - 20);
                cs.showText("Total: " + total.toString());
                cs.endText();
            }

            doc.save(outputFile.toFile());
        }
    }
}
