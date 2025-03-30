import matplotlib.pyplot as plt
import numpy as np


# Определение функций
def f1(x):
    return x ** 2 - 5.65 * np.log(x)


def f2(x):
    return np.sin(x) ** 3 - np.cos(x) ** 3


def f3(x):
    return 3 * np.exp(x) - 2 * np.sin(x)


def f4(x):
    return 3 * x ** 3 + 1.7 * x ** 2 - 15.42 * x + 6.89

# Определение производных функций
def df1(x):
    return 2 * x - 5.65 / x


def df2(x):
    return 3 * np.sin(x) ** 2 * np.cos(x) + 3 * np.cos(x) ** 2 * np.sin(x)


def df3(x):
    return 3 * np.exp(x) - 2 * np.cos(x)


def df4(x):
    return 9 * x ** 2 + 3.4 * x - 15.42

def numerical_ddf(f, x, h=1e-5):
    """Численное вычисление второй производной"""
    return (f(x + h) - 2*f(x) + f(x - h)) / (h**2)


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

    return phi, dphi, lambda_

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

            if choice < 1 or choice > 4:
                raise ValueError("Неверный номер уравнения. Пожалуйста, выберите от 1 до 4.")

            functions = [f1, f2, f3, f4]
            derivatives = [df1, df2, df3, df4]

            f = functions[choice - 1]
            df = derivatives[choice - 1]
            phi, dphi, lambda_ = phi_with_lambda(f, df, a, b)

            # Проверка интервала для функций с логарифмами
            if choice == 1 or choice == 5:
                if a <= 0 or b <= 0:
                    raise ValueError(
                        "Для выбранного уравнения интервал должен содержать только положительные числа (x > 0).")

            return f, df, phi, dphi, a, b, tol, lambda_
    except Exception as e:
        print(f"Ошибка при чтении файла: {e}")
        return None


# Ввод данных с клавиатуры
def input_from_keyboard():
    print("Выберите уравнение:")
    print("1. x² - 5.65·ln(x)")
    print("2. sin³(x) - cos³(x)")
    print("3. 3·eˣ - 2·sin(x)")
    print("4. 3x³ + 1.7x² - 15.42x + 6.89")
    choice = int(input("Введите номер уравнения: "))

    if choice < 1 or choice > 4:
        raise ValueError("Неверный номер уравнения. Пожалуйста, выберите от 1 до 4.")

    functions = [f1, f2, f3, f4]
    derivatives = [df1, df2, df3, df4]

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
    phi, dphi, lambda_ = phi_with_lambda(f, df, a, b)
    return f, df, phi, dphi, a, b, tol, lambda_


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
def check_iteration_convergence(dphi, a, b):
    x = np.linspace(a, b, 1000)
    derivative = np.abs(dphi(x))  # Численное вычисление производной
    return np.all(derivative < 1)

def check_newton_convergence(f, df, a, b):
    # 1. Проверка существования корня
    if f(a) * f(b) >= 0:
        print(f"Условие f(a)*f(b) < 0 не выполняется (f(a)={f(a):.3f}, f(b)={f(b):.3f})")
        return False

    # 2. Проверка знака f'(x)
    x_samples = np.linspace(a, b, 100)
    df_values = df(x_samples)
    df_sign_changes = np.sum(np.diff(np.sign(df_values)) != 0)

    if df_sign_changes > 0:
        print(f"f'(x) меняет знак {df_sign_changes} раз на интервале")
        return False

    # 3. Проверка знака f''(x)
    ddf_values = numerical_ddf(f, x_samples)
    ddf_sign_changes = np.sum(np.diff(np.sign(ddf_values)) != 0)

    if ddf_sign_changes > 0:
        print(f"f''(x) меняет знак {ddf_sign_changes} раз на интервале")
        return False


# Построение графика
def plot_function(f, a, b, root=None):
    width = b - a
    x_min = a - 0.5 * width
    x_max = b + 0.5 * width
    x = np.linspace(x_min, x_max, 500)
    y = f(x)

    plt.figure(figsize=(10, 6))
    plt.plot(x, y, label="f(x)")
    plt.axhline(0, color='black', linewidth=1, linestyle='--', label="y = 0")
    plt.axvline(0, color='black', linewidth=1, linestyle='--', label="x = 0")

    if root is not None:
        plt.scatter(root, f(root), color='red', label=f"Корень: {root:.5f}")

    # Вертикальные границы интервала [a, b]
    plt.axvline(a, color='red', linewidth=1.5, linestyle='--',
                    label=f'Граница интервала: [{a:.2f}, {b:.2f}]')
    plt.axvline(b, color='red', linewidth=1.5, linestyle='--')

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


