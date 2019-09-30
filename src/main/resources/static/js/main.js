var usernamePage = document.querySelector('#username-page');
var chatPage = document.querySelector('#chat-page');
var usernameForm = document.querySelector('#usernameForm');
var messageForm = document.querySelector('#messageForm');
var messageInput = document.querySelector('#message');
var messageArea = document.querySelector('#messageArea');
var connectingElement = document.querySelector('.connecting');

var stompClient = null;
var username = null;

var colors = [
    '#2196F3', '#32c787', '#00BCD4', '#ff5652',
    '#ffc107', '#ff85af', '#FF9800', '#39bbb0'
];

/*
    开启连接
 */
function connect(event){
    username = document.querySelector('#name').value.trim();
    if(username){
        //隐藏登录页
        usernamePage.classList.add('hidden');
        //展示消息页
        chatPage.classList.remove('hidden');
        var socket = new SockJS("/ws");
        stompClient = Stomp.over(socket);
        stompClient.connect({}, onConnected, onError);
        document.getElementById("connecting").style.cssText = "display: none;"
    }
    event.preventDefault();
}

/*
    连接服务器端回调事件
 */
function onConnected(){
    //订阅"/topic/public"地址,onMessageReceived为接收服务器端消息的回调函数
    stompClient.subscribe("/topic/public", onMessageReceived);
    stompClient.subscribe("/topic", onMessageReceived);
    //发送消息，给用户加入
    stompClient.send("/app/chat.addUser", {}, JSON.stringify({sender: username, type: "JOIN"}));
}

// /**
//  * websocket链接关闭的回调函数
//  */
// socket.onclose = function() {
//     console.log('close');
// };

/*
   连接失败事件
 */
function onError(error){
    connectingElement.textContent = 'Could not connect to WebSocket server. Please refresh this page to try again!';
    connectingElement.style.color = 'red';
}

/*
    发送消息方法
 */
function sendMessage(event){
    var message = messageInput.value.trim();
    if(message && stompClient){
        var chatMessage = {
            sender: username,
            type: "CHAT",
            content: message
        };
        stompClient.send("/app/chat.sendMessage", {}, JSON.stringify(chatMessage))
    }
    event.preventDefault();
}

/*
    处理消息方法
 */
function onMessageReceived(payload) {
    var uid = guid();
    var message = JSON.parse(payload.body);

    var messageElement = document.createElement('li');

    if(message.type === 'JOIN') {
        messageElement.classList.add('event-message');
        message.content = message.sender + ' joined!';
    } else if (message.type === 'LEAVE') {
        messageElement.classList.add('event-message');
        message.content = message.sender + ' left!';
    } else {
        messageElement.classList.add('chat-message');

        var avatarElement = document.createElement('i');
        var avatarText = document.createTextNode(message.sender[0]);
        avatarElement.appendChild(avatarText);
        avatarElement.style['background-color'] = getAvatarColor(message.sender);

        messageElement.appendChild(avatarElement);

        var usernameElement = document.createElement('span');
        var usernameText = document.createTextNode(message.sender);
        usernameElement.appendChild(usernameText);
        messageElement.appendChild(usernameElement);
    }

    var textElement = document.createElement('p');
    var messageText = document.createTextNode(message.content);
    textElement.appendChild(messageText);

    messageElement.appendChild(textElement);
    messageElement.setAttribute("id", uid);
    if(message.type === 'CHAT' && message.sender == username){
        console.log(true);

    }
    messageArea.appendChild(messageElement);
    messageArea.scrollTop = messageArea.scrollHeight;
}


function getAvatarColor(messageSender) {
    var hash = 0;
    for (var i = 0; i < messageSender.length; i++) {
        hash = 31 * hash + messageSender.charCodeAt(i);
    }
    var index = Math.abs(hash % colors.length);
    return colors[index];
}

/*
    获取随机字符 32位 格式: xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx
 */
function guid() {
    return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function (c) {
        var r = Math.random() * 16 | 0,
            v = c == 'x' ? r : (r & 0x3 | 0x8);
        return v.toString(16);
    });
}

usernameForm.addEventListener("submit", connect, true);
messageForm.addEventListener("submit", sendMessage, true);
