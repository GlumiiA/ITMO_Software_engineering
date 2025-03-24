import pandas as pd
import matplotlib.pyplot as plt

file_path = 'sex_bmi_smokers.csv'
df = pd.read_csv(file_path)

bmi_data = df.iloc[:, 0].values

# Построение гистограммы
plt.hist(bmi_data, bins=10, edgecolor='black')
plt.hist(bmi_data, bins=10, edgecolor='black')
plt.xlabel('ИМТ')
plt.ylabel('Частота')
plt.title('Гистограмма распределения ИМТ для курящих мужчин')
plt.grid(True)
plt.show()

