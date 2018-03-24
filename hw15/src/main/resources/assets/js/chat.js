$(() => {
    const data = $("#frame").data();
    const login = data.login;

    const socket = new WebSocket("ws://localhost:10001/chat-" + login);

    socket.onopen = function(event) {
        console.log('Connection is set up');
        console.log(arguments);
    };

    socket.onmessage = function (event) {
        let data = $.parseJSON(event.data);
        newMessage(data, data.login === login);
    };

    function newMessage(data, self) {
        if($.trim(data) === '') {
            return false;
        }

        const messageContainer = $(
                '<li class="sent"><img src="/assets/img/face.png" alt="" /><p>' +
                data.login + '<br/>' + data.time + '<br/>' + data.message +
                '</p></li>'
            )
            .appendTo($('.messages ul'));

        $('.message-input input').val(null);
        $('.contact.active .preview').html('<span>You: </span>' + data.message);
        $(".messages").animate({ scrollTop: $(document).height() }, "fast");

        if (!self) {
            messageContainer.removeClass("sent").addClass("replies");
        }
    }

    function getInputMessage() {
        return $(".message-input input").val();
    }

    function send() {
        const message = getInputMessage();
        socket.send('{"message": "' + message + '"}');
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
