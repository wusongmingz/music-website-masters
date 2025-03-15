<template>
  <transition name="fade-bounce-y" mode="out-in">
    <div v-show="showDia" id="bot-container">
      <div id="Aurora-Dia--body" :style="cssVariables">
        <div id="Aurora-Dia--tips-wrapper">
          <div id="Aurora-Dia--tips" class="Aurora-Dia--tips">早上好呀～</div>
        </div>
        <transition name="chatani">
          <el-dialog center class="aichat-outbox" v-model="isShowChatBox">
            <template #header>
              <div
                style="text-align: end;display: flex;justify-content: space-between; padding-right: 5px; align-content: center"
              >
                <span
                  style="
                    align-items: center;
                    align-content: center;
                    padding-left: 10px;
                  "
                  >AI对话</span
                >
              </div>
            </template>
            <div class="aichat-body" ref="chatbodyref">
              <div class="message-panel" id="message-panel" ref="messageRef">
                <div class="message-list">
                  <div
                    :class="['message-item', item.type == 1 ? 'ai-item' : '']"
                    v-for="(item, index) in messageList"
                    :key="item"
                    :id="'item' + index"
                  >
                    <template v-if="item.type == 0">
                      <div class="message-content" style="margin-left: 50px">
                        <div class="content-inner">{{ item.content }}</div>
                      </div>
                      <div class="user-icon">我</div>
                    </template>
                    <template v-else>
                      <div class="user-icon">AI</div>
                      <div
                        class="message-content ai-item"
                        style="margin-right: 50px"
                      >
                      <div class="conten-inner"></div>
                        <MdPreview
                          previewTheme="vuepress"
                          :codeFoldable="false"
                          editorId="preview"
                          class="preview"
                          :modelValue="item.content.join('')"
                        />
                        <div class="loading" v-if="item.loading">
                          <img src="../assets/images/发送.png" />
                        </div>
                      </div>
                    </template>
                  </div>
                </div>
              </div>
              <div class="send-panel">
                <el-form
                  :model="formData"
                  ref="formDataRef"
                  @submit.prevent
                  class="input-and-btn"
                >
                  <!--input输入-->
                  <el-form-item label="" class="input" prop="content">
                    <el-input
                      clearable
                      type="textarea"
                      placeholder="请输入你想问的问题"
                      v-model="formData.content"
                      resize="none"
                      :autosize="{ minRows: 1, maxRows: 3 }"
                      @keydown.enter="keySend"
                    ></el-input>
                  </el-form-item>
                  <!--input输入-->
                  <el-form-item label="" prop="" class="send-btn">
                    <el-button
                      type="primary"
                      @click="sendMessage"
                      style="
                        padding: 12px;
                        padding-left: 11px;
                        padding-right: 13px;
                        width: 36px;
                        height: 36px;
                      "
                      :style="[
                        formData.content
                          ? 'background:#0066ff'
                          : 'background:#dcdcdc',
                      ]"
                      :disabled="loading"
                      >                      <img
                        src="../assets/images/发送.png"
                        alt="发送"
                        style="width: 1.5rem; height: 1.5rem;"
                      /></el-button>
                  </el-form-item>
                </el-form>
              </div>
            </div>
          </el-dialog>
        </transition>
        <div id="Aurora-Dia" class="Aurora-Dia" @click="setIsShowChatBox(true)">
          <div id="Aurora-Dia--eyes" class="Aurora-Dia--eyes">
            <div id="Aurora-Dia--left-eye" class="Aurora-Dia--eye left"></div>
            <div id="Aurora-Dia--right-eye" class="Aurora-Dia--eye right"></div>
          </div>
        </div>
        <div class="Aurora-Dia--platform"></div>
      </div>
    </div>
  </transition>
</template>

<script setup lang="ts">
// import "@/assets/iconfont2/iconfont.css";
import { computed } from "vue";
import { useDiaStore } from "../store/dia";
import { useAppStore } from "../store/app";
import { HttpManager } from "@/api";
// 使用组合式 API 代替 defineComponent
const diaStore = useDiaStore();
const appStore = useAppStore();
const showDia = ref(true);

