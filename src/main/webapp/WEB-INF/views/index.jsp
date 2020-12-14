<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Pdf Signature</title>
<link rel="stylesheet"
	href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">

<style>
#draggable {
	border: 1px dashed #CCC;
	background-color: #FFFFEE;
	cursor: move;
	position: absolute;
	top: 10px;
	left: 10px;
}

#draggable:hover {
	border: 1px solid #999;
	background-color: #EEE;
}

#droppable1 {
	width: 200px;
	height: 100px;
	position: relative;
}

#droppable2 {
	padding: 0.0em;
	float: left;
	margin: 10px;
	border: 1px solid black;
}
</style>

<script src="https://code.jquery.com/jquery-1.12.4.js"></script>
<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>

<script>
	var rectangleHeight = ${origRectangle.height};
	/**
	 * calculate the position of the draggable rectangle within the image 
	 * with origin (0,0) point in the lower left corner to conform to the pdfbox default origin
	 */
	$(function() {
		$("#draggable").draggable({
			revert : 'invalid',
			start : function() {
				currentParent = $(this).parent().attr('id');
			}
		});

		$("#droppable1").droppable({
			accept : '#draggable'
		})

		$("#droppable2")
				.droppable(
						{
							accept : '#draggable',
							drop : function(event, ui) {

								// get mouse position relative to the droppable, Y position from the bottom
								var dropPositionX = event.pageX
										- $(this).offset().left;
								var dropPositionYbottom = $(this).height()
										- (event.pageY - $(this).offset().top);

								// get mouse offset relative to the draggable, Y offset from the bottom
								var dragItemOffsetX = event.offsetX;
								var dragItemOffsetYbottom = rectangleHeight
										- event.offsetY;

								// get position of the draggable relative to the droppable:
								var dragItemPositionX = dropPositionX
										- dragItemOffsetX;
								var dragItemPositionYbottom = dropPositionYbottom
										- dragItemOffsetYbottom;

								/* alert('DROPPED AT: ' + dragItemPositionX + ', '
										+ dragItemPositionYbottom); */
								$('#rectangleWidth').val(dragItemPositionX);
								$('#rectangleHeight').val(
										Math.round(dragItemPositionYbottom));
							}
						});
	});

	function imgSize() {
		var image = document.querySelector("#droppable2");
		$('#imageWidth').val(image.clientWidth);
		$('#imageHeight').val(image.clientHeight);
		/* alert("Image width=" + currWidth + ", " + "Image height=" + currHeight); */
	}
</script>
</head>

<body>

	<!-- choose pdf -->

	<div>
		<c:if test="${empty load}">
			<!--UPLOAD FILE -->
			<form method="POST" action="uploadPdfFile"
				enctype="multipart/form-data">
				File to upload: <input type="file" name="file" /> <input
					type="submit" value="Upload" />
			</form>
		</c:if>
	</div>

	<div>
		<c:if test="${not empty message}">
    	${message}
		</c:if>
		<!--DISPLAY IMAGE -->
		<c:if test="${not empty img}">


			<form action="./">
				<button type="submit">Get a new file</button>
			</form>
			<br />
			<br />
			
			<!-- save rectangle to a pdf -->
			
			<form method="POST"
				action="downloadPdfFile">
				<input type="hidden" id="rectangleWidth" name="rectangleWidth"
					value="-100">
				<input type="hidden" id="rectangleHeight" name="rectangleHeight"
					value="-100">
				<input type="hidden" id="imageWidth" name="imageWidth" value="">
				<input type="hidden" id="imageHeight" name="imageHeight" value="">
				<button type="submit" onclick="imgSize();">Save the
					rectangle and download the file</button>
				<br />
				<br />
				<br />
				<div id="droppable1"
					style="width:${origRectangle.width}px; height:${origRectangle.height}px;">
					<div id="draggable"
						style="width:${origRectangle.width}px; height:${origRectangle.height}px;"></div>
				</div>
			</form>
			<br />
			<br />
			<img src="data:image/jpg;base64,${img}" id="droppable2">
		</c:if>
	</div>
</body>
</html>