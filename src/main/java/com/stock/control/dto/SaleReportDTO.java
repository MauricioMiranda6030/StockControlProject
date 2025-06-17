package com.stock.control.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SaleReportDTO {
    private String name, docId;
    private long cont;

    public SaleReportDTO(String name, String docId, long cont) {
        this.name = name;
        this.docId = docId;
        this.cont = cont;
    }

    public String getName(){
        if(docId.equals("0000"))
            return "Clientes sin MT";
        else
            return name;
    }
}
