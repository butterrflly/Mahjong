// Used to manage WebSocket connections and message handling logic

// Encapsulates the logic of connecting, sending messages and processing messages for WebSocket.

class WebSocketService {
    constructor() {
        this.socket = null;
        // Using an array of callbacks to store message processing callback functions,
        // allows multiple components to register their own callback functions to process incoming messages.
        this.callbacks = [];
        this.messageBuffer = []; // 用于存储在监听器注册之前收到的消息
    }

    connect() {
        console.log("successful connect!");
        this.socket = new WebSocket("ws://localhost:8080/ws");

        this.socket.onopen = () => {
            console.log('WebSocket connection opened');
        };

        this.socket.onmessage = (event) => {
            try{
                const data = JSON.parse(event.data);
                this.messageBuffer.push(data); // 将消息存储在缓冲区中
                this.callbacks.forEach(callback => callback(data));
            }catch (error) {
                console.error('Failed to parse JSON:', error);
            }

        };

        this.socket.onclose = () => {
            console.log('WebSocket connection closed');
        };

        this.socket.onerror = (error) => {
            console.error('WebSocket error', error);
        };
    }

    sendMessage(message) {
        if (this.socket) {
            this.socket.send(message);
        }
    }

    addMessageListener(callback) {
        this.callbacks.push(callback);
        // 当新的监听器注册时，将缓冲区中的消息传递给它
        this.messageBuffer.forEach(data => callback(data));
    }

    removeMessageListener(callback) {
        this.callbacks = this.callbacks.filter(cb => cb !== callback);
    }

    close() {
        if (this.socket) {
            this.socket.close();
        }
    }
}

const webSocketService = new WebSocketService();
export default webSocketService;
