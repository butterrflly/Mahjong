<template>
    <div class="background">
        <div class="me">
            <div class="my_hand">
                <img alt="f-1" class="hand" id="f-1" src="../assets/tiles-front/f-1b.png"/>
                <img alt="f-2" class="hand" id="f-2" src="../assets/tiles-front/f-2b.png"/>
                <img alt="f-3" class="hand" id="f-3" src="../assets/tiles-front/f-3b.png"/>
                <img alt="f-4" class="hand" id="f-4" src="../assets/tiles-front/f-4b.png"/>
                <img alt="f-5" class="hand" id="f-5" src="../assets/tiles-front/f-5b.png"/>
                <img alt="f-6" class="hand" id="f-6" src="../assets/tiles-front/f-6b.png"/>
                <img alt="f-7" class="hand" id="f-7" src="../assets/tiles-front/f-7b.png"/>
                <img alt="f-8" class="hand" id="f-8" src="../assets/tiles-front/f-8b.png"/>
                <img alt="f-9" class="hand" id="f-9" src="../assets/tiles-front/f-9b.png"/>
                <img alt="f-10" class="hand" id="f-10" src="../assets/tiles-front/f-1t.png"/>
                <img alt="f-11" class="hand" id="f-11" src="../assets/tiles-front/f-2t.png"/>
                <img alt="f-12" class="hand" id="f-12" src="../assets/tiles-front/f-3t.png"/>
                <img alt="f-13" class="hand" id="f-13" src="../assets/tiles-front/f-4t.png"/>
            </div>
        </div>
        <div class="left">  <!--left tiles-->
            <img alt="left" id="l_wall" src="../assets/tiles-left/l_wall.png"/>
            <div class="left_table">  <!--left table-tiles-->
                <div>  <!--stack 1-->
                    <img alt="l-1" class="hand" id="l-1" src="../assets/tiles-left/l_back.png"/>
                    <img alt="l-2" class="hand" id="l-2" src="../assets/tiles-left/l_back.png"/>
                    <img alt="l-3" class="hand" id="l-3" src="../assets/tiles-left/l_back.png"/>
                    <img alt="l-4" class="hand" id="l-4" src="../assets/tiles-left/l_back.png"/>
                </div>
                <div>  <!--stack 2-->
                    <img alt="l-5" class="hand" id="l-5" src="../assets/tiles-left/l_back.png"/>
                    <img alt="l-6" class="hand" id="l-6" src="../assets/tiles-left/l_back.png"/>
                    <img alt="l-7" class="hand" id="l-7" src="../assets/tiles-left/l_back.png"/>
                </div>
            </div>
        </div>
        <div class="opposite_hand">
            <img alt="opp" class="opp_hand" src="../assets/tiles-opposite/o_hand.png"/>
            <img alt="opp" class="opp_hand" src="../assets/tiles-opposite/o_hand.png"/>
            <img alt="opp" class="opp_hand" src="../assets/tiles-opposite/o_hand.png"/>
            <img alt="opp" class="opp_hand" src="../assets/tiles-opposite/o_hand.png"/>
            <img alt="opp" class="opp_hand" src="../assets/tiles-opposite/o_hand.png"/>
            <img alt="opp" class="opp_hand" src="../assets/tiles-opposite/o_hand.png"/>
            <img alt="opp" class="opp_hand" src="../assets/tiles-opposite/o_hand.png"/>
            <img alt="opp" class="opp_hand" src="../assets/tiles-opposite/o_hand.png"/>
            <img alt="opp" class="opp_hand" src="../assets/tiles-opposite/o_hand.png"/>
            <img alt="opp" class="opp_hand" src="../assets/tiles-opposite/o_hand.png"/>
            <img alt="opp" class="opp_hand" src="../assets/tiles-opposite/o_hand.png"/>
            <img alt="opp" class="opp_hand" src="../assets/tiles-opposite/o_hand.png"/>
            <img alt="opp" class="opp_hand" src="../assets/tiles-opposite/o_hand.png"/>
        </div>
        <div class="right">
            <img alt="right" id="r_wall" src="../assets/tiles-right/r_wall.png"/>
        </div>
    </div>
</template>

<script>
export default {
    name: "GameTable"
}
</script>

<style scoped>
    .background {
        background-image: url('@/assets/table.jpg');
        background-size: cover;
        background-repeat: no-repeat;
        background-position: center;
        position: fixed;
        top:0;
        left:0;
        width: 100%;
        height: 100vh;
    }

    /* my single hand tile */
    .hand {
        width: 38px;
        height: 60px;
    }

    /* opposite single hand tile */
    .opp_hand {
        width: 38px;
        height: 60px;
    }

    .me {
        display: flex;
        justify-content: center; /* 水平居中 */
        align-items: center;    /* 垂直居中 */
        height: 100vh;          /* 满屏高度 */
    }


    .my_hand {
        position: fixed;
        left: 50%;
        bottom: 10%;
        transform: translate(-50%, 0);
    }

    /* opposite hand tiles */
    .opposite_hand {
        position: fixed;
        left: 50%;
        top: 20%;
        transform: translate(-50%, 0);
    }

    .left_table {
        display: flex;
        /*flex-direction: column; !* 纵向排列 *!*/
        align-items: center; /* 水平居中对齐，可选 */
        gap: 20px; /* 图片之间的间距 */
        position: fixed;
        left: 10%;
        bottom: 0;
        transform: translate(0, -50%);
    }

    #l_wall {
        width: 80px;
        height: 320px;
        position: fixed;
        left: 20%;
        bottom: 50%;
        transform: translate(0, 50%);
    }

    #r_wall {
        width: 80px;
        height: 320px;
        position: fixed;
        right: 20%;
        bottom: 50%;
        transform: translate(0, 50%);
    }


</style>