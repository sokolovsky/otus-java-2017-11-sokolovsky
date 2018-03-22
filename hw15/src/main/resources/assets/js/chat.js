$(() => {
    const data = $("#frame").data();
    const login = data.login;

    const socket = new WebSocket("ws://localhost:10001/chat-" + login);

    socket.onopen = function(event) {
        console.log('Connection is set up');
        console.log(arguments);
    };

    socket.onmessage = function (event) {
        console.log(event);
        if (event.data.sender === login) {
            return;
        }
        newMessage(event.data.message);
    };

    function newMessage(message) {
        if($.trim(message) === '') {
            return false;
        }
        // class="replies" // for reverse of message
        $('<li class="sent"><img src="/assets/img/face.png" alt="" /><p>' + message + '</p></li>').appendTo($('.messages ul'));
        $('.message-input input').val(null);
        $('.contact.active .preview').html('<span>You: </span>' + message);
        $(".messages").animate({ scrollTop: $(document).height() }, "fast");
    }

    function getInputMessage() {
        return $(".message-input input").val();
    }

    function send() {
        const message = getInputMessage();
        newMessage(message);
        socket.send('{"message": "' + message + '"}');
        socket.send('a simple string');
        console.log("sent", '{"message": "' + message + '"}');
    }

    $('.submit').click(function() {
        send();
    });

    $(window).on('keydown', function(e) {
        if (e.which === 13) {
            send();
            return false;
        }
    });
});
