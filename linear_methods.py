import matplotlib.pyplot as plt
import numpy as np
import sympy as sp


# Определение функций
def f1(x):
    return x ** 2 - 5.65 * np.log(x)


def f2(x):
    return np.sin(x) ** 3 - np.cos(x) ** 3


def f3(x):
    return 3 * np.exp(x) - 2 * np.sin(x)


def f4(x):
    return 3 * x ** 3 + 1.7 * x ** 2 - 15.42 * x + 6.89


def f5(x):
    return np.log(x + 1)


# Определение производных функций
def df1(x):
    return 2 * x - 5.65 / x


def df2(x):
    return 3 * np.sin(x) ** 2 * np.cos(x) + 3 * np.cos(x) ** 2 * np.sin(x)


def df3(x):
    return 3 * np.exp(x) - 2 * np.cos(x)


def df4(x):
    return 9 * x ** 2 + 3.4 * x - 15.42


def df5(x):
    return 1 / (x + 1)


# Функции для метода простой итерации
def phi_with_lambda(f, df, a, b):
    """
    Строит 𝝋(x) = x + λf(x), где λ выбирается из условия сходимости.
    """
    # Находим max |f'(x)| на [a, b]
    x_values = np.linspace(a, b, 1000)
    max_df = np.max(np.abs(df(x_values)))

    # Выбираем λ = ±1 / max |f'(x)| в зависимости от знака f'
    lambda_ = -1 / max_df if np.mean(df(x_values)) > 0 else 1 / max_df

    def phi(x):
        return x + lambda_ * f(x)

    def dphi(x):
        return 1 + lambda_ * df(x)

    return phi, dphi


# def phi1(x):
#     return np.sqrt(5.65 * np.log(x))
#
#
# def phi2(x):
#     tan_x = np.tan(x)
#     sign = np.sign(tan_x)
#     return np.arctan(sign * (abs(tan_x) ** (1 / 3)))
#
#
# def phi3(x):
#     return np.log((2 * np.sin(x)) / 3)
#
#
# def phi4(x):
#     epsilon = 1e-10
#     # проверить сходимость функции
#     return (-1.7 * x ** 2 + 15.42 * x - 6.89) / (3 * x ** 2 + epsilon)
#
#
# def phi5(x):
#     return np.exp(x) - 1
#
#
# # Производные функций для метода простой итерации
# def dphi1(x):
#     return (5.65 / (2 * x * np.sqrt(5.65 * np.log(x))))
#
#
# def dphi2(x):
#     return (1 / (3 * (np.sin(x) ** 2 + np.cos(x) ** 2)))
#
#
# def dphi3(x):
#     return (2 * np.cos(x)) / (3 * (2 * np.sin(x)))
#
#
# def dphi4(x):
#     return (-3.4 * x + 15.42) / (3 * x ** 2) - 2 * (-1.7 * x ** 2 + 15.42 * x - 6.89) / (3 * x ** 3)
#
#
# def dphi5(x):
#     return np.exp(x)


# Ввод данных из файла
def input_from_file():
    try:
        with open("input_file.txt", 'r') as file:
            lines = file.readlines()
            if len(lines) < 4:
                raise ValueError("Файл должен содержать как минимум 4 строки: номер уравнения, a, b, tol.")

            choice = int(lines[0].strip())
            a = float(lines[1].strip())
            b = float(lines[2].strip())
            tol = float(lines[3].strip())

            if choice < 1 or choice > 5:
                raise ValueError("Неверный номер уравнения. Пожалуйста, выберите от 1 до 5.")

            functions = [f1, f2, f3, f4, f5]
            derivatives = [df1, df2, df3, df4, df5]
            # phi_functions = [phi1, phi2, phi3, phi4, phi5]
            # dphi_functions = [dphi1, dphi2, dphi3, dphi4, dphi5]

            f = functions[choice - 1]
            df = derivatives[choice - 1]
            # phi = phi_functions[choice - 1]
            # dphi = dphi_functions[choice - 1]
            phi, dphi = phi_with_lambda(f, df, a, b)

            # Проверка интервала для функций с логарифмами
            if choice == 1 or choice == 5:
                if a <= 0 or b <= 0:
                    raise ValueError(
                        "Для выбранного уравнения интервал должен содержать только положительные числа (x > 0).")

            return f, df, phi, dphi, a, b, tol
    except Exception as e:
        print(f"Ошибка при чтении файла: {e}")
        return None


