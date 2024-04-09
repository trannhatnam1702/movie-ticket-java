package com.example.movie.Service;

import com.example.movie.Entity.Invoice;
import com.example.movie.Entity.Reservation;
import com.example.movie.Repository.InvoiceRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class InvoiceService {
    @Autowired
    private InvoiceRepo invoiceRepo;
    public List<Invoice> getAllInvoices () { return invoiceRepo.findAll(); }

    public void createInvoice (Invoice invoice) { invoiceRepo.save(invoice); }

    public Invoice getInvoiceById (Long id) { return invoiceRepo.findById(id).orElse(null); }

    public void deleteInvoice (Long id) { invoiceRepo.deleteById(id); }

    public List<Invoice> getAllInvoices(Integer pageNo,
                                                Integer pageSize,
                                                String sortBy){
        return invoiceRepo.getAllInvoice(pageNo, pageSize, sortBy);
    }

    public List<Invoice> searchInvoice(String keyword){
        return invoiceRepo.searchInvoice(keyword);
    }

    public BigDecimal getTotalRevenue() {
        return invoiceRepo.calculateTotalRevenue();
    }

    public List<Object[]> getRevenueByYear(int year) {
        return invoiceRepo.getRevenueByYear(year);
    }

}
