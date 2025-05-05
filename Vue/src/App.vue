<template>
    <div class="highlight-ball"></div>
    <router-view />
  </template>
  
  <style>
  html, body {
    margin: 0;
    padding: 0;
    height: 100%;
    font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
    overflow: hidden;
  }
  
  body {
    display: flex;
    justify-content: center;
    align-items: center;
    position: relative;
    color: #00ffff;
    background-color: black;
    animation: backgroundTransition 10s ease-in-out infinite alternate;
  }
  
  /* 高光发光效果 */
  @keyframes glowEffect {
    0%, 100% {
      box-shadow: inset 0 0 15px rgba(0, 255, 255, 0.1), 0 0 25px rgba(0, 255, 255, 0.2), 0 0 40px rgba(0, 255, 255, 0.3);
    }
    50% {
      box-shadow: inset 0 0 25px rgba(0, 255, 255, 0.3), 0 0 35px rgba(0, 255, 255, 0.4), 0 0 60px rgba(0, 255, 255, 0.5);
    }
  }
  
  /* 高亮球的浮动效果 */
  @keyframes highlightBallFloating {
    0% {
      transform: translate(-50%, -50%) translateY(-20px) translateX(20px); /* 初始偏移 */
    }
    25% {
      transform: translate(-50%, -50%) translateY(30px) translateX(-30px); /* 向下向左偏移 */
    }
    50% {
      transform: translate(-50%, -50%) translateY(-40px) translateX(50px); /* 向上向右偏移 */
    }
    75% {
      transform: translate(-50%, -50%) translateY(20px) translateX(-40px); /* 回到中间位置偏移 */
    }
    100% {
      transform: translate(-50%, -50%) translateY(-20px) translateX(20px); /* 结束偏移 */
    }
  }
  
  /* 高亮球的样式 */
  .highlight-ball {
    position: absolute;
    width: 200px;
    height: 200px;
    border-radius: 50%;
    background-color: rgba(0, 255, 255, 0.2);
    animation: highlightBallFloating 10s ease-in-out infinite, glowEffect 6s ease-in-out infinite;
  
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%); 
    pointer-events: none;
  }
  
 
  @keyframes backgroundTransition {
    0% {
      background-color: #000000;
    }
    50% {
      background-color: #111111;
    }
    100% {
      background-color: #000000;
    }
  }
  </style>
  