let currentR = 4;
let points = [];
let x_begin;
let y_begin;

$(document).ready(() => {
    getPointsData();
    drawPoints();
});

window.sendCoords = function sendCoords(x, y) {
    $('#graph-form\\:xGraphValue').val(x);
    $('#graph-form\\:yGraphValue').val(y);
    $('#graph-form\\:handleClick').click();
}


function getPointsData() {
    // Получите таблицу по ее идентификатору
    var table = document.getElementById("results-table");

    const rows = document.querySelectorAll('#results-table tbody tr');
    const count = rows.length;

    // Проходим по всем строкам таблицы, начиная со второй, чтобы пропустить заголовок
    for (var i = 1; i < count; i++) {
        const cells = rows[i].getElementsByTagName('td');
        if (cells.length > 0) {
            const x = parseFloat(cells[0].innerText);
            const y = parseFloat(cells[1].innerText);
            const isHit = cells[3].innerText.trim() === 'true';
            console.log(x, y, isHit);
            points.push({x: x, y: y, isHit: isHit});
        }
    }

    console.log(points);
}

function setR(newR) {
    currentR = newR;
    const svg = document.querySelector('svg');
    const existingCircles = svg.querySelectorAll('circle');
    existingCircles.forEach(circle => {
        circle.remove();
    });
    points.forEach(function(point) {
        point.isHit = result_hit(point.x, point.y, currentR);
    });
    drawPoints();
}

function setPoint(event) {
    const graph = event.currentTarget;
    const rect = graph.getBoundingClientRect();
    // Вычисляем координаты относительно области графика
    x_begin = rect.left; // Вычисляем x
    y_begin = rect.top; // Вычисляем y
    const x = event.clientX - rect.left; // Вычисляем x
    const y = event.clientY - rect.top; // Вычисляем y
    let newY = 250 - y;
    let newX = x - 250;

    let resX = (newX)/200 * currentR;
    let resY = (newY)/200 * currentR;

    sendCoords(resX.toFixed(1), resY.toFixed(1));
    addPoint(resX.toFixed(1),  resY.toFixed(1), result_hit(resX,resY,currentR));
}

function addPoint(x, y, isHit) {
    points = [];
    points.push({ x, y, isHit });
    getPointsData();
    drawPoints();
}

function addPointForm(){
    alert("add")
    getPointsData();
    drawPoints();
    //TODO не работает:(
}

function drawPoints() {
    const svg = document.querySelector('svg');

    points.forEach(point => {
        const { x, y, isHit} = point;

        let newX = (x / currentR) * 200 + 250; // Обратное преобразование по X
        let newY = 250 - (y / currentR) * 200; // Обратное преобразование по Y
        const color = isHit ? 'green' : 'red';

        // Создаем круг для отображения точки
        const circle = document.createElementNS("http://www.w3.org/2000/svg", "circle");
        circle.setAttribute("cx", newX);
        circle.setAttribute("cy", newY);
        circle.setAttribute("r", 5);
        circle.setAttribute("fill", color);

        svg.appendChild(circle);
    });
}

function result_hit(x,y,r){
    if ((x<=0 && y<=0) && (y<-r-x)){
        return false;
    }
    if ((x<=0 && y>=0) && (2*y > r || x<-r)){
        return false;
    }
    if ((x>=0 && y<=0) && ((x*x + y*y) > r*r)){
        return false;
    }
    if (x>0 && y>0){
        return false;
    }
    return true;
}

function clearPoints(){
    points = [];
    drawPoints();
    const svg = document.querySelector('svg');
    const existingCircles = svg.querySelectorAll('circle');
    existingCircles.forEach(circle => {
        circle.remove();
    });
}
