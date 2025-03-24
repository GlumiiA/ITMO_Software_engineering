import pandas as pd
import matplotlib.pyplot as plt
import scipy.stats as stats
import seaborn as sns

file_path = 'wy.csv'
df = pd.read_csv(file_path)

bmi_data = df.iloc[:, 0].values

# Гистограмма
plt.figure(figsize=(12, 5))
plt.subplot(1, 2, 1)
sns.histplot(bmi_data, kde=True)
plt.title('Гистограмма данных')

# Q-Q plot
plt.subplot(1, 2, 2)
stats.probplot(bmi_data, dist="norm", plot=plt)
plt.title('Q-Q plot')

plt.show()