// 在组件挂载时初始化 Bot
onMounted(() => {
  initializeBot();
});

// 初始化 Bot 的函数
const initializeBot = () => {
  if (!appStore.aurora_bot_enable) return;
  diaStore.initializeBot({
    locale: diaStore.aurora_bot.locale,
    tips: diaStore.aurora_bot.tips,
  });
  setTimeout(() => {
    showDia.value = true;
  }, 1000);
};

// 计算 css 变量
const cssVariables = computed(() => {
  return `
    --aurora-dia--linear-gradient: ${appStore.themeConfig.header_gradient_css};
    --aurora-dia--linear-gradient-hover: linear-gradient(
      to bottom,
      ${appStore.themeConfig.gradient.color_2},
      ${appStore.themeConfig.gradient.color_3}
    );
    --aurora-dia--platform-light: ${appStore.themeConfig.gradient.color_3};
  `;
});
const isShowChatBox = ref(false);

const setIsShowChatBox = (v: boolean) => {
  isShowChatBox.value = v;
};
import { ElMessage } from "element-plus";

import {
  ref,
  reactive,
  getCurrentInstance,
  nextTick,
  onMounted,
  onUnmounted,
} from "vue";
const { proxy } = getCurrentInstance();
import { MdPreview, MdCatalog } from "md-editor-v3";
import "md-editor-v3/lib/style.css";

const formData = ref({ content: '' });

const messageList = ref([]);
const loading = ref(false);

const keySend = (event) => {
  if (!event.ctrlKey) {
    event.preventDefault();
    if (loading.value) {
      ElMessage.warning("请等待本次回答结束~");
      return;
    }
    nextTick(() => {
      const content = document.getElementById("message-panel");
      content.scrollTop = content.scrollHeight;
    });

    sendMessage();
  } else {
    formData.value.content += "\n";
  }
};

const sendMessage = async () => {

  
  const message = formData.value.content;
  console.log(message,11111);
  formData.value.content = "";

  if (!message) {
    ElMessage({
      type: "warning",
      message: "请输入内容",
      duration: 2000,
    });
    return;
  }

  messageList.value.push({
    type: 0,
    content: message,
  });

  loading.value = true;

  try {
    const res = await HttpManager.getChatGpt(message);
    console.log(res);
    
    messageList.value.push({
      type: 1,
      content: [res],
      loading: false,
    });
  } catch (error) {
    console.error("Error:", error);
    messageList.value.push({
      type: 1,
      content: ["抱歉，回答失败，请稍后再试。"],
      loading: false,
    });
  } finally {
    loading.value = false;
    nextTick(() => {
      const content = document.getElementById("message-panel");
      content.scrollTop = content.scrollHeight;
    });
  }
};
</script>

