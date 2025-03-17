import numpy as np
import matplotlib.pyplot as plt


# Определение систем уравнений
def system_1(x, y):
    return np.sin(x + y) - 1.2 * x, x ** 2 + y ** 2 - 1


def system_2(x, y):
    return x ** 2 - y - 1, x + y ** 2 - 1


def system_3(x, y):
    return np.exp(x) + y - 2, x ** 2 + y - 2


# Преобразование систем для метода простой итерации
def g_system_1(x, y):
    if abs(x) > 1:
        raise ValueError("x выходит за допустимую область определения (|x| <= 1).")
    return np.sin(x + y) / 1.2, np.sqrt(1 - x ** 2)


def g_system_2(x, y):
    if x > 1 or y < -1:
        raise ValueError("x или y выходят за допустимую область определения (x <= 1, y >= -1).")
    return np.sqrt(y + 1), np.sqrt(1 - x)


def g_system_3(x, y):
    if y >= 2:
        raise ValueError("y выходит за допустимую область определения (y < 2).")
    return np.log(2 - y), 2 - x ** 2


# Проверка достаточного условия сходимости
def check_convergence(g1, g2, x0, y0):
    # Вычисляем частные производные
    h = 1e-5
    try:
        dg1_dx = (g1(x0 + h, y0) - g1(x0 - h, y0)) / (2 * h)
        dg1_dy = (g1(x0, y0 + h) - g1(x0, y0 - h)) / (2 * h)
        dg2_dx = (g2(x0 + h, y0) - g2(x0 - h, y0)) / (2 * h)
        dg2_dy = (g2(x0, y0 + h) - g2(x0, y0 - h)) / (2 * h)
    except ValueError as e:
        print(f"Ошибка при проверке сходимости: {e}")
        return False

    # Проверяем условие сходимости
    if abs(dg1_dx) + abs(dg1_dy) < 1 and abs(dg2_dx) + abs(dg2_dy) < 1:
        return True
    else:
        return False


# Метод простой итерации
def simple_iteration(g1, g2, x0, y0, eps=1e-6, max_iter=1000):
    x, y = x0, y0
    iterations = 0
    errors = []

    for _ in range(max_iter):
        try:
            x_new = g1(x, y)
            y_new = g2(x, y)
        except ValueError as e:
            print(f"Ошибка: {e}")
            return None, None, None, None

        error = np.sqrt((x_new - x) ** 2 + (y_new - y) ** 2)
        errors.append(error)

        if error < eps:
            break

        x, y = x_new, y_new
        iterations += 1

    return x, y, iterations, errors


# Выбор системы уравнений
def choose_system():
    print("Выберите систему уравнений:")
    print("1. sin(x + y) - 1.2x = 0, x^2 + y^2 - 1 = 0")
    print("2. x^2 - y - 1 = 0, x + y^2 - 1 = 0")
    print("3. exp(x) + y - 2 = 0, x^2 + y - 2 = 0")
    choice = int(input("Введите номер системы (1, 2 или 3): "))

    if choice == 1:
        return system_1, g_system_1
    elif choice == 2:
        return system_2, g_system_2
    elif choice == 3:
        return system_3, g_system_3
    else:
        print("Неверный выбор. По умолчанию выбрана система 1.")
        return system_1, g_system_1


# Основная программа
if __name__ == "__main__":
    # Выбор системы
    system, g_system = choose_system()
    g1 = lambda x, y: g_system(x, y)[0]
    g2 = lambda x, y: g_system(x, y)[1]

    # Ввод начальных приближений
    while True:
        x0 = float(input("Введите начальное приближение x0: "))
        y0 = float(input("Введите начальное приближение y0: "))

        # Проверка допустимости начальных приближений
        try:
            g1(x0, y0)
            g2(x0, y0)
            break
        except ValueError as e:
            print(f"Ошибка: {e}")
            print("Пожалуйста, введите другие начальные приближения.")

    # Инициализация переменных x и y
    x, y = None, None

    # Проверка условия сходимости
    if not check_convergence(g1, g2, x0, y0):
        print("Достаточное условие сходимости не выполнено!")
    else:
        # Решение системы
        x, y, iterations, errors = simple_iteration(g1, g2, x0, y0)

        if x is not None and y is not None:
            # Вывод результатов
            print(f"Решение: x = {x:.6f}, y = {y:.6f}")
            print(f"Количество итераций: {iterations}")

            # Красивый вывод вектора погрешностей
            print("Вектор погрешностей: [", end="")
            for i, error in enumerate(errors):
                if i > 0:
                    print(", ", end="")
                print(f"{error:.6f}", end="")
            print("]")

            # Проверка правильности решения
            f1, f2 = system(x, y)
            print(f"Проверка: f1(x, y) = {f1:.6e}, f2(x, y) = {f2:.6e}")
        else:
            print("Решение не найдено.")

    # Построение графиков
    x_vals = np.linspace(-2, 2, 400)
    y_vals = np.linspace(-2, 2, 400)
    X, Y = np.meshgrid(x_vals, y_vals)
    Z1, Z2 = system(X, Y)

    plt.figure(figsize=(8, 6))
    contour1 = plt.contour(X, Y, Z1, levels=[0], colors='r')
    contour2 = plt.contour(X, Y, Z2, levels=[0], colors='b')

    # Добавление меток для легенды
    proxy = [plt.Rectangle((0, 0), 1, 1, fc='red'), plt.Rectangle((0, 0), 1, 1, fc='blue')]
    plt.legend(proxy, ['f1(x, y) = 0', 'f2(x, y) = 0'])

    # Отображение координатных осей
    plt.axhline(0, color='black', linewidth=0.5)
    plt.axvline(0, color='black', linewidth=0.5)

    # Отображение решения, если оно найдено
    if x is not None and y is not None:
        plt.plot(x, y, 'go', label='Решение')
        plt.legend()

    plt.xlabel('x')
    plt.ylabel('y')
    plt.title('График системы уравнений')
    plt.grid(True)
    plt.show()