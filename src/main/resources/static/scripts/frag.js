$( document ).ready(function() {

    let sss = '';
    $.ajax({
        url : 'getNotifications',
        success : function(result) {
            if (result.length > 0) {
                $('#spanNotifyCount').text(result.length);
            } else {
                $('#spanNotifyCount').text('');
            }

            $.each(result, function (i, item) {
                sss += '<li class="media"> <div class="media-body">' + item.author + ' mentioned you on ' +
                    '<a href="showClientAndHaveSeen/' + item.id + '">'

                    + item.clientName + '</a></div></li>';
            });

            $("#ulId").find('li')
                .remove()
                .end().append(sss);

            return;
        }
    });

});