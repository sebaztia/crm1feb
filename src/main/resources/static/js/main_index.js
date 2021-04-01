/**
 * 
 */
$(document).ready(function() {

	$('.newAddBookBtn, .table .editBtn').on('click', function(event) {
		event.preventDefault();

		var href = $(this).attr('href');
		var text = $(this).text();
		if (text == 'Edit') {
			$("label.error").hide();
			$(".error").removeClass("error");
			$("#saveeditbutton").prop("value", "Update");
			$.get(href, function(addressbook, status) {
				$('.myForm #id').val(addressbook.id);
				$('.myForm #firstname').val(addressbook.firstname);
				$('.myForm #lastname').val(addressbook.lastname);
				$('.myForm #phonenumber').val(addressbook.phonenumber);
				$('.myForm #email').val(addressbook.email);
				$('.myForm #address').val(addressbook.address);
			});

			$('.myForm #exampleModal').modal();
		} else {
			$("label.error").hide();
			$(".error").removeClass("error");
			$("#saveeditbutton").prop("value", "Save");
			$('.myForm #id').val('');
			$('.myForm #firstname').val('');
			$('.myForm #lastname').val('');
			$('.myForm #phonenumber').val('');
			$('.myForm #email').val('');
			$('.myForm #address').val('');
			$('.myForm #exampleModal').modal();
		}
	});

	$('.table .delBtn').on('click', function(event) {
		event.preventDefault();
		var href = $(this).attr('href');
		$('#myModal #delRef').attr('href', href);
		$('#myModal').modal();
	});

	// $('#myTable').DataTable();

});