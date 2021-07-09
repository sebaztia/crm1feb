$(document).ready(function () {

    $("#message-text, #commentMessage").mention({
        users: [{
            name: 'AmyWinder',
            username: 'AmyWinder ',
            image: 'http://placekitten.com/25/25'
        }, {
            name: 'Dean',
            username: 'Dean ',
            image: 'http://placekitten.com/25/24'
        }, {
            name: 'Daniel',
            username: 'Daniel ',
            image: 'http://placekitten.com/25/23'
        }, {
            name: 'Dora',
            username: 'Dora ',
            image: 'http://placekitten.com/25/22'
        }, {
            name: 'Angela',
            username: 'Angela ',
            image: 'http://placekitten.com/25/21'
        }, {
            name: 'Hollie',
            username: 'Hollie ',
            image: 'http://placekitten.com/25/20'
        }, {
            name: 'SimonCooper',
            username: 'SimonCooper ',
            image: 'http://placekitten.com/24/20'
        }, {
            name: 'Stacie',
            username: 'Stacie ',
            image: 'http://placekitten.com/23/20'
        }, {
            name: 'Claire',
            username: 'Claire ',
            image: 'http://placekitten.com/22/20'
        }, {
            name: 'Sebastian',
            username: 'Sebastian ',
            image: 'http://placekitten.com/21/20'
        }]
    });
});

let addRow = function (clId) {
    let listName = 'estateProperties'; //list name in Catalog.class
    // let fieldsNames = ['id', 'title', 'info']; //field names from Movie.class
    let fieldsNames = ['worth', 'value', 'address', 'own', 'ownNo']; //field names from Movie.class
    //  let fieldName = 'title';
    let rowIndex = document.querySelectorAll('.item').length; //we can add mock class to each movie-row

    let labelText = ['Estimated Estate Worth:', 'Estimated Value:', 'Address:', 'Do you own property?:', 'Yes  No'];

    let row = document.createElement('div');
    row.classList.add('row', 'item');
    if (rowIndex % 2 == 0) {
        row.classList.add('even');
    } else {
        row.classList.add('odd');
    }


    let colinputid = document.createElement('input');
    colinputid.classList.add('form-group');
    colinputid.type = 'hidden';
    colinputid.setAttribute('name', listName + '[' + rowIndex + '].' + 'clientId');
    colinputid.value = clId;
    row.appendChild(colinputid);

    let col1 = document.createElement('label');
    col1.classList.add('col-sm-2', 'col-form-label');

    let count = 0;
    let colLG6;
    fieldsNames.forEach((fieldName) => {

        if (fieldName === 'worth' || fieldName === 'address' || fieldName === 'own') {
            colLG6 = document.createElement('div');
            colLG6.classList.add('col-lg-6', 'padtop');
        }

        let label = document.createElement('label');
        label.textContent = labelText[count];
        colLG6.appendChild(label);
        count++;
        if (fieldName === 'address') {
            let textarea = document.createElement('textarea');
            textarea.classList.add('form-control');
            textarea.rows = 3;
            textarea.id = listName + rowIndex + '.' + fieldName;
            textarea.setAttribute('name', listName + '[' + rowIndex + '].' + fieldName);
            colLG6.appendChild(textarea);
        } else {
            let input = document.createElement('input');
            if (fieldName === 'own' || fieldName === 'ownNo') {
                input.type = 'radio';
                input.value = 'true';
                if (fieldName === 'ownNo') {
                    fieldName = 'own';
                    input.value = 'false';
                }
            } else {
                input.type = 'number';
                input.classList.add('form-control');
                input.required = true;
            }
            input.id = listName + rowIndex + '.' + fieldName;
            input.setAttribute('name', listName + '[' + rowIndex + '].' + fieldName);

            colLG6.appendChild(input);
        }
        row.appendChild(colLG6);
    });

    document.getElementById('propertyList').appendChild(row);
};
function editModalContent(t, clientId, commentId, message) {
    var modal = $('#editUserModal');
    modal.find('#commentId').val(commentId);
    modal.find('#clientId').val(clientId);
    modal.find('#commentMessage').val(message);
}

function deleteModalContent(t, clientId, commentId) {
    var modal = $('#deleteModal');
    modal.find('#deleteId').val(commentId);
    modal.find('#cltId').val(clientId);
}
function deleteEPModalContent(t, epid, clientid) {
    var modal = $('#deleteEPModal');
    modal.find('#deleteEPId').val(epid);
    modal.find('#clientEPId').val(clientid);
}