package com.stock.control.util;

import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.Style;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.stock.control.dto.ProductSaveDto;
import com.stock.control.dto.SaleViewDTO;
import com.stock.control.front.tools.CurrencyFormater;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/*
* Clase que se encarga de la creación de los reportes en PDF
*/
public class PdfGenerator {

    private static final Logger log = LoggerFactory.getLogger(PdfGenerator.class);

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    private static final float[] colsForProducts = {50f, 500f, 90f, 110f}, //id, nombre, desc, precio y stock
                                 colsForSales = {30f, 50f, 80f, 150f, 30f, 90f, 50f}; //id, code, client, products, amount, price, date

    private final static String home = System.getProperty("user.home");
    private final static String downloadsPath = Paths.get(home, "Downloads").toString();
    private static final String pdfName = LocalDate.now().format(formatter) + ".pdf";

    public static void createProductsReportPdf(List<ProductSaveDto> products){
        try {
            buildPdfForProducts(products);
            log.info("Report just created at: {}", downloadsPath + "\\reporte de stock " + pdfName);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static void createSalesReportPdf(List<SaleViewDTO> sales){
        try {
            buildPdfForSales(sales);
            log.info("Sale report just created at: {}", downloadsPath + "\\reporte de ventas " + pdfName);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static void buildPdfForProducts(List<ProductSaveDto> products) throws FileNotFoundException {
        Document doc = createDoc("reporte de stock ");
        setTitle(doc, "Reporte de Stock - ");
        Table table = createTable(colsForProducts);
        setUpColumnsForProducts(table);
        fillTableProducts(products, table);
        doc.add(table);
        doc.close();
    }

    private static void buildPdfForSales(List<SaleViewDTO> sales) throws FileNotFoundException {
        Document doc = createDoc("reporte de ventas ");
        setTitle(doc, "Reporte de Ventas ");
        Table table = createTable(colsForSales);
        setUpColumnsForSale(table);
        fillTableSales(sales, table);
        doc.add(table);
        doc.close();
    }

    private static void setTitle(Document doc, String title) {
        Style style = new Style();
        style.setFontSize(18f)
                .setBold();

        Paragraph par = new Paragraph(title + LocalDate.now().format(formatter));
        par.addStyle(style);

        doc.add(par);
    }

    private static Document createDoc(String name) throws FileNotFoundException {
        PdfWriter pdfWriter = new PdfWriter(downloadsPath + "\\"+name + pdfName);
        PdfDocument pdfDocument = new PdfDocument(pdfWriter);
        pdfDocument.setDefaultPageSize(PageSize.A4);
        return new Document(pdfDocument);
    }

    private static Table createTable(float[] cols) {
        return new Table(cols);
    }

    private static void setUpColumnsForProducts(Table table) {
        table.addCell(new Cell().add("id").setBold().setBackgroundColor(Color.LIGHT_GRAY));
        table.addCell(new Cell().add("Nombre").setBold().setBackgroundColor(Color.LIGHT_GRAY));
        table.addCell(new Cell().add("Stock").setBold().setBackgroundColor(Color.LIGHT_GRAY));
        table.addCell(new Cell().add("Precio").setBold().setBackgroundColor(Color.LIGHT_GRAY));
    }

    private static void setUpColumnsForSale(Table table){
        table.addCell(new Cell().add("id").setBold().setBackgroundColor(Color.LIGHT_GRAY));
        table.addCell(new Cell().add("Código").setBold().setBackgroundColor(Color.LIGHT_GRAY));
        table.addCell(new Cell().add("Vendido a").setBold().setBackgroundColor(Color.LIGHT_GRAY));
        table.addCell(new Cell().add("Productos").setBold().setBackgroundColor(Color.LIGHT_GRAY));
        table.addCell(new Cell().add("Cantidad Total").setBold().setBackgroundColor(Color.LIGHT_GRAY));
        table.addCell(new Cell().add("Precio Final").setBold().setBackgroundColor(Color.LIGHT_GRAY));
        table.addCell(new Cell().add("Fecha de Venta").setBold().setBackgroundColor(Color.LIGHT_GRAY));
    }

    private static void fillTableProducts(List<ProductSaveDto> products, Table table) {
        products.forEach(
                p -> {
                    table.addCell(new Cell().add(String.valueOf(p.getId())));
                    table.addCell(new Cell().add(p.getName()));
                    table.addCell(new Cell().add(String.valueOf(p.getStock())));
                    table.addCell(new Cell().add(CurrencyFormater.getCurrency(p.getPrice())));
                }
        );
    }

    private static void fillTableSales(List<SaleViewDTO> sales, Table table){
        sales.forEach(
                s ->{
                    table.addCell(new Cell().add(String.valueOf(s.getId())));
                    table.addCell(new Cell().add(s.getCode()));
                    table.addCell(new Cell().add(s.getClient()));
                    table.addCell(new Cell().add(s.getProductsDetails()));
                    table.addCell(new Cell().add(String.valueOf(s.getTotalAmount())));
                    table.addCell(new Cell().add(s.getFinalPrice()));
                    table.addCell(new Cell().add(s.getDateOfSale()));
                }
        );
    }
}