# Ввод данных с клавиатуры
def input_from_keyboard():
    print("Выберите уравнение:")
    print("1. x^2 - 5.65*ln(x)")
    print("2. sin(x)^3 - cos(x)^3")
    print("3. 3 * exp(x) - 2 * sin(x)")
    print("4. 3x^3 + 1.7x^2 - 15.42x + 6.89")
    print("5. ln(x + 1)")
    choice = int(input("Введите номер уравнения: "))

    if choice < 1 or choice > 5:
        raise ValueError("Неверный номер уравнения. Пожалуйста, выберите от 1 до 5.")

    functions = [f1, f2, f3, f4, f5]
    derivatives = [df1, df2, df3, df4, df5]

    f = functions[choice - 1]
    df = derivatives[choice - 1]

    while True:
        a = float(input("Введите левую границу интервала: "))
        b = float(input("Введите правую границу интервала: "))
        tol = float(input("Введите погрешность: "))

        # Проверка интервала для функций с логарифмами
        if choice == 1 or choice == 5:
            if a <= 0 or b <= 0:
                print("Для выбранного уравнения интервал должен содержать только положительные числа (x > 0).")
                continue

        # Проверка интервала на наличие корней
        if f(a) * f(b) >= 0:
            print("На интервале либо нет корней, либо их несколько. Пожалуйста, выберите другой интервал.")
            plot_function(f, a, b)
            continue
        break
    phi, dphi = phi_with_lambda(f, df, a, b)
    return f, df, phi, dphi, a, b, tol


# Ввод данных
def input_data():
    print("Выберите способ ввода данных:")
    print("1. С клавиатуры")
    print("2. Из файла")
    input_method = int(input("Введите номер способа: "))

    if input_method == 1:
        return input_from_keyboard()
    elif input_method == 2:
        return input_from_file()
    else:
        raise ValueError("Неверный номер способа. Пожалуйста, выберите 1 или 2.")


def has_multiple_roots(f, a, b, num_points=1000):
    x = np.linspace(a, b, num_points)
    y = f(x)
    sign_changes = np.where(np.diff(np.sign(y)))[0]
    return len(sign_changes) > 1

# Верификация данных
def verify_data(f, interval, tol):
    try:
        a, b = interval
        if f(a) * f(b) >= 0:
            print("На интервале либо нет корней, либо их несколько. Пожалуйста, выберите другой интервал.")
            plot_function(f, a, b)
            return False

        # Проверка на наличие нескольких корней
        while has_multiple_roots(f, a, b):
            print("Внимание: на интервале может быть несколько корней! Пожалуйста, повторите ввод.")
            plot_function(f, a, b)
            try:
                a = float(input("Введите новую левую границу интервала: "))
                b = float(input("Введите новую правую границу интервала: "))
                interval[0], interval[1] = a, b
            except ValueError:
                print("Ошибка: введите числовые значения для границ интервала.")
                continue
        return True
    except Exception as e:
        print(f"Ошибка при вычислении функции на интервале: {e}")
        return False


# Проверка условия сходимости для метода простой итерации
def check_iteration_convergence(phi, a, b):
    x = np.linspace(a, b, 1000)
    derivative = np.abs(np.gradient(phi(x), x))  # Численное вычисление производной
    return np.all(derivative < 1)



# Построение графика
def plot_function(f, a, b, root=None):
    x = np.linspace(a, b, 400)
    y = f(x)

    plt.figure(figsize=(8, 6))
    plt.plot(x, y, label="f(x)")
    plt.axhline(0, color='black', linewidth=1, linestyle='--', label="y = 0")
    plt.axvline(0, color='black', linewidth=1, linestyle='--', label="x = 0")

    if root is not None:
        plt.scatter(root, f(root), color='red', label=f"Корень: {root:.5f}")

    plt.xlabel("x")
    plt.ylabel("f(x)")
    plt.title("График функции")
    plt.legend()
    plt.grid(True)
    plt.show()


