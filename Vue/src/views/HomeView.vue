<template>
    <div class="home">
      <div class="search-box">
        <SearchBox 
          msg="手机号"
          v-model="phoneNumber" 
          @enter="handlePost"
        />
        <button class="get-button" @click="handlePost" v-if="isMobile">获取</button>
        <div class="hitokoto">{{ hitokoto }}</div>
      </div>
      
  
      <div class="footer">
          开源地址：
          <a href="https://github.com/Xiaohai2006/XLemonWifi" target="_blank">Xiaohai2006/XLemonWifi</a>
      </div>
  
    </div>
  </template>
  
  <script>
  import SearchBox from '@/components/SearchBox.vue';
  import { ElMessage, ElMessageBox, ElLoading } from 'element-plus';
  
  export default {
    name: 'HomeView',
    components: {
      SearchBox,
    },
    data() {
      return {
        phoneNumber: '',
        isMobile: false,
        hitokoto: '正在加载一言...',
      };
    },
    watch: {
      phoneNumber(newVal) {
        console.log('HomeView watch: phoneNumber →', newVal);
      },
    },
    mounted() {
      this.checkIfMobile();
      this.fetchHitokoto();
      this.startHitokotoCycle();
      console.log('HomeView mounted, phoneNumber:', this.phoneNumber);
    },
    methods: {
      checkIfMobile() {
        this.isMobile = /Mobi|Android|iPhone/i.test(navigator.userAgent);
      },
  
      handlePost() {
        console.log('handlePost called, phoneNumber:', this.phoneNumber);
        if (!this.phoneNumber) {
          ElMessage.error('手机号不能为空');
          return;
        }
        if (this.phoneNumber.length !== 11 || !this.phoneNumber.startsWith('1')) {
          ElMessage.error('手机号格式不正确');
          return;
        }
  
        ElMessageBox.confirm(
          '您是否要为这个手机号发送一次验证码? 如果 这个手机号记录的验证码已经过期, 您需要先给这个号码重新获取一次验证码 不然可能获取不到验证码！',
          '验证码',
          {
            confirmButtonText: '发送',
            cancelButtonText: '我不需要',
            type: 'warning',
          }
        )
          .then(() => {
            ElMessage({
              type: 'success',
              message: '好的！我们将代替您发送获取一次验证码',
            });
            this.isMobile = true;
            this.postapi();
          })
          .catch(() => {
            ElMessage({
              type: 'success',
              message: '我们尊重您的选择我们不会 发送验证码',
            });
            this.isMobile = false;
            this.postapi();
          });
      },
  
      postapi() {
        const loading = ElLoading.service({
          lock: true,
          text: '正在处理您的请求，请稍候...',
          background: 'rgba(0, 0, 0, 0.5)',
        });
  
        const xhr = new XMLHttpRequest();
        xhr.open('POST', '/api/lemonwifi', true);
        xhr.setRequestHeader('Content-Type', 'application/json');
  
        xhr.onreadystatechange = function () {
          if (xhr.readyState === 4) {
            if (xhr.status === 200) {
              try {
                const response = JSON.parse(xhr.responseText);
                console.log('响应数据:', response);
                if (response.code === 200) {
                  console.log('响应成功:', response);
                  ElMessage.success('获取成功');
                  loading.close();
  
                  ElMessageBox.alert('获取成功验证码是: ' + response.data, '获取成功', {
                    confirmButtonText: 'OK',
                  });
                } else if (response.code === 0) {
                  loading.close();
  
                  ElMessageBox.alert(
                    '验证码获取失败 验证码可能失效请先获取验证码一次验证码 再来我这里进行爆破QWQ！！！ 你也可以再次尝试选择 让我们代替您发送',
                    '获取失败',
                    {
                      confirmButtonText: 'OK',
                    }
                  );
                } else {
                  loading.close();
                  ElMessage.error('获取失败，请稍后再试');
                }
              } catch (e) {
                loading.close();
                console.error('解析失败:', e);
                ElMessage.error('服务器返回了错误格式');
              }
            } else if (xhr.status === 0) {
              loading.close();
  
              ElMessageBox.alert(
                '验证码获取失败 验证码可能失效请先获取验证码一次验证码 再来我这里进行爆破QWQ！！！ 你也可以再次尝试选择 让我们代替您发送',
                '获取失败',
                {
                  confirmButtonText: 'OK',
                }
              );
            } else {
              loading.close();
              console.error('请求失败，状态码:', xhr.status);
              ElMessage.error('请求失败，请检查网络连接');
            }
          }
        };
  
        const data = {
          phone: this.phoneNumber,
          yanz: this.isMobile,
        };
        xhr.send(JSON.stringify(data));
      },
  
      fetchHitokoto() {
        fetch('/proxy/int/v1/yiyan')
          .then((response) => response.text())
          .then((text) => {
            this.hitokoto = `${text}`;
          })
          .catch(() => {
            this.hitokoto = '可爱的喵喵~';
          });
      },
  
      startHitokotoCycle() {
        setInterval(() => {
          this.fetchHitokoto();
        }, 3000);
      },
    },
  };
  </script>
  
  <style scoped>
  .home {
    display: flex;
    justify-content: center;
    align-items: center;
    height: 100vh;
  }
  
  .search-box {
    display: flex;
    flex-direction: column;
    align-items: center;
  }
  
  .get-button {
    margin-left: 10px;
    height: 40px;
    padding: 0 15px;
    border: none;
    border-radius: 20px;
    background-color: #007bff;
    color: white;
    cursor: pointer;
    transition: background-color 0.3s ease;
  }
  
  .get-button:hover {
    background-color: #0056b3;
  }
  
  .footer {
    position: absolute;
    bottom: 20px;
    text-align: center;
    font-size: 14px;
    color: #666;
    width: 100%;
  }
  
  .footer a {
    color: #007bff;
    text-decoration: none;
  }
  
  .footer a:hover {
    text-decoration: underline;
  }
  
  .hitokoto {
    margin-top: 15px;
    width: auto;
    height: 20px;
    max-width: 80%;
    font-size: 14px;
    color: #666;
    font-style: italic;
    text-align: center;
    word-wrap: break-word;
    position: relative;
    z-index: 1;
  }
  </style>