<style lang="scss" scoped>
#bot-container {
  position: fixed;
  left: 20px;
  bottom: 100px;
  z-index: 1000;
  width: 70px;
  height: 60px;
}
#Aurora-Dia--body {
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-direction: column;
  width: 100%;
  height: 100%;
  --auora-dia--width: 65px; /* 110px */
  --auora-dia--height: 50px; /* 95px */
  --auora-dia--hover-height: 60px; /* 105px */
  --auora-dia--jump-1: 55px; /* 95px */
  --auora-dia--jump-2: 60px; /* 100px */
  --auora-dia--jump-3: 45px; /* 85px */
  --auora-dia--eye-top: 10px; /* 25px */
  --auora-dia--eye-height: 15px; /* 25px */
  --auora-dia--eye-width: 8px; /* 15px */
  --auora-dia--eye-top: 10px; /* 20px */
  --auora-dia--platform-size: var(--auora-dia--jump-2); /* 100px */
  --auora-dia--platform-size-shake-1: 75px; /* 115px */
  --auora-dia--platform-size-shake-2: 45px; /* 115px */
  --auora-dia--platform-top: -15px; /* 0 */
  --aurora-dia--linear-gradient: var(
    --main-gradient
  ); /* linear-gradient(to bottom, #5fc, #1a8) */
  --aurora-dia--linear-gradient-hover: linear-gradient(
    to bottom,
    #25b0cc,
    #3f60de
  );
  --aurora-dia--platform-light: #b712ac;
}
.Aurora-Dia {
  position: absolute;
  bottom: 30px;
  width: var(--auora-dia--width);
  height: var(--auora-dia--height);
  border-radius: 45%;
  border: 4px solid var(--background-secondary);
  // box-shadow: 0 0 5px rgba(0, 0, 0, 0.5);
  animation: breathe-and-jump 3s linear infinite;
  cursor: pointer;
  z-index: 1;
}
.Aurora-Dia::before {
  content: "";
  position: absolute;
  top: -1px;
  left: -1px;
  width: calc(100% + 3px);
  height: calc(100% + 2px);
  background-color: #2cdcff;
  background: var(--aurora-dia--linear-gradient);
  border-radius: 45%;
  opacity: 0;
  opacity: 1;
  transition: 0.3s linear all;
}
.Aurora-Dia.active {
  animation: deactivate 0.75s linear, bounce-then-breathe 5s linear infinite;
}

.Aurora-Dia--eyes > .Aurora-Dia--eye {
  position: absolute;
  top: var(--auora-dia--eye-top);
  width: var(--auora-dia--eye-width);
  height: var(--auora-dia--eye-height);
  border-radius: 15px;
  background-color: #fff;
  box-shadow: 0 0 7px rgba(255, 255, 255, 0.5);
  animation: blink 5s linear infinite;
}
.Aurora-Dia--eyes > .Aurora-Dia--eye.left {
  left: 25%;
}
.Aurora-Dia--eyes > .Aurora-Dia--eye.right {
  right: 25%;
}
.Aurora-Dia--eyes.moving > .Aurora-Dia--eye {
  animation: none;
}

.Aurora-Dia--platform {
  position: relative;
  top: 0;
  transform: rotateX(70deg);
  width: var(--auora-dia--platform-size);
  height: var(--auora-dia--platform-size);
  box-shadow: 0 0 var(--auora-dia--platform-size)
      var(--aurora-dia--platform-light),
    0 0 15px var(--aurora-dia--platform-light) inset;
  animation: jump-pulse 3s linear infinite;
}

.Aurora-Dia--platform {
  border-radius: 50%;
  transition: 0.2s linear all;
}

.Aurora-Dia:hover {
  animation: shake-to-alert 0.5s linear;
  height: var(--auora-dia--hover-height);
  transform: translateY(-7px);
}
.Aurora-Dia:hover::before {
  background: var(--aurora-dia--linear-gradient-hover);
}
.Aurora-Dia:hover,
.Aurora-Dia:hover > .Aurora-Dia--eyes > .Aurora--Dia-eye {
  border-color: var(--text-accent);
  box-shadow: 0 0 5px var(--text-accent);
}
.Aurora-Dia:hover + .Aurora-Dia--platform {
  box-shadow: 0 0 var(--auora-dia--platform-size) var(--text-accent),
    0 0 15px var(--text-accent) inset;
  animation: shake-pulse 0.5s linear;
}

#Aurora-Dia--tips-wrapper {
  position: absolute;
  bottom: 80px;
  right: -120px;
  width: 200px;
  min-height: 60px;
  background: var(--aurora-dia--linear-gradient);
  color: var(--text-normal);
  padding: 0.2rem;
  border-radius: 8px;
  opacity: 0;
  animation: tips-breathe 3s linear infinite;
  transition: 0.3s linear opacity;
}
// #Aurora-Dia--tips-wrapper {
//   position: absolute;
//   bottom: 80px;
//   right: -800px;
//   width: 800px;
//   height: 600px;
//   background: var(--aurora-dia--linear-gradient);
//   color: var(--text-normal);
//   padding: 0.2rem;
//   border-radius: 8px;
//   opacity: 0;
//   animation: tips-breathe 3s linear infinite;
//   transition: 0.3s linear opacity;
// }