def newton_method(f, df, a, b, tol, max_iter=100, visualize=True):
    # Выбираем начальное приближение
    if f(a) * numerical_ddf(f, a) > 0:
        x0 = a
    elif f(b) * numerical_ddf(f, b) > 0:
        x0 = b
    else:
        x0 = (a + b) / 2

    if visualize:
        fig, ax = plt.subplots(figsize=(10, 6))
        x_vals = np.linspace(a - (b - a) / 2, b + (b - a) / 2, 400)
        ax.plot(x_vals, f(x_vals), label='f(x)')
        plt.axhline(0, color='black', linewidth=1, linestyle='--', label="y = 0")
        plt.axvline(0, color='black', linewidth=1, linestyle='--', label="x = 0")
        plt.axvline(a, color='red', linewidth=1.5, linestyle='--',
                    label=f'Граница интервала: [{a:.2f}, {b:.2f}]')
        plt.axvline(b, color='red', linewidth=1.5, linestyle='--')

    iterations = 0
    while iterations < max_iter:
        try:
            df_x0 = df(x0)
            if abs(df_x0) < 1e-12:
                raise ValueError("Производная слишком близка к нулю")
            x1 = x0 - f(x0) / df_x0

            if visualize:
                tangent = lambda x: df_x0 * (x - x0) + f(x0)
                ax.plot(x_vals, tangent(x_vals), '--', alpha=0.3)
                ax.plot([x0, x1], [f(x0), 0], 'o-', markersize=4)
                plt.grid(True)

            if x1 < a or x1 > b:
                if visualize:
                    ax.set_title(f'Внимание: x={x1:.3f} вне интервала! Итерация {iterations + 1}')
                    ax.plot(x1, 0, 'o', color='red')
                    plt.show()
                x1 = np.clip(x1, a, b)  # Возвращаем значение в интервал
                # Можно добавить здесь дополнительные корректировки

            if abs(x1 - x0) < tol:
                if visualize:
                    plt.scatter(x1, f(x1), color='red', label=f"Корень: {x1:.5f}")
                    ax.plot(x1, 0, 'o', color='red')
                    ax.set_title(f'Сходится за {iterations + 1} итераций')
                    ax.legend()
                    plt.show()
                return x1, iterations + 1

            x0 = x1
            iterations += 1

        except Exception as e:
            if visualize:
                ax.set_title(f'Ошибка: {str(e)}')
                plt.show()
            raise ValueError(f"Ошибка на итерации {iterations}: {e}")

    if visualize:
        ax.set_title(f'Не сошлось за {max_iter} итераций')
        plt.show()
    raise ValueError(f"Не сошлось за {max_iter} итераций")

#  Метод простой итерации
def simple_iteration_method(phi, a, b, tol, max_iter=1000):
    """
    Метод простой итерации для решения уравнения x = phi(x).

    Параметры:
    phi -- функция итерации
    a, b -- границы интервала (для выбора начального приближения)
    tol -- допустимая погрешность
    max_iter -- максимальное число итераций
    """
    x0 = (a + b) / 2  # Начальное приближение - середина интервала

    for iterations in range(1, max_iter + 1):
        x1 = phi(x0)
        # Условие остановки: |x1 - x0| < tol
        if abs(x1 - x0) < tol:
            return x1, iterations
        x0 = x1
    raise ValueError(f"Метод не сошёлся за {max_iter} итераций.")


def main():
    try:
        f, df, phi, dphi, a, b, tol, lambda_ = input_data()
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
                    if not check_newton_convergence(f, df, a, b):
                        print("Условие сходимости метода Ньютона не выполнено.")
                    root, iterations = newton_method(f, df, a, b, tol)  # tol передается корректно
                    print(f"x: {root:.6f}")
                    print(f"f(x): {f(root):.6e}")
                    print(f"Number of iterations: {iterations}")
                    # plot_function(f, a, b, root)
                except ValueError as e:

                    print(f"Ошибка: {e}")
            elif method_choice == 3:
                try:
                    if not check_iteration_convergence(dphi, a, b):
                        print("Условие сходимости метода простой итерации не выполнено.")
                        print(f"f(a):{f(a)},  f(b):{f(b)}")
                    root, iterations = simple_iteration_method(phi, a, b, tol)
                    print(f"phi(a)  : {phi(a)}")
                    print(f"phi(b)  : {phi(b)}")
                    print(f"λ  : {lambda_}")
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