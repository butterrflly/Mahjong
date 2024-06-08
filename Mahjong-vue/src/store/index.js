// store/index.js
import { createStore } from 'vuex';

export default createStore({
    state: {
        name: '',    // This is where the name data is stored
        owner: ''    // This is where the owner data is stored
    },
    // Synchronization method
    mutations: {
        setName(state, newName) {
            state.name = newName;
        },
        setOwner(state, newOwner) {
            state.owner = newOwner;
        }
    },
    // Asynchronous methods
    actions: {
        updateName({ commit }, newName) {
            commit('setName', newName);
        },
        updateOwner({ commit }, newOwner) {
            commit('setOwner', newOwner);
        }
    },
    // acquirer
    getters: {
        getName: state => state.name,
        getOwner: state => state.owner
    }
});
