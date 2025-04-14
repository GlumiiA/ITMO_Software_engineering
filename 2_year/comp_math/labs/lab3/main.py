import math
from scipy.integrate import quad


def left_rectangles_method(func, a, b, n):
    """Вычисление интеграла методом левых прямоугольников"""
    h = (b - a) / n  # Шаг интегрирования
    xs = [a + i * h for i in range(n + 1)]  # Сетка точек [x0, x1, ..., xn]
    # Сумма значений функции в левых концах отрезков
    return h * sum([func(xs[i]) for i in range(n)])


def right_rectangles_method(func, a, b, n):
    """Вычисление интеграла методом правых прямоугольников"""
    h = (b - a) / n
    xs = [a + i * h for i in range(n + 1)]
    # Сумма значений функции в правых концах отрезков
    return h * sum([func(xs[i]) for i in range(1, n + 1)])


def middle_rectangles_method(func, a, b, n):
    """Вычисление интеграла методом средних прямоугольников"""
    h = (b - a) / n
    xs = [a + i * h for i in range(n + 1)]
    # Сумма значений функции в средних точках отрезков
    return h * sum([func((xs[i - 1] + xs[i]) / 2) for i in range(1, n + 1)])


def trapezoid_method(func, a, b, n):
    """Вычисление интеграла методом трапеций"""
    h = (b - a) / n
    xs = [a + i * h for i in range(n + 1)]  # Сетка точек
    ys = [func(x) for x in xs]  # Значения функции в точках сетки
    # Формула трапеций: полусумма крайних + сумма средних
    return h * ((ys[0] + ys[n]) / 2 + sum([ys[i] for i in range(1, n)]))


def simpson_method(func, a, b, n):
    """Вычисление интеграла методом Симпсона (парабол)"""
    if n % 2 != 0:
        n += 1  # Для метода Симпсона n должно быть четным
    h = (b - a) / n
    xs = [a + i * h for i in range(n + 1)]  # Сетка точек
    ys = [func(x) for x in xs]  # Значения функции
    # Формула Симпсона: особые веса для четных и нечетных точек
    return h / 3 * (ys[0] + 4 * sum([ys[i] for i in range(1, n, 2)]) +
                    2 * sum([ys[i] for i in range(2, n - 1, 2)]) + ys[n])


def compute(func, a, b, eps, method):
    """
    Вычисление интеграла с заданной точностью по правилу Рунге
    с учетом порядка точности метода.
    """
    # Сначала вычисляем точное значение интеграла с помощью scipy.integrate.quad
    exact_value, _ = quad(func, a, b)

    # Определяем порядок точности метода (p)
    method_name = method.__name__
    if 'rectangle' in method_name:
        p = 2
    elif 'trapezoid' in method_name:
        p = 2
    elif 'simpson' in method_name:
        p = 4
    else:
        p = 1

    runge_coeff = (2 ** p) - 1  # Коэффициент для правила Рунге: 2^p - 1

    n = 4  # Начальное число разбиений
    i0 = method(func, a, b, n)  # Первое приближение
    i1 = method(func, a, b, n * 2)  # Уточненное приближение

    # Таблица для хранения результатов на каждом шаге
    results_table = []
    results_table.append((n, i0, None, abs(i0 - exact_value)))
    results_table.append((n * 2, i1, abs(i1 - i0) / runge_coeff, abs(i1 - exact_value)))

    # Оцениваем погрешность по правилу Рунге
    while abs(i1 - i0) / runge_coeff > eps:
        n *= 2
        i0 = i1
        i1 = method(func, a, b, n * 2)
        # Для метода Симпсона n должно быть четным
        if p == 4 and n % 2 != 0:
            n += 1
        results_table.append((n * 2, i1, abs(i1 - i0) / runge_coeff, abs(i1 - exact_value)))

    return i1, n * 2, results_table, exact_value


def input_function():
    """Интерфейс выбора подынтегральной функции"""
    while True:
        print("\nДоступные функции:")
        print("1. x^5 + x^2")
        print("2. sin(x) + cos(x)")
        print("3. cos(x) - 5")
        print("4. exp(x)^2 + 3")
        print("0. Выход")

        try:
            choice = int(input("Выберите функцию (0-5): "))
            if choice == 0:
                return None  # Выход из программы
            elif choice == 1:
                return lambda x: x ** 5 + x ** 2
            elif choice == 2:
                return lambda x: math.sin(x) + math.cos(x)
            elif choice == 3:
                return lambda x: math.cos(x) - 5
            elif choice == 4:
                return lambda x: math.exp(x) ** 2 + 3
            else:
                print("Ошибка: введите число от 0 до 5")
        except ValueError:
            print("Ошибка: введите целое число")


def main():
    methods = {
        1: ("Метод левых прямоугольников", left_rectangles_method),
        2: ("Метод правых прямоугольников", right_rectangles_method),
        3: ("Метод средних прямоугольников", middle_rectangles_method),
        4: ("Метод трапеций", trapezoid_method),
        5: ("Метод Симпсона", simpson_method)
    }

    print("Вычисление определенного интеграла")

    while True:
        # Выбор функции
        func = input_function()
        if func is None:
            print("Программа завершена.")
            break

        # Выбор метода интегрирования
        while True:
            print("\nДоступные методы интегрирования:")
            for key, (name, _) in methods.items():
                print(f"{key}. {name}")
            print("0. Вернуться к выбору функции")

            try:
                method_choice = int(input("Выберите метод интегрирования (0-5): "))

                if method_choice == 0:
                    break
                elif method_choice in methods:
                    method_name, method_func = methods[method_choice]

                    try:
                        a = float(input("Введите нижний предел интегрирования a: "))
                        b = float(input("Введите верхний предел интегрирования b: "))

                        if a >= b:
                            print("Ошибка: верхний предел должен быть больше нижнего")
                            continue

                        eps = float(input("Введите точность вычисления eps: "))

                        if eps <= 0:
                            print("Ошибка: точность должна быть положительным числом")
                            continue

                        result, n, results_table, exact_value = compute(func, a, b, eps, method_func)

                        print("\nРезультаты вычисления:")
                        print(f"Выбранный метод: {method_name}")
                        print(f"Интервал интегрирования: [{a:.2f}, {b:.2f}]")
                        print(f"Точное значение (scipy): {exact_value:.8f}")
                        print(f"Вычисленное значение: {result:.8f}")
                        print(f"Число разбиений интервала: {n}")
                        print(f"Достигнутая точность: {eps}")
                        print(f"Отклонение от точного значения: {abs(result - exact_value):.2e}")

                        # Выводим таблицу с результатами на каждом шаге
                        print("\nТаблица результатов:")
                        print(f"{'n':<8}{'Приближение':<20}{'Оценка погрешности':<25}{'Отклонение от точного'}")
                        print("-" * 65)
                        for row in results_table:
                            n, value, runge_err, exact_err = row
                            runge_str = f"{runge_err:.2e}" if runge_err is not None else "N/A"
                            print(f"{n:<8}{value:<20.6f}{runge_str:<25}{exact_err:.2e}")

                        cont = input("\nНажмите Enter для продолжения или '0' для выхода: ")
                        if cont == '0':
                            print("Программа завершена.")
                            return
                    except ValueError:
                        print("Ошибка: введите числовые значения для a, b и eps")
                else:
                    print("Ошибка: введите число от 0 до 5")
            except ValueError:
                print("Ошибка: введите целое число")


if __name__ == "__main__":
    main()