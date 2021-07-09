dragula([
    document.getElementById('first'),
    document.getElementById('instructions'),
    document.getElementById('drafting'),
    document.getElementById('signing'),
    document.getElementById('checking'),
    document.getElementById('stored'),
    document.getElementById('archived'),
    document.getElementById('pre-sign')
])

    .on('drag', function(el) {

        // add 'is-moving' class to element being dragged
        el.classList.add('is-moving');
    }).on('drop', function (el, container) {

      //  updateStickyMotion(container.id, el.getAttribute("data-id"));
        test(container.id, el.getAttribute("data-id"));
    })
    .on('dragend', function(el) {

        // remove 'is-moving' class from element after dragging has stopped
        el.classList.remove('is-moving');

        // add the 'is-moved' class for 600ms then remove it
        window.setTimeout(function() {
            el.classList.add('is-moved');
            window.setTimeout(function() {
                el.classList.remove('is-moved');
            }, 600);
        }, 100);
    });

function test (colName, clientId)  {
    var url = "/tasks/update/status/" + clientId;
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
   /* var projectId = $("#projectTitle").attr("data-id");*/
    console.log('======clientId:' + clientId);
    $
        .ajax({
            type: "POST",
            url:  url,
            dataType: 'json',
            beforeSend: function( xhr ) {
                xhr.setRequestHeader(header, token);
            },
            data: {
                colName: colName
            },
            error: function (res) {
                console.log("Sorry error::: " + JSON.stringify(res));
            },
            success: function (res) {
                console.log("Success" + JSON.stringify(res));
            }
        });
}

/*
var createOptions = (function() {
    var dragOptions = document.querySelectorAll('.drag-options');

    // these strings are used for the checkbox labels
    var options = ['Research', 'Strategy', 'Inspiration', 'Execution'];

    // create the checkbox and labels here, just to keep the html clean. append the <label> to '.drag-options'
    function create() {
        for (var i = 0; i < dragOptions.length; i++) {

            options.forEach(function(item) {
                var checkbox = document.createElement('input');
                var label = document.createElement('label');
                var span = document.createElement('span');
                checkbox.setAttribute('type', 'checkbox');
                span.innerHTML = item;
                label.appendChild(span);
                label.insertBefore(checkbox, label.firstChild);
                label.classList.add('drag-options-label');
                dragOptions[i].appendChild(label);
            });

        }
    }

    return {
        create: create
    }


}());*/

/*var showOptions = (function () {

    // the 3 dot icon
    var more = document.querySelectorAll('.drag-header-more');

    function show() {
        // show 'drag-options' div when the more icon is clicked
        var target = this.getAttribute('data-target');
        var options = document.getElementById(target);
        options.classList.toggle('active');
    }


    function init() {
        for (i = 0; i < more.length; i++) {
            more[i].addEventListener('click', show, false);
        }
    }

    return {
        init: init
    }
}());*/

//createOptions.create();
// showOptions.init();


/* Set the width of the sidebar to 250px and the left margin of the page content to 250px */
function openNav() {
    document.getElementById("mySidebar").style.width = "339px";
  /*  document.getElementById("main").style.marginLeft = "250px";*/
 //   document.getElementById("main").hidden = true;
}

/* Set the width of the sidebar to 0 and the left margin of the page content to 0 */
function closeNav() {
    document.getElementById("mySidebar").style.width = "0";
    document.getElementById("main").style.marginLeft = "0";
  //  document.getElementById("main").hidden = false;
}