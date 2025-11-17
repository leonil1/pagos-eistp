package com.iestpdj.iestpdjpagos.utils;

import com.iestpdj.iestpdjpagos.model.PagoReporte;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ExcelGenerator {
    private final DateTimeFormatter formatterFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public void generarExcel(List<PagoReporte> pagos, String rutaSalida) {
        try (Workbook workbook = new XSSFWorkbook()) {

            Sheet sheet = workbook.createSheet("Reporte Pagos");

            /** 🔵 ESTILOS **/

            // Estilo para encabezados
            CellStyle headerStyle = workbook.createCellStyle();
            Font fontBold = workbook.createFont();
            fontBold.setBold(true);
            headerStyle.setFont(fontBold);
            headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);

            // Estilo moneda
            CellStyle moneyStyle = workbook.createCellStyle();
            DataFormat format = workbook.createDataFormat();
            moneyStyle.setDataFormat(format.getFormat("S/ #,##0.00"));

            // Estilo fecha
            CellStyle dateStyle = workbook.createCellStyle();
            dateStyle.setDataFormat(format.getFormat("dd/MM/yyyy"));

            /** ESTILO TOTAL (negrita) **/
            CellStyle totalStyle = workbook.createCellStyle();
            Font totalFont = workbook.createFont();
            totalFont.setBold(true);
            totalStyle.setFont(totalFont);

            totalStyle.setDataFormat(format.getFormat("S/ #,##0.00"));


            /** 🟥 FILA DE ENCABEZADOS **/
            Row header = sheet.createRow(0);

            String[] titulos = {"ID", "Estudiante", "Monto", "Fecha Pago", "Estado"};

            for (int i = 0; i < titulos.length; i++) {
                Cell cell = header.createCell(i);
                cell.setCellValue(titulos[i]);
                cell.setCellStyle(headerStyle);
            }

            /** 🟢 FILAS DE DATOS **/
            int rowIndex = 1;
            double totalMonto = 0.0;

            for (PagoReporte p : pagos) {
                Row row = sheet.createRow(rowIndex++);

                row.createCell(0).setCellValue(p.getId());
                row.createCell(1).setCellValue(p.getEstudiante());

                // Monto
                Cell cellMonto = row.createCell(2);
                cellMonto.setCellValue(p.getMonto());
                cellMonto.setCellStyle(moneyStyle);

                totalMonto += p.getMonto();

                // Fecha
                Cell cellFecha = row.createCell(3);
                cellFecha.setCellValue(p.getFechaPago().toLocalDate().format(formatterFecha));
                cellFecha.setCellStyle(dateStyle);

                row.createCell(4).setCellValue(p.getEstado());
            }

            /** 🟡 FILA FINAL DEL TOTAL **/
            Row totalRow = sheet.createRow(rowIndex);

            // Celda "TOTAL:"
            Cell totalLabelCell = totalRow.createCell(1);
            totalLabelCell.setCellValue("TOTAL:");
            totalLabelCell.setCellStyle(totalStyle);

            // Celda total sumado
            Cell totalValueCell = totalRow.createCell(2);
            totalValueCell.setCellValue(totalMonto);
            totalValueCell.setCellStyle(totalStyle);

            /** ✨ AJUSTAR COLUMNAS **/
            for (int i = 0; i < titulos.length; i++) {
                sheet.autoSizeColumn(i);
            }

            /** 💾 GUARDAR ARCHIVO **/
            FileOutputStream fileOut = new FileOutputStream(rutaSalida);
            workbook.write(fileOut);
            fileOut.close();

            System.out.println("Excel generado en: " + rutaSalida);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}