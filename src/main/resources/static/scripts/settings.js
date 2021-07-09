$(document).ready(function() {
    $("#users").change(function() {
        var userId = $(this).val();
        if (userId > 0) {
            $.ajax({
                url : 'getUserDto',
                data : { "userId" : userId },
                success : function(result) {
                    $('#admin').prop("checked", result["admin"]);
                    $('#user').prop("checked", result["roleUser"]);
                    $('#wills').prop("checked", result["wills"]);
                    $('#leads').prop("checked", result["leads"]);
                    $('#todo').prop("checked", result["todo"]);
                    $('#resultLbl').text('');
                }
            });
        }
        //reset data
       // $('#states').html(s);
      //  $('#cities').html(s);
        $('#admin').prop("checked", false);
        $('#user').prop("checked", false);
        $('#wills').prop("checked", false);
        $('#leads').prop("checked", false);
        $('#todo').prop("checked", false);
        $('#resultLbl').text('');
    });


    $("#assignRole").click(function() {
       var adm = $('#admin').is(":checked");
        var usr = $('#user').is(":checked");
        var wls = $('#wills').is(":checked");
        var lds = $('#leads').is(":checked");
        var todos = $('#todo').is(":checked");

        var userId = $('#users').prop("value")
        if (userId > 0) {
            $.ajax({

                url : 'updateUserDto',
                data : { "userId" : userId, "admin" : adm, "roleUser" : usr, "wills" : wls, "leads" : lds, "todo" : todos},
                success : function(result) {
                   console.log('dfsfdfd' + JSON.stringify(result));
                   $('#resultLbl').text('Roles have been assigned successfully.');
                }
            });
        }
    });
});