#Aurora-Dia--tips-wrapper.active {
  opacity: 0.86;
}

.Aurora-Dia--tips {
  position: relative;
  height: 100%;
  width: 100%;
  min-height: 60px;
  border-radius: 6px;
  padding: 0.2rem 0.5rem;
  font-size: 0.8rem;
  font-weight: 800;
  // background: var(--background-secondary);
  background: #fff;
  overflow: hidden;
  text-overflow: ellipsis;
}

.Aurora-Dia--tips > span {
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  padding: 0 0.1rem;
  color: #7aa2f7;
  background-color: #7aa2f7;
  background-image: var(--strong-gradient);
}

@keyframes deactivate {
  0% {
    border-color: var(--text-sub-accent);
  }
  20%,
  60% {
    border-color: var(--text-accent);
  }
  40%,
  80%,
  100% {
    border-color: var(--background-secondary);
  }
}

@keyframes tips-breathe {
  0%,
  100% {
    transform: translateY(0);
  }
  50% {
    transform: translateY(-5px);
  }
}

@keyframes bounce-then-breathe {
  0%,
  5%,
  10%,
  15% {
    transform: translateY(0);
  }
  2.5%,
  7.5%,
  12.5% {
    transform: translateY(-15px);
  }
  20%,
  40%,
  60%,
  80%,
  100% {
    height: var(--auora-dia--jump-1);
    transform: translateY(0);
  }
  30%,
  50%,
  70%,
  90% {
    height: var(--auora-dia--jump-2);
    transform: translateY(-5px);
  }
}

@keyframes breathe-and-jump {
  0%,
  40%,
  80%,
  100% {
    height: var(--auora-dia--jump-1);
    transform: translateY(0);
  }
  20%,
  60%,
  70%,
  90% {
    height: var(--auora-dia--jump-2);
    transform: translateY(-5px);
  }
  85% {
    height: var(--auora-dia--jump-3);
    transform: translateY(-20px);
  }
}

@keyframes blink {
  0%,
  100% {
    transform: scale(1, 0.05);
  }
  5%,
  95% {
    transform: scale(1, 1);
  }
}

@keyframes jump-pulse {
  0%,
  40%,
  80%,
  100% {
    box-shadow: 0 0 30px var(--aurora-dia--platform-light),
      0 0 45px var(--aurora-dia--platform-light) inset;
  }
  20%,
  60%,
  70%,
  90% {
    box-shadow: 0 0 70px var(--aurora-dia--platform-light),
      0 0 25px var(--aurora-dia--platform-light) inset;
  }
  85% {
    box-shadow: 0 0 100px var(--aurora-dia--platform-light),
      0 0 15px var(--aurora-dia--platform-light) inset;
  }
}

@keyframes shake-to-alert {
  0%,
  20%,
  40%,
  60%,
  80%,
  100% {
    transform: rotate(0) translateY(-8px);
  }
  10%,
  25%,
  35%,
  50%,
  65% {
    transform: rotate(7deg) translateY(-8px);
  }
  15%,
  30%,
  45%,
  55%,
  70% {
    transform: roate(-7deg) translateY(-8px);
  }
}

@keyframes shake-pulse {
  0%,
  20%,
  40%,
  60%,
  80%,
  100% {
    box-shadow: 0 0 var(--auora-dia--platform-size) #2cdcff,
      0 0 15px #2cdcff inset;
  }
  10%,
  25%,
  35%,
  50%,
  65% {
    box-shadow: 0 0 var(--auora-dia--platform-size-shake-1) #2cdcff,
      0 0 15px #2cdcff inset;
  }
  15%,
  30%,
  45%,
  55%,
  70% {
    box-shadow: 0 0 var(--auora-dia--platform-size-shake-2) #2cdcff,
      0 0 15px #2cdcff inset;
  }
}
</style>

