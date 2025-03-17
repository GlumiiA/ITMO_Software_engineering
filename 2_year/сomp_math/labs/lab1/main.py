import numpy as np
from prettytable import PrettyTable


FILE_IN = "input.txt"


def read_file():
    try:
        with open(FILE_IN, 'r') as f:
            lines = f.readlines()
            a = []
            is_first = True
            n = 0
            for line in lines:
                if is_first:
                    n = line.strip().split()
                    is_first = False
                else:
                    row = list(map(float, line.strip().split()))
                    a.append(row)
            return n[0], a
    except FileNotFoundError:
        print(f"Файл {FILE_IN} не найден.")
        return None
    except ValueError:
        print("Ошибка в формате данных в файле.")
        return None



def read_input():
    print("Введите количество уравнений (строк матрицы): ")
    n = int(input())

    print("Введите коэффициенты матрицы через пробел (последняя колонка - свободные члены): ")
    a = []
    for i in range(n):
        row = list(map(float, input().strip().split()))
        a.append(row)

    a = np.array(a, dtype=float)
    return n, a


def gaussian_elimination_np(a):
    print("Реализация с помощью numpy.\n")
    n = len(a)
    det = 1  # Инициализируем детерминант

    for k in range(n):
        max_row_index = np.argmax(abs(a[k:n, k])) + k
        if a[max_row_index, k] == 0:
            raise ValueError("Система не имеет единственного решения.")

        a[[k, max_row_index]] = a[[max_row_index, k]]

        if k != max_row_index:
            det = -det

        for i in range(k + 1, n):
            factor = a[i][k] / a[k][k]
            a[i] = a[i] - factor * a[k]

    # Выводим треугольную матрицу с высокой точностью
    np.set_printoptions(precision=10)
    print("Треугольная форма матрицы:")

    table = PrettyTable()
    column_names = [f"X{i + 1}" for i in range(a.shape[1] - 1)]
    column_names.append("СЧ")
    table.field_names = column_names

    for row in a:
        table.add_row([f"{value:.4f}" for value in row])

    print(table)

    for i in range(n):
        det *= a[i][i]

    x = np.zeros(n)
    for i in range(n - 1, -1, -1):
        x[i] = (a[i][-1] - np.dot(a[i][i + 1:n], x[i + 1:n])) / a[i][i]

    return x, det


def gaussian_elimination(a):
    print("Реализация без NumPy.\n")
    n = len(a)

    det = 1
    for k in range(n):
        max_row_index = max(range(k, n), key=lambda i: abs(a[i][k]))

        if a[max_row_index][k] == 0:
            raise ValueError("Система не имеет единственного решения.")

        # Меняем местами текущую строку и строку с максимальным элементом
        if k != max_row_index:
            a[k], a[max_row_index] = a[max_row_index], a[k]
            det = -det

        # Приводим матрицу к верхнему треугольному виду
        for i in range(k + 1, n):
            factor = a[i][k] / a[k][k]
            # Вычитаем из i-ой строки k-ую строку умноженную на factor
            for j in range(k, n + 1):
                a[i][j] -= factor * a[k][j]

    print("Треугольная форма матрицы:")
    for row in a:
        print(["{:.4f}".format(value) for value in row])
    # Вычисляем детерминант
    for i in range(n):
        det *= a[i][i]
    # Решим систему уравнений с помощью обратной подстановки
    x = [0] * n
    for i in range(n - 1, -1, -1):
        x[i] = (a[i][-1] - sum(a[i][j] * x[j] for j in range(i + 1, n))) / a[i][i]

    return x, det


def main():
    print("Метод Гаусса с выбором главного элемента по столбцам")
    print("\nВзять коэффициенты из файла (1) или ввести с клавиатуры (2)?")

    method = int(input())
    while (method != 1) and (method != 2):
        print("Введите '1' или '2' для выбора способа ввода.")
        method = int(input())

    if method == 1:
        n, matrix = read_file()
    else:
        n, matrix = read_input()

    try:
        print("\nМетодом с numpy (1) или без (2)?")
        method_solve = int(input())
        while (method != 1) and (method != 2):
            print("Введите 1 или 2 для выбора метода.")
            method_solve = int(input())
        if method_solve == 1:
            matrix = np.array(matrix, dtype=float)
            solution, determinant = gaussian_elimination_np(matrix)
            residual_vector = np.dot(matrix[:, :-1], solution) - matrix[:, -1]
        else:
            solution, determinant = gaussian_elimination(matrix)
            residual_vector = []
            for row in matrix:
                # Умножаем все элементы строки, кроме последнего, на соответствующие элементы решения
                dot_product = sum(row[i] * solution[i] for i in range(len(row) - 1))
                # Вычитаем последний элемент строки
                residual = dot_product - row[-1]
                residual_vector.append(residual)

        print("Детерминант матрицы:", determinant)
        # Вычисление невязок

        print("\nВектор невязок:")
        print(residual_vector)

        print("Решение системы уравнений:")
        print(solution)
    except ValueError as e:
        print(e)


# Запуск главной функции
if __name__ == "__main__":
    main()
