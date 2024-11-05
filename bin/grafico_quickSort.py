import matplotlib.pyplot as plt
import pandas as pd

# Lista de metodologias para ler os arquivos CSV correspondentes
metodologias = ["Serial", "Paralelo_2_Threads", "Paralelo_3_Threads", "Paralelo_4_Threads"]
dados = {}

# Lê cada arquivo CSV e armazena os dados em um dicionário
for metodo in metodologias:
    df = pd.read_csv(f"quickSort_{metodo}_resultados.csv")
    dados[metodo] = df

# Plotando o gráfico
plt.figure(figsize=(10, 6))
for metodo, df in dados.items():
    plt.plot(df["Tamanho do Vetor"], df["Tempo (ns)"], label=metodo)

# Configurações do gráfico
plt.xlabel("Tamanho do Vetor")
plt.ylabel("Tempo de Execução (ns)")
plt.title("QuickSort")
plt.legend()
plt.grid(True)
plt.tight_layout()

# Exibe o gráfico
plt.show()
