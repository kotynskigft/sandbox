const WebSocket = require('ws');

var sock = new WebSocket('ws://localhost:8080/ws/oracle');

sock.onmessage = e => {
    console.log(`The answer is ${e.data}`);
    sock.close();
};

sock.onopen = () => {
    console.log(`on open.`);
    sock.send("what is the answer?");
};

sock.onerror = (err) => {
    console.log(`on error`);
}

sock.onclose = (evt) => {
    console.log(`close`);
};

