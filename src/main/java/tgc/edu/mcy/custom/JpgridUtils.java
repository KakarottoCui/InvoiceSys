package tgc.edu.mcy.custom;

import java.util.HashMap;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

public class JpgridUtils {
	private Integer id;
	private Integer page;
	private Integer rows;
	private String order;
	private String sort;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public Integer getRows() {
		return rows;
	}

	public void setRows(Integer rows) {
		this.rows = rows;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public Pageable buildPageable() {
		page--;
		return PageRequest.of(page, rows);   
	}
	
	public Pageable buildPageableDesc() {
		page--;
		Sort sort = new Sort(Direction.DESC, "id");
		return PageRequest.of(page, rows, sort);   
	}
	
	public static HashMap<String, Object> getPageResult(Page page) {
		HashMap<String, Object> result = new HashMap<>();
		result.put("total", page.getTotalElements());   //数据库中数据的总页面数
		result.put("page", page.getNumber()+1);   //当前页面数量
		result.put("rows", page.getContent());
		result.put("sort", page.getSort());
		return result;
	}
}
