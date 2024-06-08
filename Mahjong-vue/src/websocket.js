// Used to manage WebSocket connections and message handling logic

// Encapsulates the logic of connecting, sending messages and processing messages for WebSocket.

import ipAddress from "../ipAddress.js";

const ip = ipAddress.LAN_IP_BACK;

class WebSocketService {
    constructor() {
        this.socket = null;
        // Using an array of callbacks to store message processing callback functions,
        // allows multiple components to register their own callback functions to process incoming messages.
        this.callbacks = [];
        this.messageBuffer = []; // Used to store messages received before the listener registers
    }

    connect() {
        console.log("successful connect!");
        this.socket = new WebSocket(`ws://${ip}/ws`);

        this.socket.onopen = () => {
            console.log('WebSocket connection opened');
        };

        this.socket.onmessage = (event) => {
            try{
                const data = JSON.parse(event.data);
                this.messageBuffer.push(data); // Store the message in a buffer
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
        // When a new listener registers, the message in the buffer is passed to it
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
