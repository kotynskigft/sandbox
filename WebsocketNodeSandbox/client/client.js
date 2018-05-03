const WebSocket = require('ws');

const ws = new WebSocket('ws://localhost:8080/', {
  origin: 'https://websocket.org'
});

ws.on('open', function open() {
  console.log('connected');
  ws.send(Date.now());
});

ws.on('close', function close() {
  console.log('disconnected');
});

ws.on('message', function incoming(data) {
  let dataAsJson = JSON.stringify(data);
  console.log(`Roundtrip time: ${Date.now() - data} ms, message: ${dataAsJson}`);
});