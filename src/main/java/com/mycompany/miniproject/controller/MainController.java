package com.mycompany.miniproject.controller;

import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mycompany.miniproject.dto.ProductDto;
import com.mycompany.miniproject.dto.ProductImageDto;
import com.mycompany.miniproject.service.ProductService;
import com.mycompany.miniproject.service.UserService;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class MainController {
	@Autowired
	private ProductService productService;
	@Autowired
	private UserService userService;
	
	@RequestMapping("")
	public String main(Model model, Authentication authentication) {
		List<ProductDto> recProducts = productService.getRecList();
		List<ProductDto> newProducts = productService.getNewList();
		model.addAttribute("recProducts", recProducts);
		model.addAttribute("newProducts", newProducts);
		
		if(authentication != null) {
			List<Integer> userWishlist = productService.getUserWishlist(authentication.getName());
			Map<Integer, Boolean> isWishlist = new HashMap<>();
			for(ProductDto product :recProducts) {
				isWishlist.put(product.getProductId(), userWishlist.contains(product.getProductId()));
			}
			for(ProductDto product :newProducts) {
				isWishlist.put(product.getProductId(), userWishlist.contains(product.getProductId()));
			}
			model.addAttribute("isWishlist", isWishlist);
		}
		
		return "main";
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
	
	@GetMapping("recieveCoupon")
	public ResponseEntity<String> recieveCoupon(Authentication authentication) {
		if (authentication != null) {
			int couponStatus = userService.getUserCouponStatus(authentication.getName());
			if (couponStatus == 0) {
				userService.updateCouponStatus(1, authentication.getName());
				return ResponseEntity.ok("" + couponStatus);
			} else {
				return ResponseEntity.ok("" + couponStatus);
			}	
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 하지 않은 상태");
		}
	}
}
