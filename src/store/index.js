// store/index.js
import { createStore } from 'vuex';

export default createStore({
    state: {
        name: '',    // 这里存储 name 数据
        owner: ''    // 这里存储 owner 数据
    },
    // 同步方法
    mutations: {
        setName(state, newName) {
            state.name = newName;
        },
        setOwner(state, newOwner) {
            state.owner = newOwner;
        }
    },
    // 异步方法
    actions: {
        updateName({ commit }, newName) {
            commit('setName', newName);
        },
        updateOwner({ commit }, newOwner) {
            commit('setOwner', newOwner);
        }
    },
    // 获取器
    getters: {
        getName: state => state.name,
        getOwner: state => state.owner
    }
});
