package com.mycompany.miniproject.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mycompany.miniproject.dao.OrderDao;
import com.mycompany.miniproject.dto.CartDto;
import com.mycompany.miniproject.dto.OrderDetailDto;
import com.mycompany.miniproject.dto.OrderDto;

@Service
public class OrderService {
	@Autowired
	private OrderDao orderDao;
	
	public void addCart(CartDto cartDto) {
		orderDao.addProductToCart(cartDto);
	}

	public List<CartDto> getCart(String userId) {
		List<CartDto> cartList = orderDao.getCartProuducts(userId);
		return cartList;
	}

	public boolean checkCart(CartDto cartDto) {
		if (orderDao.checkCartProduct(cartDto) == 0) {
			return true;
		}
		return false;
	}

	public void updateQty(CartDto cartDto) {
		orderDao.updateProductQty(cartDto);
	}

	public void deleteProduct(CartDto cartDto) {
		orderDao.deleteCartProduct(cartDto);
	}

	public CartDto getProduct(CartDto cartDto) {
		CartDto selectedProduct  = orderDao.getSeletedProduct(cartDto);
		return selectedProduct;
	}

	public void insertOrder(OrderDto orderDto) {
		orderDao.insertOrderProducts(orderDto);
	}

	public void insertOrderDetail(OrderDetailDto orderDetailDto) {
		orderDao.insertOrderDetailProducts(orderDetailDto);
	}

	public int getCartNum(String userId) {
		return orderDao.getCartNumById(userId);
	}
	
	public int totalOrderNum(String userId) {
		return orderDao.totalOrderNum(userId);
	}

	public List<OrderDto> getOrdersByOrderIds(Map<String, Object> params) {
		return orderDao.getOrdersByOrderIds(params);
	}

}
