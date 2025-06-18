package com.stock.control.serviceImp;

import com.stock.control.dto.SaleDTO;
import com.stock.control.dto.SaleReportDTO;
import com.stock.control.dto.SaleViewDTO;
import com.stock.control.entity.Sale;
import com.stock.control.entity.SaleDetails;
import com.stock.control.mapper.ISaleMapper;
import com.stock.control.repository.ISaleRepository;
import com.stock.control.service.ISaleDetailsService;
import com.stock.control.service.ISaleService;
import com.stock.control.util.PdfGenerator;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class SaleServiceImp implements ISaleService {

    @Autowired
    private ISaleRepository saleRepository;

    @Autowired
    private ISaleDetailsService saleDetailsService;

    @Override
    @Transactional
    public Long saveSale(SaleDTO saleDto){
        Sale sale = ISaleMapper.INSTANCE.saleDtoToSale(saleDto);
        sale.setDateOfSale(LocalDate.now());

        return saleRepository.save(sale).getId();
    }

    @Override
    public List<Sale> getAllSales() {
        return saleRepository.findAll();
    }

    @Override
    public List<SaleViewDTO> getAllSalesViewDto() {
        return toSalesViewDto(getAllSales());
    }

    @Override
    public List<SaleViewDTO> findSalesByDatesCodeAndExclude(LocalDate dateFrom, LocalDate dateTo, String code, boolean exclude) {

        code = code.isBlank() ? null : "%" + code + "%";
        dateTo = dateTo == null ? LocalDate.now() : dateTo;

        return toSalesViewDto(saleRepository.findAllByDateCodeAndExclude(dateFrom, dateTo, code, exclude));
    }

    @Override
    @Transactional
    public void deleteSaleById(Long id) {
        Sale sale = saleRepository.findById(id).orElseThrow();

        saleDetailsService.deleteAllBySale(sale);
        saleRepository.deleteById(sale.getId());
    }

    @Override
    public void createPdfReport(List<SaleViewDTO> sales, String totalAmount, String totalCurrency) {
        PdfGenerator.createSalesReportPdf(sales, totalAmount, totalCurrency);
    }

    @Override
    public void createClientReport(LocalDate dateFrom, LocalDate dateTo, boolean exclude) {
        List<SaleReportDTO> sales = getClientReport(dateFrom, dateTo, exclude);
        PdfGenerator.createClientReportPdf(sales, dateFrom, dateTo);
    }

    private List<SaleReportDTO> getClientReport(LocalDate dateFrom, LocalDate dateTo, boolean exclude){
        return saleRepository.getClientReport(dateFrom, dateTo, exclude);
    }

    private List<SaleViewDTO> toSalesViewDto(List<Sale> sales){
        return sales.stream()
                .map(s -> new SaleViewDTO(s,saleDetailsService.getSaleDetailsBySaleId(s.getId())))
                .toList();
    }
}
