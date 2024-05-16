<template>
    <div class="background">
        <div class="content">
            <header>
                <h1>北京麻将规则</h1>
            </header>

            <section>
                <h2>基本规则</h2>
                <p>北京麻将是一种中国传统麻将游戏。它通常由四名玩家进行游戏，使用136张牌，包括：</p>
                <ul>
                    <li>筒子（1到9，每种各四张）</li>
                    <li>条子（1到9，每种各四张）</li>
                    <li>万子（1到9，每种各四张）</li>
                    <li>风牌（东、南、西、北各四张）</li>
                    <li>箭牌（中、发、白各四张）</li>
                </ul>
            </section>

            <section>
                <h2>游戏目标</h2>
                <p>游戏的目标是通过抓牌和吃、碰、杠等操作，组成规定的牌型并“胡牌”。</p>
            </section>

            <section>
                <h2>胡牌条件</h2>
                <p>胡牌需要满足以下基本条件：</p>
                <ul>
                    <li>必须有一对将牌（两张相同的牌）</li>
                    <li>其余的牌必须组成顺子、刻子或杠子</li>
                </ul>
            </section>

            <section>
                <h2>计分规则</h2>
                <p>胡牌后，根据牌型和特殊牌的组合计算分数。以下是一些常见的计分方式：</p>
                <ul>
                    <li>平胡：基础分</li>
                    <li>自摸：加分</li>
                    <li>七对：加分</li>
                    <li>清一色：加分</li>
                    <li>大三元：大加分</li>
                </ul>
            </section>

            <section>
                <h2>常用术语</h2>
                <p>在北京麻将中，有一些常用术语需要了解：</p>
                <ul>
                    <li>吃：从上家那里拿一张牌，和自己手中的两张牌组成一个顺子</li>
                    <li>碰：拿到一张与自己手中两张相同的牌，组成一个刻子</li>
                    <li>杠：拿到一张与自己手中三张相同的牌，组成一个杠子，并从牌墙中再抓一张牌</li>
                    <li>听牌：手中牌已经凑成听牌状态，只需再抓到一张特定的牌即可胡牌</li>
                    <li>花牌：一些特殊的牌，通常在抓到时可以立即放置一旁并从牌墙中再抓一张牌</li>
                    <li>庄家：总是由庄家先摸牌，并且先出牌，出牌时不需要先摸牌</li>
                </ul>
            </section>

            <section>
                <h2>游戏流程</h2>
                <p>以下是北京麻将的基本游戏流程：</p>
                <ul>
                    <li><strong>准备阶段：</strong>洗牌、码牌并确定庄家。每位玩家抓13张牌，庄家抓14张。</li>
                    <li><strong>抓牌：</strong>按照逆时针顺序依次抓牌，庄家首先打出一张牌。</li>
                    <li><strong>出牌：</strong>每位玩家轮流出牌，其他玩家可以选择吃、碰或杠。</li>
                    <li><strong>吃、碰、杠：</strong>根据规则进行吃牌、碰牌和杠牌的操作，并补充牌。</li>
                    <li><strong>胡牌：</strong>当一位玩家满足胡牌条件时，宣布胡牌并进行结算。</li>
                    <li><strong>结算：</strong>根据胡牌的牌型和特殊牌进行计分，并进行下一局游戏。</li>
                </ul>
            </section>

            <img alt="Mahjong" class="logo" src="./assets/mahjong.png" width="334" height="240"/>
        </div>

    </div>
</template>

<script>
export default {
    name: "GameRules"
}
</script>

<style scoped>
    h1 {
        font-weight: 500;
        font-size: 3rem;
        position: fixed;
        top: 20px;
        left: 620px;
        text-align: center;
    }

    .logo {
        margin: 10rem 0 0 0;
        position: fixed;
        left: 950px;
        bottom: 10px;
    }

    .background {
        background-image: url('@/assets/background.png');
        background-size: cover;
        background-repeat: no-repeat;
        background-position: center;
        position: fixed;
        top:0;
        left:0;
        width: 100%;
        height: 100vh;
    }

    section {
        padding: 20px;
        margin-bottom: 10px; /*间距*/
        border-radius: 8px;
        box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
        float: left;
        width: 45%;
        margin-right: 5%;
    }

    .content {
        margin: 10rem 0 0 0;
        position: fixed;
        left: 80px;
        top:-50px;
    }
</style>