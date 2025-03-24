import pandas as pd
import matplotlib.pyplot as plt


file_path = 'sex_bmi_smokers.csv'
df = pd.read_csv(file_path)

bmi_data = df.iloc[:, 0].values

plt.boxplot(bmi_data)
plt.xlabel('ИМТ')
plt.ylabel('Значения')
plt.title('Box-plot распределения ИМТ для ккурящих мужчин')
plt.show()