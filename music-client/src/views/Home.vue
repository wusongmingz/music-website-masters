<template>
  <!-- <Header></Header> -->
  <!--轮播图-->
  <el-carousel v-if="swiperList.length" class="swiper-container" type="card" height="20vw" :interval="4000">
    <el-carousel-item v-for="(item, index) in swiperList" :key="index">
      <img :src="HttpManager.attachImageUrl(item.pic)" />
    </el-carousel-item>
  </el-carousel>
  <!--热门歌单-->
  <play-list class="play-list-container" title="歌单" path="song-sheet-detail" :playList="songList"></play-list>
  <!--热门歌手-->
  <play-list class="play-list-container" title="歌手" path="singer-detail" :playList="singerList"></play-list>
  <Dia></Dia>
</template>

<script lang="ts" setup>
import { ref, onMounted } from "vue";

import PlayList from "@/components/PlayList.vue";
import {  NavName } from "@/enums";
import { HttpManager } from "@/api";
import mixin from "@/mixins/mixin";
import Dia from '@/components/Dia.vue'

const songList = ref([]); // 歌单列表
const singerList = ref([]); // 歌手列表
const swiperList = ref([]);// 轮播图 每次都在进行查询
const { changeIndex } = mixin();
try {

  HttpManager.getBannerList().then((res) => {
    swiperList.value = (res as ResponseBody).data.sort();
  });

  HttpManager.getSongList().then((res) => {
    songList.value = (res as ResponseBody).data.sort().slice(0, 10);
  });

  HttpManager.getAllSinger().then((res) => {
    singerList.value = (res as ResponseBody).data.sort().slice(0, 10);
  });

  onMounted(() => {
    changeIndex(NavName.Home);
  });
} catch (error) {
  console.error(error);
}
</script>

<style lang="scss" scoped>
@import "@/assets/css/var.scss";

/*轮播图*/
.swiper-container {
  width: 90%;
  margin: auto;
  padding-top: 20px;
  img {
    width: 100%;
    radius: 10px;
  }
}
.swiper-container {
  perspective: 1000px;
}
.carousel-item-3d {
  transform-style: preserve-3d;
  transition: transform 0.6s ease-in-out;
  transform-origin: center center -500px;
}
.carousel-item-3d img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}
.carousel-3d-enter-active,
.carousel-3d-leave-active {
  transition: all 0.6s ease-in-out;
}
.carousel-3d-enter,
.carousel-3d-leave-to {
  transform: rotateY(90deg) translateZ(200px);
}
.swiper-container:deep(.el-carousel__indicators.el-carousel__indicators--outside) {
  display: inline-block;
  transform: translateX(30vw);
}

.el-slider__runway {
  background-color: $color-blue;
}
</style>
