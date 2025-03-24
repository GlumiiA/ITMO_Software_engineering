const resultTable = document.getElementsByClassName("result")[0];
const graph = document.getElementById("graphChange");


document.addEventListener('DOMContentLoaded', function() {
    // Код для кнопок
    document.querySelectorAll('.buttonY').forEach(button => {
        button.addEventListener('click', function() {
            document.querySelectorAll('.buttonY').forEach(btn => btn.classList.remove('active'));
            this.classList.add('active');
        });
    });
});

$('input[type="checkbox"]').click(function() {
    var checkboxGroup = 'input[name="' + $(this).attr('name') + '"]';
    $(checkboxGroup).not(this).prop('checked', false);
});

document.getElementById('submit').addEventListener('click', async function() {
    const checkboxesR = document.querySelectorAll('input[name="rValue"]:checked');
    let rValue;

    if (checkboxesR.length > 0 && checkboxesR.length < 2) {
        rValue = checkboxesR[0].value; // Получаем значение первого отмеченного чекбокса
    } else {
        rValue = undefined; // Если ничего не отмечено
    }

    const xValue = document.querySelector('input[name="xValue"]').value;
    let yValue;
    const yButtons = document.querySelectorAll('.buttonY');

    yButtons.forEach(button => {
        if (button.classList.contains('active')) {
            yValue = button.value;
        }
    });

    // Проверка на корректность введенных значений
    if (isNaN(yValue) || isNaN(rValue) || isNaN(xValue)) {
        alert("Заполните все поля");
    } else {
        if ((xValue <= -3) || (xValue >= 3)) {
            alert("Значение X должно быть в диапозоне [-3;3]");
        }else{
            await sendForm(xValue, yValue, rValue);
        }
    }
});

graph.addEventListener('click', async ({clientX, clientY}) => {
    const checkboxesR = document.querySelectorAll('input[name="rValue"]:checked');
    let r;
    if (checkboxesR.length > 0 && checkboxesR.length < 2) {
        r = checkboxesR[0].value; // Получаем значение первого отмеченного чекбокса
    } else {
        r = undefined; // Если ничего не отмечено
    }

    if (r === undefined) {
        alert("Значение R не выбрано");
    } else {
        const rect = graph.getBoundingClientRect();
        const x = clientX - rect.left; // Вычисляем x
        const y = clientY - rect.top; // Вычисляем y
        let newY = 250 - y;
        let newX = x - 250;
        if (x >= 250){
            let newX = 250 - x;
        }
        if (y <= 250){
            let newY = 250 - y;
        }
        let resX = newX/200*r
        let resY = newY/200*r
        await sendForm(resX, resY, r);
        }
    });

async function sendForm(x_result, y_result, r_result) {
    if (x_result === "" || y_result === "" || r_result === undefined) {
        alert("Все поля должны быть заполнены");
    } else {
        let params = new URLSearchParams();
        params.append("x", x_result.toFixed(3));
        params.append("y", y_result.toFixed(3));
        params.append("r", r_result);
        let response = await fetch(`controller-servlet?${params.toString()}`, {
            method: "POST",
        });
        if (response.status === 400) {
            alert("Значение X должно быть в диапозоне [-3;3]");
        } else if (response.status === 200) {
            window.location.href = response.url;
        }
    }
}

function drawPoints(){
    let rows = resultTable.rows;
    console.log(rows);

    let r_result = parseFloat(rows[rows.length - 1].cells[2].innerHTML);
    for (let i = 1; i < rows.length; i++) {
         let x_coordinate = parseFloat(rows[i].cells[0].innerText);
         let y_coordinate = parseFloat(rows[i].cells[1].innerText);
         let hit = rows[i].cells[3].innerText;
         const rect = graph.getBoundingClientRect();

         let restoredNewX = (x_coordinate * 200) / r_result;
         let restoredNewY = (y_coordinate * 200) / r_result;

         let x = restoredNewX + 250;
         let y = 250 - restoredNewY;
         console.log(x);
         console.log(y);
         let point = document.createElementNS("http://www.w3.org/2000/svg", "circle");
         point.setAttribute("cx", `${x}`);
         point.setAttribute("cy", `${y}`);
         point.setAttribute("r", "2");
         if (hit == 'true'){
              point.setAttribute("stroke", "blue");
         } else {
               point.setAttribute("stroke", "red");
         }
         point.setAttribute("strokeWidth", "2px");
         graph.appendChild(point);
    }
}

window.onload = drawPoints();