<style lang="scss">
.Aurora-Dia--tips > span {
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  padding: 0 0.05rem;
  color: #7aa2f7;
  background-color: #7aa2f7;
  background-image: var(--strong-gradient);
}

.aichat-outbox {
  width: 70vw;
  height: 760px;
  position: absolute;
  left: 15vw;
  top: -100px;
  padding-bottom: 5px;
  background: #eff1f7;
  // background: linear-gradient(130deg, #24c7dc, #5433ff 41.07%, #ff0099 76.05%);
  border-radius: 10px;
  box-shadow: 0px 0px 3.5px rgb(0, 0, 0, 0.2);
  display: flex;
  .el-dialog__body {
    height: 100%;
    padding-bottom: 5px;
  }
  flex-direction: column;
  .aichat-body {
    width: 100%;
    height: 100%;
    // background: #fff;
    border-radius: 10px;
    display: flex;
    flex-direction: column;
    justify-content: space-between;

    padding: 0px 10px;
    position: relative;
    overscroll-behavior: contain;
  }
}
.chatani-enter-active,
.chatani-leave-active {
  transition: all 0.5s ease;
}

.chatani-enter-from,
.chatani-leave-to {
  opacity: 0;

  transform: translateX(-400px);
}
.message-panel {
  position: relative;
  overflow: auto;
  height: 600px;

  padding-bottom: 10px;
  .message-list {
    margin: 0px auto;
    width: 100%;
    .message-item {
      margin: 10px 0px;
      width: 100%;
      display: flex;
      .user-icon {
        min-width: 40px;
        max-width: 40px;
        height: 40px;
        line-height: 40px;
        border-radius: 20px;
        background: #535353;
        color: #fff;
        text-align: center;
        margin-left: 10px;
      }
      .message-content {
        width: calc(100% - 100px);
        margin-left: 10px;
        align-items: center;
        display: flex;
        justify-content: flex-end;
        margin-top: 40px;
      }
      .content-inner {
        background: #6d95f9;
        border-radius: 5px;
        padding: 10px;
        color: #fff;
        white-space: wrap;
      }
    }
    .ai-item {
      line-height: 23px;
      .message-content {
        display: block;
        background: #fff;
        margin-top: 40px;
        border-radius: 5px;
      }
      .user-icon {
        background: #64018f;
        margin-left: 0px;
      }
      :deep(.md-editor-previewOnly) {
        border-radius: 5px;
        background: #fff;
      }
      :deep(.md-editor-preview-wrapper) {
        padding: 10px;
      }
      .loading {
        text-align: center;
      }
    }
  }
}
.send-panel {
  width: 100%;
  // max-height: 90px;
  background: #fff;
  box-shadow: 0px 0px 3px rgb(0, 0, 0, 0.2);
  border-radius: 5px;
  padding: 10px;
  .el-form-item {
    margin-bottom: 0px;
    // height: 100%;
    .el-form-item__content {
      // height: 100%;
    }
  }

  .input-and-btn {
    display: flex;
    height: 100%;
    align-items: end;
  }
  .input {
    flex: 1;
    // height: 100%;
    user-select: none;
    .el-textarea {
      // height: 100%;
      min-height: 36px;

      .el-textarea__inner {
        // height: 100%;
        // max-height: 80px;
        box-shadow: none !important;
        min-height: 36px !important;
        align-content: center;
      }
      .el-textarea__inner:hover {
        box-shadow: none;
      }
      .el-textarea__inner:focus {
        box-shadow: none;
      }
    }
  }
  .send-btn {
    // text-align: right;
    // margin-bottom: 0px;
    // align-content: end;
    // // padding: 5px;
    // :deep(.el-form-item__content) {
    //   justify-content: flex-end;
    // }
    .el-button {
      background: #fff;
      border: none;
    }
  }
  :deep(.el-textarea__inner) {
    border: 0 !important;
    resize: none !important;
    box-shadow: none;
  }
}
.no-data {
  text-align: center;
  color: #5f5f5f;
}
.preview {
background: '#202020';
border-radius: 10px;
  p {
    margin: 5px;
  }
}
</style>
