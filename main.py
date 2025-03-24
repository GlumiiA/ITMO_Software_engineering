import pandas as pd
import numpy as np
import matplotlib.pyplot as plt


file_path = 'sex_bmi_smokers.csv'
df = pd.read_csv(file_path)

data = df.iloc[:, 0].values

print("Данные ИМТ:", data)

sorted_data = np.sort(data)

# Вычисление накопленных частот
y = np.arange(1, len(sorted_data) + 1) / len(sorted_data)

plt.step(sorted_data, y, where='post', label='ЭФР')
plt.xlabel('Значения данных (ИМТ)')
plt.ylabel('Накопленная частота')
plt.title('Эмпирическая функция распределения для курящих мужчин')
plt.legend()
plt.grid(True)
plt.show()