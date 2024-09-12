package com.mycompany.miniproject.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mycompany.miniproject.dao.ProductDao;
import com.mycompany.miniproject.dao.ProductImageDao;
import com.mycompany.miniproject.dto.PagerDto;
import com.mycompany.miniproject.dto.ProductDto;
import com.mycompany.miniproject.dto.ProductImageDto;
import com.mycompany.miniproject.dto.SearchDto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ProductService {
	@Autowired
	private ProductDao productDao;
	
	@Autowired
	private ProductImageDao productImageDao;
	
	public List<ProductDto> getProducts(PagerDto pager) {
		List<ProductDto> products = productDao.getProductAll(pager);
		return products;
	}
	
	public List<ProductDto> getSearchProduct(SearchDto searchDto){
		List<ProductDto> products = productDao.getSearchProduct(searchDto);
		return products;
	}

	public ProductImageDto getMainImg(int productId) {
		ProductImageDto productImage = productImageDao.selectImgByProductId(productId);
		return productImage;
	}

	public int getTotalRows() {
		int totalRows = productDao.countRows();
		return totalRows;
	}
	
	public ProductDto getProductDetail(int productId) {
		ProductDto product = productDao.getProductDetailInfo(productId);
		return product;
	}
	
	public ProductImageDto getImgByProductId(int productImgId) {
		ProductImageDto productImage = productImageDao.getImgByProductId(productImgId);
		return productImage;
	}
	
	public List<ProductImageDto> getImgsByProductId(int productId) {
		List<ProductImageDto> productImages = productImageDao.getImgsByProductId(productId);
		return productImages;
	}
	
	



}
