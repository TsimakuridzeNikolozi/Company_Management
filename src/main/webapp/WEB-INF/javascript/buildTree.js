function toggleChildren (e) {
    e.stopPropagation();

    var el = $(this);
    el.children('ul').toggle();
}

function buildNests (obj, parent) {
    var item, list;
    item = $('<li />', {
        text: obj.person.firstName + " " + obj.person.lastName + "  \"" + obj.person.post.postName + "\"",
        on: {
            click: toggleChildren
        }
    });
    list = $('<ul />', {
        html: [item]
    });

    parent.append(list);

    if (obj.children.length > 0) {
        obj.children.forEach(function (e) {
            buildNests(e, item);
        });
    }
}