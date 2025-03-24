import math
import pandas as pd

file_path = 'sex_bmi_smokers.csv'
df = pd.read_csv(file_path)
data = df.iloc[:, 0].values
n = len(data)

# Оценка μ и σ² аналитически
mu_hat = sum(data) / n
sigma2_hat = sum((x - mu_hat)**2 for x in data) / n
print(f"Аналитические оценки:")
print(f"Оценка μ: {mu_hat}")
print(f"Оценка σ²: {sigma2_hat}")

# Функция для вычисления логарифмической функции правдоподобия
def log_likelihood(mu, log_sigma2, data):
    sigma2 = math.exp(log_sigma2)
    n = len(data)
    return -n/2 * math.log(2*math.pi) - n/2 * log_sigma2 - 1/(2*sigma2) * sum((x - mu)**2 for x in data)

# Функции для вычисления градиентов
def gradient_mu(mu, log_sigma2, data):
    sigma2 = math.exp(log_sigma2)
    return sum((x - mu) for x in data) / sigma2

def gradient_log_sigma2(mu, log_sigma2, data):
    sigma2 = math.exp(log_sigma2)
    n = len(data)
    return -n/2 + 1/(2*sigma2) * sum((x - mu)**2 for x in data)

# Градиентный спуск
mu = 0  # Начальное значение μ
log_sigma2 = 1  # Начальное значение ln(σ²)
learning_rate_mu = 0.1  # Скорость обучения для μ
learning_rate_log_sigma2 = 0.001  # Скорость обучения для ln(σ²)
epochs = 10000  # Количество итераций

for epoch in range(epochs):
    grad_mu = gradient_mu(mu, log_sigma2, data)
    grad_log_sigma2 = gradient_log_sigma2(mu, log_sigma2, data)

    mu += learning_rate_mu * grad_mu
    log_sigma2 += learning_rate_log_sigma2 * grad_log_sigma2

    # Ограничение на log_sigma2, чтобы избежать переполнения
    if log_sigma2 > 100:
        log_sigma2 = 100
    elif log_sigma2 < -100:
        log_sigma2 = -100

    if epoch % 1000 == 0:
        sigma2 = math.exp(log_sigma2)
        print(f"Epoch {epoch}: μ = {mu}, σ² = {sigma2}")

sigma2 = math.exp(log_sigma2)
print(f"Оценка μ (градиентный спуск): {mu}")
print(f"Оценка σ² (градиентный спуск): {sigma2}")
print(f"MSE σ²: {2*sigma2*sigma2/n + sigma2*sigma2/n/n}")
print(f"I σ²: {n/2/sigma2/sigma2}")
print(f"I μ: {n/sigma2}")
