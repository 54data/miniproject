function checkValid() {
	// 상품명 유효성 검사
	if ($("#productName").val().length > 30) {
		Swal.fire({
			icon : "error",
			title : "오류",
			text : "상품명은 띄어쓰기 포함 30글자 이내로 작성해주세요.",
		});
		return false;
	}
	// 상품가격 유효성 검사
	const productPriceValid = /^[0-9]{1,10}$/
	if (!productPriceValid.test($("#productPrice").val())) {
		Swal.fire({
			icon : "error",
			title : "오류",
			text : "유효하지 않은 판매가 입니다.",
		});
		return false;
	}

	// 상품수량 유효성 검사
	const productStockValid = /^[0-9]{1,10}$/
	if (!productStockValid.test($("#productStock").val())) {
		Swal.fire({
			icon : "error",
			title : "오류",
			text : "유효하지 않은 상품 수량 입니다.",
		});
		return false;
	}

	// 상품 대표 설명 유효성 검사
	if ($("#productSummary").val().length > 40) {
		Swal.fire({
			icon : "error",
			title : "오류",
			text : "상품 대표 설명은 띄어쓰기 포함 40글자 이내로 작성해 주세요.",
		});
		return false;
	}

	// 상세페이지 대표 설명 유효성 검사
	if ($("#productDetailSummary").val().length > 80) {
		Swal.fire({
			icon : "error",
			title : "오류",
			text : "상세페이지 대표 설명은 띄어쓰기 포함 80글자 이내로 작성해 주세요.",
		});
		return false;
	}

	if (document.getElementById('MainImage') === null) {
		Swal.fire({
			icon : "error",
			title : "오류",
			text : "Main이미지는 필수 입력사항 입니다.",
		});
		return false;
	}

	if (document.getElementById('DetailImage') === null) {
		Swal.fire({
			icon : "error",
			title : "오류",
			text : "상품detail은 필수 입력사항 입니다.",
		});
		return false;
	}

	if (document.getElementById('Sub3Image') !== null) {
		if (document.getElementById('Sub2Image') === null) {
			Swal.fire({
				icon : "error",
				title : "오류",
				text : "Sub2 이미지를 먼저 입력해주세요.",
			});
			return false;
		}
	}

	if (document.getElementById('Sub2Image') !== null) {
		if (document.getElementById('Sub1Image') === null) {
			Swal.fire({
				icon : "error",
				title : "오류",
				text : "Sub1 이미지를 먼저 입력해주세요.",
			});
			return false;
		}
	}
	return true;

}


/* 사진 업로드 */
function previewImage(event, previewId) {
	const file = event.target.files[0];
	const imagePreview = document.getElementById(previewId);
	console.log(imagePreview);
	const usage = event.target.dataset.usage;
	console.log("previewImage 동작중")
	
	if (file) {
		const reader = new FileReader();
		reader.onload = function(e) {
			imagePreview.innerHTML = `<div class="deleteImgButton">
				<a class="emptyInputFile btn btn-sm text-danger"
				data-preview="${previewId}" data-usage="${usage}">X</a>
				</div>
				<img id="${usage}Image" src="${e.target.result}" />`;
		};
		reader.readAsDataURL(file);
	} else {
		imagePreview.innerHTML = `<span>${usage}</span>`;
	}
	
}
	
$(".imageInput").on('change',function(){
	previewId = $(this).data('connect');
	previewImage(event, previewId);
})

$(document).on('click','.image-preview',function(event){
	inputId = "#product" + $(this).data('usage') + "Image";
	console.log(inputId)
	$(inputId).click();
});

$(document).on('click','.emptyInputFile',function(event){
	console.log("emptyInputFile 동작중")
	const previewId = "#" + $(this).data('preview');
	const usage = $(this).data('usage');
	const inputId = "#product"+ usage + "Image";
	
	$(previewId).html(`<span>${usage}</span>`);
	$(inputId).val('');
	
	event.preventDefault();
	event.stopPropagation();
});

/*
 * $('#product-name').on('input', function() { var currentLength =
 * $(this).val().length;
 * 
 * var maxLength = 250; if (currentLength > maxLength) {
 * $(this).val($(this).val().substring(0, maxLength)); // 최대 길이 초과 시 자르기
 * currentLength = maxLength; // 현재 길이 업데이트 }
 * 
 * $('#charCount').text(currentLength + " / " + maxLength); });
 */

var toggleElement1 = document.getElementById('product');
var toggleElement2 = document.getElementById('notice')
var collapseElement1 = document.getElementById('home-collapse1');
var collapseElement2 = document.getElementById('home-collapse2');

document.addEventListener('DOMContentLoaded', function() {

	toggleElement1.addEventListener('click', function() {
		var bsCollapse = new bootstrap.Collapse(collapseElement1, {
			toggle : true
		});
		bsCollapse.toggle();
	});

	toggleElement2.addEventListener('click', function() {
		var bsCollapse = new bootstrap.Collapse(collapseElement2, {
			toggle : true
		});
		bsCollapse.toggle();
	});
});
