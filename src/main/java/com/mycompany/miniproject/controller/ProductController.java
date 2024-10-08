package com.mycompany.miniproject.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mycompany.miniproject.dto.PagerDto;
import com.mycompany.miniproject.dto.ProductDto;
import com.mycompany.miniproject.dto.ProductImageDto;
import com.mycompany.miniproject.dto.ReviewDto;
import com.mycompany.miniproject.dto.SearchDto;
import com.mycompany.miniproject.dto.WishlistDto;
import com.mycompany.miniproject.service.ProductService;
import com.mycompany.miniproject.service.ReviewService;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/product")
@Slf4j
public class ProductController {
	@Autowired
	private ProductService productService;
	
	@Autowired
	private ReviewService reviewService;
	
    @GetMapping("/detailInfo")
    public String detailInfo(@RequestParam int productId, Model model) {
    	ProductDto product = productService.getProductDetail(productId);
		model.addAttribute("product", product);
		
		List<ProductImageDto> productImages = productService.getProductImgs(productId);
		Map<Integer, String> map = new HashMap<>();
		
		for (ProductImageDto productImage: productImages) {
			map.put(productImage.getProductImgId(), productImage.getProductImgUsage());
		}
		
		model.addAttribute("map", map);
        return "product/detailInfo";  
    }


    @GetMapping("/reviewsSelect")
    public String reviewsSelect(@RequestParam int productId, @RequestParam(defaultValue = "latest") String sortOrder,
                                @RequestParam(defaultValue = "1") int pageNo, Model model,
                                HttpSession session) {
    	ProductDto product = productService.getProductDetail(productId);
		model.addAttribute("product", product);
		
        int totalRows = reviewService.getTotalRows(productId);
        PagerDto pager = new PagerDto(5, 5, totalRows, pageNo);      

        pager.setSort(sortOrder);
        
        List<ReviewDto> reviewList;
        reviewList = reviewService.getReviewsByProductId(productId, pager, sortOrder); 
        
        session.setAttribute("pager", pager);
        model.addAttribute("reviewList", reviewList);
        
        return "product/reviewsSelect";
    }

	@GetMapping("/detailpage")
	public String detailpage(@RequestParam int productId, Authentication authentication,
			@RequestParam(defaultValue="1") int pageNo, Model model) {
		ProductDto product = productService.getProductDetail(productId);
		model.addAttribute("product", product);
		
		List<ProductImageDto> productImages = productService.getProductImgs(productId);
		Map<Integer, String> map = new HashMap<>();
		
		for (ProductImageDto productImage: productImages) {
			map.put(productImage.getProductImgId(), productImage.getProductImgUsage());
		}
		
		model.addAttribute("map", map);
		
		int totalRows = reviewService.getTotalRows(productId);
	    PagerDto pager = new PagerDto(5, 5, totalRows, pageNo);
	    model.addAttribute("pager", pager);
	    
	    if(authentication != null) {
	    	List<Integer> userWishlist = productService.getUserWishlist(authentication.getName());
	    	boolean isWishlist = userWishlist.contains(productId);
	    	model.addAttribute("isWishlist", isWishlist);
	    }
		return "product/detailpage";
	}
	
	@GetMapping("/loadProductImgs")
	public void loadProductImgs(int productImgId, String productImgUsage, HttpServletResponse response) throws IOException {
		ProductImageDto productImage = productService.getProductImg(productImgId);

		OutputStream out = response.getOutputStream();
		out.write(productImage.getProductImg());
		out.flush();
		out.close();		
	}
	
	@GetMapping("/search")
	public String search(String category, String search, String sort, Model model, Authentication authentication) {
		log.info("실행");
		SearchDto searchDto = new SearchDto();
		searchDto.setCategory(category);
		searchDto.setSearchContent(search);
		searchDto.setSort(sort);
		List<ProductDto> productList = productService.getSearchProduct(searchDto);
		int searchedRows = productService.getSearchedRows(searchDto);
		model.addAttribute("searchedRows", searchedRows);
		model.addAttribute("searchDto", searchDto);
		model.addAttribute("productList", productList);
		
		if(authentication != null) {
			List<Integer> userWishlist = productService.getUserWishlist(authentication.getName());
			Map<Integer, Boolean> isWishlist = new HashMap<>();
			for(ProductDto product :productList) {
				isWishlist.put(product.getProductId(), userWishlist.contains(product.getProductId()));
			}
			model.addAttribute("isWishlist", isWishlist);
		}
		
		return "product/search";
	}
	
	@GetMapping("/searchProductAll")
	public String searchProductAll(Model model, Authentication authentication,
				@RequestParam(defaultValue="1")int pageNo,
				HttpSession session, String sort) {
		
		int totalRows = productService.getTotalRows();
		PagerDto pager = new PagerDto(15, 5, totalRows, pageNo, sort);
		session.setAttribute("pager", pager);
		
		List<ProductDto> productList;
		productList = productService.getProducts(pager);
				
		model.addAttribute("productList", productList);
		log.info("실행");
		
		if(authentication != null) {
			List<Integer> userWishlist = productService.getUserWishlist(authentication.getName());
			Map<Integer, Boolean> isWishlist = new HashMap<>();
			for(ProductDto product :productList) {
				isWishlist.put(product.getProductId(), userWishlist.contains(product.getProductId()));
			}
			model.addAttribute("isWishlist", isWishlist);
		}
		
		return "product/search";
	}
	

	@GetMapping("loadMainImg")
	public void loadMainImg(int productId, HttpServletResponse response) throws Exception {
		ProductImageDto productImage = productService.getMainImg(productId);
		
		String contentType = productImage.getProductImgType();
		response.setContentType(contentType);
		
		String fileName = productImage.getProductImgName();
		String encodingFileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
		response.setHeader("Content-Disposition", "attachment; filename=\"" + encodingFileName + "\"");		

		OutputStream out = response.getOutputStream();
		out.write(productImage.getProductImg());
		out.flush();
		out.close();
	}
	
	@GetMapping("loadReviewImg")
	public void loadReviewImg(@RequestParam int reviewId, HttpServletResponse response) throws Exception {
		ReviewDto reviewImg = reviewService.getReviewImgByReviewId(reviewId);
		
		String contentType = reviewImg.getReviewImgType();
		response.setContentType(contentType);
		
		String fileName = reviewImg.getReviewImgName();
		String encodingFileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
		response.setHeader("Content-Disposition", "attachment; filename=\"" + encodingFileName + "\"");		

		OutputStream out = response.getOutputStream();
		out.write(reviewImg.getReviewImg());
		out.flush();
		out.close();

	}
	
	@GetMapping("/Wishlist")
	@ResponseBody
	public String addWishlist(Authentication authentication,
			@RequestParam("productId") int productId) {
		String result;
		if(authentication == null) {
			result = "notLogin";
		}else {
			WishlistDto wishlist = new WishlistDto();
			wishlist.setProductId(productId);
			wishlist.setUserId(authentication.getName());
			if(productService.getWishlist(wishlist) == null) {
				productService.addWishlist(wishlist);
				result = "fill";
			}else {
				productService.deleteWishlist(wishlist);						
				result = "empty";
			}			
		}
		
		return result;
	}
}
