// Used to manage WebSocket connections and message handling logic

// Encapsulates the logic of connecting, sending messages and processing messages for WebSocket.

class WebSocketService {
    constructor() {
        this.socket = null;
        // Using an array of callbacks to store message processing callback functions,
        // allows multiple components to register their own callback functions to process incoming messages.
        this.callbacks = [];
    }

    connect() {
        console.log("11111");
        this.socket = new WebSocket("ws://localhost:8080/ws");

        this.socket.onopen = () => {
            console.log('WebSocket connection opened');
        };

        this.socket.onmessage = (event) => {
            const data = JSON.parse(event.data);
            this.callbacks.forEach(callback => callback(data));
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
