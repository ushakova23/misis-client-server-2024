function getRequest(context) {
    if (context && context.request) {
        return context.request.rawRequest;
    }
    return {};
}

function getSelectedItem(request) {
    if (request &&
        request.payload &&
        request.payload.meta &&
        request.payload.meta.current_app &&
        request.payload.meta.current_app.state
    ) {
        return request.payload.selected_item;
    }
    return null;
}

function getItems(request) {
    if (request &&
        request.payload &&
        request.payload.meta &&
        request.payload.meta.current_app &&
        request.payload.meta.current_app.state &&
        request.payload.meta.current_app.state.item_selector
    ) {
        return request.payload.meta.current_app.state.item_selector.items;
    }
    return null;
}

function reply(body, response) {
    var replyData = {
        type: "raw",
        body: body
    };    
    response.replies = response.replies || [];
    response.replies.push(replyData);
}

function addAction(action, context) {
    var command = {
        type: "smart_app_data",
        action: action
    };
    
    if (context && context.response) {
        for (var index = 0; context.response.replies && index < context.response.replies.length; index ++) {
            if (context.response.replies[index].type === "raw" &&
                context.response.replies[index].body &&
                context.response.replies[index].body.items
            ) {
                context.response.replies[index].body.items.push({command: command});
                return;
            }
        }
    }
    
    
    return reply({items: [{command: command}]}, context.response);
}

function findItemIdBySelectedItem(request) {
    var items = getItems(request);
    var selectedItem = getSelectedItem(request);
    if (selectedItem && items && items.length > selectedItem.index) {
        return items[selectedItem.index].id;
    }
    return null;
}

function findItemIdByNumber(number, request) {
    var items = getItems(request);
    if (items && items.length) {
        for (var ind = 0; ind < items.length; ind++) {
            if (items[ind].number == number) {
                return items[ind].id;
            }
        }
    }
    return null;
}

var iitemId = null;
