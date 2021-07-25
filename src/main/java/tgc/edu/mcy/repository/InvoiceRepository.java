package tgc.edu.mcy.repository;

import org.springframework.stereotype.Repository;

import tgc.edu.mcy.custom.CommonRepository;
import tgc.edu.mcy.entity.Invoice;

@Repository
public interface InvoiceRepository extends CommonRepository<Invoice, Integer> {

	public Invoice findByNumber(String number);
}
