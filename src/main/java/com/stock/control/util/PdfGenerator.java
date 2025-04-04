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

    private final static String home = System.getProperty("user.home");
    private final static String downloadsPath = Paths.get(home, "Downloads").toString();
    private final static String savePath = downloadsPath + "\\reporte-de-stock-" + LocalDate.now().format(formatter) + ".pdf";

    public static void createPdf(List<ProductSaveDto> products){
        try {
            buildPdf(products);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static void buildPdf(List<ProductSaveDto> products) throws FileNotFoundException {
        Document doc = createDoc();
        setTitle(doc);
        Table table = createTable();
        setUpColumns(table);
        fillTable(products, table);
        doc.add(table);
        doc.close();
        log.info("Report just created at: {}", savePath);
    }

    private static void setTitle(Document doc) {
        Style style = new Style();
        style.setFontSize(18f)
                .setBold();

        Paragraph par = new Paragraph("Reporte de Stock " + LocalDate.now().format(formatter));
        par.addStyle(style);

        doc.add(par);
    }

    private static Document createDoc() throws FileNotFoundException {
        PdfWriter pdfWriter = new PdfWriter(savePath);
        PdfDocument pdfDocument = new PdfDocument(pdfWriter);
        pdfDocument.setDefaultPageSize(PageSize.A4);
        return new Document(pdfDocument);
    }

    private static Table createTable() {
        float[] columns = {50f, 200f, 300f, 100f, 100f}; //id, nombre, desc, precio y stock
        return new Table(columns);
    }

    private static void setUpColumns(Table table) {
        table.addCell(new Cell().add("id").setBold().setBackgroundColor(Color.LIGHT_GRAY));
        table.addCell(new Cell().add("Nombre").setBold().setBackgroundColor(Color.LIGHT_GRAY));
        table.addCell(new Cell().add("Descripción").setBold().setBackgroundColor(Color.LIGHT_GRAY));
        table.addCell(new Cell().add("Stock").setBold().setBackgroundColor(Color.LIGHT_GRAY));
        table.addCell(new Cell().add("Precio").setBold().setBackgroundColor(Color.LIGHT_GRAY));
    }

    private static void fillTable(List<ProductSaveDto> products, Table table) {
        products.forEach(
                p -> {
                    table.addCell(new Cell().add(String.valueOf(p.getId())));
                    table.addCell(new Cell().add(p.getName()));
                    table.addCell(new Cell().add(p.getDescription()));
                    table.addCell(new Cell().add(String.valueOf(p.getStock())));
                    table.addCell(new Cell().add(String.valueOf(p.getPrice())));
                }
        );
    }
}
