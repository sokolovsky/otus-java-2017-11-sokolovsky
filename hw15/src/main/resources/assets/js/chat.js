$(() => {
    const data = $("#frame").data();

    function newMessage() {
        const message = $(".message-input input").val();
        if($.trim(message) === '') {
            return false;
        }
        // class="replies" // for reverse of message
        $('<li class="sent"><img src="/assets/img/face.png" alt="" /><p>' + message + '</p></li>').appendTo($('.messages ul'));
        $('.message-input input').val(null);
        $('.contact.active .preview').html('<span>You: </span>' + message);
        $(".messages").animate({ scrollTop: $(document).height() }, "fast");
    }

    $('.submit').click(function() {
        newMessage();
    });

    $(window).on('keydown', function(e) {
        if (e.which === 13) {
            newMessage();
            return false;
        }
    });

    const socket = new WebSocket("ws://localhost:10001/chat-" + data.login);

    socket.onopen = function(event) {
        console.log('Connection is set up');
        console.log(arguments);
    };

    socket.onmessage = function (event) {
        console.log(event);
    };
});
