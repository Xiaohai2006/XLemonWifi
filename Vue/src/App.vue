<template>
  <!-- <nav>
    <router-link to="/">Home</router-link>
  </nav> -->
  <router-view/>
</template>

<style>
body{

    margin: 0;

    display: flex;
    justify-content: center;
    align-items: center;

    background: radial-gradient(circle at 50% 50%, #1a1a1a, #000000);
    box-shadow: inset 0 0 20px rgba(255, 255, 255, 0.1), 0 0 15px rgba(0, 255, 255, 0.3);
    color: white;
    animation: backgroundAnimation 30s infinite alternate ease-in-out, glowAnimation 20s infinite ease-in-out;
}


@keyframes backgroundAnimation {
    0% {
        background: radial-gradient(circle at 50% 50%, #1a1a1a, #000000);
    }
    25% {
        background: radial-gradient(circle at 52% 48%, #222222, #0a0a0a);
    }
    50% {
        background: radial-gradient(circle at 55% 45%, #2a2a2a, #0a0a0a);
    }
    75% {
        background: radial-gradient(circle at 53% 47%, #222222, #0a0a0a);
    }
    100% {
        background: radial-gradient(circle at 50% 50%, #1a1a1a, #000000);
    }
}

@keyframes glowAnimation {
    0%, 100% {
        box-shadow: inset 0 0 20px rgba(255, 255, 255, 0.1), 0 0 15px rgba(0, 255, 255, 0.3);
    }
    50% {
        box-shadow: inset 0 0 30px rgba(255, 255, 255, 0.15), 0 0 25px rgba(0, 255, 255, 0.4);
    }
}
</style>
