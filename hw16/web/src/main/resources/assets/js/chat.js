$(() => {
    const container = $("#frame")[0];
    const messageInput = $(container).find('.message-input input')[0];
    const data = $(container).data();
    const login = data.login;
    const port = data.port;

    const socket = new WebSocket("ws://localhost:" + port + "/chat-" + login);
    $.extend(socket, {
        onopen: function (event) {
            console.log('Connection is set up');
            console.log(arguments);
        },
        onmessage: function (event) {
            let data = $.parseJSON(event.data);
            newMessage(data, data.login === login);
        }
    });

    function clearInputArea() {
        $(messageInput).val(null);
    }

    function scrollMessages() {
        $(container).find(".messages").animate({ scrollTop: $(window).height() }, "fast");
    }

    function includeMessage(messageElement) {
        $(messageElement).appendTo($(container).find('.messages ul:first'));
    }

    function setMessageAsForeign(messageElement) {
        $(messageElement).removeClass("sent").addClass("replies")
    }

    function getInputMessageText() {
        return $(messageInput).val();
    }

    function addZeros(n, needLength) {
        needLength = needLength || 2;
        n = String(n);
        while (n.length < needLength) {
            n = "0" + n;
        }
        return n
    }
    
    function createMessageElement(login, time, message) {

        // it can to work out with template, but is a bit hardier than it need to
        return $(
            '<li class="sent"><img src="/assets/img/face.png" alt="" /><p><b>' + login + '</b><br/>' +
            addZeros(time.getHours(), 2) + ':' + addZeros(time.getMinutes(), 2) + '<br/><br /><i>' +
            message + '</i></p></li>'
        )[0];
    }

    function newMessage(data, self) {
        if($.trim(data) === '') {
            return false;
        }
        const time = new Date(data.time);
        const messageContainer = createMessageElement(data.login, time, data.message);

        includeMessage(messageContainer);
        scrollMessages();

        if (!self) {
            setMessageAsForeign(messageContainer);
        }
    }

    function send() {
        const message = getInputMessageText();
        if (!message) {
            return;
        }
        socket.send('{"message": "' + message + '"}');
        console.log("sent", '{"message": "' + message + '"}');
    }

    $(container).find('.submit').click(function() {
        send();
        clearInputArea()
    });

    $(window).on('keydown', function(e) {
        if (e.which === 13) {
            send();
            clearInputArea();
            return false;
        }
    });
});
