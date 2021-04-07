/**
 *
 */
$(document).ready(function() {

    $('.newAddBookBtn, .table .editBtn, .table .emailBtn').on('click', function(event) {
        event.preventDefault();

        var href = $(this).attr('href');
        var text = $(this).text();
        $("#simple-msg").html('');
        if (text == 'Edit') {
            $("label.error").hide();
            $(".error").removeClass("error");
            $("#saveeditbutton").prop("value", "Update");
            $('.myForm #exampleModalLabel').text('Edit Call List');
            $('.myForm #refNumber').prop("disabled", false);
            $.get(href, function(enquiryCallList, status) {

                $('.myForm #id').val(enquiryCallList.id);
                $('.myForm #contactName').val(enquiryCallList.contactName);
                $('.myForm #contactNumber').val(enquiryCallList.contactNumber);
                $('.myForm #refNumber').val(enquiryCallList.refNumber);
                $('.myForm #query').val(enquiryCallList.query);

                let sss = '';
                $.ajax({
                    url : 'getStaffLists',
                    data : { "userId" : 12},
                    success : function(result) {
                        for (var i=0; i< result.length; i++) {
                            if (enquiryCallList.staffName == result[i]) {
                                sss += '<option selected="selected" value=' + result[i] + '>' + result[i] + '</option>';
                            } else {
                                sss += '<option value=' + result[i] + '>' + result[i] + '</option>';
                            }
                        }
                        $(".myForm #staffName").find('option')
                            .remove()
                            .end().append(sss);
                    }
                });
                $('.myForm #staffName').val(enquiryCallList.staffName);
                $('.myForm #emailCheck').prop("checked", enquiryCallList.emailCheck);
                $('.myForm #callCheck').prop("checked", enquiryCallList.callCheck);
                $('.myForm #emailDone').prop("checked", enquiryCallList.emailDone);
                $('.myForm #callDone').prop("checked", enquiryCallList.callDone);
            });

            $('.myForm #exampleModal').modal();
        } else if (text == 'Email') {
            $("label.error").hide();
            $(".error").removeClass("error");

            $.get(href, function(emailDto, status) {
                $('.myEmailForm #to').val('');
                $('.myEmailForm #subject').val(emailDto.subject);
                $('.myEmailForm #text').val(emailDto.text);

            });

            $('#emailModal').modal();
        } else {
            $("label.error").hide();
            $(".error").removeClass("error");
            $("#saveeditbutton").prop("value", "Save");
            $('.myForm #id').val('');
            //  $('.myForm #record-date').val(new Date().toJSON().slice(0, 19));
            $('.myForm #contactName').val('');
            $('.myForm #contactNumber').val('');
            $('.myForm #refNumber').val('');
            $('.myForm #refNumber').prop("disabled", true);
            $('.myForm #query').val('');
            $('.myForm #emailCheck').prop("checked", false);
            $('.myForm #callCheck').prop("checked", false);
            $('.myForm #emailDone').prop("checked", false);
            $('.myForm #callDone').prop("checked", false);
            $('.myForm #exampleModalLabel').text('New Call List');

            let sss = '';
            $.ajax({
                url : 'getStaffLists',
                data : { "userId" : 12},
                success : function(result) {
                    for (var i=0; i< result.length; i++) {
                        console.log(result[i]);
                        sss += '<option value=' + result[i] + '>' + result[i] + '</option>';
                    }
                    $(".myForm #staffName").find('option')
                        .remove()
                        .end().append(sss);
                }
            });

            $('.myForm #exampleModal').modal();
        }
    });

    $('.table .delBtn').on('click', function(event) {
        event.preventDefault();
        var href = $(this).attr('href');
        $('#myModal #delRef').attr('href', href);
        $('#myModal').modal();
    });

    $('.table .leadsBtn').on('click', function(event) {
        event.preventDefault();
        var href = $(this).attr('href');
        $('#myLeadsModal #leadsRef').attr('href', href);
        $('#myLeadsModal').modal();
    });

    // $('#myTable').DataTable();

});