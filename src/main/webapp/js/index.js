function clock() {
    const hoursArrow = document.querySelector('.hours');
    const minutesArrow = document.querySelector('.minutes');
    const secondsArrow = document.querySelector('.seconds');
    const deg = 6; // 1 секунда = 6 градусов

    function updateClock() {
        const day = new Date();
        const hours = day.getHours() * 30; // 1 час - 30 градусов
        const minutes = day.getMinutes() * deg;
        const seconds = day.getSeconds() * deg;

        hoursArrow.style.transform = `rotateZ(${hours}deg)`;
        minutesArrow.style.transform = `rotateZ(${minutes}deg)`;
        secondsArrow.style.transform = `rotateZ(${seconds}deg)`;

        const currentDateTime = day.toLocaleString('ru-RU', {
            year: 'numeric',
            month: 'long',
            day: 'numeric',
            hour: '2-digit',
            minute: '2-digit',
            second: '2-digit'
        });

        document.getElementById('current-datetime').innerText = currentDateTime;
    }
    updateClock();
    // Интервал обновления каждые 5 секунд
    setInterval(updateClock, 5000);
}

clock();