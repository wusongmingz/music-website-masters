<template>
<div class="headerBox">
  <div class="shell">
    <header>
      <div class="img"></div>
    </header>
    <div class="main">
      <h2>AIGC音乐播放器</h2>
    </div>

  </div>
</div>
</template>

<script lang="ts" setup>
import { ref } from 'vue';
import { defineExpose } from 'vue';
const header = document.querySelector('header');
const img = document.querySelector('.img');
let scrollDistance = 0;
let requestId;

function updateHeaderClipPath() {
    const clipPathValue = "polygon(0 0, 100% 0, 100% ${(scrollDistance <= 600)? 100 - ((scrollDistance / 600) * 60) : 75}%, 0 100%)";
    header.style.clipPath = clipPathValue;
    const scaleValue = 1 + ((scrollDistance / 600) * 1);
    // img.style.transform = `scale(${scaleValue})`;
    const opacityValue = (scrollDistance / 600);
}

function scrollHandler(event) {
    if (event.deltaY < 0) {
        scrollDistance = Math.max(0, scrollDistance + event.deltaY);
    } else {
        scrollDistance = Math.min(600, scrollDistance + event.deltaY);
    }
    if (!requestId) {
        requestId = window.requestAnimationFrame(() => {
            updateHeaderClipPath();
            requestId = null;
        });
    }
}

window.addEventListener('wheel', scrollHandler);

// 定义响应式数据
const text = ref('这是headers组件默认显示的文本');

// 定义一个方法用于修改文本内容
const changeText = () => {
  text.value = '文本已被修改';
};

// 向外暴露组件内的方法和数据（这里仅暴露了changeText方法，可按需添加更多）
defineExpose({
  changeText
});
</script>
<style lang="scss" scoped>
*{
  margin: 0;
  padding: 0;
}
.shell{
  width: 100%;
  display: flex;
  flex-direction: column;
}
.headerBox{
  height: 1000px;
}
header{
  width: 100%;
  height: 500px;
  overflow: hidden;
  clip-path: polygon(0 0, 100% 0, 100% 100%, 0 100%);
  transition: clip-path 0.5s ease;

}
 .img{
  width: 100%;
  height: 100%;
  background: url(../assets/images/裁剪效果.jpg);
 }
 .main{
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  flex-direction: column;
  margin-top: 100px;
 }
 .main h2{
  font-size: 50px;
  margin-bottom: 40px;
 }
 .mian img{
  width:50px
 }
</style>