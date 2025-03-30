import numpy as np

def f1(x):
    return x ** 2 - 5.65 * np.log(x)

def ddf1(x):
    return 2 + 5.65 / (x ** 2)


def numerical_ddf(f, x, h=1e-5):
    """Численное вычисление второй производной"""
    return (f(x + h) - 2*f(x) + f(x - h)) / (h**2)

# Пример проверки для f1:
x_test = 1.5
print("Аналитическая ddf1:", ddf1(x_test))
print("Численная ddf1:", numerical_ddf(f1, x_test))