# Метод половинного деления
def bisection_method(f, a, b, tol):
    if f(a) * f(b) >= 0:
        raise ValueError("Функция должна иметь разные знаки на концах интервала")

    iterations = 0
    # while (b - a) / 2.0 > tol:
    while abs(f((b + a) / 2)) > tol:
        midpoint = (a + b) / 2.0
        if f(a) * f(midpoint) < 0:
            b = midpoint
        else:
            a = midpoint
        iterations += 1

    return (a + b) / 2.0, iterations


def newton_method(f, df, ddf, a, b, tol, max_iter=100):
    if f(a) * ddf(a) > 0:
        x0 = a
    elif f(b) * ddf(b) > 0:
        x0 = b
    else:
        x0 = (a + b) / 2

    iterations = 0
    while iterations < max_iter:
        x1 = x0 - f(x0) / df(x0)
        if abs(x1 - x0) < tol:
            return x1, iterations
        if x1 < a or x1 > b:
            raise ValueError("Метод Ньютона вышел за пределы интервала. Попробуйте другой метод.")
        x0 = x1
        iterations += 1

    raise ValueError(f"Метод Ньютона не сошелся за {max_iter} итераций.")


#  Метод простой итерации
def simple_iteration_method(phi, a, b, tol, f, max_iter=1000):
    x0 = (a + b) / 2

    iterations = 0
    print(f"phi(a)  : {phi(a)}")
    print(f"phi(a)  : {phi(b)}")
    while iterations < max_iter:
        x1 = phi(x0)
        if abs(f(x1)) < tol:
            return x1, iterations
        x0 = x1
        iterations += 1

    raise ValueError("Метод простой итерации не сошелся за максимальное число итераций.")


def main():
    try:
        f, df, phi, dphi, a, b, tol = input_data()
        interval = [a, b]  # Используем список для хранения интервала

        if not verify_data(f, interval, tol):
            return
        a, b = interval

        while True:
            print("Выберите метод решения:")
            print("1. Метод половинного деления")
            print("2. Метод Ньютона")
            print("3. Метод простой итерации")
            print("4. Выход")
            method_choice = int(input("Введите номер метода: "))

            if method_choice == 1:
                try:
                    root, iterations = bisection_method(f, a, b, tol)
                    f_root = f(root)
                    print(f"x: {root}")
                    print(f"f(x): {f_root}")
                    print(f"Number of iterations: {iterations}")
                    plot_function(f, a, b, root)
                except ValueError as e:
                    print(f"Ошибка при выполнении метода половинного деления: {e}")

            elif method_choice == 2:
                try:
                    root, iterations = newton_method(f, df, a, b, tol)
                    f_root = f(root)
                    print(f"x: {root}")
                    print(f"f(x): {f_root}")
                    print(f"Number of iterations: {iterations}")
                    plot_function(f, a, b, root)
                except ValueError as e:
                    print(f"Ошибка при выполнении метода Ньютона: {e}")
            elif method_choice == 3:
                try:
                    if not check_iteration_convergence(phi, a, b):
                        print("Условие сходимости метода простой итерации не выполнено.")
                        print(f"f(a):{f(a)},  f(b):{f(b)}")
                    root, iterations = simple_iteration_method(phi, a, b, f, 1e-6)
                    f_root = f(root)
                    print(f"x: {root}")
                    print(f"f(x): {f_root}")
                    print(f"Number of iterations: {iterations}")
                    plot_function(f, a, b, root)

                except ValueError as e:
                    print(f"Ошибка при выполнении метода простой итерации: {e}")

            elif method_choice == 4:
                print("Выход из программы.")
                break  # Выход из цикла
            else:
                print("Неверный номер метода. Пожалуйста, выберите от 1 до 4.")

    except ValueError as e:
        print(f"Ошибка: {e}")


if __name__ == "__main__":
    main()
