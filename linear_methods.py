import matplotlib.pyplot as plt
import numpy as np

# Определение функций
def f1(x):
    return x ** 2 - 5.65 * np.log(x)

def f2(x):
    return np.sin(x)**3 - np.cos(x)**3

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
    return 3 * np.sin(x)**2 * np.cos(x) + 3 * np.cos(x)**2 * np.sin(x)

def df3(x):
    return 3 * np.exp(x) - 2 * np.cos(x)

def df4(x):
    return 9 * x ** 2 + 3.4 * x - 15.42

def df5(x):
    return 1 / (x + 1)

# Функции для метода простой итерации
def phi1(x):
    return np.sqrt(5.65 * np.log(x))

def phi2(x):
    return np.arctan(np.tan(x)**(1/3))

def phi3(x):
    return np.log((2 * np.sin(x)) / 3)

def phi4(x):
    return (-1.7 * x ** 2 + 15.42 * x - 6.89) / (3 * x ** 2)

def phi5(x):
    return np.exp(x) - 1

# Производные функций для метода простой итерации
def dphi1(x):
    return (5.65 / (2 * x * np.sqrt(5.65 * np.log(x))))

def dphi2(x):
    return (1 / (3 * (np.sin(x)**2 + np.cos(x)**2)))

def dphi3(x):
    return (2 * np.cos(x)) / (3 * (2 * np.sin(x)))

def dphi4(x):
    return (-3.4 * x + 15.42) / (3 * x ** 2) - 2 * (-1.7 * x ** 2 + 15.42 * x - 6.89) / (3 * x ** 3)

def dphi5(x):
    return np.exp(x)

# Ввод данных
def input_data():
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
    phi_functions = [phi1, phi2, phi3, phi4, phi5]
    dphi_functions = [dphi1, dphi2, dphi3, dphi4, dphi5]

    f = functions[choice - 1]
    df = derivatives[choice - 1]
    phi = phi_functions[choice - 1]
    dphi = dphi_functions[choice - 1]

    a = float(input("Введите левую границу интервала: "))
    b = float(input("Введите правую границу интервала: "))
    tol = float(input("Введите погрешность: "))

    # Проверка интервала для функций с логарифмами
    if choice == 1 or choice == 5:
        if a <= 0 or b <= 0:
            raise ValueError("Для выбранного уравнения интервал должен содержать только положительные числа (x > 0).")

    return f, df, phi, dphi, a, b, tol

# Верификация данных
def verify_data(f, a, b):
    try:
        if f(a) * f(b) >= 0:
            print("На интервале либо нет корней, либо их несколько. Пожалуйста, выберите другой интервал.")
            return False
        return True
    except Exception as e:
        print(f"Ошибка при вычислении функции на интервале: {e}")
        return False

# Проверка условия сходимости для метода простой итерации
def check_iteration_convergence(dphi, a, b):
    x = np.linspace(a, b, 1000)
    if np.all(np.abs(dphi(x))) < 1:
        return True
    return False

# Вывод результатов
def output_results(root, f_root, iterations, output_file=None):
    if output_file:
        with open(output_file, 'w') as file:
            file.write(f"x: {root}\n")
            file.write(f"f(x): {f_root}\n")
            file.write(f"Number of iterations: {iterations}\n")
    else:
        print(f"x: {root}")
        print(f"f(x): {f_root}")
        print(f"Number of iterations: {iterations}")

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
    while (b - a) / 2.0 > tol:
        midpoint = (a + b) / 2.0
        if f(midpoint) == 0:
            return midpoint, iterations
        elif f(a) * f(midpoint) < 0:
            b = midpoint
        else:
            a = midpoint
        iterations += 1

    return (a + b) / 2.0, iterations

# Метод Ньютона
def newton_method(f, df, a, b, tol, max_iter=100):
    # Используем метод половинного деления для грубого приближения
    x0, _ = bisection_method(f, a, b, tol)

    iterations = 0
    while iterations < max_iter:
        x1 = x0 - f(x0) / df(x0)
        if abs(x1 - x0) < tol:
            return x1, iterations
        if x1 < a or x1 > b:  # Проверка, чтобы не выйти за пределы интервала
            raise ValueError("Метод Ньютона вышел за пределы интервала. Попробуйте другой метод.")
        x0 = x1
        iterations += 1

    raise ValueError("Метод Ньютона не сошелся за максимальное число итераций.")

# Метод простой итерации
def simple_iteration_method(phi, a, b, tol, max_iter=100):
    # Проверка условия сходимости
    if not check_iteration_convergence(phi, a, b):
        raise ValueError("Условие сходимости метода простой итерации не выполнено.")

    # Выбор начального приближения
    x0 = (a + b) / 2

    iterations = 0
    while iterations < max_iter:
        x1 = phi(x0)
        if abs(x1 - x0) < tol:
            return x1, iterations
        x0 = x1
        iterations += 1

    raise ValueError("Метод простой итерации не сошелся за максимальное число итераций.")

def main():
    try:
        f, df, phi, dphi, a, b, tol = input_data()

        if not verify_data(f, a, b):
            return

        # Выбор метода
        print("Выберите метод решения:")
        print("1. Метод половинного деления")
        print("2. Метод Ньютона")
        print("3. Метод простой итерации")
        method_choice = int(input("Введите номер метода: "))

        if method_choice == 1:
            root, iterations = bisection_method(f, a, b, tol)
        elif method_choice == 2:
            root, iterations = newton_method(f, df, a, b, tol)
        elif method_choice == 3:
            root, iterations = simple_iteration_method(phi, a, b, tol)
        else:
            raise ValueError("Неверный номер метода. Пожалуйста, выберите от 1 до 3.")

        f_root = f(root)

        output_option = input("Вывести результаты в файл? (y/n): ")
        if output_option.lower() == 'y':
            output_results(root, f_root, iterations, "output_file.txt")
        else:
            output_results(root, f_root, iterations)

        plot_function(f, a, b, root)
    except ValueError as e:
        print(f"Ошибка: {e}")

if __name__ == "__main__":
    main()