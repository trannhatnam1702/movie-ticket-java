package com.example.movie.Repository;

import com.example.movie.Entity.Invoice;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface InvoiceRepo extends JpaRepository<Invoice, Long> {
    default List<Invoice> getAllInvoice(Integer pageNo,
                                                        Integer pageSize,
                                                        String sortBy){
        return findAll(PageRequest.of(pageNo,
                pageSize,
                Sort.by(sortBy)))
                .getContent();
    }
    @Query("""
            SELECT r
            FROM Invoice r
            WHERE r.user.username LIKE %?1%
                OR r.createdAt LIKE %?1%
            """)
    List<Invoice> searchInvoice(String keyword);

    @Query(value = "SELECT COALESCE(SUM(i.total), 0) FROM Invoice i")
    BigDecimal calculateTotalRevenue();

    @Query(value = "SELECT months.month, " +
            "       COALESCE(SUM(i.total), 0) AS totalRevenue, " +
            "       COALESCE(SUM(i.quantity), 0) AS totalTicketsSold " +
            "FROM (" +
            "    SELECT 1 AS month UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 " +
            "    UNION SELECT 5 UNION SELECT 6 UNION SELECT 7 UNION SELECT 8 " +
            "    UNION SELECT 9 UNION SELECT 10 UNION SELECT 11 UNION SELECT 12 " +
            ") AS months " +
            "LEFT JOIN Invoice i ON months.month = MONTH(i.created_at) AND YEAR(i.created_at) = ?1 " +
            "GROUP BY months.month", nativeQuery = true)
    List<Object[]> getRevenueByYear(int year);
}
