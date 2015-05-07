$(document).ready(function() {
    reload();

    $.widget('myLib.bookAutocomplete', $.ui.autocomplete, {
        _renderItem: function(ul, item) {
            var bookData = '<p><strong>' + item.label + '</strong> (' + item.id + ')</p><p><small>(' + item.authors + ')</small></p>';
            return $('<li>')
                    .attr('data-value', item.value)
                    .append($('<a>').html(bookData))
                    .append(generateLabels(item.status, item.id))
                    .appendTo(ul);
        }
    });

    $('#search').bookAutocomplete({
        minLength: 3,
        focus: function(event, ui) {
            return;
        },
        source: function(request, response) {
            $.ajax({
                url: "/search/" + request.term,
                dataType: "json",
                success: function(data) {
                    response($.map(data, function(book) {
                        return {
                            label: book.title,
                            id: book.flibustaID,
                            authors: getAuthorsLastNames(book.authors),
                            status: book.status,
                            value: ''
                        };
                    }));
                }
            });
        }
    });
});

function reload() {
    $('#gtr').empty();
    $('#rng').empty();
    $('#r').empty();
    $.getJSON('/catalog', function(data) {
        var gtrC = 0;
        var rngC = 0;
        var rC = 0;

        var gtrR = 0;
        var rngR = 0;
        var rR = 0;
        for (var i = 0; i < data.length; i++) {
            var book = data[i];
            var status = book.status;

            var div = $('<div class="col-md-4"></div>');
            var img = $('<p>');
            if (book.image) {
                img.html('<img src="' + book.image + '" width="85" height="128">');
            } else {
                $.post('/cover/' + book.flibustaID);
                img.html('<img src="/img/default.png">');
            }
            var title = $('<h3>').html('<a href="http://flibusta.net/b/' + book.flibustaID + '" target="_blank">' + book.title + '</a>');
            var authors = $('<h5>').text(getAuthors(book.authors));
            var labels = generateLabels(status, book.flibustaID);

            div.append(img).append(title).append(authors).append(labels);
            if (status === 'GOING_TO_READ') {
                if (gtrC % 3 === 0) {
                    $('#gtr').append($('<div class="row" id="gtrrow' + gtrR + '"></div>'));
                    gtrR++;
                }
                $('#gtrrow' + (gtrR - 1)).append(div);
                gtrC++;
            }
            if (status === 'READING') {
                if (rngC % 3 === 0) {
                    $('#rng').append($('<div class="row" id="rngrow' + rngR + '"></div>'));
                    rngR++;
                }
                $('#rngrow' + (rngR - 1)).append(div);
                rngC++;
            }
            if (status === 'READ') {
                if (rC % 3 === 0) {
                    $('#r').append($('<div class="row" id="rrow' + rR + '"></div>'));
                    rR++;
                }
                $('#rrow' + (rR - 1)).append(div);
                rC++;
            }
        }
        
        var textAGTR = $('#agtr').text().replace(/\d+/, '' + gtrC);
        var textARNG = $('#arng').text().replace(/\d+/, '' + rngC);;
        var textAR = $('#ar').text().replace(/\d+/, '' + rC);;
        $('#agtr').text(textAGTR);
        $('#arng').text(textARNG);
        $('#ar').text(textAR);
    });
}

function getAuthorsLastNames(authors) {
    var lNames = [];
    for (var i = 0; i < authors.length; i++) {
        lNames[i] = authors[i].lastName;
    }
    return lNames.join(', ');
}

function getAuthors(authors) {
    var names = [];
    for (var i = 0; i < authors.length; i++) {
        var author = authors[i];
        var name = '';
        if (author.firstName !== '') {
            name += author.firstName;
            name += ' ';
        }
        if (author.middleName !== '') {
            name += author.middleName;
            name += ' ';
        }
        name += author.lastName;
        names[i] = name;
    }
    return names.join(', ');
}

function generateLabels(status, flibustaID) {
    var read = $('<button type="button" class="btn btn-default btn-xs">Прочитал</button>');
    var reading = $('<button type="button" class="btn btn-default btn-xs">Читаю</button>');
    var willRead = $('<button type="button" class="btn btn-default btn-xs">Хочу прочитать</button>');
    var noStatus = $('<button type="button" class="btn btn-default btn-xs">Нет статуса</button>');
    if (status === 'NO_STATUS') {
        noStatus.removeClass('btn-default').addClass("btn-success");
    }
    if (status === 'READ') {
        read.removeClass('btn-default').addClass("btn-success");
    }
    if (status === 'READING') {
        reading.removeClass('btn-default').addClass("btn-success");
    }
    if (status === 'GOING_TO_READ') {
        willRead.removeClass('btn-default').addClass("btn-success");
    }
    read.click(function() {
        changeStatus(flibustaID, 'READ');
    });
    reading.click(function() {
        changeStatus(flibustaID, 'READING');
    });
    willRead.click(function() {
        changeStatus(flibustaID, 'GOING_TO_READ');
    });
    noStatus.click(function() {
        changeStatus(flibustaID, 'NO_STATUS');
    });
    return $('<p>').append(read).append(reading).append(willRead).append(noStatus);
}

function changeStatus(flibustaID, status) {
    $.post('/book/' + flibustaID + '/' + status);
    reload();
}