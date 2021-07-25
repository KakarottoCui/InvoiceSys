package tgc.edu.mcy.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tgc.edu.mcy.custom.CommonService;
import tgc.edu.mcy.entity.Invoice;
import tgc.edu.mcy.repository.InvoiceRepository;

@Service
public class InvoiceService extends CommonService<Invoice, Integer> {
	@Autowired
	private InvoiceRepository invoiceDAO;

	public Invoice findByNumber(String number) {
		return invoiceDAO.findByNumber(number);
	}
}
