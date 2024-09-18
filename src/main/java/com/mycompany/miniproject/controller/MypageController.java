package com.mycompany.miniproject.controller;

import java.io.OutputStream;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mycompany.miniproject.dto.OrderDetailDto;
import com.mycompany.miniproject.dto.ProductImageDto;
import com.mycompany.miniproject.dto.ReviewDto;
import com.mycompany.miniproject.interceptor.LoginCheck;
import com.mycompany.miniproject.service.OrderDetailService;
import com.mycompany.miniproject.service.OrderService;
import com.mycompany.miniproject.service.ProductService;
import com.mycompany.miniproject.service.ReviewService;
import com.mycompany.miniproject.service.UserService;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/mypage")
@Slf4j
public class MypageController {
	@Autowired
	private ProductService productService;

	@Autowired
	private OrderService orderService;	
	
	@Autowired
	private OrderDetailService orderDetailService;
	
	@Autowired
	private ReviewService reviewService;

	@Autowired
	private UserService userService;
	
	@RequestMapping("/editMyInfo")
	public String editMyInfo() {
		log.info("실행");
		return "mypage/editMyInfo";
	}
	
	@RequestMapping("/likedProducts")
	public String likedProducts() {
		log.info("실행");
		
		return "mypage/likedProducts";
	}
	
	@LoginCheck
	@RequestMapping("/mypage")
	public String mypage() {
		log.info("실행");
		return "mypage/mypage";
	}
	
	@GetMapping("/orderList")
	public String orderList(Model model) {
	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    String userId = null;
	    if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
	        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
	        userId = userDetails.getUsername(); 
	    }

	    log.info("User ID: " + userId);

	    List<OrderDetailDto> orderDetails = orderDetailService.getOrderDetailsByOd(userId);
	    String userName = userService.getUserName(userId);
	    
	    model.addAttribute("orderDetails", orderDetails);
	    model.addAttribute("userName", userName);
	    model.addAttribute("userId", userId);
	    
	    log.info("실행");
	    return "mypage/orderList";
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
	
	@GetMapping("/reviews")
	public String reviews() {
		log.info("실행");
		return "mypage/reviews";
	}
	
	@PostMapping("/reviewInsert")
	public String reviewInsert(ReviewDto reviewDto) {
		log.info("Review DTO: " + reviewDto);
		reviewService.insertReview(reviewDto);	
		
		return "redirect:/mypage/orderList";
	}
	
}
