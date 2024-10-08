<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!DOCTYPE html>
<html>

<head>
	<meta charset="UTF-8">
	<title>장바구니</title>
	<script src="${pageContext.request.contextPath}/resources/jquery/jquery.min.js"></script>
	<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/basket.css">
	<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/common.css">
	<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/sweetalert2/sweetalert2.min.css">
	<script src="${pageContext.request.contextPath}/resources/sweetalert2/sweetalert2.min.js"></script>
</head>

<body>
	<script src="${pageContext.request.contextPath}/resources/js/basket.js"></script>
	<div id="header">
		<%@ include file="/WEB-INF/views/common/header.jsp" %>
	</div>

	<h1>장바구니</h1>

	<div class="basket" id="basket">
		<div class="product-list">
			<div class="basket-list-header">
				<div class="checkbox-container">
					<input type="checkbox" id="allchk" onclick="allchk"> 
						<label for="allchk">전체선택</label>
				</div>
				<div class="button-group">
					<button type="button" class="selected-delete-btn">
						선택 상품 삭제
					</button>
					<button type="button" class="soldout-delete-btn">
						품절 상품 삭제
					</button>
				</div>
			</div>
			<hr id="hr-topLine">
			<div id="productList">
				<c:if test="${not empty cartList}">
					<c:forEach items="${cartList}" var="cart">
						<div class="product">
			                <div class="product-body">
			                    <input type="checkbox" class="product-checkbox" data-pid="${cart.productId}">
			                    <div class="product-info">
				                    <div class="img">
				                    	<a href="${pageContext.request.contextPath}/product/detailpage?productId=${cart.productId}">
				                    		<img src="${pageContext.request.contextPath}/order/loadMainImg?productId=${cart.productId}" alt="${cart.productName}" class="picture">
				                    	</a>
				                    </div>
				                    <div class="product-label">
				                        <div class="product-name">
				                        	<a href="${pageContext.request.contextPath}/product/detailpage?productId=${cart.productId}">
					                        	<span><strong>${cart.productName}</strong></span>
					                        </a>
				                        </div>
				                        <div class="product-description">
				                        	<span>${cart.productSummary}</span>
				                        </div>
				                    </div>
				                </div>
			                    <c:if test="${cart.productStock != 0}">
			                    	<div class="amount">
				                    	<select class="product-amount" data-pid="${cart.productId}" data-qty="${cart.productQty}">
				                    		<option value="1">1</option>
				                    		<option value="2">2</option>
				                    		<option value="3">3</option>
				                    		<option value="4">4</option>
				                    		<option value="5">5</option>
				                    		<option class="select-amount" value="${cart.productQty}" style="display: none;">${cart.productQty}</option>
				                    		<option class="input-amount">직접입력</option>
				                    	</select>
				                    	<div class="userInput">
					                    	<input type="number" class="custom-amount" style="display: none;" placeholder="직접 입력" min="1" max="${cart.productStock}" />
					                    	<button class="custom-amount-btn" data-stock="${cart.productStock}" style="display: none;">변경</button>
					                    </div>
			                    	</div>
			                    </c:if>
			                    <c:if test="${cart.productStock == 0}">
			                    	<div class="soldout" data-status="soldout" data-pid="${cart.productId}">품절</div>
			                    </c:if>
			                    <div class="product-price" data-price="${cart.productPrice}">
									<span class="product-total-price" data-total-price="${cart.productQty * cart.productPrice}">
										<fmt:formatNumber value="${cart.productQty * cart.productPrice}" type="number" groupingUsed="true"/></span>원
			                    </div>
			                    <button class="basket-delete" onclick="location.href='${pageContext.request.contextPath}/order/deleteBasket?productId=${cart.productId}'">
			                    	<img src="${pageContext.request.contextPath}/resources/image/X버튼.png" alt="삭제 버튼" class="delete-icon" style="width: 20px; height: 20px;">
			                    </button>
			                </div>
		                </div>
		        	</c:forEach>
		        </c:if>
		        <c:if test="${empty cartList}">
		        	<div class="emptyInfo">
		        		<img src="${pageContext.request.contextPath}/resources/image/info.png">
		        		<span class="emptyInfoText">장바구니에 저장된 상품이 없습니다.</span>
		        	</div>
		        </c:if>
			</div>
			<button class="scroll-btn-up" onclick="scrollToTop()"></button>
		</div>

		<!-- 결제 정보 창 -->
		<div class="payment-info">
			<h2>결제정보</h2>
			<div id="payment-info-body">
				<div class="payment-info-body-content1">
					<div class="orderPrice">
						<span>총 주문 금액 &nbsp;</span><span id="sumPrice">0원</span>
					</div>
					<div class="delivery">
						<span>배송비 </span><span id="deliveryPrice">2,500 원</span>
					</div>
				</div>
				<div class="divider"></div>
				<div class="payment-info-body-content1">
					<div class="totalPrice" id="sum_p_price">
						<span>총 결제 금액 &nbsp;</span><span id="totalPrice-num">0</span> 원
					</div>
					<div id="goOrder" class="">
						<div id="product-order">
							<button id="order-button">선택 상품 주문</button>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div id="footer">
		<%@ include file="/WEB-INF/views/common/footer.jsp" %>
	</div>
</body>

</html>